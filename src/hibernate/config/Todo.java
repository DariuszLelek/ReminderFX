package hibernate.config;
// Generated Apr 18, 2017 10:13:09 PM by Hibernate Tools 4.3.1

import java.util.Date;
import java.util.Objects;

/**
 * Todo generated by hbm2java
 */
public class Todo implements java.io.Serializable {
    private Integer id;
    private String name;
    private Integer priority;
    private Date enterDate;

    // <editor-fold defaultstate="collapsed" desc=" Constructors ">
    public Todo() {
    }
    
    public Todo(String name, Integer priority, Date enterDate) {
        this.name = name;
        this.priority = priority;
        this.enterDate = enterDate;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc=" Getters Setters ">
    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Date getEnterDate() {
        return enterDate;
    }

    public void setEnterDate(Date enterDate) {
        this.enterDate = enterDate;
    }

    public Integer getPriority() {
        return priority;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
    // </editor-fold>

    @Override
    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof Todo) {
            result = ((Todo) obj).getName().equals(name);

        }
        return result;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + Objects.hashCode(this.name);
        return hash;
    }
}
