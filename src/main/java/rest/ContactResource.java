/*
 * Programing exam
 */
package rest;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dto.ContactDTO;
import errorhandling.API_Exception;
import facades.ContactFacade;
import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import utils.EMF_Creator;
import utils.HttpUtils;

/**
 * REST Web Service
 *
 * @author magda
 */
@Path("contact")
public class ContactResource {

    @Context
    SecurityContext securityContext;
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static final Gson GSON = HttpUtils.getGSON();
    private static ContactFacade contact_facade = ContactFacade.getContactFacade(EMF);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("user")
    @Path("addcontact")
    public String addContact( String jsonString) throws API_Exception {
        String thisuser = securityContext.getUserPrincipal().getName();
        try {
            JsonObject json = JsonParser.parseString(jsonString).getAsJsonObject();
                ContactDTO data = GSON.fromJson(jsonString, ContactDTO.class);
                ContactDTO result = contact_facade.addContact(data, thisuser);
                return GSON.toJson(result);

        } catch (Exception e) {
            throw new API_Exception("Malformed JSON Suplied", 400, e);
        }

        
    }
}
