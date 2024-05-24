package sanil.Identification.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sanil.Identification.entities.Contact;

public interface ContactRepository extends JpaRepository<Contact, Integer> {

}
