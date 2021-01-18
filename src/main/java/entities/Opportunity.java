/*
 * Programing exam
 */
package entities;

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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author magda
 */
@Entity
public class Opportunity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private int amount;
    @Temporal(TemporalType.DATE)
    private Date closeDate;
    @ManyToOne
    OpStatus status;
    @JoinColumn(name = "contact_opportunity")
    Contact contact;
    @OneToMany(mappedBy = "opportunity", cascade = CascadeType.ALL)
    List tasks = new ArrayList();

    public Opportunity() {
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }

    public Date getCloseDate() {
        return closeDate;
    }

    public OpStatus getStatus() {
        return status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setCloseDate(Date closeDate) {
        this.closeDate = closeDate;
    }

    public void setStatus(OpStatus status) {
        this.status = status;
    }
    
    public Contact getContact() {
        return contact;
    }
    
    public void setContact(Contact contact) {
        this.contact = contact;
        if (!contact.getOpportunities().contains(this)) {
            contact.addOpportunity(this);
        }
    }
    
    public void addTask(Task task){
        this.tasks.add(task);
        if (task.getOpportunity()==null){
            task.setOpportunity(this);
        }
    }

    public List getTasks() {
        return tasks;
    }
    

   

}
