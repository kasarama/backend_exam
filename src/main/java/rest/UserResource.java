/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dto.demo.UserDTO;
import errorhandling.API_Exception;
import errorhandling.NotFoundException;
import facades.UserFacade;
import fetch.DemoFetch;
import java.util.concurrent.ExecutorService;
import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Context;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import security.errorhandling.AuthenticationException;
import utils.EMF_Creator;
import utils.HttpUtils;

/**
 * REST Web Service
 *
 * @author magda
 */
@Path("user")
public class UserResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static final Gson GSON = HttpUtils.getGSON();
    private static final ExecutorService threadPool = HttpUtils.getThreadPool();
    private static final UserFacade user_facade = UserFacade.getUserFacade(EMF);

    @Context
    SecurityContext securityContext;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("test")
    @RolesAllowed("user")
    public String getFromAdmin() {
        // String thisuser = securityContext.getUserPrincipal().getName();
        return "{\"msg\": \"Yes, you are aothorized to see this\"}";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("demo")
    @RolesAllowed("user")
    public String getDemo() {

        return GSON.toJson(DemoFetch.fetchDemo(GSON, threadPool));
        // return "{\"msg\": \"Yes, you are aothorized to see this\"}";
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("add")
    public String addUser(String jsonString) throws AuthenticationException, API_Exception, NotFoundException {
        String username;
        String password;
        System.out.println("jsonString:" +jsonString);
        try {
            JsonObject json = JsonParser.parseString(jsonString).getAsJsonObject();
            username = json.get("username").getAsString();
            password = json.get("password").getAsString();
        } catch (Exception e) {
            throw new API_Exception("Malformed JSON Suplied", 400, e);
        }
        UserDTO added = user_facade.addNewUser(username, password);
        return GSON.toJson(added);
    }

   
}
