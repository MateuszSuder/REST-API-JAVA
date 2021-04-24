package RESTAPIproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;

@SpringBootApplication
@RestController
public class RestApiProjectApplication {
	private Shop shop = new Shop();

	public static void main(String[] args) {
		SpringApplication.run(RestApiProjectApplication.class, args);
	}

	@PostMapping("/category")
	public ArrayList<Category> greeting(@RequestBody Category test) {
		shop.addCategory(test);
		return shop.getCategories();
	}
}
