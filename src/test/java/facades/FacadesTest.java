package facades;

import dto.ContactDTO;
import dto.OpportunityDTO;
import dto.UserDTO;
import entities.Contact;
import entities.OpStatus;
import entities.Opportunity;
import utils.EMF_Creator;
import entities.Role;
import entities.TaskStatus;
import entities.TaskType;
import entities.User;
import errorhandling.API_Exception;
import errorhandling.NotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import security.errorhandling.AuthenticationException;

//Uncomment the line below, to temporarily disable this test
//@Disabled
public class FacadesTest {

    private static EntityManagerFactory emf;

    private static UserFacade user_facade;
    private static ContactFacade contact_facade;
    private static int contact_ID;
    private static int oportunity_ID;

    public FacadesTest() {
    }

    @BeforeAll
    public static void setUpClass() {

        emf = EMF_Creator.createEntityManagerFactoryForTest();
        user_facade = UserFacade.getUserFacade(emf);
        contact_facade = ContactFacade.getContactFacade(emf);
    }

    @AfterAll
    public static void tearDownClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            em.createQuery("DELETE from Contact").executeUpdate();
            em.createQuery("DELETE from OpStatus").executeUpdate();
            em.createQuery("DELETE from User").executeUpdate();
            em.createQuery("DELETE from Role").executeUpdate();
            em.createQuery("DELETE from Task").executeUpdate();
            em.createQuery("DELETE from Opportunity").executeUpdate();
            em.createQuery("DELETE from TaskStatus").executeUpdate();
            em.createQuery("DELETE from TaskType").executeUpdate();

            em.getTransaction().commit();

        } finally {
            em.close();
        }
    }

    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createQuery("DELETE from Contact").executeUpdate();
            em.createQuery("DELETE from OpStatus").executeUpdate();
            em.createQuery("DELETE from User").executeUpdate();
            em.createQuery("DELETE from Role").executeUpdate();
            em.createQuery("DELETE from Task").executeUpdate();
            em.createQuery("DELETE from Opportunity").executeUpdate();
            em.createQuery("DELETE from TaskStatus").executeUpdate();
            em.createQuery("DELETE from TaskType").executeUpdate();
            //OpStatus ops1 = new OpStatus("Non Started");
            // OpStatus ops2 = new OpStatus("In Progress");
            Role userRole = new Role("user");
            Role adminRole = new Role("admin");
            User user = new User("userF", "test");
            user.addRole(userRole);
            User admin = new User("adminF", "test");
            admin.addRole(adminRole);
            User both = new User("user_adminF", "test");
            both.addRole(userRole);
            both.addRole(adminRole);

            Contact con1 = new Contact("contact01", user);
            Contact con2 = new Contact("contact02", user);
            Contact con3 = new Contact("contact03", user);
            em.persist(new OpStatus("OP_status"));
            em.persist(new TaskStatus("T_status"));
            em.persist(new TaskType("T_type"));
            em.persist(userRole);
            em.persist(adminRole);
            em.persist(user);
            em.persist(admin);
            em.persist(both);
            em.getTransaction().commit();
            contact_ID = con1.getId();

        } finally {
            em.close();
        }
    }

    @AfterEach
    public void tearDown() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            em.createQuery("DELETE from Contact").executeUpdate();
            //  em.createQuery("DELETE from OpStatus").executeUpdate();
            em.createQuery("DELETE from Role").executeUpdate();
            em.createQuery("DELETE from User").executeUpdate();
            em.getTransaction().commit();

        } finally {
            em.close();
        }
    }

    @Test
    public void allUsersTest() {
        ArrayList<UserDTO> users = user_facade.allUsers();
        int size = users.size();
        for (UserDTO user1 : users) {
        }
        assertEquals(3, size);
    }

    @Test
    public void addNewUserTest() throws NotFoundException, API_Exception {
        System.out.println("Test addNewUser()");
        Assertions.assertThrows(NotFoundException.class, () -> user_facade.addNewUser("123", "1234"));
        Assertions.assertThrows(NotFoundException.class, () -> user_facade.addNewUser("1234", "123"));
        UserDTO user = user_facade.addNewUser("Magdalena", "Wawrzak");
        System.out.println(user.toString());
        assertTrue("Magdalena".equals(user.getUsername()));
        assertTrue(user.isIsAdmin() == false);
        assertTrue(user.isIsUser() == true);
    }

    @Test
    public void changePasswordWrongPassword() {
        Assertions.assertThrows(AuthenticationException.class, () -> user_facade.changePassword("userF", "tes", "teees"));

    }

    @Test
    public void changePasswordShortPassword() {
        Assertions.assertThrows(NotFoundException.class, () -> user_facade.changePassword("userF", "test", "tee"));
    }

    @Test
    public void changePassword() throws NotFoundException, AuthenticationException {
        assertTrue(user_facade.changePassword("userF", "test", "new test") != null);
    }

    @Test
    public void deleteUserThatNotExistTest() {
        Assertions.assertThrows(API_Exception.class, () -> user_facade.deleteUser("Magdalena Wawrzak"));
    }

    @Test
    public void deleteUserTest() throws API_Exception {
        assertTrue(user_facade.deleteUser("userF").equals("User deleted"));
        assertEquals(2, user_facade.allUsers().size());
    }

    @Test
    public void addContact() throws AuthenticationException {
        ContactDTO data = new ContactDTO("Contact", "", "", "", "");
        contact_facade.addContact(data, "userF");
        EntityManager em = emf.createEntityManager();
        int size = 0;

        try {
            // Query query = em.createQuery("SELECT u from users u". User.class);
            User user = em.find(User.class, "userF");
            size = user.getContacts().size();
        } finally {
            em.close();
        }
        assertEquals(4, size);

    }

    @Test
    public void addContactWrongUser() {
        ContactDTO data = new ContactDTO("Contact", "", "", "", "");
        Assertions.assertThrows(AuthenticationException.class, () -> contact_facade.addContact(data, "MAgda"));
    }

    @Test
    public void getContactsByUser() throws AuthenticationException {
        ArrayList con = contact_facade.getContactsByUser("userF");
        assertEquals(3, con.size());
    }

    @Test
    public void testEditContact() throws API_Exception {
        ContactDTO newValues = new ContactDTO("aa", "aa", "aa", "aa", "aa");
        newValues.setId(contact_ID);
        ContactDTO edited = contact_facade.editContact(newValues);
        assertTrue(edited.getCompany().equals("aa"));
    }

    @Test
    public void testDeleteContact () throws API_Exception, AuthenticationException{
        System.out.println("before: "+contact_facade.getContactsByUser("userF").size() );
        String delete=contact_facade.deleteContact(contact_ID);
        assertTrue(delete.equals("DELETED"));
                System.out.println("after: "+contact_facade.getContactsByUser("userF").size() );

        assertEquals(2,contact_facade.getContactsByUser("userF").size());
    }
    
    @Test
    public void testAddOpportunity() throws API_Exception{
        OpportunityDTO data = new OpportunityDTO("ab",3,contact_ID,"OP_status", new Date());
     
        OpportunityDTO result=contact_facade.addOpportunity(data);
        assertEquals(contact_ID, result.getContatcID());
        
    }
}
