package RESTAPIproject.routes;

import RESTAPIproject.RestApiProjectApplication;
import RESTAPIproject.classes.Address;
import RESTAPIproject.classes.Delivery;
import RESTAPIproject.classes.Shop;
import RESTAPIproject.classes.User;
import RESTAPIproject.models.AddressInput;
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

@RequestMapping("/user/{id}/delivery/address")
@SpringBootApplication
@Api(tags = "User")
public class UserAddressRoute extends RestApiProjectApplication {

    @GetMapping(value = "fix")
    @ApiOperation(value = "Fix swagger zzz", hidden = true) // Swagger ma problem ze zwrotami czasami, mam nadzieje ze temporary fix
    public Address fix() {
        return new Address("test", "test", "test", "test", "test");
    }

    @GetMapping(value = "")
    @Operation(summary = "Get user's address")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Query successful", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Address.class))
            }),
            @ApiResponse(responseCode = "404", description = "User not found / User has no delivery address",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Error",
                    content = @Content)
    })
    public ResponseEntity getAddress(@PathVariable UUID id) {
        try {
            if(shop.getUsers().containsKey(id)) {
                User u = shop.getUsers().get(id);
                if(u.getDeliverDetails() != null) {
                    return ResponseEntity.status(HttpStatus.OK).body(u.getDeliverDetails().getAddress());
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

    @PostMapping(value = "")
    @Operation(summary = "Change user's delivery address")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Query successful", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Address.class))
            }),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found / User has no delivery",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Error",
                    content = @Content)
    })
    public ResponseEntity mutateAddress(@PathVariable UUID id, @RequestBody AddressInput input) {
        Address a;
        try {
            if(input.city != null && input.country != null && input.number != null && input.postcode != null && input.street != null) {
                a = new Address(input.city, input.country, input.number, input.postcode, input.street);
            } else {
                throw new Shop.CustomException("Invalid input", HttpStatus.BAD_REQUEST);
            }
            if(shop.getUsers().containsKey(id)) {
                User u = shop.getUsers().get(id);
                if(u.getDeliverDetails() != null) {
                    u.getDeliverDetails().setAddress(a);
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

    @DeleteMapping(value = "")
    @Operation(summary = "Delete user's delivery address")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Query successful", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Address.class))
            }),
            @ApiResponse(responseCode = "404", description = "User not found / User has no delivery",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Error",
                    content = @Content)
    })
    public ResponseEntity deleteAddress(@PathVariable UUID id) {
        try {
            if(shop.getUsers().containsKey(id)) {
                User u = shop.getUsers().get(id);
                if(u.getDeliverDetails() != null) {
                    u.getDeliverDetails().setAddress(null);
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
