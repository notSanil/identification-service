package sanil.Identification.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sanil.Identification.entities.Contact;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, Integer> {
    List<Contact> findByEmail(String email);
    List<Contact> findByPhoneNumber(String phoneNumber);
}
