package sanil.Identification.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sanil.Identification.entities.Contact;

import java.util.List;
import java.util.Optional;

public interface ContactRepository extends JpaRepository<Contact, Integer> {

    List<Contact> findByPhoneNumber(String phoneNumber);

    List<Contact> findByEmail(String email);

    Optional<Contact> findByPhoneNumberAndEmail(String phoneNumber, String email);

    @Query("Select c from Contact c where c.linkedId.id=:id")
    List<Contact> findByLinkedId(Integer id);
}
