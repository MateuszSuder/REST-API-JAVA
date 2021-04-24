package RESTAPIproject;

import java.util.ArrayList;

public class Category {
    private ArrayList<Category> subCategories;
    private ArrayList<Product> products;

    private String name;

    public ArrayList<Category> getSubCategories() {
        return subCategories;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public String getName() {
        return name;
    }
}
