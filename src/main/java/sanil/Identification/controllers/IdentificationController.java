package sanil.Identification.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import sanil.Identification.dto.IdentificationRequestDto;
import sanil.Identification.dto.IdentificationResponseDto;
import sanil.Identification.services.IdentificationService;

@RestController
public class IdentificationController {
    @Autowired
    private IdentificationService identificationService;
    @PostMapping("/identify")
    public IdentificationResponseDto identify(@RequestBody IdentificationRequestDto identificationRequestDto) {
//        if (identificationRequestDto.getEmail() == null && identificationRequestDto.getPhoneNumber() == null) {
//            throw new HttpClientErrorException(HttpStatusCode.valueOf(400));
//        }
        return identificationService.identify(identificationRequestDto);
    }
}
