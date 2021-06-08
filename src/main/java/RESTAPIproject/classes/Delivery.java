package RESTAPIproject.classes;

// Delivery details rather than delivery options
public class Delivery {
    private String name;
    private String lastName;
    private Address address;

    public Delivery() {

    }

    public Delivery(String n, String l) {
        this.name = n;
        this.lastName = l;
    }

    public Delivery(String n, String l, Address a) {
        this.name = n;
        this.lastName = l;
        this.address = a;
    }

    /**
     * Zwraca adres
     * @return Address
     */
    public Address getAddress() {
        return address;
    }

    /**
     * Ustawia adres dostawy
     * @param address adres
     */
    public void setAddress(Address address) {
        this.address = address;
    }

    /**
     * Zwraca imie
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * Ustawia imie
     * @param name imie
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Zwraca nazwisko
     * @return String
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Ustawia nazwisko
     * @param lastName nazwisko
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
