/*
 * Programing exam
 */
package entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author magda
 */
@Entity
public class TaskType implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String name;

    public TaskType() {
    }

    public TaskType(String name) {
        this.name = name;
    }

    
    
}