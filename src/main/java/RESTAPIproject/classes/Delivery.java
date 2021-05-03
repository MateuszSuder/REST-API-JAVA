package RESTAPIproject.classes;

// Delivery details rather than delivery options
public class Delivery {
    private String name;
    private String lastName;
    private Address address;

    public Delivery(String n, String l) {
        this.name = n;
        this.lastName = l;
    }

    public Delivery(String n, String l, Address a) {
        this.name = n;
        this.lastName = l;
        this.address = a;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
