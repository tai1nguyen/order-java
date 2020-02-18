package org.nguyen.orderjava.controllers;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.nguyen.orderjava.models.Bean.BeanType;
import org.nguyen.orderjava.models.jpa.InventoryEntry;
import org.nguyen.orderjava.services.InventoryRepoService;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class InventoryTest {

    @LocalServerPort
    private int port;

    @InjectMocks
    Inventory controller;

    @MockBean
    InventoryRepoService repoService;

    @Test
    void getInventoryDataForBeanType_ShouldReturnDataForBeanType_GivenBeanTypeExistsInDatabase() {
        InventoryEntry mock = new InventoryEntry();
        
        mock.setBeanType(BeanType.ARABICA);
        mock.setPricePerUnit("0");
        mock.setWeightPerUnit("1");
        mock.setQuantity("1");

        when(repoService.getEntryByType(any())).thenReturn(mock);

        given()
            .port(port)
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
}
