/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import com.google.gson.JsonObject;
import entities.Role;
import entities.User;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.parsing.Parser;
import java.net.URI;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import static org.hamcrest.Matchers.equalTo;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator;

/**
 *
 * @author magda
 */
public class ResourcesTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";
    private static User user, admin, user_admin;

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    @BeforeAll
    public static void setUpClass() {
        //This method must be called before you request the EntityManagerFactory
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactoryForTest();

        httpServer = startServer();
        //Setup RestAssured
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;
    }

    @AfterAll
    public static void closeTestServer() {
        //System.in.read();

        //Don't forget this, if you called its counterpart in @BeforeAll
        EMF_Creator.endREST_TestWithDB();
        httpServer.shutdownNow();
    }

    // Setup the DataBase (used by the test-server and this test) in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the EntityClass used below to use YOUR OWN (renamed) Entity class
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        user = new User("user", "test");
        admin = new User("admin", "test");
        user_admin = new User("user_admin", "test");
        try {
            em.getTransaction().begin();
            em.createNamedQuery("User.deleteAllRows").executeUpdate();
            em.createQuery("Delete from Role", Role.class).executeUpdate();
            Role userRole = new Role("user");
            Role adminRole = new Role("admin");
            user.addRole(userRole);
            admin.addRole(adminRole);
            user_admin.addRole(userRole);
            user_admin.addRole(adminRole);
            em.persist(userRole);
            em.persist(adminRole);
            em.persist(user);
            em.persist(admin);
            em.persist(user_admin);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    private static String securityToken;

    //Utility method to login and set the returned securityToken
    private static void login(String role, String password) {
        String json = String.format("{username: \"%s\", password: \"%s\"}", role, password);
        securityToken = given()
                .contentType("application/json")
                .body(json)
                //.when().post("/api/login")
                .when().post("/login")
                .then()
                .extract().path("token");
        //System.out.println("TOKEN ---> " + securityToken);
    }

    @Test
    public void testServerIsUp() {
        given().when().get("/info").then().statusCode(200);
    }

    //This test assumes the database contains two rows
    @Test
    public void testDummyMsg() throws Exception {
        given()
                .contentType("application/json")
                .get("/info/").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("msg", equalTo("Hello anonymous"));
    }

    @Test
    public void testCount() throws Exception {
        given()
                .contentType("text/xml")
                .get("/info/all").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("count", equalTo(3));
    }

    //@Disabled
    @Test
    void testAddNewUser() {
        JsonObject data = new JsonObject();
        data.addProperty("username", "Magda");
        data.addProperty("password", "Magda");
        given()
                .contentType("application/json")
                .body(data)
                .when()
                .post("/user/add")
                .then()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("username", equalTo("Magda"));
    }

    @Test
    void testAddNewUserMalformed() {
        JsonObject data = new JsonObject();
        data.addProperty("abc", "Magda");
        data.addProperty("password", "Magda");
        given()
                .contentType("application/json")
                .body(data)
                .when()
                .post("/user/add")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST_400.getStatusCode())
                .body("message", equalTo("Malformed JSON Suplied"));
    }

    @Test
    void testDeleteUser() {
        login("admin", "test");

        JsonObject data = new JsonObject();
        data.addProperty("abc", "Magda");
        data.addProperty("password", "Magda");
        given()
                .contentType("application/json")
                .header("x-access-token", securityToken)
                .when()
                .delete("/admin/deleteuser/user")
                .then()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("msg", equalTo("User deleted"));
    }

    @Test
    void testDeleteUserNoTotAutorized() {
        given()
                .contentType("application/json")
                .when()
                .delete("/admin/deleteuser/user")
                .then()
                .statusCode(403)
                .body("message", equalTo("Not authenticated - do login"));
    }

    @Test
    void testDeleteUserInvalidTOken() {
        login("admin", "test");

        JsonObject data = new JsonObject();
        data.addProperty("abc", "Magda");
        data.addProperty("password", "Magda");
        given()
                .contentType("application/json")
                .header("x-access-token", securityToken + "a")
                .when()
                .delete("/admin/deleteuser/user")
                .then()
                .statusCode(403)
                .body("message", equalTo("Token not valid (timed out?)"));
    }

    @Test
    void testAddContact() {

        login("user", "test");
        JsonObject data = new JsonObject();
        data.addProperty("name", "Magda");
        data.addProperty("email", "Magda");
        data.addProperty("company", "Magda");
        data.addProperty("jobtitle", "Magda");
        data.addProperty("phone", "123456");

        given()
                .contentType("application/json")
                .header("x-access-token", securityToken)
                .body(data)
                .when()
                .post("/contact/addcontact")
                .then()
                .statusCode(200)
                .body("user", equalTo("user"));
    }
    
    @Test
    void testAddContactMalformed() {

        login("user", "test");
        JsonObject data = new JsonObject();
        data.addProperty("xxx", "Magda");
       
        given()
                .contentType("application/json")
                .header("x-access-token", securityToken)
                .body(data)
                .when()
                .post("/contact/addcontact")
                .then()
                .statusCode(400)
                .body("message", equalTo("Malformed JSON Suplied"));
    }
}
