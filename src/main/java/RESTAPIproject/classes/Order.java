package RESTAPIproject.classes;

import RESTAPIproject.declarations.OrderStatus;
import RESTAPIproject.declarations.Status;
import RESTAPIproject.models.ProductQuantity;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.UUID;

public class Order {
    private final ArrayList<ProductQuantity> items;
    private final int price; // In pennies
    private final UUID ID;
    private final User user;
    private final Delivery delivery;

    private ArrayList<OrderStatus> status;


    public Order(ArrayList<ProductQuantity> i, User u, Delivery d) throws Shop.CustomException {
        for(ProductQuantity p : i) {
            if(p.quantity <= 0) {
                throw new Shop.CustomException(p.quantity + " is not correct quantity", HttpStatus.BAD_REQUEST);
            }
            if(p.quantity > p.product.getAmount()) {
                throw new Shop.CustomException("Not enough products in store", HttpStatus.BAD_REQUEST);
            }
        }

        for(ProductQuantity p : i) {
            p.product.setAmount(p.product.getAmount() - p.quantity);
        }
        items = i;
        user = u;
        delivery = d;
        ID = UUID.randomUUID();

        status.add(new OrderStatus(Status.Ordered));

        price = calcPrice(i);
    }

    /**
     * Zwraca produkty z zamowienia
     * @return ArrayList
     */
    public ArrayList<ProductQuantity> getItems() {
        return items;
    }

    /**
     * Zmienia status zamowienia
     * @param status status zamowienia
     */
    public void setStatus(Status status) {
        this.status.add(new OrderStatus(status));
    }

    /**
     * Liczy cene
     * @return int
     */
    private int calcPrice(ArrayList<ProductQuantity> ps) {
        int price = 0;

        for(ProductQuantity p : ps) {
            price += p.product.getPrice() * p.quantity;
        }

        return price;
    }

    /**
     * Zwraca cene
     * @return int
     */
    public int getPrice() {
        return price;
    }

    public UUID getID() {
        return ID;
    }

    public ArrayList<OrderStatus> getStatus() {
        return status;
    }

    public Delivery getDelivery() {
        return delivery;
    }

    public User getUser() {
        return user;
    }

    public UUID getUserID() {
        return user.getID();
    }
}
