package RESTAPIproject.classes;

import java.util.UUID;

public class Company {
    private String name;
    private UUID ID;

    public Company(String n) {
        this.name = n;
        this.ID = UUID.randomUUID();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
