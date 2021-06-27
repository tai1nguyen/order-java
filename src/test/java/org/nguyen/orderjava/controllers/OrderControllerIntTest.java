package org.nguyen.orderjava.controllers;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.nguyen.orderjava.models.BeanTypeEnum;
import org.nguyen.orderjava.models.dto.OrderContentDto;
import org.nguyen.orderjava.models.dto.OrderDto;
import org.nguyen.orderjava.models.dto.OrderUpdateDto;
import org.nguyen.orderjava.models.jpa.InventoryEntryJpa;
import org.nguyen.orderjava.models.jpa.OrderContentJpa;
import org.nguyen.orderjava.models.jpa.OrderJpa;
import org.nguyen.orderjava.repositories.InventoryRepository;
import org.nguyen.orderjava.repositories.OrderRepository;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration(
    exclude = {
        DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class
    }
)
class OrderControllerIntTest {

    private final String ORDER_RESPONSE_SCHEMA = "json/order-response.schema.json";

    @LocalServerPort
    private int portNumber;

    @MockBean
    OrderRepository orderRepo;

    @MockBean
    InventoryRepository inventoryRepo;

    @Test
    void Given_OrderDataExists_When_ARequestForOrderByIdIsMade_Then_OrderShouldBeReturned() {
        OrderJpa order = new OrderJpa();
        OrderContentJpa orderContent1 = new OrderContentJpa(BeanTypeEnum.ARABICA, 1);
        OrderContentJpa orderContent2 = new OrderContentJpa(BeanTypeEnum.LIBERIAN, 4);
        orderContent1.setId("1");
        orderContent2.setId("2");
        order.addContent(orderContent1);
        order.addContent(orderContent2);
        order.setId("1");
        order.setOrderedBy("foo");
        when(orderRepo.findById("1")).thenReturn(Optional.of(order));
        when(inventoryRepo.findAll()).thenReturn(createInventory());

        given()
                .port(portNumber)
                .queryParam("id", "1")
        .when()
                .get("/order-java/v1/order")
        .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(matchesJsonSchemaInClasspath(ORDER_RESPONSE_SCHEMA));
    }

    @Test
    void Given_OrderDataDoesNotExist_When_ARequestToGetOrderByIdIsMade_Then_RespondWithNotFoundStatusCode() {
        when(orderRepo.findById("1")).thenReturn(Optional.empty());

        given()
                .port(portNumber)
                .queryParam("id", "1")
        .when()
                .get("/order-java/v1/order")
        .then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("error", equalTo("Not Found"))
                .body("message", equalTo("Order '1' Not Found"))
                .body("path", equalTo("/order-java/v1/order"));
    }

    @Test
    void Given_OrderData_When_ARequestToCreateOrderIsMade_Then_OrderShouldBeSavedAndReturned() {
        OrderDto order = new OrderDto();
        OrderContentDto orderContent1 = new OrderContentDto(BeanTypeEnum.ARABICA, 1);
        OrderContentDto orderContent2 = new OrderContentDto(BeanTypeEnum.LIBERIAN, 4);
        order.setContents(Arrays.asList(orderContent1, orderContent2));
        order.setOrderedBy("foo");
        when(orderRepo.save(any(OrderJpa.class))).then(returnOrderWithId("1"));
        when(inventoryRepo.findAll()).thenReturn(createInventory());

        given()
                .port(portNumber)
                .request()
                .body(order)
                .contentType("application/json")
        .when()
                .post("/order-java/v1/order")
        .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(matchesJsonSchemaInClasspath(ORDER_RESPONSE_SCHEMA));
    }

    @Test
    void Given_OrderExists_When_ARequestToUpdateOrderIsMadeWithOrderIdInPath_Then_OrderShouldBeUpdated() {
        OrderJpa order = new OrderJpa();
        OrderContentJpa orderContent1 = new OrderContentJpa(BeanTypeEnum.ARABICA, 1);
        OrderContentJpa orderContent2 = new OrderContentJpa(BeanTypeEnum.LIBERIAN, 4);
        orderContent1.setId("1");
        orderContent2.setId("2");
        order.addContent(orderContent1);
        order.addContent(orderContent2);
        order.setId("1");
        order.setOrderedBy("foo");
        when(orderRepo.save(any(OrderJpa.class))).then(returnOrderWithId(null));
        when(orderRepo.findById("1")).thenReturn(Optional.of(order));
        when(inventoryRepo.findAll()).thenReturn(createInventory());

        given()
                .port(portNumber)
                .request()
                .body(createOrderUpdate("1"))
                .contentType("application/json")
        .when()
                .patch("/order-java/v1/order/1")
        .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(matchesJsonSchemaInClasspath(ORDER_RESPONSE_SCHEMA));
    }

    @Test
    void Given_OrderExists_When_ARequestToUpdateOrderIsMadeWithoutOrderIdInPath_Then_OrderShouldBeUpdated() {
        OrderJpa order = new OrderJpa();
        OrderContentJpa orderContent1 = new OrderContentJpa(BeanTypeEnum.ARABICA, 1);
        OrderContentJpa orderContent2 = new OrderContentJpa(BeanTypeEnum.LIBERIAN, 4);
        orderContent1.setId("1");
        orderContent2.setId("2");
        order.addContent(orderContent1);
        order.addContent(orderContent2);
        order.setId("1");
        order.setOrderedBy("foo");
        when(orderRepo.save(any(OrderJpa.class))).then(returnOrderWithId(null));
        when(orderRepo.findById("1")).thenReturn(Optional.of(order));
        when(inventoryRepo.findAll()).thenReturn(createInventory());

        given()
                .port(portNumber)
                .request()
                .body(createOrderUpdate("1"))
                .contentType("application/json")
        .when()
                .put("/order-java/v1/order")
        .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(matchesJsonSchemaInClasspath(ORDER_RESPONSE_SCHEMA));
    }

    @Test
    void Given_OrderDoesNotExist_When_ARequestToUpdateOrderIsMadeWithOrderIdInPath_Then_RespondWithNotFoundStatusCode() {
        when(orderRepo.findById("1")).thenReturn(Optional.empty());

        given()
                .port(portNumber)
                .request()
                .body(createOrderUpdate("1"))
                .contentType("application/json")
        .when()
                .patch("/order-java/v1/order/1")
        .then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("error", equalTo("Not Found"))
                .body("message", equalTo("Order '1' Not Found"))
                .body("path", equalTo("/order-java/v1/order/1"));
    }

    @Test
    void Given_OrderDoesNotExist_When_ARequestToUpdateOrderIsMadeWithoutIdInPath_Then_RespondWithNotFoundStatusCode() {
        when(orderRepo.findById("1")).thenReturn(Optional.empty());

        given()
                .port(portNumber)
                .request()
                .body(createOrderUpdate("1"))
                .contentType("application/json")
        .when()
                .put("/order-java/v1/order")
        .then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("error", equalTo("Not Found"))
                .body("message", equalTo("Order '1' Not Found"))
                .body("path", equalTo("/order-java/v1/order"));
    }

    @Test
    void Given_OrderExists_When_ARequestToDeleteOrderIsMade_Then_ServiceWillRespondWithOkStatusCode() {
        doNothing().when(orderRepo).deleteById("1");

        given()
                .port(portNumber)
        .when()
                .delete("/order-java/v1/order/1")
        .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value());
    }

    private OrderUpdateDto createOrderUpdate(String id) {
        OrderUpdateDto orderUpdate = new OrderUpdateDto();
        orderUpdate.setId(id);

        orderUpdate.setContentAdditions(
                Arrays.asList(new OrderContentDto(BeanTypeEnum.ROBUSTA, 3))
        );
        orderUpdate.setContentDeletions(
                Arrays.asList(new OrderContentDto(BeanTypeEnum.ARABICA, null))
        );
        orderUpdate.setContentUpdates(
                Arrays.asList(new OrderContentDto(BeanTypeEnum.LIBERIAN, 6))
        );

        return orderUpdate;
    }

    private List<InventoryEntryJpa> createInventory() {
        List<InventoryEntryJpa> list = new ArrayList<>();

        list.add(
                new InventoryEntryJpa(
                        BeanTypeEnum.ARABICA,
                        new BigDecimal("0.25"),
                        new BigDecimal("0.25"),
                        10
                )
        );
        list.add(
                new InventoryEntryJpa(
                        BeanTypeEnum.EXCELSA,
                        new BigDecimal("0.50"),
                        new BigDecimal("0.50"),
                        10
                )
        );
        list.add(
                new InventoryEntryJpa(
                        BeanTypeEnum.LIBERIAN,
                        new BigDecimal("0.75"),
                        new BigDecimal("0.75"),
                        10
                )
        );
        list.add(
                new InventoryEntryJpa(
                        BeanTypeEnum.ROBUSTA,
                        new BigDecimal("1.00"),
                        new BigDecimal("1.00"),
                        10
                )
        );

        return list;
    }

    private Answer<OrderJpa> returnOrderWithId(String id) {
        return new Answer<OrderJpa>() {

            @Override
            public OrderJpa answer(InvocationOnMock invocation) throws Throwable {
                OrderJpa argument = invocation.getArgument(0, OrderJpa.class);

                if (StringUtils.isBlank(argument.getId())) {
                    argument.setId(id);
                }

                return argument;
            }
        };
    }
}
