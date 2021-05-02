package RESTAPIproject.classes;

import RESTAPIproject.declarations.Permission;
import RESTAPIproject.models.UserMutationInput;
import org.springframework.http.HttpStatus;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Shop {
    private ArrayList<Category> categories;
    private ConcurrentHashMap<UUID, User> users;
    private ConcurrentHashMap<UUID, Company> companies;


    public Shop() {
        categories = new ArrayList<Category>();

        users = new ConcurrentHashMap<UUID, User>();
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public ConcurrentHashMap getUsers() {
        return users;
    }

    public User getUser(UUID id) throws CustomException {
        if(users.containsKey(id)) {
            return users.get(id);
        } else {
            throw new CustomException("User not found", HttpStatus.NOT_FOUND);
        }
    }

    public UUID addUser(String username) throws CustomException {
        for (Map.Entry<UUID, User> entry : users.entrySet()) {
            UUID k = entry.getKey();
            User v = entry.getValue();
            if (v.getUsername().equals(username)) {
                throw new CustomException("User with same username already exists", HttpStatus.CONFLICT);
            }
        }
        User user = new User(username);
        UUID id = user.getID();

        users.putIfAbsent(id, user);

        return id;
    }

    public void modifyUser(UUID userID, UserMutationInput input) throws CustomException {
        try {
            User u = getUser(userID);

            if(input.companyID != null) {
                if(companies.containsKey(input.companyID)) {
                    u.setCompany(companies.get(input.companyID));
                } else {
                    throw new CustomException("Company not found", HttpStatus.NOT_FOUND);
                }
            }

            if(input.deliveryDetails != null) {
                u.setDeliverDetails(input.deliveryDetails);
            }

            if(input.permission != null) {
                u.setPermission(input.permission);
            }
        } catch (CustomException e) {
            throw new CustomException(e.getMessage(), e.getStatus());
        }
    }

    public static boolean contains(String test) {
        for (Permission p : Permission.values()) {
            if (p.name().equals(test)) {
                return true;
            }
        }
        return false;
    }

    public void addCategory(Category cat) {
        categories.add(cat);
    }

    public void initializeShop() {}

    public class CustomException extends Exception {
        HttpStatus status;
        public CustomException(String msg, HttpStatus status) {
            super(msg);
            this.status = status;
        }

        public HttpStatus getStatus() {
            return status;
        }
    }
}
