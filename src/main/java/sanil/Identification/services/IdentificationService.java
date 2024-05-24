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
        Optional<Contact> contact = contactRepository.findByPhoneNumberAndEmail(identificationRequestDto.getPhoneNumber(), identificationRequestDto.getEmail());
        if (contact.isPresent()) {
            return findDetailsFromContact(contact.get());
        }

        List<Contact> idByPhone = contactRepository.findByPhoneNumber(identificationRequestDto.getPhoneNumber());
        List<Contact> idByEmail = contactRepository.findByEmail(identificationRequestDto.getEmail());

        if (idByPhone.isEmpty() && idByEmail.isEmpty()) {
            return createNewContact(identificationRequestDto, null);
        } else if (idByPhone.isEmpty() || idByEmail.isEmpty()) {
            Contact primaryContact;
            if (!idByPhone.isEmpty()) {
                primaryContact = getPrimaryContact(idByPhone.get(0));
            } else {
                primaryContact = getPrimaryContact(idByEmail.get(0));
            }

            return createNewContact(identificationRequestDto, primaryContact);
        } else {
            Contact phonePrimaryContact = getPrimaryContact(idByPhone.get(0));
            Contact emailPrimaryContact = getPrimaryContact(idByEmail.get(0));

            if (Objects.equals(emailPrimaryContact.getId(), phonePrimaryContact.getId())) {
                return findDetailsFromContact(emailPrimaryContact);
            } else {

            }
        }

        System.out.print(idByPhone);
        System.out.print(idByEmail);
        return null;
    }

    private IdentificationResponseDto createNewContact(IdentificationRequestDto identificationRequestDto, Contact primary) {
        Contact contact = Contact.builder()
                .email(identificationRequestDto.getEmail())
                .phoneNumber(identificationRequestDto.getPhoneNumber())
                .linkedId(primary)
                .linkPrecedence(primary == null ? LinkPrecedence.Primary : LinkPrecedence.Secondary)
                .build();

        Contact repositoryContact = contactRepository.save(contact);
        return new IdentificationResponseDto(IdentificationResponseDto.Contact.builder()
                .primaryContactId(repositoryContact.getId())
                .phoneNumbers(List.of(repositoryContact.getPhoneNumber()))
                .emails(List.of(repositoryContact.getEmail()))
                .build());
    }

    private IdentificationResponseDto findDetailsFromContact(Contact contact) {
        Integer id = contact.getLinkPrecedence() == LinkPrecedence.Primary ? contact.getId() : contact.getLinkedId().getId();
        IdentificationResponseDto.Contact.ContactBuilder builder = IdentificationResponseDto.Contact.builder();
        builder.primaryContactId(id);

        Contact primaryContact = contactRepository.findById(id).get();
        List<String> emails = new ArrayList<>();
        emails.add(primaryContact.getEmail());

        List<String> phoneNumbers = new ArrayList<>();
        phoneNumbers.add(primaryContact.getPhoneNumber());

        List<Contact> secondaryContacts = contactRepository.findByLinkedId(id);
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
}
