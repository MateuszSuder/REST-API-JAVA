package RESTAPIproject;

import java.util.UUID;

enum Status {
    Ordered,
    PaymentDone,
    Shipped,
    Delivered
}

public class Order {
    private Product[] items;
    private User user;
    private Delivery delivery;

    private int price; // In pennies
    private Status status;
    private UUID ID;

    Order() {}

    public Product[] getItems() {
        return items;
    }

    public void addItem(Product p) {}

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    private int calcPrice() {
        int price = 0;
        return price;
    }

    public int getPrice() {
        return price;
    }
}
