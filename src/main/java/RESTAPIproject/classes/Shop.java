package RESTAPIproject.classes;

import RESTAPIproject.declarations.Permission;
import org.springframework.http.HttpStatus;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Shop {
    private ArrayList<Category> categories;
    private ConcurrentHashMap<UUID, User> users;
    private ConcurrentHashMap<UUID, Company> companies;
    private ConcurrentHashMap<UUID, Product> products;

    /**
     * Konstruktor
     */
    public Shop() {
        categories = new ArrayList<Category>();

        users = new ConcurrentHashMap<UUID, User>();

        initializeShop();
    }

    /**
     *Getter zwracający globalną listę kategorii
     * @return ArrayList<Category>
     */
    public ArrayList<Category> getCategories() {
        return categories;
    }

    /**
     * Getter zwracający wszystkich dostępnych użytkowników
     * @return ConcurrentHashMap<UUID, User>
     */
    public ConcurrentHashMap<UUID, User> getUsers() {
        return users;
    }

    public User getUser(UUID id) {
        return users.get(id);
    }

    /**
     *Getter zwracający globalną listę produktów
     * @return ConcurrentHashMap<UUID, Product>
     */
    public ConcurrentHashMap<UUID, Product> getProducts() {
        return products;
    }

    /**
     *Getter zwracający globalną listę firm
     * @return ConcurrentHashMap<UUID, Company>
     */
    public ConcurrentHashMap<UUID, Company> getCompanies() {
        return companies;
    }

    /**
     * Metoda do wyszukania kategorii o nazwie podanej w parametrze
     * @param catName nazwa kategorii
     * @return Category
     */
    public Category findCategory(String catName) throws CustomException {
        for(Category c : categories) {
            if(c.getName().equals(catName)) {
                return c;
            }
        }
        throw new CustomException("Category doesn't exist", HttpStatus.NOT_FOUND);
    }

    /**
     * Metody zapisujace aktualny stan sklepu do pliku CSV
     */
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

    public void saveCategoriesToFile() throws IOException {}

    public void saveCompaniesToFile() throws IOException {}

    public void saveProductsToFile() throws IOException {}

    /**
     * Metody pozyskujące zapisany stan sklepu z pliku CSV
     */
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

    public void getCategoriesFromFile() throws IOException {}

    public void getCompaniesFromFile() throws IOException {}

    public void getProductsFromFile() throws IOException {}

    /**
     * Metoda inicjalizująca
     */
    public void initializeShop() {
        try {
            getUsersFromFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Pomocnicza klasa do zarządzania specjalnymi wyjątkami
     */
    public static class CustomException extends Exception {
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
