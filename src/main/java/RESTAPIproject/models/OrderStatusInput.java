package RESTAPIproject.models;

import RESTAPIproject.declarations.Status;

public class OrderStatusInput {
    public Status status;
    public boolean override = false; // Dla true nadpisuje kazda wartosc, false pozwala tylko na nowy status z wiekszym priorytetem
}
