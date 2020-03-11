package org.nguyen.orderjava.controllers;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.nguyen.orderjava.exceptions.OrderNotFoundException;
import org.nguyen.orderjava.models.OrderData;
import org.nguyen.orderjava.services.OrderService;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderTest {

    @LocalServerPort
    private int port;

    @InjectMocks
    Order controller;

    @MockBean
    OrderService orderService;

    @Test
    void getOrderById_ShouldReturnOrderData_GivenOrderExists() throws OrderNotFoundException {
        OrderData mock = new OrderData();

        mock.setId("test");
        
        when(orderService.getOrderById(any())).thenReturn(mock);

        given()
            .port(port)
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
        when(orderService.getOrderById(any())).thenThrow(new OrderNotFoundException());

        given()
            .port(port)
            .queryParam("id", "test")
        .when()
            .get("/order")
        .then()
            .assertThat()
            .statusCode(500)
            .contentType("application/json");
    }
}