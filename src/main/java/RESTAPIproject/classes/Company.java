package RESTAPIproject.classes;

import java.util.UUID;

public class Company {
    private String name;
    private UUID ID;

    /**
     * Konstruktor
     * @param n nazwa firmy
     */
    public Company(String n) {
        name = n;
        ID = UUID.randomUUID();
    }

    /**
     * Konstruktor potrzebny podczas wczytywania firm z pliku
     * @param i ID firmy
     * @param n Nazwa firmy
     */
    public Company(UUID i, String n) {
        ID = i;
        name = n;
    }

    /**
     * Zwraca nazwe firmy
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * Ustawia nazwe firmy
     * @param name nazwa firmy
     * @return void
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Zwraca id firmy
     * @return company's id
     */
    public UUID getID() {
        return ID;
    }
}
