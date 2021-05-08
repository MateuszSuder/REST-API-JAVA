package RESTAPIproject.routes;

import RESTAPIproject.RestApiProjectApplication;
import RESTAPIproject.classes.Product;
import RESTAPIproject.classes.User;
import RESTAPIproject.models.ErrorResponse;
import RESTAPIproject.models.ProductsResult;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequestMapping("/product")
@SpringBootApplication
@Api(tags = "Product")
public class ProductRoute extends RestApiProjectApplication  {

    @GetMapping(value = "")
    @Operation(summary = "Get all products",
            description = "Return all products")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Query successful", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Product.class))
            }),
            @ApiResponse(responseCode = "500", description = "Internal Error",
                    content = @Content)
    })
    public ResponseEntity<ConcurrentHashMap<UUID, Product>> getProducts() {
        return ResponseEntity.status(HttpStatus.OK).body(shop.getProducts());
    }

    @GetMapping("{id}")
    @Operation(summary = "Get product",
            description = "Get product by UUID specified in path variable")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Query successful", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Product.class))
            }),
            @ApiResponse(responseCode = "404", description = "Product not found", content = {
                    @Content(mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "500", description = "Internal Error",
                    content = @Content)
    })
    public ResponseEntity getProduct(@PathVariable UUID id) {
        ConcurrentHashMap<UUID, Product> products = shop.getProducts();

        if(products.containsKey(id)) {
            return ResponseEntity.status(HttpStatus.OK).body(products.get(id));
        } else {
            ErrorResponse er = new ErrorResponse("UUID " + id + " not found", HttpStatus.NOT_FOUND.value());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(er);
        }
    }

    public void addProduct() {}

    public void modifyProduct() {}

    public void deleteProduct() {}
}
