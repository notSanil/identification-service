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

@Service
public class IdentificationService {

    @Autowired
    private ContactRepository contactRepository;
    public IdentificationResponseDto identify(IdentificationRequestDto identificationRequestDto) {
        List<Contact> contactsByEmail = contactRepository.findByEmail(identificationRequestDto.getEmail());
        List<Contact> contactsByNumber = contactRepository.findByPhoneNumber(identificationRequestDto.getPhoneNumber());

        if (contactsByEmail.isEmpty() && contactsByNumber.isEmpty()) {
            Contact newRecord = Contact.builder()
                    .email(identificationRequestDto.getEmail())
                    .phoneNumber(identificationRequestDto.getPhoneNumber())
                    .build();
            newRecord = contactRepository.save(newRecord);

            return new IdentificationResponseDto(IdentificationResponseDto.Contact.builder()
                    .primaryContactId(newRecord.getId())
                    .emails(List.of(newRecord.getEmail()))
                    .phoneNumbers(List.of(newRecord.getPhoneNumber()))
                    .secondaryContactIds(new ArrayList<>())
                    .build());
        }

        return null;
    }
}
