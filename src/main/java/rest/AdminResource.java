/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.Context;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import utils.EMF_Creator;

/**
 * REST Web Service
 *
 * @author magda
 */
@Path("admin")
public class AdminResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();

    @Context
    SecurityContext securityContext;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("test")
    @RolesAllowed("admin")
    public String getFromAdmin() {
       // String thisuser = securityContext.getUserPrincipal().getName();
       boolean isLoggedIn=false;
       if (isLoggedIn==false) {
       throw new NotAuthorizedException("Not authenticated - do login");
       }
        return "{\"msg\": \"Yes, you are aothorized to see this\"}";
    }
    
}