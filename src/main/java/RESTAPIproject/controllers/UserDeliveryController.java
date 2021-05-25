package RESTAPIproject.controllers;

import RESTAPIproject.RestApiProjectApplication;
import RESTAPIproject.classes.Delivery;
import RESTAPIproject.classes.Shop;
import RESTAPIproject.classes.User;
import RESTAPIproject.models.DeliveryInput;
import RESTAPIproject.models.ErrorResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("/user/{id}/delivery")
@SpringBootApplication
@Api(tags = "User", description = "User Controller")
public class UserDeliveryController extends RestApiProjectApplication {

    @GetMapping(value = "fix")
    @ApiOperation(value = "Fix swagger zzz", hidden = true) // Swagger ma problem ze zwrotami czasami, mam nadzieje ze temporary fix
    public Delivery fix() {
        return new Delivery("test", "test");
    }

    @GetMapping(value = "")
    @Operation(summary = "Get user's delivery information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Query successful", content =
                    @Content(schema = @Schema(implementation = Delivery.class))
            ),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Error",
                    content = @Content)
    })
    public ResponseEntity getDelivery(@PathVariable UUID id) {
        try {
            if(shop.getUsers().containsKey(id)) {
                User u = shop.getUser(id);
                return ResponseEntity.status(HttpStatus.OK).body(u.getDeliverDetails());
            } else {
                throw new Shop.CustomException("User not found", HttpStatus.NOT_FOUND);
            }
        } catch(Shop.CustomException e) {
            ErrorResponse er = new ErrorResponse(e.getMessage(), e.getStatus().value());
            return ResponseEntity.status(e.getStatus()).body(er);
        }
    }

    @PostMapping(value = "")
    @Operation(summary = "Change user's delivery details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Query successful", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Error",
                    content = @Content)
    })
    public ResponseEntity mutateDelivery(@PathVariable UUID id, @RequestBody DeliveryInput input) {
        Delivery d = new Delivery(input.name, input.lastName);
        try {
            if(shop.getUsers().containsKey(id)) {
                shop.getUser(id).setDeliverDetails(d);
                return ResponseEntity.status(HttpStatus.OK).body(null);
            } else {
                throw new Shop.CustomException("User not found", HttpStatus.NOT_FOUND);
            }
        } catch(Shop.CustomException e) {
            ErrorResponse er = new ErrorResponse(e.getMessage(), e.getStatus().value());
            return ResponseEntity.status(e.getStatus()).body(er);
        }
    }

    @DeleteMapping(value = "")
    @Operation(summary = "Delete delivery details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found / Delivery details not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Error",
                    content = @Content)
    })
    public ResponseEntity deleteDelivery(@PathVariable UUID id) {
        try {
            if(shop.getUsers().containsKey(id)) {
                User u = shop.getUsers().get(id);
                if(u.getDeliverDetails() != null) {
                    u.setDeliverDetails(null) ;
                    return ResponseEntity.status(HttpStatus.OK).body(null);
                }
                throw new Shop.CustomException("User has no delivery details", HttpStatus.NOT_FOUND);
            } else {
                throw new Shop.CustomException("User not found", HttpStatus.NOT_FOUND);
            }
        } catch(Shop.CustomException e) {
            ErrorResponse er = new ErrorResponse(e.getMessage(), e.getStatus().value());
            return ResponseEntity.status(e.getStatus()).body(er);
        }
    }
}
