package domain.data.json;

import domain.data.Contact;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ContactDataSource {
    private final JsonConverter jsonConverter;
    private final String path = "contact.json";

    public ContactDataSource(JsonConverter jsonConverter) {
        this.jsonConverter = jsonConverter;
    }

    public List<Contact> readContact() {
        List<Contact> contactList = new ArrayList<>();
        try {
            String json = Files.readString(Path.of(path));
            contactList = jsonConverter.fromJson(json);
        } catch (Exception e) {
        }
        return contactList;
    }

    public void writeContact(List<Contact> contactList) {
        try {
            String json = jsonConverter.toJson(contactList);
            Files.writeString(Path.of(path), json);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
