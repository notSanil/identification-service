package sanil.Identification.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
    public ResponseEntity<IdentificationResponseDto> identify(@RequestBody IdentificationRequestDto identificationRequestDto) {
        if (identificationRequestDto.getEmail() == null && identificationRequestDto.getPhoneNumber() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else if (identificationRequestDto.getEmail() == null || identificationRequestDto.getPhoneNumber() == null) {
            if (identificationRequestDto.getEmail() != null)
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(identificationService.identifyWithEmail(identificationRequestDto));
            else
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(identificationService.identifyWithPhone(identificationRequestDto));
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(identificationService.identify(identificationRequestDto));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteAllRecords() {
        // Note that this endpoint is strictly for this task, anyone may use this endpoint for deleting the database
        identificationService.deleteAll();
        return ResponseEntity.ok().body("Success");
    }
}
