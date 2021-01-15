package facades;

import dto.demo.UserDTO;
import entities.User;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import security.errorhandling.AuthenticationException;
import utils.EMF_Creator;

/**
 * @author lam@cphbusiness.dk
 */
public class UserFacade {

    private static EntityManagerFactory emf;
    private static UserFacade instance;

    private UserFacade() {
    }

    /**
     *
     * @param _emf
     * @return the instance of this facade.
     */
    public static UserFacade getUserFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new UserFacade();
        }
        return instance;
    }

    public User getVeryfiedUser(String username, String password) throws AuthenticationException {
        EntityManager em = emf.createEntityManager();
        User user;
        try {
            user = em.find(User.class, username);
            if (user == null || !user.verifyPassword(password)) {
                throw new AuthenticationException("Invalid user name or password");
            }
        } finally {
            em.close();
        }
        return user;
    }

    public ArrayList<UserDTO> allUsers() {
        EntityManager em = emf.createEntityManager();
        ArrayList<UserDTO> all = new ArrayList();
        try {
            // Query query = em.createQuery("SELECT u from users u". User.class);
            System.out.println("allUsers");
            List<User> allUsers = em.createQuery("SELECT u from User u", User.class)
                    .getResultList();
        
            for (User user : allUsers) {
                all.add(new UserDTO(user));
            }
            return all;
        } finally {
            em.close();
        }

    }

}
