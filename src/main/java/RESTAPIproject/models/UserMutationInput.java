package RESTAPIproject.models;

import RESTAPIproject.classes.Delivery;
import RESTAPIproject.declarations.Permission;

import java.util.UUID;

public class UserMutationInput {
    public Permission permission = Permission.user;
    public UUID companyID;
    public Delivery deliveryDetails;
}
