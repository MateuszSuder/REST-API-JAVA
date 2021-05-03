package RESTAPIproject.classes;

public class Address {
    private String postcode;
    private String city;
    private String street;
    private String number; // if ex 21/4
    private String country;
    
    Address(String ci, String co, String n, String p, String s) {
        this.postcode = p;
        this.city = ci;
        this.street = s;
        this.number = n;
        this.country = co;
    }

    public void setAddress() {

    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getPostcode() {
        return postcode;
    }

    public String getNumber() {
        return number;
    }

    public String getStreet() {
        return street;
    }
}
