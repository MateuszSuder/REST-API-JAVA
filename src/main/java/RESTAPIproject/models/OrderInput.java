package RESTAPIproject.models;

import RESTAPIproject.classes.Delivery;
import RESTAPIproject.declarations.OrderProductInput;

import java.util.ArrayList;
import java.util.UUID;

public class OrderInput {
    public ArrayList<OrderProductInput> items;
    public Delivery delivery;
    public UUID userID;
}

