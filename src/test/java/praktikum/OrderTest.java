package praktikum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import praktikum.service.UserService;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.greaterThan;

public class OrderTest extends BaseTest {
    UserRequest user;
    String token;
    List<String> allIngredients;

    @Before
    public void init(){
         user = UserService.randomUser();
         userService.registerUser(user);
         token = userService.getToken(user);

        var response = orderService.getIngredientsInfo();
        allIngredients = response.then().extract().path("data._id");
    }

    @After
    public void clean(){
        userService.delete(user);
    }

    @Test
    @DisplayName("Создание заказа авторизованным пользователем")
    public void testCreateOrderWithAuth() {
        // when
        List<String> ingredients = orderService.pickRandomIngredients(allIngredients, 5);
        var response = orderService.create(token, new OrderRequest(ingredients));

        // then
        response
                .then()
                .assertThat()
                .body("order.number", greaterThan(0));
    }

    @Ignore("по документации без авторизации должен возращаться редирект, но заказ успешно создается")
    @DisplayName("Создание заказа без авторизации")
    @Test
    public void testCreateOrderFailOnNoAuth() {
        List<String> ingredients = orderService.pickRandomIngredients(allIngredients, 5);
        var response = orderService.create("", new OrderRequest(ingredients));

        response
                .then()
                .assertThat()
                .header("Location", contains("/login"));
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    public void testCreateOrderWithoutIngredients() {
        // when
        List<String> ingredients = orderService.pickRandomIngredients(allIngredients, 0);
        var response = orderService.create(token, new OrderRequest(ingredients));

        // then
        response
                .then()
                .assertThat()
                .body("message",equalTo("Ingredient ids must be provided"))
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиента")
    public void testCreateOrderFailOnIncorrectIngredient() {
        // when
        List<String> ingredients = orderService.pickRandomIngredients(allIngredients, 3);
        //добавление ингредиента с неправильным хешем
        ingredients.add("61c0c5a71d1f82001bdaaa6vv");
        var response = orderService.create(token, new OrderRequest(ingredients));

        // then
        response
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }


    @Test
    @DisplayName("Получение заказов авторизованного пользователя")
    public void testGetInfoUsersOrder() {
        String token = userService.getToken(existingUser);

        Response response = orderService.getInfoUsersOrder(token);

        response.then()
                .assertThat()
                .body("success", equalTo(true))
                .statusCode(HttpStatus.SC_OK);

    }


    @Test
    @DisplayName("Получение заказов неавторизованного пользователя")
    public void testGetInfoOrderFailUserNotExisting() {

        Response response = orderService.getInfoUsersOrder(" ");

        response.then()
                .assertThat()
                .body("success", equalTo(false))
                .body("message",equalTo("You should be authorised"))
                .statusCode(HttpStatus.SC_UNAUTHORIZED);

    }
}
