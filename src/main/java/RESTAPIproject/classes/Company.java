package RESTAPIproject.classes;

import java.util.UUID;

public class Company {
    private String name;
    private UUID ID;

    public Company(String n) {
        name = n;
        ID = UUID.randomUUID();
    }

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

    public UUID getID() {
        return ID;
    }
}
