package RESTAPIproject.routes;

import RESTAPIproject.RestApiProjectApplication;
import RESTAPIproject.classes.User;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.UUID;

@RequestMapping("/user")
@SpringBootApplication
@Api(tags = "User")
public class UserRoute extends RestApiProjectApplication {

    @GetMapping("/")
    @Operation(summary = "Get all users")
    public ArrayList<User> getUsers() {
        return shop.getUsers();
    }

    @GetMapping("{id}")
    @Operation(summary = "Get user by UUID")
    public String getUser(@PathVariable UUID id) {
        return "XDDDDD";
    }
}
