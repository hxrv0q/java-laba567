package domain;

import domain.data.Contact;
import domain.data.Relationships;

import java.time.LocalDate;
import java.util.List;

public interface Commands {
    void add(Contact contact);

    void delete(List<Contact> searchResult, int index);

    List<Contact> search(String search);

    void show(Contact contact);

    void updateContact(List<Contact> contactList, String newName,
                       String newSurname, String newPhoneNumber,
                       String newEmail, LocalDate newBirthDate,
                       Relationships newRelationships, String search, int id);

    void saveChanges();
}
