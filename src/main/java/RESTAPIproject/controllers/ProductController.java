package RESTAPIproject.controllers;

import RESTAPIproject.RestApiProjectApplication;
import RESTAPIproject.classes.Product;
import RESTAPIproject.classes.Shop;
import RESTAPIproject.models.ErrorResponse;
import RESTAPIproject.models.ProductInput;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RequestMapping("/product")
@SpringBootApplication
@Api(tags = "Product")
public class ProductController extends RestApiProjectApplication  {

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
    public ResponseEntity<Collection<Product>> getProducts() {
        return ResponseEntity.status(HttpStatus.OK).body(shop.getProducts().values());
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

    @PostMapping("")
    @Operation(summary = "Create product")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product created",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UUID.class))),
            @ApiResponse(responseCode = "400", description = "Missing product name", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Error",
                    content = @Content)
    })
    public ResponseEntity addProduct(@RequestBody ProductInput input) {
        try {
            if(input.name == null) {
                throw new Shop.CustomException("Product name is needed", HttpStatus.BAD_REQUEST);
            }
            if(input.name.length() < 3) {
                throw new Shop.CustomException("Product name has to be at least 3 characters", HttpStatus.BAD_REQUEST);
            }

            Product p = new Product(input.name);

            if(input.amount > 0) {
                p.setAmount(input.amount);
            }

            if(input.price > 0) {
                p.setPrice(input.price);
            }

            if(input.description != null) {
                p.setDescription(input.description);
            }

            if(input.specification != null) {
                p.setSpecification(input.specification);
            }

            shop.getProducts().put(p.getID(), p);

            try {
                shop.saveProductsToFile();
            } catch(IOException e) {
                e.printStackTrace();
                throw new Shop.CustomException("Error while saving file", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(p.getID());
        } catch(Shop.CustomException e) {
            ErrorResponse er = new ErrorResponse(e.getMessage(), e.getStatus().value());
            return ResponseEntity.status(e.getStatus()).body(er);
        }
    }


    @PostMapping("{id}")
    @Operation(summary = "Modify product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Query successful", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "404", description = "Product not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Error",
                    content = @Content)
    })
    public ResponseEntity modifyProduct(@PathVariable UUID id, @RequestBody ProductInput input) {
        try {
            Product p;

            if(shop.getProducts().containsKey(id)) {
                p = shop.getProducts().get(id);
            } else {
                throw new Shop.CustomException("Product with given id doesn't exist", HttpStatus.NOT_FOUND);
            }

            if(input.name != null) {
                if(input.name.length() < 3) {
                    throw new Shop.CustomException("Product name has to be at least 3 characters", HttpStatus.BAD_REQUEST);
                }

            }

            if(input.amount > 0) {
                p.setAmount(input.amount);
            }

            if(input.price > 0) {
                p.setPrice(input.price);
            }

            if(input.description != null) {
                p.setDescription(input.description);
            }

            if(input.specification != null) {
                p.setSpecification(input.specification);
            }

            shop.getProducts().put(p.getID(), p);

            try {
                shop.saveProductsToFile();
            } catch(IOException e) {
                e.printStackTrace();
                throw new Shop.CustomException("Error while saving file", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch(Shop.CustomException e) {
            ErrorResponse er = new ErrorResponse(e.getMessage(), e.getStatus().value());
            return ResponseEntity.status(e.getStatus()).body(er);
        }
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Delete product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Query successful", content = @Content),
            @ApiResponse(responseCode = "404", description = "Product not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Error",
                    content = @Content)
    })
    public ResponseEntity deleteProduct(@PathVariable UUID id) {
        try {
            if(shop.getProducts().containsKey(id)) {
                shop.getProducts().remove(id);
                try {
                    shop.saveProductsToFile();
                } catch(IOException e) {
                    e.printStackTrace();
                    throw new Shop.CustomException("Error while saving file", HttpStatus.INTERNAL_SERVER_ERROR);
                }
                return ResponseEntity.status(HttpStatus.OK).body(null);
            } else {
                throw new Shop.CustomException("Product with given id doesn't exist", HttpStatus.NOT_FOUND);
            }
        } catch (Shop.CustomException e) {
            ErrorResponse er = new ErrorResponse(e.getMessage(), e.getStatus().value());
            return ResponseEntity.status(e.getStatus()).body(er);
        }
    }
}
