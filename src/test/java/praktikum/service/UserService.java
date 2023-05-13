package praktikum.service;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import praktikum.UserRequest;

import static io.restassured.RestAssured.given;

public class UserService extends BaseService {
    public static final String PATH_REGISTRATION = "/api/auth/register";
    public static final String PATH_LOGIN = "/api/auth/login";
    public static final String PATH_USER = "/api/auth/user";

    @Step("Запрос на регистрацию нового юзера. Send post /api/auth/register")
    public Response registerUser(UserRequest userRequest) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(userRequest)
                .when()
                .post(PATH_REGISTRATION);
    }

    @Step("Изъятие токина юзера")
    public String getToken(UserRequest userRequest){
        var request = auth(userRequest);
        return request.then().extract().path("accessToken");
    }

    @Step("Запрос на удаление юзера. Send delete /api/auth/user")
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

    @Step("Запрос на авторизацию. Send post /api/auth/login")
    public Response auth(UserRequest userRequest){
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(userRequest)
                .when()
                .post(PATH_LOGIN);
    }

    @Step("Запрос на обновление кредов/имени юзера. Send patch /api/auth/user")
    public Response updateUser(String token, UserRequest updateRequest){

        return given()
                .header("Content-type", "application/json")
                .header("Authorization", token)
                .and()
                .when()
                .body(updateRequest)
                .patch(PATH_USER);

    }

    @Step("Получение данных юзера. Send get /api/auth/user")
    public Response getInfo(String token){

        return given()
                .header("Content-type", "application/json")
                .header("Authorization", token)
                .and()
                .when()
                .get(PATH_USER);

    }

    @Step("Строение рандомного юзера")
    public static UserRequest randomUser() {
        return UserRequest.builder()
                .email(faker.internet().emailAddress())
                .name(faker.name().firstName())
                .password(faker.crypto().sha1())
                .build();
    }
}