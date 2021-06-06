package RESTAPIproject.controllers;

import RESTAPIproject.RestApiProjectApplication;
import RESTAPIproject.classes.Shop;
import RESTAPIproject.classes.User;
import RESTAPIproject.declarations.UserMinifiedResult;
import RESTAPIproject.declarations.UsersInfoResult;
import RESTAPIproject.models.ErrorResponse;
import RESTAPIproject.models.UserInput;
import RESTAPIproject.models.UserMutationInput;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Description;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static org.springframework.http.MediaType.APPLICATION_JSON;


@RequestMapping("/user")
@SpringBootApplication
@Api(tags = "User")
public class UserController extends RestApiProjectApplication {

    public Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("")
    @Operation(summary = "Get all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Query successful", content = {
                    @Content(mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "500", description = "Internal Error",
                    content = @Content)
    })
    public Collection<User> getUsers() {
        return shop.getUsers().values();
    }

    @GetMapping("info")
    @Operation(summary = "Get information about users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Query successful", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = UsersInfoResult.class))
            }),
            @ApiResponse(responseCode = "500", description = "Internal Error",
                    content = @Content)
    })
    public UsersInfoResult getUsersInfo() {
        Collection<User> us = shop.getUsers().values();
        int numberOfUsers = us.size();
        UsersInfoResult res = new UsersInfoResult();
        res.numberOfUsers = numberOfUsers;
        return res;
    }

    @GetMapping("v2")
    @Operation(summary = "Get minified information about users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Query successful", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = UserMinifiedResult.class))
            }),
            @ApiResponse(responseCode = "500", description = "Internal Error",
                    content = @Content)
    })
    public ArrayList<UserMinifiedResult> getUsersMinified() {
        ArrayList<UserMinifiedResult> res = new ArrayList<UserMinifiedResult>();

        for(User u : shop.getUsers().values()) {
            UserMinifiedResult temp = new UserMinifiedResult();
            temp.id = u.getID();
            temp.username = u.getUsername();
            if(u.getCompany() != null) {
                temp.companyName = u.getCompany().getName();
            }
            temp.permission = u.getPermission();
            res.add(temp);
        }

        return res;
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
            if(shop.getUsers().containsKey(id)) {
                return ResponseEntity.ok(shop.getUser(id));
            } else {
                throw new Shop.CustomException("User not found", HttpStatus.NOT_FOUND);
            }
        } catch(Shop.CustomException e) {
            ErrorResponse er = new ErrorResponse(e.getMessage(), e.getStatus().value());
            return ResponseEntity.status(e.getStatus()).body(er);
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
            @ApiResponse(responseCode = "409", description = "User with same username already exists",
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
            for (Map.Entry<UUID, User> entry : shop.getUsers().entrySet()) {
                UUID k = entry.getKey();
                User v = entry.getValue();
                if (v.getUsername().equals(input.username)) {
                    throw new Shop.CustomException("User with same username already exists", HttpStatus.CONFLICT);
                }
            }
            User user = new User(input.username);
            UUID id = user.getID();

            shop.getUsers().putIfAbsent(id, user);

            try {
                shop.saveUsersToFile();
            } catch(IOException e) {
                e.printStackTrace();
                throw new Shop.CustomException("Error while saving file", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return ResponseEntity.status(HttpStatus.CREATED).contentType(APPLICATION_JSON).body(id);
        } catch(Shop.CustomException e) {
            ErrorResponse er = new ErrorResponse(e.getMessage(), e.getStatus().value());
            return ResponseEntity.status(e.getStatus()).contentType(APPLICATION_JSON).body(er);
        }
    }

    @PostMapping(value = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Modify user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User modified", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UUID.class))
            }),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Error",
                    content = @Content)
    })
    public ResponseEntity mutateUser(@PathVariable UUID id, @RequestBody UserMutationInput input) {
        try {
            User u = shop.getUsers().get(id);

            if(input.companyID != null) {
                if(shop.getCompanies().containsKey(input.companyID)) {
                    u.setCompany(shop.getCompanies().get(input.companyID));
                } else {
                    throw new Shop.CustomException("Company not found", HttpStatus.NOT_FOUND);
                }
            }

            if(input.deliveryDetails != null) {
                u.setDeliverDetails(input.deliveryDetails);
            }

            if(input.permission != null) {
                u.setPermission(input.permission);
            }

            try {
                shop.saveUsersToFile();
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

    @DeleteMapping(value = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Delete user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Error",
                    content = @Content)
    })
    public ResponseEntity deleteUser(@PathVariable UUID id) {
        try {
            if(shop.getUsers().containsKey(id)) {
                shop.getUsers().remove(id);
            } else {
                throw new Shop.CustomException("User not found", HttpStatus.NOT_FOUND);
            }
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (Shop.CustomException e) {
            ErrorResponse er = new ErrorResponse(e.getMessage(), e.getStatus().value());
            return ResponseEntity.status(e.getStatus()).body(er);
        }
    }
}
