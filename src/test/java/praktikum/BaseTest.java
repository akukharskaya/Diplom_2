package praktikum;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.Before;


public abstract class BaseTest {
    public static final String URL = "https://stellarburgers.nomoreparties.site";

    @Before
    public void setUp() {
        RestAssured.baseURI = URL;
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }
}