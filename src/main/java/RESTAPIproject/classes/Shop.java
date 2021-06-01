package RESTAPIproject.classes;

import RESTAPIproject.declarations.Permission;
import RESTAPIproject.declarations.Specification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Shop {
    private ArrayList<Category> categories;
    private ConcurrentHashMap<UUID, User> users;
    private ConcurrentHashMap<UUID, Company> companies;
    private ConcurrentHashMap<UUID, Product> products;
    private ConcurrentHashMap<UUID, Order> orders;

    public Logger logger = LoggerFactory.getLogger(Shop.class);

    /**
     * Konstruktor
     */
    public Shop() {
        categories = new ArrayList<Category>();
        users = new ConcurrentHashMap<UUID, User>();
        companies = new ConcurrentHashMap<UUID, Company>();
        products = new ConcurrentHashMap<UUID, Product>();
        orders = new ConcurrentHashMap<UUID, Order>();

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

    public User getUser(UUID id) throws CustomException {
        if(users.containsKey(id)) {
            return users.get(id);
        }
        throw new CustomException("User with given id doesn't exist", HttpStatus.NOT_FOUND);
    }

    /**
     *Getter zwracający globalną listę produktów
     * @return ConcurrentHashMap<UUID, Product>
     */
    public ConcurrentHashMap<UUID, Product> getProducts() {
        return products;
    }

    public Product getProduct(UUID id) throws CustomException {
        if(products.containsKey(id)) {
            return products.get(id);
        }
        throw new CustomException("Product with given id doesn't exist", HttpStatus.NOT_FOUND);
    }

    /**
     *Getter zwracający globalną listę zamowien
     * @return ConcurrentHashMap<UUID, Product>
     */
    public ConcurrentHashMap<UUID, Order> getOrders() {
        return orders;
    }

    /**
     *Getter zwracający zamowienie z id podanym w argumencie
     * @param id ID zamowienia
     * @return Order
     */
    public Order getOrder(UUID id) throws CustomException {
        if(orders.containsKey(id)) {
            return orders.get(id);
        } else {
            throw new CustomException("Order with given id doesn't exists", HttpStatus.NOT_FOUND);
        }
    }

    /**
     *Getter zwracający globalną listę firm
     * @return ConcurrentHashMap<UUID, Company>
     */
    public ConcurrentHashMap<UUID, Company> getCompanies() {
        return companies;
    }

    public Company getCompany(UUID id) throws CustomException {
        if(companies.containsKey(id)) {
            return companies.get(id);
        }
        throw new CustomException("Company with given id doesn't exist", HttpStatus.NOT_FOUND);
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
        userWriter.append(",");
        userWriter.append("CompanyID");
        userWriter.append("\n");

        FileWriter deliveryWriter = new FileWriter("user_deliveries.csv");
        deliveryWriter.append("userID");
        deliveryWriter.append(",");
        deliveryWriter.append("Name");
        deliveryWriter.append(",");
        deliveryWriter.append("LastName");
        deliveryWriter.append("\n");

        FileWriter addressWriter = new FileWriter("user_addresses.csv");
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
            userWriter.append(",");
            if(u.getCompany() != null) {
                userWriter.append(u.getCompany().getID().toString());
            } else {
                userWriter.append("");
            }

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

    public void saveCategoriesToFile() throws IOException {
        FileWriter categoryWriter = new FileWriter("category.csv");
        FileWriter product_categoryWriter = new FileWriter("products_category.csv"); // Zapisuje w osobnym pliku - mniej problemow
        categoryWriter.append("Name");
        categoryWriter.append("\n");

        product_categoryWriter.append("ID");
        product_categoryWriter.append(",");
        product_categoryWriter.append("Category");
        product_categoryWriter.append("\n");

        for(Category c : this.categories) {
            categoryWriter.append(c.getName());
            for(Product p : c.getProducts()) {
                product_categoryWriter.append(p.getID().toString());
                product_categoryWriter.append(",");
                product_categoryWriter.append(c.getName());
                product_categoryWriter.append("\n");
            }
        }

        categoryWriter.flush();
        categoryWriter.close();

        product_categoryWriter.flush();
        product_categoryWriter.close();
    }

    public void saveCompaniesToFile() throws IOException {
        FileWriter companyWriter = new FileWriter("companies.csv");
        companyWriter.append("ID");
        companyWriter.append(",");
        companyWriter.append("Name");
        companyWriter.append("\n");

        for(Company c : this.companies.values()) {
            companyWriter.append(c.getID().toString());
            companyWriter.append(",");
            companyWriter.append(c.getName());
            companyWriter.append("\n");
        }

        companyWriter.flush();
        companyWriter.close();
    }

    public void saveProductsToFile() throws IOException {
        FileWriter productWriter = new FileWriter("products.csv");
        FileWriter specificationWriter = new FileWriter("specifications.csv");
        productWriter.append("ID");
        productWriter.append(",");
        productWriter.append("Name");
        productWriter.append(",");
        productWriter.append("Description");
        productWriter.append(",");
        productWriter.append("Price");
        productWriter.append(",");
        productWriter.append("Amount");
        productWriter.append("\n");

        specificationWriter.append("ID");
        specificationWriter.append(",");
        specificationWriter.append("Key");
        specificationWriter.append(",");
        specificationWriter.append("Value");
        specificationWriter.append("\n");

        for(Product p : this.products.values()) {
            productWriter.append(p.getID().toString());
            productWriter.append(",");
            productWriter.append(p.getName());
            productWriter.append(",");
            productWriter.append(p.getDescription());
            productWriter.append(",");
            productWriter.append(Integer.toString(p.getPrice()));
            productWriter.append(",");
            productWriter.append(Integer.toString(p.getAmount()));
            productWriter.append("\n");

            if(p.getSpecification() != null) {
                for(Specification s : p.getSpecification()) {
                    specificationWriter.append(p.getID().toString());
                    specificationWriter.append(",");
                    specificationWriter.append(s.key);
                    specificationWriter.append(",");
                    specificationWriter.append(s.val);
                    specificationWriter.append("\n");
                }
            }
        }

        productWriter.flush();
        productWriter.close();

        specificationWriter.flush();
        specificationWriter.close();
    }

    public void saveOrdersToFile() throws IOException {
        FileWriter orderWriter = new FileWriter("orders.csv");
        orderWriter.append("ID");
        orderWriter.append(",");
        orderWriter.append("Price");
        orderWriter.append(",");
        orderWriter.append("userID");
        orderWriter.append("\n");

        FileWriter orderStatusWriter = new FileWriter("orders_statuses.csv");
        orderStatusWriter.append("orderID");
        orderStatusWriter.append(",");
        orderStatusWriter.append("Status");
        orderStatusWriter.append("\n");

        FileWriter orderProductsWriter = new FileWriter("orders_products.csv");
        orderProductsWriter.append("orderID");
        orderProductsWriter.append(",");
        orderProductsWriter.append("productID");
        orderProductsWriter.append(",");
        orderProductsWriter.append("Quantity");
        orderProductsWriter.append(",");
        orderProductsWriter.append("\n");

        FileWriter deliveryWriter = new FileWriter("order_deliveries.csv");
        deliveryWriter.append("orderID");
        deliveryWriter.append(",");
        deliveryWriter.append("Name");
        deliveryWriter.append(",");
        deliveryWriter.append("LastName");
        deliveryWriter.append("\n");

        FileWriter addressWriter = new FileWriter("order_addresses.csv");
        addressWriter.append("orderID");
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

        for(Order o : this.orders.values()) {
            orderWriter.append(o.getID().toString());
            orderWriter.append(",");
            orderWriter.append(Integer.toString(o.getPrice()));
            orderWriter.append(",");
            orderWriter.append(o.getUserID().toString());
            orderWriter.append("\n");
        }
    }

    /**
     * Metody pozyskujące zapisany stan sklepu z plików CSV
     */
    public void getCompaniesFromFile() throws IOException {}

    public void getUsersFromFile() throws IOException {
        try {
            BufferedReader userReader = new BufferedReader(new FileReader("users.csv"));
            BufferedReader deliveryReader = new BufferedReader(new FileReader("user_deliveries.csv"));
            BufferedReader addressReader = new BufferedReader(new FileReader("user_addresses.csv"));

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
        } catch(Exception e) {
            e.printStackTrace();
            return;
        }

    }

    public void getProductsFromFile() throws IOException {}

    public void getCategoriesFromFile() throws IOException {}

    public void getOrdersFromFile() throws IOException {}

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
