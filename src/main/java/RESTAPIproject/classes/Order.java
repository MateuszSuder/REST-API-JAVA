package RESTAPIproject.classes;

import java.util.ArrayList;
import java.util.UUID;

enum Status {
    WaitingForCompletion,
    Ordered,
    PaymentDone,
    Shipped,
    Delivered
}

public class Order {
    private ArrayList<Product> items;
    private User user;
    private Delivery delivery;

    private int price; // In pennies
    private Status status;
    private UUID ID;

    Order() {}

    /**
     * Zwraca proudkty z zamowienia
     * @return ArrayList
     */
    public ArrayList<Product> getItems() {
        return items;
    }

    /**
     * Dodaje pojedynczy produkt do zamowienia
     * @param p produkt do dodania do zamowienia
     */
    public void addItems(Product p) {}

    /**
     * Dodaje liste produktow do zamowienia
     * @param p lista produktow do dodania do zamowienia
     */
    public void addItems(ArrayList<Product> p) {}

    /**
     * Ustawia dane do dostawy
     * @param delivery dane dostawy
     */
    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
    }

    /**
     * Zmienia status zamowienia
     * @param status status zamowienia
     */
    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * Ustawia cene
     * @param p cena
     */
    private void setPrice(int p){}

    /**
     * Liczy cene
     * @return int
     */
    private int calcPrice() {
        int price = 0;
        return price;
    }

    /**
     * Zwraca cene
     * @return int
     */
    public int getPrice() {
        return price;
    }
}
