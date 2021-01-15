package facades;

import utils.EMF_Creator;
import entities.Role;
import entities.User;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

//Uncomment the line below, to temporarily disable this test
//@Disabled
public class FacadesTest {

    private static EntityManagerFactory emf = EMF_Creator.createEntityManagerFactoryForTest();
    ;
    private static UserFacade user_facade = UserFacade.getUserFacade(emf);

    private static User user = new User("user", "test");
    private static User admin = new User("admin", "test");
    private static User user_admin = new User("user_admin", "test");
    private static Role userRole = new Role("user");
    private static Role adminRole = new Role("admin");

    public FacadesTest() {
    }

    @Disabled
    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            em.persist(userRole);
            em.persist(adminRole);

            em.getTransaction().commit();

        } finally {
            em.close();
        }
    }

    @AfterAll
    public static void tearDownClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            em.createQuery("DELETE from User").executeUpdate();
            em.createQuery("DELETE from User").executeUpdate();
            em.createQuery("DELETE from Role").executeUpdate();
            em.getTransaction().commit();

        } finally {
            em.close();
        }
    }

    // Setup the DataBase in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the code below to use YOUR OWN entity class
    @BeforeEach
    public void setUp() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        EntityManager em = emf.createEntityManager();
//        userRole=em.find(Role.class, "user");
//        adminRole=em.find(Role.class, "admin");
     //   em.persist(userRole);
       // em.persist(adminRole);
        user.addRole(userRole);
        admin.addRole(adminRole);
        user_admin.addRole(userRole);
        user_admin.addRole(adminRole);

        try {
            em.getTransaction().begin();
            em.persist(user);
            em.persist(admin);
            em.persist(user_admin);
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
            em.createQuery("DELETE from User").executeUpdate();
            em.getTransaction().commit();

        } finally {
            em.close();
        }
    }

    @Test
    public void allUsersTest() {
        System.out.println("Test UserFacade.allUsers");
        int size = user_facade.allUsers().size();
        assertEquals(size, 3);
    }
}
