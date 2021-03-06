package RESTAPIproject.classes;


import RESTAPIproject.declarations.Specification;

import java.util.ArrayList;
import java.util.UUID;

public class Product {
    private String name;
    private int price; // In pennies
    private String description;
    private ArrayList<Specification> specification;
    private int amount;
    private UUID ID;

    /**
     * Konstruktor
     * @param name nazwa produktu
     */
    public Product(String name) {
        this.name = name;

        specification = new ArrayList<>();

        ID = UUID.randomUUID();
    }

    /**
     * Konstruktor potrzebny podczas wczytywania z pliku
     * @param id id produktu
     * @param name nazwa produktu
     */
    public Product(UUID id, String name) {
        ID = id;
        this.name = name;
        specification = new ArrayList<>();
    }

    /**
     * Zwraca cene produktu w groszach
     * @return int
     */
    public int getPrice() {
        return price;
    }

    /**
     * Ustawia cene produktu podana w argumencie
     * @param price cena w groszach
     * @return void
     */
    public void setPrice(int price) {
        this.price = price;
    }

    /**
     * Zwraca ilosc dostepnych produktow
     * @return int
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Ustawia ilosc dostepnych produktow
     * @param amount ilosc
     * @return void
     */
    public void setAmount(int amount) {
        this.amount = amount;
    }

    /**
     * Zwraca nazwe produktu
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * Ustawia nazwe produktu z argumentu
     * @param name nazwa produktu
     * @return void
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Zwraca opis produktu
     * @return String
     */
    public String getDescription() {
        return description;
    }

    /**
     * Ustawia opis produktu z argumentu
     * @param description nowy opis
     * @return void
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Zwraca specyfikacje produktu
     * @return ConcurrentHashMap<String, ArrayList<String>>
     */
    public ArrayList<Specification> getSpecification() {
        return specification;
    }

    /**
     * Ustawia specyfikacje produktu podana w argumencie
     * @param specification specyfikacja
     * @return void
     */
    public void setSpecification(ArrayList<Specification> specification) {
        String test = this.specification.toString();
        this.specification = specification;
    }

    /**
     * Dodaje specyfikacje do listy specyfikacji
     * @param s specyfikacja do dodania
     */
    public void addToSpec(Specification s) {
        this.specification.add(s);
    }

    /**
     * Zwraca ID produktu
     * @return ID produktu
     */
    public UUID getID() {
        return ID;
    }
}
