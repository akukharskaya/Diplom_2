package praktikum.service;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import praktikum.OrderRequest;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

public class OrderService extends BaseService {
    public static final String PATH_ORDER = "/api/orders";
    public static final String PATH_INGREDIENTS = "/api/ingredients";

    @Step("Запрос на просмотр списка доступных ингредиентов. Send get /api/ingredients")
    public Response getIngredientsInfo() {
        return given()
                .header("Content-type", "application/json")
                .and()
                .when()
                .get(PATH_INGREDIENTS);
    }

    @Step("Запрос на создание заказа. Send post /api/ingredients")
    public Response create(String token, OrderRequest request) {
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", token)
                .and()
                .body(request)
                .when()
                .post(PATH_ORDER);
    }

    @Step("Запрос на просмотр заказов определенного юзера. Send post /api/ingredients\"")
    public Response getInfoUsersOrder(String token) {
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", token)
                .and()
                .when()
                .get(PATH_ORDER);
    }

    @Step("Выбор рандомных ингредиентов")
    public List<String> pickRandomIngredients(List<String> allIngredients, int elements) {
        List<String> picked = new ArrayList<>();

        for (int i = 0; i<elements; i++){
            var ingredient = allIngredients.get(random.nextInt(0, allIngredients.size()));
            picked.add(ingredient);
        }

        return picked;
    }
}
