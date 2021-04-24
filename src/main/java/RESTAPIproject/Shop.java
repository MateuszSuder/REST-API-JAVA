package RESTAPIproject;

import java.util.ArrayList;
import java.util.UUID;

public class Shop {
    private ArrayList<Category> categories;
    private ArrayList<User> users;

    Shop() {
        categories = new ArrayList<Category>();
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public User getUser(UUID id) {
        return users.get(0);
    }

    public void addUser(User user) {
        users.add(user);
    }

    public void addCategory(Category cat) {
        categories.add(cat);
    }

    public void initalizeShop() {}
}
