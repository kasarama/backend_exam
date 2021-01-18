/*
 * Programing exam
 */
package dto;

import entities.Contact;
import java.util.Date;

/**
 *
 * @author magda
 */
public class ContactDTO {

    private String name;
    private String email;
    private String company;
    private String jobtitle;
    private String phone;
    private Date created;

    public ContactDTO(Contact contact) {
        this.name = contact.getName();
        this.email = contact.getEmail();
        this.company = contact.getCompany();
        this.jobtitle = contact.getJobtitle();
        this.phone = contact.getPhone();
        this.created = contact.getCreated();
    }

    public ContactDTO(String name, String email, String company, String jobtitle, String phone) {
        this.name = name;
        this.email = email;
        this.company = company;
        this.jobtitle = jobtitle;
        this.phone = phone;
       
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

    @Override
    public String toString() {
        return "ContactDTO{" + "name=" + name + ", email=" + email + ", company=" + company + ", jobtitle=" + jobtitle + ", phone=" + phone + ", created=" + created + '}';
    }
    
    

    
}
