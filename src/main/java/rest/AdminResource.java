/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import errorhandling.API_Exception;
import facades.UserFacade;
import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.DELETE;
import javax.ws.rs.core.Context;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import utils.EMF_Creator;
import utils.HttpUtils;

/**
 * REST Web Service
 *
 * @author magda
 */
@Path("admin")
public class AdminResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static final UserFacade user_facade = UserFacade.getUserFacade(EMF);
    private static final Gson GSON = HttpUtils.getGSON();
    @Context
    SecurityContext securityContext;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("test")
    @RolesAllowed("admin")
    public String getFromAdmin() {

        return "{\"msg\": \"Yes, you are aothorized to see this\"}";
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("deleteuser/{username}")
    @RolesAllowed("admin")
    public String deleteUser(@PathParam("username") String username) throws API_Exception {
        String status = user_facade.deleteUser(username);
        JsonObject data = new JsonObject();
        data.addProperty("msg", status);

        return GSON.toJson(data);
    }

}
