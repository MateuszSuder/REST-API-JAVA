package RESTAPIproject;

import RESTAPIproject.classes.Shop;
import io.swagger.annotations.Api;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


@CrossOrigin(origins = "http://localhost:3000")
@Configuration
@EnableWebMvc
@SpringBootApplication
@RestController
@Api(tags = "Main controller")
public class RestApiProjectApplication {

	public static Shop shop = new Shop();

	public static void main(String[] args) {
		SpringApplication.run(RestApiProjectApplication.class, args);
	}

}
