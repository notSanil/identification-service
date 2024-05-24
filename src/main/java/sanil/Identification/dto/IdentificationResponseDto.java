package sanil.Identification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IdentificationResponseDto {

    Contact contact;
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    static public class Contact {
        private Integer primaryContactId;
        private List<String> emails;
        private List<String> phoneNumbers;
        private List<Integer> secondaryContactIds;
    }
}
