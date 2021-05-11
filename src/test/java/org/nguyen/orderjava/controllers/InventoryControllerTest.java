package org.nguyen.orderjava.controllers;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.nguyen.orderjava.models.BeanTypeEnum;
import org.nguyen.orderjava.models.jpa.InventoryEntryJpa;
import org.nguyen.orderjava.repositories.InventoryRepository;
import org.nguyen.orderjava.services.InventoryRepoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class InventoryControllerTest {

    @LocalServerPort
    private int portNumber;

    @InjectMocks
    InventoryController controller;

    @Autowired
    InventoryRepoService repoService;

    @MockBean
    InventoryRepository inventoryRepo;

    @Test
    void getInventoryDataForBeanType_ShouldReturnDataForBeanType_GivenBeanTypeExistsInDatabase() {
        InventoryEntryJpa mock = new InventoryEntryJpa();
        
        mock.setBeanType(BeanTypeEnum.ARABICA);
        mock.setPricePerUnit("0");
        mock.setWeightPerUnit("1");
        mock.setQuantity("1");

        when(inventoryRepo.findById(any())).thenReturn(Optional.of(mock));

        given()
            .port(portNumber)
            .queryParam("type", BeanTypeEnum.ARABICA.getName())
        .when()
            .get("/order-java/v1/inventory/bean")
        .then()
            .assertThat()
            .statusCode(200)
            .contentType("application/json")
            .body("beanType", equalTo(BeanTypeEnum.ARABICA.getName()))
            .body("pricePerUnit", equalTo("0"))
            .body("weightPerUnit", equalTo("1"))
            .body("quantity", equalTo("1"));
    }

    @Test
    void getAllInventoryData_ShouldReturnAListOfBeanData() {
        List<InventoryEntryJpa> list = new ArrayList<>();
        InventoryEntryJpa mock = new InventoryEntryJpa();
        
        mock.setBeanType(BeanTypeEnum.ARABICA);
        mock.setPricePerUnit("0");
        mock.setWeightPerUnit("1");
        mock.setQuantity("1");

        list.add(mock);

        when(inventoryRepo.findAll()).thenReturn(list);

        List<InventoryEntryJpa> result = given()
            .port(portNumber)
        .when()
            .get("/order-java/v1/inventory/beans")
        .then()
            .assertThat()
            .statusCode(200)
            .contentType("application/json")
            .extract()
            .response()
            .jsonPath()
            .getList("$");

        assertEquals(result.size(), 1);
    }
}
