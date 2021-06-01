package RESTAPIproject.classes;

import RESTAPIproject.declarations.OrderStatus;
import RESTAPIproject.declarations.Status;
import RESTAPIproject.declarations.ProductQuantity;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.UUID;

public class Order {
    private final ArrayList<ProductQuantity> items;
    private final int price; // In pennies
    private final UUID ID;
    private final UUID userID;
    private final Delivery delivery;

    private final ArrayList<OrderStatus> status;


    public Order(ArrayList<ProductQuantity> i, UUID uid, Delivery d) throws Shop.CustomException {
        status = new ArrayList<OrderStatus>();
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
        userID = uid;
        delivery = d;
        ID = UUID.randomUUID();

        status.add(new OrderStatus(Status.Ordered));

        price = calcPrice(i);
    }

    public Order(ArrayList<ProductQuantity> i, int price, UUID id, UUID userID, Delivery d, ArrayList<OrderStatus> s) throws Shop.CustomException {
        for(ProductQuantity p : i) {
            if(p.quantity <= 0) {
                throw new Shop.CustomException(p.quantity + " is not correct quantity", HttpStatus.BAD_REQUEST);
            }
            if(p.quantity > p.product.getAmount()) {
                throw new Shop.CustomException("Not enough products in store", HttpStatus.BAD_REQUEST);
            }
        }

        items = i;
        this.price = price;
        ID = id;
        this.userID = userID;
        delivery = d;
        status = s;
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

    public UUID getUserID() {
        return userID;
    }
}
