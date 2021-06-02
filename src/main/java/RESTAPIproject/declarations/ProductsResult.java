package RESTAPIproject.declarations;

import RESTAPIproject.classes.Product;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ProductsResult {
    ConcurrentHashMap<UUID, Product> Products;
}
