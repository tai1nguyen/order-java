package org.nguyen.orderjava.controllers;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.nguyen.orderjava.models.BeanTypeEnum;
import org.nguyen.orderjava.models.jpa.InventoryEntryJpa;
import org.nguyen.orderjava.repositories.InventoryRepository;
import org.nguyen.orderjava.repositories.OrderRepository;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration(exclude = {
    DataSourceAutoConfiguration.class,
    DataSourceTransactionManagerAutoConfiguration.class,
    HibernateJpaAutoConfiguration.class
})
class InventoryControllerIntTest {

    private final String INVENTORY_RESPONSE_SCHEMA = "json/inventory-response.schema.json";

    private final String INVENTORY_LIST_RESPONSE_SCHEMA = "json/inventory-list-response.schema.json";

    @LocalServerPort
    private int portNumber;

    @MockBean
    OrderRepository orderRepo;

    @MockBean
    InventoryRepository inventoryRepo;

    @Test
    void Given_BeanDataExists_When_ARequestIsMadeForBeanDataByType_Then_BeanDataShouldBeReturned() {
        InventoryEntryJpa mock = new InventoryEntryJpa(
            BeanTypeEnum.ARABICA,
            new BigDecimal("1"),
            new BigDecimal("0"),
            1
        );
        when(inventoryRepo.findById(any())).thenReturn(Optional.of(mock));

        given()
            .port(portNumber)
            .queryParam("beanType", BeanTypeEnum.ARABICA.getName())
        .when()
            .get("/order-java/v1/inventory/bean")
        .then()
            .assertThat()
            .statusCode(HttpStatus.OK.value())
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(matchesJsonSchemaInClasspath(INVENTORY_RESPONSE_SCHEMA));
    }

    @Test
    void Given_BeanDataExists_When_ARequestIsMadeForAllBeanData_Then_AllBeanDataShouldBeReturnedInAList() {
        List<InventoryEntryJpa> list = new ArrayList<>();
        list.add(
            new InventoryEntryJpa(BeanTypeEnum.ARABICA, new BigDecimal("1"), new BigDecimal("0"), 1)
        );
        when(inventoryRepo.findAll()).thenReturn(list);

        given()
            .port(portNumber)
        .when()
            .get("/order-java/v1/inventory/beans")
        .then()
            .assertThat()
            .statusCode(HttpStatus.OK.value())
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(matchesJsonSchemaInClasspath(INVENTORY_LIST_RESPONSE_SCHEMA));
    }
}
