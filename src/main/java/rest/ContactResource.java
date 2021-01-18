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
import javax.ws.rs.DELETE;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
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
    public String addContact(String jsonString) throws API_Exception {
        String thisuser = securityContext.getUserPrincipal().getName();
        String name;
        String email;
        String company;
        String jobtitle;
        String phone;
        try {
            JsonObject json = JsonParser.parseString(jsonString).getAsJsonObject();
            name = json.get("name").getAsString();
            email = json.get("email").getAsString();
            company = json.get("company").getAsString();
            jobtitle = json.get("jobtitle").getAsString();
            phone = json.get("phone").getAsString();

            ContactDTO data = new ContactDTO(name, email, company, jobtitle, phone);

            ContactDTO result = contact_facade.addContact(data, thisuser);

            return GSON.toJson(result);

        } catch (Exception e) {
            throw new API_Exception("Malformed JSON Suplied", 400, e);
        }

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("user")
    @Path("userscontacts")
    public String getContactsOfUSer() throws AuthenticationException {
        return GSON.toJson(contact_facade.getContactsByUser(securityContext.getUserPrincipal().getName()));

    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("user")
    @Path("editcontact")
    public String editContact(String jsonString) throws AuthenticationException, API_Exception {
        String name;
        String email;
        String company;
        String jobtitle;
        String phone;
        int id;
        System.out.println(jsonString);
        try {
            JsonObject json = JsonParser.parseString(jsonString).getAsJsonObject();
            name = json.get("name").getAsString();
            email = json.get("email").getAsString();
            company = json.get("company").getAsString();
            jobtitle = json.get("jobtitle").getAsString();
            phone = json.get("phone").getAsString();
            id = json.get("id").getAsInt();

            ContactDTO data = new ContactDTO(name, email, company, jobtitle, phone);
            data.setId(id);
            return GSON.toJson(contact_facade.editContact(data));
        } catch (Exception e) {
            throw new API_Exception("Malformed JSON Suplied", 400, e);
        }

    }

    @DELETE
    @Path("delete")
    @RolesAllowed("user")
    @Produces(MediaType.APPLICATION_JSON)
    public String  deleteContact(int id) throws API_Exception{
        System.out.println("DELET with id: "+id);
       String msg=contact_facade.deleteContact(id);
        return "{\"msg\": \""+msg+"\"}";
    }


}
