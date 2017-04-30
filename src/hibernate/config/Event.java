/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hibernate.config;

import java.util.Date;

/**
 *
 * @author Dariusz
 */
public class Event implements Comparable<Event> {

    private Integer id;
    private String name;
    private String location;
    private Date eventDate;
    private Date enterDate;
    private String eventTime;
    private String comments;
    private boolean onlyOnce;
    private String repeatRatio;

    // <editor-fold defaultstate="collapsed" desc=" Constructors ">
    public Event() {
    }

    public Event(String name, Date eventDate, String repeatRatio) {
        this.name = name;
        this.eventDate = eventDate;
        this.repeatRatio = repeatRatio;
    }

    public Event(String name, String location, Date eventDate, Date enterDate, String eventTime, String comments, String repeatRatio) {
        this.name = name;
        this.location = location;
        this.eventDate = eventDate;
        this.enterDate = enterDate;
        this.eventTime = eventTime;
        this.comments = comments;
        this.repeatRatio = repeatRatio;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc=" Getters Setters ">
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public Date getEnterDate() {
        return enterDate;
    }

    public void setEnterDate(Date enterDate) {
        this.enterDate = enterDate;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public boolean isOnlyOnce() {
        return onlyOnce;
    }

    public void setOnlyOnce(boolean onlyOnce) {
        this.onlyOnce = onlyOnce;
    }

    public String getRepeatRatio() {
        return repeatRatio;
    }

    public void setRepeatRatio(String repeatRatio) {
        this.repeatRatio = repeatRatio;
    }
    // </editor-fold>

    @Override
    public int compareTo(Event e) {
        return this.eventDate.compareTo(e.eventDate);
    }
}
