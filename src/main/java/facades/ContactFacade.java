/*
 * Programing exam
 */
package facades;

import dto.ContactDTO;
import dto.OpportunityDTO;
import dto.TaskDTO;
import entities.Contact;
import entities.OpStatus;
import entities.Opportunity;
import entities.Task;
import entities.TaskStatus;
import entities.TaskType;
import entities.User;
import errorhandling.API_Exception;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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

    public OpportunityDTO addOpportunity(OpportunityDTO data) throws API_Exception {
        EntityManager em = emf.createEntityManager();
        try {
            Contact contact = em.find(Contact.class, data.getContatcID());
            if (contact == null) {
                throw new API_Exception("Contact does not exist", 404);
            }
            Opportunity opp = new Opportunity(data);
            opp.setStatus(em.find(OpStatus.class, data.getStatus()));
            opp.setContact(contact);
            em.persist(opp);
            em.getTransaction().begin();
            return new OpportunityDTO(opp);
        } finally {
            em.close();
        }

    }

    public List<OpportunityDTO> contactsOpportunities(int contactID) throws API_Exception {
        EntityManager em = emf.createEntityManager();
        try {
            Contact contact = em.find(Contact.class, contactID);
            if (contact == null) {
                throw new API_Exception("Contact does not exist", 404);
            }
            List<OpportunityDTO> list = new ArrayList();
            for (Opportunity opp : contact.getOpportunities()) {
                list.add(new OpportunityDTO(opp));

            }
            return list;
        } finally {
            em.close();
        }

    }

    public HashMap<OpportunityDTO, ContactDTO> opportunietiesByStatus(String status) {
        HashMap<OpportunityDTO, ContactDTO> map = new HashMap();
        EntityManager em = emf.createEntityManager();
        try {
            List<Opportunity> list = em.createQuery("SELECT o from Opportunity o where o.status=:status")
                    .setParameter("status", status)
                    .getResultList();

            for (Opportunity opportunity : list) {
                map.put(new OpportunityDTO(opportunity), new ContactDTO(opportunity.getContact()));

            }
            return map;
        } finally {
            em.close();
        }
    }

    public OpportunityDTO addTask(TaskDTO data) throws API_Exception {
        EntityManager em = emf.createEntityManager();
        Opportunity opp = em.find(Opportunity.class, data.getOpportunityID());
        if (opp == null) {
            throw new API_Exception("Contact does not exist", 404);
        }
        TaskType type = em.find(TaskType.class, data.getType());
        if (type == null) {
            throw new API_Exception("Task Type does not exist", 404);
        }
        TaskStatus status = em.find(TaskStatus.class, data.getStatus());
        if (status == null) {
            throw new API_Exception("Task Status does not exist", 404);

        }

        try {
            Task task = new Task(data);
            task.setOpportunity(opp);
            task.setType(type);
            task.setStatus(status);
            em.getTransaction().begin();
            em.persist(task);

            em.getTransaction().commit();
            return new OpportunityDTO(opp);
        } finally {
            em.close();
        }

    }

    public List<TaskDTO> TasksOfOpp(int oppId) throws API_Exception {
        List<TaskDTO> list = new ArrayList();
        EntityManager em = emf.createEntityManager();
        try {
            Opportunity opp = em.find(Opportunity.class, oppId);
            if (opp == null) {
                throw new API_Exception("Contact does not exist", 404);
            }
            for (Task task : opp.getTasks()) {
                list.add(new TaskDTO(task));
            }

            return list;
        } finally {
            em.close();
        }

    }

}
