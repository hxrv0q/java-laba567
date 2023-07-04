package domain.data.json;

import domain.data.Contact;

import java.util.List;

public interface JsonConverter {
    String toJson(List<Contact> contactList);
    List<Contact> fromJson(String contactList);
}