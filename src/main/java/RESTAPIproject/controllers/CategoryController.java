package RESTAPIproject.controllers;

import RESTAPIproject.RestApiProjectApplication;
import RESTAPIproject.classes.Category;
import RESTAPIproject.classes.Product;
import RESTAPIproject.classes.Shop;
import RESTAPIproject.declarations.CategoriesResult;
import RESTAPIproject.models.CategoryInput;
import RESTAPIproject.models.ErrorResponse;
import RESTAPIproject.models.MoveProductInput;
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
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RequestMapping("/category")
@CrossOrigin(origins = "http://localhost:3000")
@SpringBootApplication
@Api(tags = "Category")
public class CategoryController extends RestApiProjectApplication {

    @GetMapping("all")
    @Deprecated
    @Operation(summary = "Get all categories with products",
            description = "Return all categories")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Query successful", content = {
                    @Content(mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "500", description = "Internal Error",
                    content = @Content)
    })
    public ArrayList<Category> getCategoriesWithProducts() {
        return shop.getCategories();
    }

    @GetMapping("")
    @Operation(summary = "Get all categories",
            description = "Return all categories")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Query successful", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = CategoriesResult.class))
            }),
            @ApiResponse(responseCode = "500", description = "Internal Error",
                    content = @Content)
    })
    public ArrayList<CategoriesResult> getCategories() {
        ArrayList<CategoriesResult> res = new ArrayList<>();
        ArrayList<Category> l = shop.getCategories();

        for(Category c : l) {
            CategoriesResult cr = new CategoriesResult();
            cr.categoryName = c.getName();
            res.add(cr);
        }
        return res;
    }

    @PostMapping("")
    @Operation(summary = "Add new category",
            description = "Adds category with name provided in body")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Category created", content = {
                    @Content(mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Error",
                    content = @Content)
    })
    public ResponseEntity addCategory(@RequestBody CategoryInput catName) {
        if(catName.categoryName == null) {
            ErrorResponse er = new ErrorResponse("Category name is empty", HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(er);
        }
        if(catName.categoryName.length() < 3) {
            ErrorResponse er = new ErrorResponse("Category name has to be longer than 3 characters", HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(er);
        }
        try {
            Category cat = new Category(catName.categoryName);

            for(Category c : shop.getCategories()) {
                if(c.getName().equals(catName)) {
                    throw new Shop.CustomException("Category with same name exists", HttpStatus.CONFLICT);
                }
            }

            shop.getCategories().add(cat);
            shop.saveCategoriesToFile();
            return ResponseEntity.status(HttpStatus.CREATED).body(null);
        } catch (Shop.CustomException e) {
            ErrorResponse er = new ErrorResponse(e.getMessage(), e.getStatus().value());
            return ResponseEntity.status(e.getStatus()).body(er);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
        }
    }

    @PostMapping("{name}")
    @Operation(summary = "Modify category",
            description = "Change category name provided in Path Variable with string given in body")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category modified",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Category not found",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Error",
                    content = @Content)
    })
    public ResponseEntity changeCategoryName(@PathVariable String name, @RequestBody CategoryInput after) {
        try {
            Category c = shop.findCategory(name);
            c.setName(after.categoryName);
            shop.saveCategoriesToFile();
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (Shop.CustomException e) {
            ErrorResponse er = new ErrorResponse(e.getMessage(), e.getStatus().value());
            return ResponseEntity.status(e.getStatus()).body(er);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
        }
    }

    @PostMapping("{name}/add")
    @Operation(summary = "Add product to category",
            description = "Add product / products given in Request Body to category from Path Variable")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category modified",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Category not found",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Error",
                    content = @Content)
    })
    public ResponseEntity addProductsToCategory(@PathVariable String name, @RequestBody ArrayList<UUID> products) {
        try {
            ConcurrentHashMap<UUID, Product> p = shop.getProducts();
            ArrayList<Product> ps = new ArrayList<>();

            for(UUID id : products) {
                if(p.containsKey(id)) {
                    ps.add(p.get(id));
                } else {
                    throw new Shop.CustomException("Product with ID " + id + " not found", HttpStatus.NOT_FOUND);
                }
            }

            for(Category c : shop.getCategories()) {
                if (c.getName().equals(name)) {
                    c.addProducts(ps);
                    try {
                        shop.saveCategoriesToFile();
                    } catch(IOException e) {
                        e.printStackTrace();
                        throw new Shop.CustomException("Error while saving file", HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                    return ResponseEntity.status(HttpStatus.OK).body(null);
                }
            }
            throw new Shop.CustomException("Category not found", HttpStatus.NOT_FOUND);
        } catch (Shop.CustomException e) {
            ErrorResponse er = new ErrorResponse(e.getMessage(), e.getStatus().value());
            return ResponseEntity.status(e.getStatus()).body(er);
        }
    }

    @PostMapping("{name}/move")
    @Operation(summary = "Move products from category",
            description = "Move product from category given in path to category given in request body - for null deletes product from category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category modified",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Category not found",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Error",
                    content = @Content)
    })
    public ResponseEntity changeProductCategory(@PathVariable String name, @RequestBody MoveProductInput input) {
        try {
            Category from = shop.findCategory(name);
            Category to;

            if(input.categoryName != null) {
                to = shop.findCategory(input.categoryName);
            } else {
                to = null;
            }

            Product p = shop.getProduct(input.productID);
            from.removeProduct(p);
            if(to != null) {
                to.addProducts(p);
            }

            return ResponseEntity.ok(null);
        } catch (Shop.CustomException e) {
            ErrorResponse er = new ErrorResponse(e.getMessage(), e.getStatus().value());
            return ResponseEntity.status(e.getStatus()).body(er);
        }
    }

    @GetMapping("{name}")
    @Operation(summary = "Get all products from category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Query successful",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Category not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Error",
                    content = @Content)
    })
    public ResponseEntity getProductsFromCategory(@PathVariable String name) {
        try {
            Category c = shop.findCategory(name);

            return ResponseEntity.status(HttpStatus.OK).body(c.getProducts());
        } catch (Shop.CustomException e) {
            ErrorResponse er = new ErrorResponse(e.getMessage(), e.getStatus().value());
            return ResponseEntity.status(e.getStatus()).body(er);
        }
    }

    @DeleteMapping("{name}")
    @Operation(summary = "Delete category",
            description = "Delete category provided in Path Variable")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Category not found",
                    content = @Content),
            @ApiResponse(responseCode = "406", description = "Category not empty, move products first",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Error",
                    content = @Content)
    })
    public ResponseEntity deleteCategory(@PathVariable String categoryName) {
        try {
            Category c = shop.findCategory(categoryName);
            if(c.getProducts().size() == 0) {
                shop.getCategories().remove(c);
                shop.saveCategoriesToFile();
            } else {
                throw new Shop.CustomException("Category not empty", HttpStatus.NOT_ACCEPTABLE);
            }
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (Shop.CustomException e) {
            ErrorResponse er = new ErrorResponse(e.getMessage(), e.getStatus().value());
            return ResponseEntity.status(e.getStatus()).body(er);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
        }
    }
}
