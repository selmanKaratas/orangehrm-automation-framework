package com.selman.automation.api;

import com.selman.automation.config.ConfigManager;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EmployeeApiTest {

    private static String sessionCookie;

    @BeforeAll
    static void authenticate() {
        sessionCookie = new ApiClient().loginAndGetSessionCookie(
                ConfigManager.get().defaultUsername(),
                ConfigManager.get().defaultPassword()
        );
    }

    @Test
    void employeeListEndpointReturns200AndValidStructure() {
        Response response = RestAssured.given()
                .cookie("orangehrm", sessionCookie)
                .queryParam("limit", 5)
                .when()
                .get(ConfigManager.get().baseUrl() + "/web/index.php/api/v2/pim/employees");

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.contentType()).contains("application/json");

        Integer total = response.jsonPath().get("meta.total");
        assertThat(total).as("Employee total count").isPositive();
    }

    @Test
    void employeeListRespectsLimitParameter() {
        Response response = RestAssured.given()
                .cookie("orangehrm", sessionCookie)
                .queryParam("limit", 3)
                .when()
                .get(ConfigManager.get().baseUrl() + "/web/index.php/api/v2/pim/employees");

        java.util.List<Object> employees = response.jsonPath().getList("data");
        assertThat(employees).hasSizeLessThanOrEqualTo(3);
    }

    @Test
    void unauthenticatedRequestIsRejected() {
        Response response = RestAssured.given()
                .when()
                .get(ConfigManager.get().baseUrl() + "/web/index.php/api/v2/pim/employees");

        assertThat(response.statusCode()).isNotEqualTo(200);
    }
}