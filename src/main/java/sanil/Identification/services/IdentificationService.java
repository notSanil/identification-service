package sanil.Identification.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sanil.Identification.dto.IdentificationRequestDto;
import sanil.Identification.dto.IdentificationResponseDto;
import sanil.Identification.repositories.ContactRepository;

import java.util.List;

@Service
public class IdentificationService {

    @Autowired
    private ContactRepository contactRepository;
    public IdentificationResponseDto identify(IdentificationRequestDto identificationRequestDto) {
        IdentificationResponseDto identificationResponseDto = IdentificationResponseDto.builder()
                .contact(new IdentificationResponseDto.Contact(
                        1,
                        List.of("199", "19"),
                        List.of("1", "11"),
                        List.of(1, 23, 45)
                ))
                .build();

        return identificationResponseDto;
    }
}
