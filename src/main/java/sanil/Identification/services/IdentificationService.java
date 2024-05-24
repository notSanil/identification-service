package sanil.Identification.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sanil.Identification.dto.IdentificationRequestDto;
import sanil.Identification.dto.IdentificationResponseDto;
import sanil.Identification.entities.Contact;
import sanil.Identification.enums.LinkPrecedence;
import sanil.Identification.repositories.ContactRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class IdentificationService {
    @Autowired
    private ContactRepository contactRepository;

    public IdentificationResponseDto identify(IdentificationRequestDto identificationRequestDto) {
        //Step 1: Check if record already exists with the same details
        Optional<Contact> contact = contactRepository.findByPhoneNumberAndEmail(identificationRequestDto.getPhoneNumber(), identificationRequestDto.getEmail());
        if (contact.isPresent()) {
            return findDetailsFromContact(contact.get());
        }

        //Step 2: It doesn't so try to find the primary contact
        List<Contact> idByPhone = contactRepository.findByPhoneNumber(identificationRequestDto.getPhoneNumber());
        List<Contact> idByEmail = contactRepository.findByEmail(identificationRequestDto.getEmail());

        if (idByPhone.isEmpty() && idByEmail.isEmpty()) {
            //Step 3a: No primary, create new
            return createNewContact(identificationRequestDto, null);
        } else if (idByPhone.isEmpty() || idByEmail.isEmpty()) {
            //Step 3b: Primary found, add this contact to the tree
            Contact primaryContact;
            if (!idByPhone.isEmpty()) {
                primaryContact = getPrimaryContact(idByPhone.get(0));
            } else {
                primaryContact = getPrimaryContact(idByEmail.get(0));
            }
            return createNewContact(identificationRequestDto, primaryContact);
        } else {
            //Step 3c: Two primaries found
            Contact phonePrimaryContact = getPrimaryContact(idByPhone.get(0));
            Contact emailPrimaryContact = getPrimaryContact(idByEmail.get(0));

            if (Objects.equals(emailPrimaryContact.getId(), phonePrimaryContact.getId())) {
                return findDetailsFromContact(emailPrimaryContact);
            } else {
                return mergeContacts(phonePrimaryContact, emailPrimaryContact);
            }
        }
    }

    private IdentificationResponseDto createNewContact(IdentificationRequestDto identificationRequestDto, Contact primary) {
        Contact contact = Contact.builder()
                .email(identificationRequestDto.getEmail())
                .phoneNumber(identificationRequestDto.getPhoneNumber())
                .linkedId(primary)
                .linkPrecedence(primary == null ? LinkPrecedence.Primary : LinkPrecedence.Secondary)
                .build();

        Contact repositoryContact = contactRepository.save(contact);
        return findDetailsFromContact(repositoryContact);
    }

    private IdentificationResponseDto findDetailsFromContact(Contact contact) {
        Integer primaryId = getPrimaryContact(contact).getId();
        IdentificationResponseDto.Contact.ContactBuilder builder = IdentificationResponseDto.Contact.builder();
        builder.primaryContactId(primaryId);

        Contact primaryContact = contactRepository.findById(primaryId).get();
        List<String> emails = new ArrayList<>();
        emails.add(primaryContact.getEmail());

        List<String> phoneNumbers = new ArrayList<>();
        phoneNumbers.add(primaryContact.getPhoneNumber());

        List<Contact> secondaryContacts = contactRepository.findByLinkedId(primaryId);
        builder.secondaryContactIds(secondaryContacts.stream().map(Contact::getId).toList());
        phoneNumbers.addAll(secondaryContacts.stream().map(Contact::getPhoneNumber).toList());
        emails.addAll(secondaryContacts.stream().map(Contact::getEmail).toList());

        builder.emails(emails.stream().distinct().collect(Collectors.toList()));
        builder.phoneNumbers(phoneNumbers.stream().distinct().collect(Collectors.toList()));

        return new IdentificationResponseDto(builder.build());
    }

    private Contact getPrimaryContact(Contact contact) {
        return contact.getLinkPrecedence() == LinkPrecedence.Primary ? contact : contact.getLinkedId();
    }

    private IdentificationResponseDto mergeContacts(Contact contact1, Contact contact2) {
        Contact newerContact = contact1.getCreatedAt().isAfter(contact2.getCreatedAt()) ? contact1 : contact2;
        Contact olderContact = contact1.getCreatedAt().isAfter(contact2.getCreatedAt()) ? contact2 : contact1;

        List<Contact> newContactLinks = contactRepository.findByLinkedId(newerContact.getId());
        newContactLinks.add(newerContact);

        newContactLinks.forEach(contact -> {
            contact.setLinkedId(olderContact);
            contact.setLinkPrecedence(LinkPrecedence.Secondary);
        });

        contactRepository.saveAll(newContactLinks);

        return findDetailsFromContact(olderContact);
    }

    public IdentificationResponseDto identifyWithEmail(IdentificationRequestDto identificationRequestDto) {
        List<Contact> contacts = contactRepository.findByEmail(identificationRequestDto.getEmail());
        if (contacts.isEmpty()) {
            return createNewContact(identificationRequestDto, null);
        }

        return findDetailsFromContact(contacts.get(0));
    }

    public IdentificationResponseDto identifyWithPhone(IdentificationRequestDto identificationRequestDto) {
        List<Contact> contacts = contactRepository.findByPhoneNumber(identificationRequestDto.getPhoneNumber());
        if (contacts.isEmpty()) {
            return createNewContact(identificationRequestDto, null);
        }

        return findDetailsFromContact(contacts.get(0));
    }

    public void deleteAll() {
        contactRepository.deleteAll();
    }
}
