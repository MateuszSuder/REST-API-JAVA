package RESTAPIproject.classes;

import java.util.ArrayList;

public class Category {
    private ArrayList<Product> products;

    private String name;

    public Category(String categoryName) {
        name = categoryName;
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
}
