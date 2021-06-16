package RESTAPIproject.classes;

import java.util.ArrayList;

public class Category {
    private ArrayList<Product> products;

    private String name;

    /**
     * Konstruktor
     * @param categoryName nazwa kategorii
     */
    public Category(String categoryName) {
        name = categoryName;

        products = new ArrayList<>();
    }

    /**
     * Zwraca list produktow w kategorii
     * @return ArrayList<Product>
     */
    public ArrayList<Product> getProducts() {
        return products;
    }

    /**
     * Ustawia nazwe produktu
     * @param name nazwa produktu
     * @return void
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Zwraca nazwe kategorii
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * Dodaje produkty do kategorii
     * @param products lista produktow do dodania
     * @return void
     */
    public void addProducts(ArrayList<Product> products) {
        this.products.addAll(products);
    }

    /**
     * Dodaje pojedynczy produkt do kategorii
     * @param product produkt do dodania
     */
    public void addProducts(Product product) {
        this.products.add(product);
    }

    /**
     * Usuwa produkty do kategorii
     * @param products lista produktow do dodania
     * @return void
     */
    public void removeProducts(ArrayList<Product> products) {
        this.products.removeAll(products);
    }

    /**
     * Usuwa pojedynczy produkt do kategorii
     * @param product produkt do dodania
     */
    public void removeProduct(Product product) {
        this.products.remove(product);
    }
}
