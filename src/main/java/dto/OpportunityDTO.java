/*
 * Programing exam
 */
package dto;

import entities.Opportunity;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author magda
 */
public class OpportunityDTO {
    private int id;

    private String name;
    private int amount;
    private int contatcID;
    private String status;
    private List<TaskDTO> tasks=new ArrayList();
    private Date closeDate;

    public OpportunityDTO(Opportunity op) {
        this.id = op.getId();
        this.name = op.getName();
        this.amount = op.getAmount();
        this.contatcID = op.getContact().getId();
        this.status = op.getStatus().getName();
        this.closeDate=op.getCloseDate();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getContatcID() {
        return contatcID;
    }

    public void setContatcID(int contatcID) {
        this.contatcID = contatcID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<TaskDTO> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskDTO> tasks) {
        this.tasks = tasks;
    }

    public Date getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(Date closeDate) {
        this.closeDate = closeDate;
    }

    public OpportunityDTO(String name, int amount, int contatcID, String status, Date closeDate) {
        this.name = name;
        this.amount = amount;
        this.contatcID = contatcID;
        this.status = status;
        this.closeDate = closeDate;
    }
    
    
    
    
    
            
            
            
    
}
