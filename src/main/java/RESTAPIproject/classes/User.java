package RESTAPIproject.classes;

import RESTAPIproject.declarations.Permission;

import java.util.ArrayList;
import java.util.UUID;

public class User implements java.io.Serializable {
    private ArrayList<Order> orders;
    private Delivery deliverDetails;
    private Company company;

    private String username;
    private Permission permission;
    private final UUID ID;

    public User(String username) {
        this.username = username;
        this.permission = Permission.user;

        ID = UUID.randomUUID();
    }

    public User(String username, Permission permission, UUID id) {
        this.username = username;
        this.permission = permission;
        this.ID = id;
    }

    /**
     * Zwraca liste zamowien uzytkownika
     * @return ArrayList
     */
    public ArrayList<Order> getOrders() {
        return orders;
    }

    /**
     * Zwraca firme uzytkownika
     * @return Company
     */
    public Company getCompany() {
        return company;
    }

    /**
     * Ustawia firme uzytkownikowi
     * @param company dane firmy
     */
    public void setCompany(Company company) {
        this.company = company;
    }

    /**
     * Zwraca dane dostawy uzytkownika
     * @return Delivery
     */
    public Delivery getDeliverDetails() {
        return deliverDetails;
    }

    /**
     * Ustawia dane dostawy uzytkownika
     * @param deliverDetails dane dostawy
     */
    public void setDeliverDetails(Delivery deliverDetails) {
        this.deliverDetails = deliverDetails;
    }

    /**
     * Zwraca prawa uzytkownika
     * @return Permission
     */
    public Permission getPermission() {
        return permission;
    }

    /**
     * Ustawia prawa uzytkownika
     * @param p prawa
     */
    public void setPermission(Permission p) {
        this.permission = p;
    }

    /**
     * Zwraca nazwe uzytkownika
     * @return String
     */
    public String getUsername() {
        return username;
    }

    /**
     * Zwraca ID uzytkownika
     * @return UUID
     */
    public UUID getID() {
        return ID;
    }
}
