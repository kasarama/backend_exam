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
import javax.validation.constraints.AssertTrue;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

//Uncomment the line below, to temporarily disable this test
//@Disabled
public class FacadesTest {

    private static EntityManagerFactory emf = EMF_Creator.createEntityManagerFactoryForTest();

    private static UserFacade user_facade;

    public FacadesTest() {
    }

    @BeforeAll
    public static void setUpClass() {

        emf = EMF_Creator.createEntityManagerFactoryForTest();
user_facade=UserFacade.getUserFacade(emf);
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
            //Delete existing users and roles to get a "fresh" database
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
            //System.out.println("Saved test data to database");
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

    //@Disabled
    @Test
    public void allUsersTest() {
        System.out.println("Test UserFacade.allUsers");
        ArrayList<UserDTO> users = user_facade.allUsers();
        int size = users.size();
        for (UserDTO user1 : users) {
            System.out.println(user1.toString());
        }
        assertEquals(3, size);
    }

    public static UserDTO addNewUser(String username, String password) throws NotFoundException, API_Exception {
        if (username.length() < 4) {
            throw new NotFoundException("username to short");
        }
        if (password.length() < 4) {
            throw new NotFoundException("password to short");
        }
        EntityManager em = emf.createEntityManager();

        User user = new User(username, password);
        try {
            if (em.find(User.class, username) != null) {
                throw new API_Exception("Username not available", 409);
            } else {
                em.getTransaction().begin();
                user.addRole(em.find(Role.class, "user"));
                em.persist(user);
                em.getTransaction().commit();
            }

        } finally {
            em.close();
        }

        return new UserDTO(user);

    }

    @Test
    public void addNewUser() throws NotFoundException, API_Exception {
        System.out.println("Test addNewUser()");
        Assertions.assertThrows(NotFoundException.class, () -> addNewUser("123", "1234"));
        Assertions.assertThrows(NotFoundException.class, () -> addNewUser("1234", "123"));
        UserDTO user = addNewUser("Magdalena", "Wawrzak");
        addNewUser("Magdalensa", "Wawrzak");
        addNewUser("Magdalenaa", "Wawrzak");
        System.out.println(user.toString());
        assertTrue("Magdalena".equals(user.getUsername()));
        assertTrue(user.isIsAdmin() == false);
        assertTrue(user.isIsUser() == true);
    }


}
