package RESTAPIproject.classes;

import java.util.UUID;

public class Company {
    private String name;
    private UUID ID;

    Company() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
