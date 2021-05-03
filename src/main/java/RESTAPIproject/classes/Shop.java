package RESTAPIproject.classes;

import RESTAPIproject.declarations.Permission;
import RESTAPIproject.models.UserMutationInput;
import com.opencsv.CSVReader;
import org.springframework.http.HttpStatus;

import java.io.*;
import java.lang.reflect.InvocationHandler;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class Shop {
    private ArrayList<Category> categories;
    private ConcurrentHashMap<UUID, User> users;
    private ConcurrentHashMap<UUID, Company> companies;

    static private final String pathToFile = "test.txt";


    public Shop() {
        categories = new ArrayList<Category>();

        users = new ConcurrentHashMap<UUID, User>();

        initializeShop();
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

        try {
            saveUsersToFile();
        } catch(IOException e) {
            e.printStackTrace();
            throw new CustomException("Error while saving file", HttpStatus.INTERNAL_SERVER_ERROR);
        }

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

            try {
                saveUsersToFile();
            } catch(IOException e) {
                e.printStackTrace();
                throw new CustomException("Error while saving file", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (CustomException e) {
            throw new CustomException(e.getMessage(), e.getStatus());
        }
    }

    public void addCategory(Category cat) {
        categories.add(cat);
    }

    public void saveUsersToFile() throws IOException {
        FileWriter userWriter = new FileWriter("users.csv");
        userWriter.append("ID");
        userWriter.append(",");
        userWriter.append("Username");
        userWriter.append(",");
        userWriter.append("Permission");
        userWriter.append("\n");

        FileWriter deliveryWriter = new FileWriter("deliveries.csv");
        deliveryWriter.append("userID");
        deliveryWriter.append(",");
        deliveryWriter.append("Name");
        deliveryWriter.append(",");
        deliveryWriter.append("LastName");
        deliveryWriter.append("\n");

        FileWriter addressWriter = new FileWriter("addresses.csv");
        addressWriter.append("userID");
        addressWriter.append(",");
        addressWriter.append("Postcode");
        addressWriter.append(",");
        addressWriter.append("City");
        addressWriter.append(",");
        addressWriter.append("Street");
        addressWriter.append(",");
        addressWriter.append("Number");
        addressWriter.append(",");
        addressWriter.append("Country");
        addressWriter.append("\n");

        for(User u : users.values()) {
            userWriter.append(u.getID().toString());
            userWriter.append(",");
            userWriter.append(u.getUsername());
            userWriter.append(",");
            userWriter.append(u.getPermission().toString());
            userWriter.append("\n");

            if(u.getDeliverDetails() != null) {
                deliveryWriter.append(u.getID().toString());
                deliveryWriter.append(",");
                deliveryWriter.append(u.getDeliverDetails().getName());
                deliveryWriter.append(",");
                deliveryWriter.append(u.getDeliverDetails().getLastName());
                deliveryWriter.append("\n");

                if(u.getDeliverDetails().getAddress() != null) {
                    Address a = u.getDeliverDetails().getAddress();
                    addressWriter.append(u.getID().toString());
                    addressWriter.append(",");
                    addressWriter.append(a.getPostcode());
                    addressWriter.append(",");
                    addressWriter.append(a.getCity());
                    addressWriter.append(",");
                    addressWriter.append(a.getStreet());
                    addressWriter.append(",");
                    addressWriter.append(a.getNumber());
                    addressWriter.append(",");
                    addressWriter.append(a.getCountry());
                    addressWriter.append("\n");
                    a = null;
                    System.gc();
                }
            }
        }

        userWriter.flush();
        userWriter.close();
        addressWriter.flush();
        addressWriter.close();
        deliveryWriter.flush();
        deliveryWriter.close();
    }

    public void getUsersFromFile() throws IOException {
        BufferedReader userReader = new BufferedReader(new FileReader("users.csv"));
        BufferedReader deliveryReader = new BufferedReader(new FileReader("deliveries.csv"));
        BufferedReader addressReader = new BufferedReader(new FileReader("addresses.csv"));

        /**
         * Users
         **/

        String row;
        ArrayList<String[]> result = new ArrayList<>();
        while ((row = userReader.readLine()) != null) {
            result.add(row.split(","));
        }

        result.remove(0);
        for(String[] d : result) {
            UUID id = UUID.fromString(d[0]);
            String username = d[1];
            Permission permission = Permission.valueOf(d[2]);

            User u = new User(username, permission, id);
            users.putIfAbsent(id, u);
        }

        /**
         * Deliveries
         **/

        row = "";
        result = new ArrayList<>();
        while ((row = deliveryReader.readLine()) != null) {
            result.add(row.split(","));
        }

        result.remove(0);
        for(String[] de : result) {
            UUID id = UUID.fromString(de[0]);
            String name = de[1];
            String lastName = de[2];

            Delivery d = new Delivery(name, lastName);

            users.get(id).setDeliverDetails(d);
        }

        /**
         * Addresses
         **/

        row = "";
        result = new ArrayList<>();
        while ((row = addressReader.readLine()) != null) {
            result.add(row.split(","));
        }

        result.remove(0);
        for(String[] de : result) {
            UUID id = UUID.fromString(de[0]);
            String p = de[1];
            String c = de[2];
            String s = de[3];
            String n = de[4];
            String co = de[5];

            Address a = new Address(c, co, n, p, s);

            users.get(id).getDeliverDetails().setAddress(a);
        }
        row = "";


        addressReader.close();
        deliveryReader.close();
        userReader.close();
    }

    public void initializeShop() {
        try {
            getUsersFromFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
