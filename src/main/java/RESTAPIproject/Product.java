package RESTAPIproject;


import java.util.ArrayList;
import java.util.UUID;

public class Product {
    private String name;
    private int price; // In pennies
    private String description;
    private ArrayList<Object> specification;
    private int Amount;
    private UUID ID;

    public Product getProduct() {
        return this;
    }

    Product(
            String name,
            int price,
            String description,
            ArrayList<Object> specification,
            int Amount
    ) {

    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getAmount() {
        return Amount;
    }

    public void setAmount(int amount) {
        Amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<Object> getSpecification() {
        return specification;
    }

    public void setSpecification(ArrayList<Object> specification) {
        this.specification = specification;
    }
}
