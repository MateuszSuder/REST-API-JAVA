package RESTAPIproject.models;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ProductInput {
    public String name;
    public int price; // In pennies
    public String description;
    public ConcurrentHashMap<String, ArrayList<String>> specification;
    public int amount;
}
