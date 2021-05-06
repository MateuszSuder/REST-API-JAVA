package RESTAPIproject.classes;

import java.util.ArrayList;

public class Category {
    private ArrayList<Product> products;

    private String name;

    public Category(String categoryName) {
        name = categoryName;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addProducts(ArrayList<Product> products) {
        this.products.addAll(products);
    }

    public void addProducts(Product product) {
        this.products.add(product);
    }
}
