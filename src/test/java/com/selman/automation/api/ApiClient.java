package com.selman.automation.api;

import com.selman.automation.config.ConfigManager;
import io.restassured.RestAssured;
import io.restassured.response.Response;

    public class ApiClient {

        private final String baseUrl = ConfigManager.get().baseUrl();

        public String loginAndGetSessionCookie(String username, String password) {

            Response loginPage = RestAssured.given()
                    .redirects().follow(false)
                    .when()
                    .get(baseUrl + "/web/index.php/auth/login");

            String initialSession = loginPage.getCookie("orangehrm");
            String csrfToken = extractCsrfToken(loginPage.getBody().asString());

            Response loginResponse = RestAssured.given()
                    .cookie("orangehrm", initialSession)
                    .contentType("application/x-www-form-urlencoded")
                    .formParam("_token", csrfToken)
                    .formParam("username", username)
                    .formParam("password", password)
                    .redirects().follow(false)
                    .when()
                    .post(baseUrl + "/web/index.php/auth/validate");

            return loginResponse.getCookie("orangehrm");
        }

        private String extractCsrfToken(String html) {
            int tokenIndex = html.indexOf(":token=");
            int start = html.indexOf("&quot;", tokenIndex) + 6;
            int end = html.indexOf("&quot;", start);
            return html.substring(start, end);
        }
    }




