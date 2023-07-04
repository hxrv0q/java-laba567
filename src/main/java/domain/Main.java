package domain;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import domain.data.Contact;
import domain.data.Relationships;
import domain.data.json.ContactDataSource;
import domain.data.json.GsonConverter;
import domain.data.json.JsonConverter;
import domain.data.json.LocalDateConverter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDate.class, new LocalDateConverter())
                .create();
        JsonConverter gsonConverter = new GsonConverter(gson);
        ContactDataSource contactDataSource = new ContactDataSource(gsonConverter);
        List<Contact> contactList = contactDataSource.readContact();

        if (contactList == null) {
            contactList = new ArrayList<>();
        }

        AppCommands appCommands = new AppCommands(contactDataSource, contactList);
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("""
                    -------- Список команд --------
                    0. Вихід
                    1. Додати контакт
                    2. Редагувати контакт
                    3. Видалити контакт
                    4. Показати всі контакти
                    5. Знайти контакт
                    """);
            String choice = scanner.nextLine();
            switch (choice) {
                case "0" -> {
                    System.out.println(">> Завершення роботи... ");
                    contactDataSource.writeContact(contactList);
                    System.exit(0);
                }
                case "1" -> {
                    System.out.println(">> Введіть ім'я контакту:");
                    String name = scanner.nextLine();
                    System.out.println(">> Введіть прізвище контакту:");
                    String surname = scanner.nextLine();
                    System.out.println(">> Введіть номер телефону контакту:");
                    String phoneNumber = scanner.nextLine();
                    System.out.println(">> Введіть електронну адресу контакту:");
                    String email = scanner.nextLine();
                    System.out.println(">> Введіть дату народження контакту (рік-місяць-день):");
                    LocalDate birthDate = LocalDate.parse(scanner.nextLine());
                    System.out.println(">> Введіть стосунок до контакту :" );
                    Relationships relationships = addRelationships(scanner);
                    System.out.println(">> Введіть ID контакту: ");
                    int id = scanner.nextInt();
                    Contact newContact = new Contact(name, surname, phoneNumber, email, birthDate, relationships, id);
                    appCommands.add(newContact);
                }
                case "2" -> {
                    System.out.println(">> Введіть контакт, який потрібно змінити");
                    String searchUpdate = scanner.nextLine();
                    List<Contact> searchResults = appCommands.search(searchUpdate);
                    if (searchResults.isEmpty()) {
                        System.out.println(">> Контакт не знайдено");
                    } else if (searchResults.size() > 1) {
                        System.out.println(">> Знайдено декілька контактів. Уточніть інформацію про контакт, який потрібно змінити");
                    } else {
                        Contact contactToUpdate = searchResults.get(0);
                        System.out.println(">> Введіть нове ім'я (або залиште порожнім, якщо не потрібно змінювати)");
                        String newName = scanner.nextLine().trim();
                        System.out.println(">> Введіть нове прізвище (або залиште порожнім, якщо не потрібно змінювати)");
                        String newSurname = scanner.nextLine().trim();
                        System.out.println(">> Введіть новий номер телефону (або залиште порожнім, якщо не потрібно змінювати)");
                        String newPhoneNumber = scanner.nextLine().trim();
                        System.out.println(">> Введіть новий email (або залиште порожнім, якщо не потрібно змінювати)");
                        String newEmail = scanner.nextLine().trim();
                        System.out.println(">> Введіть нову дату народження (у форматі yyyy-mm-dd або залиште порожнім, якщо не потрібно змінювати)");
                        String newBirthDateStr = scanner.nextLine().trim();
                        LocalDate newBirthDate = null;
                        if (!newBirthDateStr.isEmpty()) {
                            newBirthDate = LocalDate.parse(newBirthDateStr);
                        }
                        System.out.println(">> Введіть новий статус зв'язку (або залиште порожнім, якщо не потрібно змінювати)");
                        String newRelationshipStr = scanner.nextLine().trim();
                        Relationships newRelationship = null;
                        if (!newRelationshipStr.isEmpty()) {
                            newRelationship = Relationships.valueOf(newRelationshipStr);
                        }
                        appCommands.updateContact(
                                contactList,
                                newName.isEmpty() ? null : newName,
                                newSurname.isEmpty() ? null : newSurname,
                                newPhoneNumber.isEmpty() ? null : newPhoneNumber,
                                newEmail.isEmpty() ? null : newEmail,
                                newBirthDate,
                                newRelationship,
                                searchUpdate,
                                contactToUpdate.getId()
                        );
                        System.out.println(">> Контакт успішно оновлено!");
                    }
                }
                case "3" -> {

                    System.out.println(">> Введіть контакт, який потрібно видалити:");
                    String search_delete = scanner.nextLine();
                    List<Contact> searchResults = appCommands.search(search_delete);

                    if (searchResults.isEmpty()) {
                        System.out.println(">> Контакт не знайдено");
                    } else if (searchResults.size() > 1) {
                        System.out.println(">> Знайдено декілька контактів. Уточніть інформацію про контакт, який потрібно видалити:");
                    } else {
                        Contact contactToDelete = searchResults.get(0);
                        System.out.println(">> Ви впевнені, що хочете видалити наступний контакт:");
                        System.out.println(contactToDelete);
                        System.out.println(">> Введіть 'Так' або 'Ні':");
                        String confirmDelete = scanner.nextLine();

                        if (confirmDelete.equalsIgnoreCase("Так")) {
                            appCommands.delete(searchResults, contactToDelete.getId());
                            System.out.println(">> Контакт був успішно видалений.");
                        } else {
                            System.out.println(">> Видалення контакта скасовано.");
                        }
                    }
                }

                case "4" -> {
                    System.out.println("-------- Всі контакти --------");
                    for (Contact contact : contactDataSource.readContact()) {
                        System.out.println(contact);
                    }
                }
                case "5" -> {
                    System.out.println(">> Введіть текст для пошуку:");
                    String searchQuery = scanner.nextLine();
                    List<Contact> searchResults = appCommands.search(searchQuery);
                    if (searchResults.isEmpty()) {
                        System.out.println(">> Контакт не знайдено");
                    } else {
                        System.out.println(">> Знайдені наступні контакти:");
                        for (Contact contact : searchResults) {
                            System.out.println(contact);
                        }
                    }
                }

                default -> System.out.println(">> Не вірна команда. Спробуйте ще.");
            }
        }
    }
    public static Relationships addRelationships (Scanner scanner){
        Relationships relationships;
        System.out.println("""
                1. Siblings
                2. Parents
                3. Friends
                4. Boss
                5. Colleagues""");
        String choice = scanner.nextLine();
        switch (choice) {
            case "1" -> relationships = Relationships.SIBLINGS;
            case "2" -> relationships = Relationships.PARENTS;
            case "3" -> relationships = Relationships.FRIENDS;
            case "4" -> relationships = Relationships.BOSS;
            case "5" -> relationships = Relationships.COLLEAGUES;
            default -> relationships = null;
        }
        return relationships;
    }
}