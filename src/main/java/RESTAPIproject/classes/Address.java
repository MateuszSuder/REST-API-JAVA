package RESTAPIproject.classes;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Address {
    private String postcode;
    private String city;
    private String street;
    private String number; // if ex 21/4
    private String country;
    
    public Address(String ci, String co, String n, String p, String s) {
        this.postcode = p;
        this.city = ci;
        this.street = s;
        this.number = n;
        this.country = co;
    }

    public Address() {};


    /**
     * Ustawia adress
     * @param ci miasto
     * @param co państwo
     * @param n number domu
     * @param p kod pocztowy
     * @param s ulica
     * @return void
     */
    public void setAddress(String ci, String co, String n, String p, String s) {

    }

    /**
     * Zwraca kraj
     * @return String
     */
    public String getCountry() {
        return country;
    }

    /**
     * Zwraca miasto
     * @return String
     */
    public String getCity() {
        return city;
    }

    /**
     * Zwraca kod pocztowy
     * @return String
     */
    public String getPostcode() {
        return postcode;
    }

    /**
     * Zwraca number domu
     * @return String
     */
    public String getNumber() {
        return number;
    }

    /**
     * Zwraca ulice
     * @return String
     */
    public String getStreet() {
        return street;
    }


}
