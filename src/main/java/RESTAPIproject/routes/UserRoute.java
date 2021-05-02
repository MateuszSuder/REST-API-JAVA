package RESTAPIproject.routes;

import RESTAPIproject.RestApiProjectApplication;
import RESTAPIproject.classes.Shop;
import RESTAPIproject.classes.User;
import RESTAPIproject.models.ErrorResponse;
import RESTAPIproject.models.UserInput;
import RESTAPIproject.models.UserMutationInput;
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
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static org.springframework.http.MediaType.APPLICATION_JSON;


@RequestMapping("/user")
@SpringBootApplication
@Api(tags = "User")
public class UserRoute extends RestApiProjectApplication {

    @GetMapping("")
    @Operation(summary = "Get all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Query successful", content = {
                    @Content(mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "500", description = "Internal Error",
                    content = @Content)
    })
    public ConcurrentHashMap<UUID, User> getUsers() {
        return shop.getUsers();
    }

    @GetMapping("{id}")
    @Operation(summary = "Get user by UUID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))
            }),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Error",
                    content = @Content)
    })
    public ResponseEntity getUser(@PathVariable UUID id) {
        try {
            User u = shop.getUser(id);
            return ResponseEntity.ok(shop.getUser(id));
        } catch(Shop.CustomException e) {
            return ResponseEntity.status(e.getStatus()).body(e);
        }
    }

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Add user")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UUID.class))
            }),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Error",
                    content = @Content)
    })
    public ResponseEntity addUser(@RequestBody UserInput input) {
        if(input.username == null) {
            ErrorResponse er = new ErrorResponse("Username is empty", HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(er);
        }
        if(input.username.length() < 3) {
            ErrorResponse er = new ErrorResponse("Username has to be longer than 3 characters", HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(er);
        }
        try {
            UUID id = shop.addUser(input.username);
            return ResponseEntity.status(HttpStatus.CREATED).contentType(APPLICATION_JSON).body(id);
        } catch(Shop.CustomException e) {
            ErrorResponse er = new ErrorResponse(e.getMessage(), e.getStatus().value());
            return ResponseEntity.status(e.getStatus()).contentType(APPLICATION_JSON).body(er);
        }
    }

    @PostMapping(value = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Modify user")
    public ResponseEntity mutateUser(@PathVariable UUID id, @RequestBody UserMutationInput input) {
        try {
            shop.modifyUser(id, input);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch(Shop.CustomException e) {
            ErrorResponse er = new ErrorResponse(e.getMessage(), e.getStatus().value());
            return ResponseEntity.status(e.getStatus()).body(er);
        }
    }
}
