/*
 * Programing exam
 */
package entities;

import dto.ContactDTO;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author magda
 */
@Entity
public class Contact implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String email;
    private String company;
    private String jobtitle;
    private String phone;
    @Temporal(TemporalType.DATE)
    private Date created;
    @JoinColumn(name = "user_name")
    private User user;

    public Contact() {
    }

    public Contact(ContactDTO dto) {

        this.name = dto.getName();
        this.email = dto.getEmail();
        this.company = dto.getCompany();
        this.jobtitle = dto.getJobtitle();
        this.phone = dto.getPhone();
        this.created = new Date();

    }

    public Contact(String name, User user) {

        this.name = name;
        this.email = "email";
        this.company = "company";
        this.jobtitle = "jobtitle";
        this.phone = "phone";
        setUser(user);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getCompany() {
        return company;
    }

    public String getJobtitle() {
        return jobtitle;
    }

    public String getPhone() {
        return phone;
    }

    public Date getCreated() {
        return created;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;

        if (!user.getContacts().contains(this)) {

            user.addContact(this);
        }
    }

}
