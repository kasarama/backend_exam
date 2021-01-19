/*
 * Programing exam
 */
package entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author magda
 */
@Entity
public class Task implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    String title;
    String comment;
    @Temporal(TemporalType.DATE)
    Date dueDate;
    @ManyToOne
    Opportunity opportunity;
    @ManyToOne
    TaskStatus status;
    @ManyToOne
    TaskType type;

    public Task() {
    }

    public void setOpportunity(Opportunity opportunity) {
        this.opportunity = opportunity;
        if (!opportunity.getTasks().contains(this)) {
            opportunity.addTask(this);
        }
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getComment() {
        return comment;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public Opportunity getOpportunity() {
        return opportunity;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public TaskType getType() {
        return type;
    }

    public void setType(TaskType type) {
        this.type = type;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }
    

}
