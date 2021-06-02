package RESTAPIproject.controllers;

import RESTAPIproject.RestApiProjectApplication;
import RESTAPIproject.classes.Company;
import RESTAPIproject.classes.Shop;
import RESTAPIproject.classes.User;
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

@RequestMapping("/user/{id}/company")
@SpringBootApplication
@Api(tags = "User")
public class UserCompanyController extends RestApiProjectApplication {

    @GetMapping(value = "fix")
    @ApiOperation(value = "Fix swagger zzz", hidden = true) // Swagger ma problem ze zwrotami czasami, mam nadzieje ze temporary fix
    public Company fix() {
        return new Company("test");
    }

    @GetMapping(value = "")
    @Operation(summary = "Get user's company")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Query successful", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Company.class))
            }),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Error",
                    content = @Content)
    })
    public ResponseEntity getUserCompany(@PathVariable UUID id) {
        try {
            User u = shop.getUser(id);
            return ResponseEntity.status(HttpStatus.OK).body(u.getCompany());
        } catch(Shop.CustomException e) {
            ErrorResponse er = new ErrorResponse(e.getMessage(), e.getStatus().value());
            return ResponseEntity.status(e.getStatus()).body(er);
        }
    }

    @PostMapping(value = "{cid}")
    @Operation(summary = "Change user's company")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Query successful", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found / Company not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Error",
                    content = @Content)
    })
    public ResponseEntity changeUserCompany(@PathVariable UUID id, @PathVariable UUID companyID) {
        try {
            User u = shop.getUser(id);
            Company c = shop.getCompany(companyID);

            u.setCompany(c);

            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch(Shop.CustomException e) {
            ErrorResponse er = new ErrorResponse(e.getMessage(), e.getStatus().value());
            return ResponseEntity.status(e.getStatus()).body(er);
        }
    }

    @DeleteMapping(value = "")
    @Operation(summary = "Remove user from company")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Query successful", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Error",
                    content = @Content)
    })
    public ResponseEntity removeUserCompany(@PathVariable UUID id) {
        try {
            User u = shop.getUser(id);
            u.setCompany(null);

            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch(Shop.CustomException e) {
            ErrorResponse er = new ErrorResponse(e.getMessage(), e.getStatus().value());
            return ResponseEntity.status(e.getStatus()).body(er);
        }
    }
}
