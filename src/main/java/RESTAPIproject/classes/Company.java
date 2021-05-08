package RESTAPIproject.classes;

import java.util.UUID;

public class Company {
    private String name;
    private UUID ID;

    public Company(String n) {
        this.name = n;
        this.ID = UUID.randomUUID();
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
}
