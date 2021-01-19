/*
 * Programing exam
 */
package entities;

import dto.ContactDTO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
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

    @OneToMany( mappedBy="contact",cascade=CascadeType.ALL)
    private List<Opportunity> opportunities = new ArrayList();
    
    
    
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

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setJobtitle(String jobtitle) {
        this.jobtitle = jobtitle;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<Opportunity> getOpportunities() {
        return opportunities;
    }
   
    public void addOpportunity(Opportunity opo){
        this.opportunities.add(opo);
        if(opo.getContact()==null){
            opo.setContact(this);
        }
    }

}
