package RESTAPIproject.controllers;

import RESTAPIproject.RestApiProjectApplication;
import RESTAPIproject.classes.*;
import RESTAPIproject.declarations.*;
import RESTAPIproject.models.ErrorResponse;
import RESTAPIproject.models.OrderInput;
import RESTAPIproject.models.OrderStatusInput;
import RESTAPIproject.models.ProductsInfoInput;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RequestMapping("/order")
@SpringBootApplication
@Api(tags = "Order")
public class OrderController extends RestApiProjectApplication {


    @GetMapping(value = "")
    @Operation(summary = "Get all orders")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Query successful", content = @Content
            ),
            @ApiResponse(responseCode = "500", description = "Internal Error",
                    content = @Content)
    })
    public Collection<Order> getOrders() {
        return shop.getOrders().values();
    }

    @GetMapping(value = "v2")
    @Operation(summary = "Get all orders minified")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Query successful", content = @Content
            ),
            @ApiResponse(responseCode = "500", description = "Internal Error",
                    content = @Content)
    })
    public ArrayList<OrderMinifiedResult> getOrdersMinified() {
        ArrayList<OrderMinifiedResult> result = new ArrayList<OrderMinifiedResult>();

        for(Order o : shop.getOrders().values()) {
            OrderMinifiedResult r = new OrderMinifiedResult();
            r.id = o.getID();
            r.price = o.getPrice();
            r.lastStatus = o.getStatus().get(o.getStatus().size() - 1).getStatus();

            result.add(r);
        }

        return result;
    }

    @GetMapping("info")
    @Operation(summary = "Get info about orders")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Query successful", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = OrdersInfoResult.class))
            }),
            @ApiResponse(responseCode = "500", description = "Internal Error",
                    content = @Content)
    })
    public OrdersInfoResult getOrdersInfo() {
        OrdersInfoResult result = new OrdersInfoResult();

        result.delivered = 0;
        result.waitingForPayment = 0;
        result.shipped = 0;
        result.paymentDone = 0;

        for(Order o : shop.getOrders().values()) {
            Status s = o.getStatus().get(o.getStatus().size()).getStatus();

            if(s.equals(Status.Ordered)) {
                result.waitingForPayment++;
            }
            if(s.equals(Status.Delivered)) {
                result.delivered++;
            }
            if(s.equals(Status.PaymentDone)) {
                result.paymentDone++;
            }
            if(s.equals(Status.Shipped)) {
                result.shipped++;
            }
        }

        return result;
    }


    @GetMapping(value = "{id}")
    @Operation(summary = "Get order with specific id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Query successful", content = @Content(schema = @Schema(implementation = Order.class))
            ),
            @ApiResponse(responseCode = "500", description = "Internal Error",
                    content = @Content)
    })
    public ResponseEntity getOrder(@PathVariable UUID id) {
        try {
            Order o = shop.getOrder(id);

            return ResponseEntity.status(HttpStatus.OK).body(o);
        } catch(Shop.CustomException e) {
            ErrorResponse er = new ErrorResponse(e.getMessage(), e.getStatus().value());
            return ResponseEntity.status(e.getStatus()).body(er);
        }
    }


    @PostMapping(value = "")
    @Operation(summary = "Place order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order created", content = @Content(schema = @Schema(implementation = Order.class))),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal Error",
                    content = @Content)
    })
    public ResponseEntity placeOrder(@RequestBody OrderInput orderInput) {
        try {
            User u = shop.getUser(orderInput.userID);

            if(orderInput.items.size() < 1) {
                throw new Shop.CustomException("Invalid input: Atleast 1 product needed in order", HttpStatus.BAD_REQUEST);
            }

            ArrayList<ProductQuantity> productsForOrder = new ArrayList<ProductQuantity>();

            for(OrderProductInput p : orderInput.items) {
                ProductQuantity pr = new ProductQuantity();
                pr.product = shop.getProduct(p.productID);
                pr.quantity = p.quantity;

                productsForOrder.add(pr);

            }

            Order o = new Order(productsForOrder, orderInput.userID, orderInput.delivery);
            u.addOrder(o);

            try {
                shop.saveOrdersToFile();
            } catch(IOException e) {
                e.printStackTrace();
                throw new Shop.CustomException("Error while saving file", HttpStatus.INTERNAL_SERVER_ERROR);
            }

            System.gc();
            return ResponseEntity.status(HttpStatus.CREATED).body(o);
        } catch(Shop.CustomException e) {
            ErrorResponse er = new ErrorResponse(e.getMessage(), e.getStatus().value());
            return ResponseEntity.status(e.getStatus()).body(er);
        }
    }


    @PostMapping(value = "{id}")
    @Operation(summary = "Change order status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order created", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal Error",
                    content = @Content)
    })
    public ResponseEntity changeOrderStatus(@PathVariable UUID id, @RequestBody OrderStatusInput statusInput) {
        try {
            Order o = shop.getOrder(id);

            if(!statusInput.override) {
                ArrayList<OrderStatus> ss = o.getStatus();
                Status currentStatus = (ss.get(ss.size() - 1)).getStatus();

                if(Status.valueOf(statusInput.status.toString()).ordinal() <= Status.valueOf(currentStatus.toString()).ordinal()) {
                    throw new Shop.CustomException("Cannot override higher priority status with lower one. Provided " + statusInput.status +
                            " but current status is " + currentStatus + ".", HttpStatus.NOT_ACCEPTABLE);
                }
            }
            o.setStatus(statusInput.status);

            try {
                shop.saveOrdersToFile();
            } catch(IOException e) {
                e.printStackTrace();
                throw new Shop.CustomException("Error while saving file", HttpStatus.INTERNAL_SERVER_ERROR);
            }

            System.gc();

            return ResponseEntity.status(HttpStatus.CREATED).body(null);
        } catch(Shop.CustomException e) {
            ErrorResponse er = new ErrorResponse(e.getMessage(), e.getStatus().value());
            return ResponseEntity.status(e.getStatus()).body(er);
        }
    }
}
