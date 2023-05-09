package praktikum.service;

import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import praktikum.UserRequest;

import static io.restassured.RestAssured.given;

public class UserService extends BaseService {
    public static final String PATH_REGISTRATION = "/api/auth/register";
    public static final String PATH_LOGIN = "/api/auth/login";
    public static final String PATH_USER = "/api/auth/user";

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
                .delete(PATH_USER)
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
                .post(PATH_LOGIN);
    }

    public Response updateUser(String token, UserRequest updateRequest){

        return given()
                .header("Content-type", "application/json")
                .header("Authorization", token)
                .and()
                .when()
                .body(updateRequest)
                .patch(PATH_USER);

    }

    public Response getInfo(String token){

        return given()
                .header("Content-type", "application/json")
                .header("Authorization", token)
                .and()
                .when()
                .get(PATH_USER);

    }

    public static UserRequest randomUser() {
        return UserRequest.builder()
                .email(faker.internet().emailAddress())
                .name(faker.name().firstName())
                .password(faker.crypto().sha1())
                .build();
    }
}