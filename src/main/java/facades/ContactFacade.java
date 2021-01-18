/*
 * Programing exam
 */
package facades;

import dto.ContactDTO;
import entities.Contact;
import entities.User;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import security.errorhandling.AuthenticationException;

/**
 *
 * @author magda
 */
public class ContactFacade {

    private static EntityManagerFactory emf;
    private static ContactFacade instance;

    private ContactFacade() {
    }

    public static ContactFacade getContactFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new ContactFacade();
        }
        return instance;
    }

    public ContactDTO addContact(ContactDTO data, String username) throws AuthenticationException {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            User user = em.find(User.class, username); 
             if (user == null) {
                throw new AuthenticationException("Invalid user name");
            }
            Contact contact = new Contact(data);

            contact.setUser(user);
            em.persist(contact);
            
            em.getTransaction().commit();
            return new ContactDTO(contact);
        } finally {
            em.close();

        }

       
    }

}
