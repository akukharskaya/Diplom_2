package praktikum;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.Before;
import praktikum.service.OrderService;
import praktikum.service.UserService;


public abstract class BaseTest {
    public static final String URL = "https://stellarburgers.nomoreparties.site";

    UserService userService = new UserService();
    OrderService orderService = new OrderService();

    UserRequest existingUser = new UserRequest("jimmy.berge@yahoo.com",
            "c531d4d72ab23484254ea24aea649c1a50e1bc6c",
            "jasper.rempel");

    @Before
    public void setUp() {
        RestAssured.baseURI = URL;
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }
}