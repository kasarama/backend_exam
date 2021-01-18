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
public class TaskStatus implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String name;

    public TaskStatus() {
    }

    public TaskStatus(String name) {
        this.name = name;
    }
    

}