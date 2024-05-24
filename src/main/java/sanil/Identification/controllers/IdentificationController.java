package sanil.Identification.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sanil.Identification.dto.IdentificationRequestDto;
import sanil.Identification.dto.IdentificationResponseDto;
import sanil.Identification.services.IdentificationService;

@RestController
public class IdentificationController {
    @Autowired
    private IdentificationService identificationService;
    @PostMapping("/identify")
    public IdentificationResponseDto identify(@RequestBody IdentificationRequestDto identificationRequestDto) {
        return identificationService.identify(identificationRequestDto);
    }
}
