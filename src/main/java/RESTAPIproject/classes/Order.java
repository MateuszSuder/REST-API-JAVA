package RESTAPIproject.classes;

import RESTAPIproject.declarations.OrderStatus;
import RESTAPIproject.declarations.Status;
import RESTAPIproject.declarations.ProductQuantity;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.UUID;

public class Order {
    private ArrayList<ProductQuantity> items = new ArrayList<>();
    private final int price; // In pennies
    private final UUID ID;
    private final UUID userID;
    private Delivery delivery;

    private ArrayList<OrderStatus> status;

    /**
     * Konstruktor
     * @param i Lista produktów w zamówieniu
     * @param uid ID zamówienia
     * @param d Adres zamówienia
     * @throws Shop.CustomException
     */
    public Order(ArrayList<ProductQuantity> i, UUID uid, Delivery d) throws Shop.CustomException {
        status = new ArrayList<OrderStatus>();

        // Sprawdza czy podana ilość produktów jest prawidłowa
        for(ProductQuantity p : i) {
            if(p.quantity <= 0) {
                throw new Shop.CustomException(p.quantity + " is not correct quantity", HttpStatus.BAD_REQUEST);
            }
            if(p.quantity > p.product.getAmount()) {
                throw new Shop.CustomException("Not enough products in store", HttpStatus.BAD_REQUEST);
            }
        }

        // Zmniejsza ilość dostępnych produktów w magayznie o te zamówione
        for(ProductQuantity p : i) {
            p.product.setAmount(p.product.getAmount() - p.quantity);
        }

        items = i;
        userID = uid;
        delivery = d;
        ID = UUID.randomUUID();

        // Dodaje status - zamówione
        status.add(new OrderStatus(Status.Ordered));

        // Oblicza cene
        price = calcPrice(i);
    }

    /**
     * Konstruktor potrzebny do wczytywania zamówień z pliku
     * @param id id zamówienia
     * @param price cena
     * @param userID id użytkownika
     */
    public Order(UUID id, int price, UUID userID) {
        status = new ArrayList<OrderStatus>();
        this.price = price;
        ID = id;
        this.userID = userID;
    }

    /**
     * Zwraca produkty z zamowienia
     * @return ArrayList
     */
    public ArrayList<ProductQuantity> getItems() {
        return items;
    }

    /**
     * Dodaje przedmiot do zamówienia
     * @param p przedmiot, który chcemy dodać
     */
    public void addItem(ProductQuantity p) {
        this.items.add(p);
    }

    /**
     * Zmienia status zamowienia
     * @param status status zamowienia
     */
    public void setStatus(Status status) {
        this.status.add(new OrderStatus(status));
    }

    /**
     * Ustawia status zamówienia
     * @param status status, który chcemy ustawić
     */
    public void setOrderStatus(OrderStatus status) {
        this.status.add(status);
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

    /**
     * Zwraca id zamówienia
     * @return id zamówienia
     */
    public UUID getID() {
        return ID;
    }

    /**
     * Zwraca status zamówienia
     * @return status zamówienia
     */
    public ArrayList<OrderStatus> getStatus() {
        return status;
    }

    /**
     * Zwraca adres zamówienia
     * @return adres zamówienia
     */
    public Delivery getDelivery() {
        return delivery;
    }

    /**
     * Ustawia adres zamówienia
     * @param delivery adres zamówienia
     */
    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
    }

    /**
     * Zwraca id użytkownika
     * @return id użytkownika
     */
    public UUID getUserID() {
        return userID;
    }
}
