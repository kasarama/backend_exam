/*
 * Programing exam
 */
package dto;

import entities.Task;
import java.util.Date;

/**
 *
 * @author magda
 */
public class TaskDTO {
    private int id;
    private String title;
    private String comment;
    private Date dueDate;
    private int opportunityID;
    private String status;
    private String type;

    public TaskDTO(Task ta){
        this.id = ta.getId();
        this.title = ta.getTitle();
        this.comment = ta.getComment();
        this.dueDate = ta.getDueDate();
        this.opportunityID = ta.getOpportunity().getId();
        this.status = ta.getStatus().getName();
        this.type = ta.getType().getName();
    }

    public TaskDTO(String title, String comment, Date dueDate, int opportunityID, String status, String type) {
        this.title = title;
        this.comment = comment;
        this.dueDate = dueDate;
        this.opportunityID = opportunityID;
        this.status = status;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public int getOpportunityID() {
        return opportunityID;
    }

    public void setOpportunityID(int opportunityID) {
        this.opportunityID = opportunityID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    
    
    
    
}
