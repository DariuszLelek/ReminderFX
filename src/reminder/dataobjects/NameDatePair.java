/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reminder.dataobjects;

import java.util.Date;
import java.util.Objects;

/**
 *
 * @author Dariusz
 */
public class NameDatePair{
    private final String name;
    private final Date date;

    public NameDatePair(String name, Date date) {
        this.name = name;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof NameDatePair && ((NameDatePair)obj).getName().equals(name);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.name);
        return hash;
    }
}
