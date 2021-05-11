package org.nguyen.orderjava.controllers;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.nguyen.orderjava.exceptions.OrderNotFoundException;
import org.nguyen.orderjava.models.dto.OrderDto;
import org.nguyen.orderjava.models.dto.OrderUpdateDto;
import org.nguyen.orderjava.models.jpa.OrderEntryJpa;
import org.nguyen.orderjava.repositories.InventoryRepository;
import org.nguyen.orderjava.repositories.OrderRepository;
import org.nguyen.orderjava.services.OrderMapperService;
import org.nguyen.orderjava.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderControllerTest {

    @LocalServerPort
    private int portNumber;

    @InjectMocks
    OrderController controller;

    @MockBean
    OrderRepository orderRepo;

    @MockBean
    InventoryRepository inventoryRepo;

    @Autowired
    OrderMapperService orderMapper;

    @Autowired
    OrderService orderService;

    @Test
    void getOrderById_ShouldReturnOrderData_GivenOrderExists() throws OrderNotFoundException {
        OrderEntryJpa mock = new OrderEntryJpa();

        mock.setId("test");
        
        when(orderRepo.findById(any())).thenReturn(Optional.of(mock));

        given()
            .port(portNumber)
            .queryParam("id", "test")
        .when()
            .get("/order-java/v1/order")
        .then()
            .assertThat()
            .statusCode(200)
            .contentType("application/json")
            .body("id", equalTo("test"));
    }

    @Test
    void getOrderById_ShouldReturnError_GivenExceptionIsThrown() throws OrderNotFoundException {
        when(orderRepo.findById(any())).thenReturn(null);

        given()
            .port(portNumber)
            .queryParam("id", "test")
        .when()
            .get("/order-java/v1/order")
        .then()
            .assertThat()
            .statusCode(500)
            .contentType("application/json");
    }

    @Test
    void createOrder_ShouldInsertAnOrderEntryIntoTheDatabase_GivenOrderDataIsProvided() {
        OrderDto mock = new OrderDto();
        OrderEntryJpa mockResponse = new OrderEntryJpa();

        mock.setPrice(new BigDecimal(100));
        mock.setBeans(new ArrayList<>());
        mockResponse.setId("test");

        when(orderRepo.save(any())).thenReturn(mockResponse);

        given()
            .port(portNumber)
            .request().body(mock)
            .contentType("application/json")
        .when()
            .post("/order-java/v1/order")
        .then()
            .assertThat()
            .statusCode(200)
            .body("id", equalTo("test"))
            .contentType("application/json");
    }

    @Test
    void updateOrder_ShouldUpdateAnExistingOrderAndThenSaveTheChange_GivenAnExistingOrderExists() {
        OrderUpdateDto mock = new OrderUpdateDto();
        OrderEntryJpa mockOrderEntry = new OrderEntryJpa();

        mock.setBeanAdditions(new ArrayList<>());
        mock.setBeanDeletions(new ArrayList<>());
        mock.setBeanUpdates(new ArrayList<>());
        mockOrderEntry.setId("test");

        when(orderRepo.save(any())).thenReturn(mockOrderEntry);
        when(orderRepo.findById("test")).thenReturn(Optional.of(mockOrderEntry));

        given()
            .port(portNumber)
            .request().body(mock)
            .contentType("application/json")
        .when()
            .patch("/order-java/v1/order/test")
        .then()
            .assertThat()
            .statusCode(200)
            .body("id", equalTo("test"))
            .contentType("application/json");
    }

    @Test
    void updateOrder_ShouldUpdateAnExistingOrderAndThenSaveTheChange_GivenNoIdInPathAndAnExistingOrderExists() {
        OrderUpdateDto mock = new OrderUpdateDto();
        OrderEntryJpa mockOrderEntry = new OrderEntryJpa();

        mock.setBeanAdditions(new ArrayList<>());
        mock.setBeanDeletions(new ArrayList<>());
        mock.setBeanUpdates(new ArrayList<>());
        mock.setId("test");
        mockOrderEntry.setId("test");

        when(orderRepo.save(any())).thenReturn(mockOrderEntry);
        when(orderRepo.findById("test")).thenReturn(Optional.of(mockOrderEntry));

        given()
            .port(portNumber)
            .request().body(mock)
            .contentType("application/json")
        .when()
            .put("/order-java/v1/order")
        .then()
            .assertThat()
            .statusCode(200)
            .body("id", equalTo("test"))
            .contentType("application/json");
    }

    @Test
    void updateOrder_ShouldReturnNotFoundException_GivenAnExistingOrderEntryIsNotFound() {
        OrderUpdateDto mock = new OrderUpdateDto();

        when(orderRepo.findById("test")).thenReturn(Optional.ofNullable(null));

        given()
            .port(portNumber)
            .request().body(mock)
            .contentType("application/json")
        .when()
            .patch("/order-java/v1/order/test")
        .then()
            .assertThat()
            .statusCode(404)
            .body("error", equalTo("Not Found"))
            .body("message", equalTo("Order 'test' Not Found"))
            .body("path", equalTo("/order-java/v1/order/test"))
            .contentType("application/json");
    }

    @Test
    void updateOrder_ShouldReturnNotFoundException_GivenNoIdInPathAndAnExistingOrderEntryIsNotFound() {
        OrderUpdateDto mock = new OrderUpdateDto();
        mock.setId("test");

        when(orderRepo.findById("test")).thenReturn(Optional.ofNullable(null));

        given()
            .port(portNumber)
            .request().body(mock)
            .contentType("application/json")
        .when()
            .put("/order-java/v1/order")
        .then()
            .assertThat()
            .statusCode(404)
            .body("error", equalTo("Not Found"))
            .body("message", equalTo("Order 'test' Not Found"))
            .body("path", equalTo("/order-java/v1/order"))
            .contentType("application/json");
    }

    @Test
    void deleteOrder_ShouldReturnStatusOk() {
        doNothing().when(orderRepo).deleteById("1");

        given()
            .port(portNumber)
        .when()
            .delete("/order-java/v1/order/1")
        .then()
            .assertThat()
            .statusCode(200);
    }
}