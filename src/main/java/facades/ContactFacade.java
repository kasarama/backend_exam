/*
 * Programing exam
 */
package facades;

import dto.ContactDTO;
import entities.Contact;
import entities.User;
import errorhandling.API_Exception;
import java.util.ArrayList;
import java.util.HashSet;
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

    public ArrayList<ContactDTO> getContactsByUser(String username) throws AuthenticationException {
        EntityManager em = emf.createEntityManager();
        ArrayList<ContactDTO> allContacts = new ArrayList();
        try {
            em.getTransaction().begin();
            User user = em.find(User.class, username);
            if (user == null) {
                throw new AuthenticationException("Invalid user name");
            }

            for (Contact contact : user.getContacts()) {
                allContacts.add(new ContactDTO(contact));

            }

            em.getTransaction().commit();
            return allContacts;
        } finally {
            em.close();

        }

    }

    public ContactDTO editContact(ContactDTO contact) throws API_Exception {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Contact edited = em.find(Contact.class, contact.getId());
            if (edited == null) {
                throw new API_Exception("Contact does not exist", 404);
            }
            edited.setCompany(contact.getCompany());
            edited.setEmail(contact.getEmail());
            edited.setJobtitle(contact.getJobtitle());
            edited.setPhone(contact.getPhone());
            edited.setName(contact.getName());
            em.merge(edited);
            
            em.getTransaction().commit();
            return new ContactDTO(edited);
        } finally {
            em.close();

        }
    }

    public String deleteContact(int id) throws API_Exception {
        EntityManager em = emf.createEntityManager();
        try {
            Contact edited = em.find(Contact.class, id);
            if (edited == null) {
                throw new API_Exception("Contact does not exist", 404);
            }
            em.getTransaction().begin();
            User user = edited.getUser();
            user.deleteContact(edited);
            em.remove(edited);
            em.merge(user);
            em.getTransaction().commit();

            return "DELETED";
        } finally {
            em.close();

        }
    }

    public static void main(String[] args) {

    }
}
