package RESTAPIproject.controllers;

import RESTAPIproject.RestApiProjectApplication;
import RESTAPIproject.classes.Company;
import RESTAPIproject.classes.Shop;
import RESTAPIproject.classes.User;
import RESTAPIproject.models.CompanyInput;
import RESTAPIproject.models.ErrorResponse;
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
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@RequestMapping("/company")
@SpringBootApplication
@Api(tags = "Company")
public class CompanyController extends RestApiProjectApplication {

    @GetMapping("")
    @Operation(summary = "Get all companies")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Query successful", content = {
                    @Content(mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "500", description = "Internal Error",
                    content = @Content)
    })
    public ConcurrentHashMap<UUID, Company> getCompanies() {
        return shop.getCompanies();
    }

    @GetMapping("{id}")
    @Operation(summary = "Get company by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Query successful", content = {
                    @Content(mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "404", description = "No company found with this id",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Error",
                    content = @Content)
    })
    public ResponseEntity getCompany(@PathVariable UUID id) {
        try {
            Company c = shop.getCompany(id);
            return ResponseEntity.status(HttpStatus.OK).body(c);
        } catch(Shop.CustomException e) {
            ErrorResponse er = new ErrorResponse(e.getMessage(), e.getStatus().value());
            return ResponseEntity.status(e.getStatus()).body(er);
        }
    }

    @PostMapping("")
    @Operation(summary = "Create company with given name")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Company created", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UUID.class))
            }),
            @ApiResponse(responseCode = "409", description = "Company with given name already exists", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Error", content = @Content)
    })
    public ResponseEntity createCompany(@RequestBody CompanyInput input) {
        if(input.name == null) {
            ErrorResponse er = new ErrorResponse("Company's name is empty", HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(er);
        }

        if(input.name.length() < 3) {
            ErrorResponse er = new ErrorResponse("Company's name is shorter than 3 characters", HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(er);
        }

        try {
            for (Map.Entry<UUID, Company> entry : shop.getCompanies().entrySet()) {
                UUID k = entry.getKey();
                Company v = entry.getValue();
                if (v.getName().equals(input.name)) {
                    throw new Shop.CustomException("Company with same name already exists", HttpStatus.CONFLICT);
                }
            }
            Company c = new Company(input.name);
            UUID id = c.getID();

            shop.getCompanies().putIfAbsent(id, c);

            return ResponseEntity.status(HttpStatus.CREATED).contentType(APPLICATION_JSON).body(id);
        } catch(Shop.CustomException e) {
            ErrorResponse er = new ErrorResponse(e.getMessage(), e.getStatus().value());
            return ResponseEntity.status(e.getStatus()).contentType(APPLICATION_JSON).body(er);
        }
    }

    @PostMapping("{id}")
    @Operation(summary = "Change company name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Query successful", content = @Content),
            @ApiResponse(responseCode = "404", description = "Company with given id doesn't exists", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Error", content = @Content)
    })
    public ResponseEntity changeCompanyName(@PathVariable UUID id, @RequestBody CompanyInput i) {
        if(i.name == null) {
            ErrorResponse er = new ErrorResponse("Company's name is empty", HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(er);
        }

        if(i.name.length() < 3) {
            ErrorResponse er = new ErrorResponse("Company's name is shorter than 3 characters", HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(er);
        }

        try {
            for (Map.Entry<UUID, Company> entry : shop.getCompanies().entrySet()) {
                UUID k = entry.getKey();
                Company v = entry.getValue();
                if (v.getName().equals(i.name)) {
                    throw new Shop.CustomException("Company with same name already exists", HttpStatus.CONFLICT);
                }
            }

            Company c = shop.getCompany(id);
            c.setName(i.name);

            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch(Shop.CustomException e) {
            ErrorResponse er = new ErrorResponse(e.getMessage(), e.getStatus().value());
            return ResponseEntity.status(e.getStatus()).contentType(APPLICATION_JSON).body(er);
        }
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Delete company")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Query successful", content = @Content),
            @ApiResponse(responseCode = "404", description = "Company with given id doesn't exists", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Error", content = @Content)
    })
    public ResponseEntity deleteCompany(@PathVariable UUID id) {
        try {
            shop.getCompany(id);
            shop.getCompanies().remove(id);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch(Shop.CustomException e) {
            ErrorResponse er = new ErrorResponse(e.getMessage(), e.getStatus().value());
            return ResponseEntity.status(e.getStatus()).contentType(APPLICATION_JSON).body(er);
        }
    }
}
