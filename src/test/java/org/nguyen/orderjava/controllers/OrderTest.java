package org.nguyen.orderjava.controllers;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.nguyen.orderjava.exceptions.OrderNotFoundException;
import org.nguyen.orderjava.models.OrderData;
import org.nguyen.orderjava.models.jpa.OrderEntry;
import org.nguyen.orderjava.repositories.InventoryRepository;
import org.nguyen.orderjava.repositories.OrderRepository;
import org.nguyen.orderjava.services.OrderMapperService;
import org.nguyen.orderjava.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderTest {

    @LocalServerPort
    private int portNumber;

    @InjectMocks
    Order controller;

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
        OrderEntry mock = new OrderEntry();
        mock.setId("test");
        
        when(orderRepo.findById(any())).thenReturn(Optional.of(mock));

        given()
            .port(portNumber)
            .queryParam("id", "test")
        .when()
            .get("/order")
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
            .get("/order")
        .then()
            .assertThat()
            .statusCode(500)
            .contentType("application/json");
    }

    @Test
    void createOrder_ShouldInsertAnOrderEntryIntoTheDatabase_GivenOrderDataIsProvided() {
        OrderData mock = new OrderData();
        OrderEntry mockResponse = new OrderEntry();

        mock.setPrice(new BigDecimal(100));
        mock.setBeans(new ArrayList<>());
        mockResponse.setId("test");

        when(orderRepo.save(any())).thenReturn(mockResponse);

        given()
            .port(portNumber)
            .request().body(mock)
            .contentType("application/json")
        .when()
            .post("/order")
        .then()
            .assertThat()
            .statusCode(200)
            .body("id", equalTo("test"))
            .contentType("application/json");
    }
}