package facades;

import dto.demo.UserDTO;
import entities.Role;
import entities.User;
import errorhandling.API_Exception;
import errorhandling.NotFoundException;
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
    
    public UserDTO addNewUser(String username, String password) throws NotFoundException, API_Exception {
        
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
    
    
    public static String changePassword(String username, String oldPass, String newPass) throws NotFoundException, AuthenticationException {
        //TODO Make it possibla to edit roles
        String status = "ERROR";
        
        try {
            User user = instance.getVeryfiedUser(username, oldPass);
            EntityManager em = emf.createEntityManager();
            if (newPass.length() < 4) {
                throw new NotFoundException("password to short");
            }
            try {
                em.getTransaction().begin();
                user.setUserPass(newPass);
                em.merge(user);
                em.getTransaction().commit();
                status="Password changed";
            } finally {
                em.close();
            }
            
        } catch (AuthenticationException ex) {
            throw new AuthenticationException(ex.getMessage());
        }
        
        return status;

    }
    
    public String deleteUser(String username) throws API_Exception {
        //TODO prevent from deleteing logged in user
        //TODO prevent from deleteing user with role admin
        EntityManager em = emf.createEntityManager();
        String status = "Error";

        try {
            em.getTransaction().begin();
            User user = em.find(User.class, username);
            if (user == null) {
                throw new API_Exception("Username not available", 409);
            }
            em.remove(user);
            em.getTransaction().commit();
            status = "User deleted";
        } finally {
            em.close();
        }
        return status;

    }

    


}
