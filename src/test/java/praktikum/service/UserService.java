package praktikum.service;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import praktikum.UserRequest;

import static io.restassured.RestAssured.given;

public class UserService extends BaseService {
    public static final String PATH_REGISTRATION = "/api/auth/register";
    public static final String PATH_AUTH = "/api/auth/login";
    public static final String PATH_DELETE = "/api/auth/user";

    @Step
    public Response registerUser(UserRequest userRequest) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(userRequest)
                .when()
                .post(PATH_REGISTRATION);
    }

    public String getToken(UserRequest userRequest){
        var request = auth(userRequest);
        return request.then().extract().path("accessToken");
    }

    public void delete(UserRequest userRequest){
        var token = getToken(userRequest);

        given()
                .header("Content-type", "application/json")
                .header("Authorization", token)
                .and()
                .when()
                .delete(PATH_DELETE)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_ACCEPTED);
    }

    public Response auth(UserRequest userRequest){
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(userRequest)
                .when()
                .post(PATH_AUTH);
    }

    public static UserRequest randomUser() {
        return UserRequest.builder()
                .email(faker.internet().emailAddress())
                .name(faker.name().username())
                .password(faker.crypto().sha1())
                .build();
    }
}