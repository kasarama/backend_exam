package facades;

import dto.demo.UserDTO;
import utils.EMF_Creator;
import entities.Role;
import entities.User;
import errorhandling.API_Exception;
import errorhandling.NotFoundException;
import java.util.ArrayList;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import security.errorhandling.AuthenticationException;

//Uncomment the line below, to temporarily disable this test
//@Disabled
public class FacadesTest {

    private static EntityManagerFactory emf;

    private static UserFacade user_facade;

    public FacadesTest() {
    }

    @BeforeAll
    public static void setUpClass() {

        emf = EMF_Creator.createEntityManagerFactoryForTest();
        user_facade = UserFacade.getUserFacade(emf);
    }

    @AfterAll
    public static void tearDownClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            em.createQuery("DELETE from User").executeUpdate();
            em.createQuery("DELETE from Role").executeUpdate();
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
            em.createQuery("delete from User").executeUpdate();
            em.createQuery("delete from Role").executeUpdate();

            Role userRole = new Role("user");
            Role adminRole = new Role("admin");
            User user = new User("userF", "test");
            user.addRole(userRole);
            User admin = new User("adminF", "test");
            admin.addRole(adminRole);
            User both = new User("user_adminF", "test");
            both.addRole(userRole);
            both.addRole(adminRole);
            em.persist(userRole);
            em.persist(adminRole);
            em.persist(user);
            em.persist(admin);
            em.persist(both);
            em.getTransaction().commit();
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
        assertTrue(user_facade.changePassword("userF", "test", "new test")!=null);
    }

   
    @Test
    public void deleteUserThatNotExistTest() {
        Assertions.assertThrows(API_Exception.class, () -> user_facade.deleteUser("Magdalena Wawrzak"));
    }
    
    @Test
    public void deleteUserTest() throws API_Exception{
        assertTrue(user_facade.deleteUser("userF").equals("User deleted"));
        assertEquals(2, user_facade.allUsers().size());
    }
}
