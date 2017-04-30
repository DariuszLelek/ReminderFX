package hibernate.config;
// Generated Apr 18, 2017 10:13:09 PM by Hibernate Tools 4.3.1

import java.util.Date;
import java.util.Objects;
import reminder.components.ReminderComponentFXMLController;
import utilities.Strings;
import utilities.Utilities;

/**
 * Reminder generaimport utilities.Utilities; ted by hbm2java
 */
public class Reminder implements java.io.Serializable {

    private Integer id;
    private String name;
    private String weekDays;
    private String timer;
    private String active;
    private String repeatFrequency;
    private boolean toRemind;
    private Date lastTimeCompleted;
    private int timeInSeconds;
    private ReminderComponentFXMLController reminderComponent;
    private String counter;

    // <editor-fold defaultstate="collapsed" desc=" Constructors ">
    public Reminder() {
    }

    public Reminder(String name, String weekDays, String timer, String active, String repeatFrequency) {
        this.name = name;
        this.weekDays = weekDays;
        this.timer = timer;
        this.active = active;
        this.repeatFrequency = repeatFrequency;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc=" Public methods ">
    public void resetTimeInSeconds() {
        if (isDaily()) {
            timeInSeconds = Utilities.getInstance().getSeconds(timer);
        }
        updateTimeCounter();
    }

    public String getCounter() {
        return counter;
    }

    public void setCounter(String counter) {
        this.counter = counter;
        updateComponentCounter();
    }

    public boolean isDaily() {
        return weekDays.isEmpty();
    }

    public boolean isTodayCompleted() {
        boolean result = lastTimeCompleted != null;
        if (result) {
            result = lastTimeCompleted.compareTo(utilities.Utilities.getInstance().getCurrentDate()) == 0;
        }
        return result;
    }

    public void updateTimeCounter() {
        if (isDaily()) {
            setCounter(utilities.Utilities.getInstance().getFormattedTime(timeInSeconds));
        }
    }

    public void clearCounter() {
        setCounter(Strings.EMPTY_STRING_PLACE_HOLDER);
    }
    
    private void updateComponentCounter(){
        if(reminderComponent != null){
             reminderComponent.setCounter(counter);
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc=" Getters Setters ">
    public Date getLastTimeCompleted() {
        return lastTimeCompleted;
    }

    public void setLastTimeCompleted(Date lastTimeCompleted) {
        this.lastTimeCompleted = lastTimeCompleted;
    }

    public ReminderComponentFXMLController getReminderComponent() {
        return reminderComponent;
    }

    public void setReminderComponent(ReminderComponentFXMLController reminderComponent) {
        this.reminderComponent = reminderComponent;
    }

    public boolean getToRemind() {
        return toRemind;
    }

    public void setToRemind(boolean toRemind) {
        this.toRemind = toRemind;
    }

    public int getTimeInSeconds() {
        return timeInSeconds;
    }

    public void setTimeInSeconds(int timeInSeconds) {
        this.timeInSeconds = timeInSeconds;
    }

    public String getRepeatFrequency() {
        return repeatFrequency;
    }

    public void setRepeatFrequency(String repeatFrequency) {
        this.repeatFrequency = repeatFrequency;
    }

    public Integer getId() {
        return this.id;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
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

    public String getWeekDays() {
        return this.weekDays;
    }

    public void setWeekDays(String weekDays) {
        this.weekDays = weekDays;
    }

    public String getTimer() {
        return this.timer;
    }

    public void setTimer(String timer) {
        this.timer = timer;
    }
// </editor-fold>

    @Override
    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof Reminder) {
            result = ((Reminder) obj).getName().equals(name);
        }
        return result;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + Objects.hashCode(this.name);
        hash = 23 * hash + Objects.hashCode(this.weekDays);
        hash = 23 * hash + Objects.hashCode(this.timer);
        return hash;
    }
}
