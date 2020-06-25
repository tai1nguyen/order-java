package org.nguyen.orderjava.controllers;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.nguyen.orderjava.models.BeanType;
import org.nguyen.orderjava.models.jpa.InventoryEntry;
import org.nguyen.orderjava.repositories.InventoryRepository;
import org.nguyen.orderjava.services.InventoryRepoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
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
        InventoryEntry mock = new InventoryEntry();
        
        mock.setBeanType(BeanType.ARABICA);
        mock.setPricePerUnit("0");
        mock.setWeightPerUnit("1");
        mock.setQuantity("1");

        when(inventoryRepo.findById(any())).thenReturn(Optional.of(mock));

        given()
            .port(portNumber)
            .queryParam("type", "arabica")
        .when()
            .get("/inventory/bean")
        .then()
            .assertThat()
            .statusCode(200)
            .contentType("application/json")
            .body("beanType", equalTo(BeanType.ARABICA.getName()))
            .body("pricePerUnit", equalTo("0"))
            .body("weightPerUnit", equalTo("1"))
            .body("quantity", equalTo("1"));
    }

    @Test
    void getAllInventoryData_ShouldReturnAListOfBeanData() {
        List<InventoryEntry> list = new ArrayList<>();
        InventoryEntry mock = new InventoryEntry();
        
        mock.setBeanType(BeanType.ARABICA);
        mock.setPricePerUnit("0");
        mock.setWeightPerUnit("1");
        mock.setQuantity("1");

        list.add(mock);

        when(inventoryRepo.findAll()).thenReturn(list);

        List<InventoryEntry> result = given()
            .port(portNumber)
        .when()
            .get("/inventory/beans")
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
