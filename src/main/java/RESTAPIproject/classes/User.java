package RESTAPIproject.classes;

import RESTAPIproject.declarations.Permission;

import java.util.ArrayList;
import java.util.UUID;

public class User {
    private ArrayList<Order> orders;
    private Delivery deliverDetails;
    private Company company;

    private String username;
    private Permission permission;
    private UUID ID;

    public User(String username) {
        this.username = username;
        this.permission = Permission.user;

        ID = UUID.randomUUID();
    }

    public ArrayList<Order> getOrders() {
        return orders;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Delivery getDeliverDetails() {
        return deliverDetails;
    }

    public void setDeliverDetails(Delivery deliverDetails) {
        this.deliverDetails = deliverDetails;
    }

    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission p) {
        this.permission = p;
    }

    public String getUsername() {
        return username;
    }

    public UUID getID() {
        return ID;
    }
}
