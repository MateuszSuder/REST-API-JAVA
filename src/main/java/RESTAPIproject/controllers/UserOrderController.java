package RESTAPIproject.controllers;

import RESTAPIproject.RestApiProjectApplication;
import RESTAPIproject.classes.Order;
import RESTAPIproject.classes.Shop;
import RESTAPIproject.models.ErrorResponse;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.UUID;

@RequestMapping("/user/{userid}/order")
@SpringBootApplication
@Api(tags = "Order")
public class UserOrderController extends RestApiProjectApplication {

    @GetMapping(value = "")
    @Operation(summary = "Get all orders for user given in path variable")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Query successful", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Error",
                    content = @Content)
    })
    public ResponseEntity getOrders(@PathVariable UUID userid) {
        try {
            shop.getUser(userid);
            ArrayList<Order> os = new ArrayList<Order>();
            for(Order o : shop.getOrders().values()) {
                if(o.getUserID() == userid) {
                    os.add(o);
                }
            }
            return ResponseEntity.status(HttpStatus.OK).body(os);
        } catch (Shop.CustomException e) {
            ErrorResponse er = new ErrorResponse(e.getMessage(), e.getStatus().value());
            return ResponseEntity.status(e.getStatus()).body(er);
        }
    }
}
