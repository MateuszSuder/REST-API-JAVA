package RESTAPIproject.classes;

import java.util.ArrayList;
import java.util.UUID;

enum Permission {
    user,
    admin
}

public class User {
    private ArrayList<Order> orders;
    private Delivery deliverDetails;
    private Company company;

    private String Username;
    private Permission permission;
    private UUID ID;

    User() {}

    public ArrayList<Order> getOrders() {
        return orders;
    }

    public Company getCompany() {
        return company;
    }

    public Delivery getDeliverDetails() {
        return deliverDetails;
    }

    public Permission getPermission() {
        return permission;
    }

    public String getUsername() {
        return Username;
    }
}
