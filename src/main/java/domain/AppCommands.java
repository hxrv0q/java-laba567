package domain;

import domain.data.Contact;
import domain.data.Relationships;
import domain.data.json.ContactDataSource;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AppCommands implements Commands {
    private final ContactDataSource contactDataSource;
    private final List<Contact> contacts;

    public AppCommands(ContactDataSource contactDataSource, List<Contact> contacts) {
        this.contactDataSource = contactDataSource;
        this.contacts = contacts;
    }

    public void writeFile() {
        contactDataSource.writeContact(contacts);
    }

    @Override
    public void add(Contact contact) {
        contacts.add(contact);
    }

    @Override
    public void delete(List<Contact> searchResult, int index) {
        contacts.removeAll(searchResult);
    }

    @Override
    public List<Contact> search(String search) {
        List<Contact> searchResults = new ArrayList<>();
        for (Contact contact : contacts) {
            if (contact.getName().toLowerCase().contains(search.toLowerCase()) ||
                    contact.getSurname().toLowerCase().contains(search.toLowerCase()) ||
                    contact.getPhoneNumber().toLowerCase().contains(search.toLowerCase()) ||
                    contact.getEmail().toLowerCase().contains(search.toLowerCase())) {
                searchResults.add(contact);
            }
        }
        return searchResults;
    }

    @Override
    public void show(Contact contact) {
        contactDataSource.readContact();
        System.out.println(contact);
        if (!contacts.isEmpty()) {
            for (Contact contact1 : contacts) {
                System.out.println(contact1);
            }
        } else {
            System.out.println("No contacts found");
        }
    }

    @Override
    public void updateContact(List<Contact> contactList, String newName, String newSurname, String newPhoneNumber, String newEmail,
                              LocalDate newBirthDate, Relationships newRelationships, String search, int id) {
        boolean contactFound = contactList.stream()
                .filter(contact -> contact.getName().equals(search) && contact.getId() == id)
                .findFirst()
                .map(
                        contact -> {
                            Contact updatedContact = new Contact(
                                    newName != null ? newName : contact.getName(),
                                    newSurname != null ? newSurname : contact.getSurname(),
                                    newPhoneNumber != null ? newPhoneNumber : contact.getPhoneNumber(),
                                    newEmail != null ? newEmail : contact.getEmail(),
                                    newBirthDate != null ? newBirthDate : contact.getBirthDate(),
                                    newRelationships != null ? newRelationships : contact.getRelationships(),
                                    contact.getId()
                            );
                            int index = contactList.indexOf(contact);
                            contactList.set(index, updatedContact);
                            return true;
                        }
                ).orElse(false);

        if (!contactFound) {
            System.out.println("такого контакту не існує");
        }
    }

    @Override
    public void saveChanges() {
        contactDataSource.writeContact(contacts);
    }
}
