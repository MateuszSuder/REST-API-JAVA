package RESTAPIproject.classes;

import RESTAPIproject.declarations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.io.*;
import java.time.LocalDateTime;
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

    /**
     * Zwraca użytkownika
     * @param id ID szukanego użytkownika
     * @return szukanego użytkownika
     * @throws CustomException
     */
    public User getUser(UUID id) throws CustomException {
        if(users.containsKey(id)) {
            return users.get(id);
        }
        throw new CustomException("User with id " + id + " doesn't exist", HttpStatus.NOT_FOUND);
    }

    /**
     *Getter zwracający globalną listę produktów
     * @return ConcurrentHashMap<UUID, Product>
     */
    public ConcurrentHashMap<UUID, Product> getProducts() {
        return products;
    }

    /**
     * Zwraca Produkt
     * @param id ID szukanego produktu
     * @return szukany produkt
     * @throws CustomException
     */
    public Product getProduct(UUID id) throws CustomException {
        if(products.containsKey(id)) {
            return products.get(id);
        }
        throw new CustomException("Product with id " + id + " doesn't exist", HttpStatus.NOT_FOUND);
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
            throw new CustomException("Order with id " + id + " doesn't exists", HttpStatus.NOT_FOUND);
        }
    }

    /**
     *Getter zwracający globalną listę firm
     * @return ConcurrentHashMap<UUID, Company>
     */
    public ConcurrentHashMap<UUID, Company> getCompanies() {
        return companies;
    }

    /**
     * Zwraca firme
     * @param id ID szukanej firmy
     * @return Firma
     * @throws CustomException
     */
    public Company getCompany(UUID id) throws CustomException {
        if(companies.containsKey(id)) {
            return companies.get(id);
        }
        throw new CustomException("Company with id " + id + " doesn't exist", HttpStatus.NOT_FOUND);
    }

    /**
     * Metoda do wyszukania kategorii o nazwie podanej w parametrze
     * @param catName nazwa kategorii
     * @return Category
     */
    public Category findCategory(String catName) throws CustomException {
        for(Category c : categories) {
            if(c.getName().equalsIgnoreCase(catName)) {
                return c;
            }
        }
        throw new CustomException("Category " + catName + " doesn't exist", HttpStatus.NOT_FOUND);
    }

    /**
     * Metody zapisujace aktualny stan sklepu do pliku CSV
     */
    public void saveUsersToFile() throws IOException {
        int i = 0;
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
            i++;
        }

        userWriter.flush();
        userWriter.close();
        addressWriter.flush();
        addressWriter.close();
        deliveryWriter.flush();
        deliveryWriter.close();

        logger.info(i + " users saved to file!");
    }

    public void saveCategoriesToFile() throws IOException {
        int i = 0;
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
            categoryWriter.append("\n");
            for(Product p : c.getProducts()) {
                product_categoryWriter.append(p.getID().toString());
                product_categoryWriter.append(",");
                product_categoryWriter.append(c.getName());
                product_categoryWriter.append("\n");
            }
            i++;
        }

        categoryWriter.flush();
        categoryWriter.close();

        product_categoryWriter.flush();
        product_categoryWriter.close();

        logger.info(i + " categories saved to file!");
    }

    public void saveCompaniesToFile() throws IOException {
        int i = 0;
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

            i++;
        }

        companyWriter.flush();
        companyWriter.close();

        logger.info(i + " companies saved to file!");
    }

    public void saveProductsToFile() throws IOException {
        int i = 0;
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
            i++;
        }

        productWriter.flush();
        productWriter.close();

        specificationWriter.flush();
        specificationWriter.close();

        logger.info(i + " products saved to file!");
    }

    public void saveOrdersToFile() throws IOException {
        int i = 0;
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
        orderStatusWriter.append(",");
        orderStatusWriter.append("Date");
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
            UUID id = o.getID();
            orderWriter.append(id.toString());
            orderWriter.append(",");
            orderWriter.append(Integer.toString(o.getPrice()));
            orderWriter.append(",");
            orderWriter.append(o.getUserID().toString());
            orderWriter.append("\n");

            for(OrderStatus s : o.getStatus()) {
                orderStatusWriter.append(id.toString());
                orderStatusWriter.append(",");
                orderStatusWriter.append(s.getStatus().toString());
                orderStatusWriter.append(",");
                orderStatusWriter.append(s.getDate().toString());
                orderStatusWriter.append("\n");
            }

            for(ProductQuantity p : o.getItems()) {
                orderProductsWriter.append(id.toString());
                orderProductsWriter.append(",");
                orderProductsWriter.append(p.product.getID().toString());
                orderProductsWriter.append(",");
                orderProductsWriter.append(Integer.toString(p.quantity));
                orderProductsWriter.append(",");
                orderProductsWriter.append("\n");
            }

            if(o.getDelivery() != null) {
                Delivery d = o.getDelivery();
                deliveryWriter.append(id.toString());
                deliveryWriter.append(",");
                deliveryWriter.append(d.getName());
                deliveryWriter.append(",");
                deliveryWriter.append(d.getLastName());
                deliveryWriter.append("\n");

                if(d.getAddress() != null) {
                    Address a = d.getAddress();
                    addressWriter.append(id.toString());
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
            i++;
        }

        orderWriter.flush();
        orderWriter.close();
        orderStatusWriter.flush();
        orderStatusWriter.close();
        orderProductsWriter.flush();
        orderProductsWriter.close();
        deliveryWriter.flush();
        deliveryWriter.close();
        addressWriter.flush();
        addressWriter.close();
        logger.info(i + " orders saved to file!");
    }

    /**
     * Metody pozyskujące zapisany stan sklepu z plików CSV
     */
    public void getCompaniesFromFile() {
        try {
            int i = 0;
            BufferedReader companiesReader = new BufferedReader(new FileReader("companies.csv"));

            String row;
            ArrayList<String[]> result = new ArrayList<>();
            while ((row = companiesReader.readLine()) != null) {
                result.add(row.split(","));
            }

            result.remove(0);

            for(String[] d : result) {
                UUID id = UUID.fromString(d[0]);
                String name = d[1];
                Company c = new Company(id, name);

                companies.putIfAbsent(id, c);
                i++;
            }

            companiesReader.close();
            logger.info(i + " companies loaded from file!");
        } catch(IOException e) {
            logger.error("Error while loading companies!");
            e.printStackTrace();
            return;
        }
    }

    public void getUsersFromFile() {
        try {
            int i = 0;
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

                if(d.length > 3 && d[3] != null) {
                    Company c = getCompany(UUID.fromString(d[3]));
                    u.setCompany(c);
                }

                users.putIfAbsent(id, u);

                i++;
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
                Delivery d = new Delivery();

                if(de.length > 1)
                d.setName(de[1]);

                if(de.length > 2) d.setLastName(de[2]);

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

            logger.info(i + " users loaded from file!");
        } catch(IOException e) {
            logger.error("Error while loading users!");
            e.printStackTrace();
            return;
        } catch (CustomException e) {
            logger.error(e.getMessage());
        }

    }

    public void getProductsFromFile() {
        try {
            int i = 0;
            BufferedReader productReader = new BufferedReader(new FileReader("products.csv"));
            BufferedReader specificationReader = new BufferedReader(new FileReader("specifications.csv"));

            String row;
            ArrayList<String[]> result = new ArrayList<>();
            while ((row = productReader.readLine()) != null) {
                result.add(row.split(","));
            }

            result.remove(0);


            for(String[] d : result) {
                UUID id = UUID.fromString(d[0]);
                String name = d[1];
                Product p = new Product(id, name);

                String des = d[2];
                int price = Integer.parseInt(d[3]);
                int amount = Integer.parseInt(d[4]);

                p.setDescription(des);
                p.setPrice(price);
                p.setAmount(amount);

                products.putIfAbsent(id, p);

                i++;
            }

            productReader.close();

            row = "";
            result = new ArrayList<>();

            while ((row = specificationReader.readLine()) != null) {
                result.add(row.split(","));
            }

            result.remove(0);


            for(String[] d : result) {
                UUID id = UUID.fromString(d[0]);
                try {
                    Product p = getProduct(id);
                    String k = d[1];
                    String v = d[2];

                    Specification s = new Specification();
                    s.key = k;
                    s.val = v;

                    p.addToSpec(s);
                } catch (CustomException e) {
                    e.printStackTrace();
                }
            }

            specificationReader.close();

            logger.info(i + " products loaded from file!");
        } catch (IOException e) {
            logger.error("Error while loading products!");
            e.printStackTrace();
            return;
        }

    }

    public void getCategoriesFromFile() {
        try {
            int i = 0;
            BufferedReader categoriesReader = new BufferedReader(new FileReader("category.csv"));
            BufferedReader product_categoryReader = new BufferedReader(new FileReader("products_category.csv"));

            String row;
            ArrayList<String[]> result = new ArrayList<>();
            while ((row = categoriesReader.readLine()) != null) {
                result.add(row.split(","));
            }

            result.remove(0);

            for(String[] d : result) {
                String name = d[0];

                Category c = new Category(name);
                categories.add(c);

                i++;
            }

            categoriesReader.close();

            row = "";
            result = new ArrayList<>();
            while ((row = product_categoryReader.readLine()) != null) {
                result.add(row.split(","));
            }

            result.remove(0);

            for(String[] d : result) {
                Product p = getProduct(UUID.fromString(d[0]));
                Category c = findCategory(d[1]);
                c.addProducts(p);
            }

            product_categoryReader.close();

            logger.info(i + " companies loaded from file!");
        } catch(IOException e) {
            logger.error("Error while loading categories!");
            e.printStackTrace();
            return;
        } catch (CustomException e) {
            logger.error(e.getMessage());
        }
    }

    public void getOrdersFromFile() {
        try {
            int i = 0;
            BufferedReader ordersReader = new BufferedReader(new FileReader("orders.csv"));

            String row;
            ArrayList<String[]> result = new ArrayList<>();
            while ((row = ordersReader.readLine()) != null) {
                result.add(row.split(","));
            }

            result.remove(0);

            for(String[] d : result) {
                UUID id = UUID.fromString(d[0]);
                int price = Integer.parseInt(d[1]);
                UUID userid = UUID.fromString(d[2]);

                Order o = new Order(id, price, userid);

                User u = getUser(userid);

                u.addOrder(o);
                orders.putIfAbsent(id, o);

                i++;
            }

            ordersReader.close();

            row = "";
            result = new ArrayList<>();
            BufferedReader orders_statusesReader = new BufferedReader(new FileReader("orders_statuses.csv"));
            while ((row = orders_statusesReader.readLine()) != null) {
                result.add(row.split(","));
            }

            result.remove(0);

            for(String[] d : result) {
                UUID orderid = UUID.fromString(d[0]);
                Status status = Status.valueOf(d[1]);
                LocalDateTime date = LocalDateTime.parse(d[2]);

                OrderStatus os = new OrderStatus(status, date);

                Order o = getOrder(orderid);
                o.setOrderStatus(os);
            }

            orders_statusesReader.close();

            row = "";
            result = new ArrayList<>();
            BufferedReader order_productsReader = new BufferedReader(new FileReader("orders_products.csv"));
            while ((row = order_productsReader.readLine()) != null) {
                result.add(row.split(","));
            }

            result.remove(0);

            for(String[] d : result) {
                UUID orderid = UUID.fromString(d[0]);
                UUID productid = UUID.fromString(d[1]);
                int quantity = Integer.parseInt(d[2]);

                Product p = getProduct(productid);
                ProductQuantity pq = new ProductQuantity();
                pq.product = p;
                pq.quantity = quantity;

                Order o = getOrder(orderid);
                o.addItem(pq);
            }

            order_productsReader.close();

            row = "";
            result = new ArrayList<>();
            BufferedReader order_deliveriesReader = new BufferedReader(new FileReader("order_deliveries.csv"));
            while ((row = order_deliveriesReader.readLine()) != null) {
                result.add(row.split(","));
            }

            result.remove(0);

            for(String[] d : result) {
                UUID orderid = UUID.fromString(d[0]);
                String name = d[1];
                String lastName = d[2];

                Order o = getOrder(orderid);

                Delivery de = new Delivery(name, lastName);

                o.setDelivery(de);
            }

            order_deliveriesReader.close();

            row = "";
            result = new ArrayList<>();
            BufferedReader order_addressesReader = new BufferedReader(new FileReader("order_addresses.csv"));
            while ((row = order_addressesReader.readLine()) != null) {
                result.add(row.split(","));
            }

            result.remove(0);

            for(String[] d : result) {
                UUID orderid = UUID.fromString(d[0]);
                String pc = d[1];
                String c = d[2];
                String st = d[3];
                String nr = d[4];
                String co = d[5];

                Address a = new Address(c, co, nr, pc, st);

                Order o = getOrder(orderid);

                o.getDelivery().setAddress(a);
            }

            order_addressesReader.close();

            logger.info(i + " orders loaded from file!");
        } catch(IOException e) {
            logger.error("Error while loading orders!");
            e.printStackTrace();
            return;
        } catch (CustomException e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * Metoda inicjalizująca
     */
    public void initializeShop() {
        getCompaniesFromFile();
        getUsersFromFile();
        getProductsFromFile();
        getCategoriesFromFile();
        getOrdersFromFile();
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
