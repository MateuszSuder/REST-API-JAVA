package RESTAPIproject.models;

import RESTAPIproject.declarations.Specification;

import java.util.ArrayList;

public class ProductInput {
    public String name;
    public int price; // In pennies
    public String description;
    public ArrayList<Specification> specification;
    public int amount;
    public String category;
}
