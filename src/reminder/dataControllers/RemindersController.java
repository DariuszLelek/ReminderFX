/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reminder.dataControllers;

import hibernate.config.Reminder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.concurrent.Task;
import utilities.Strings;
import utilities.Utilities;

/**
 *
 * @author Dariusz
 */
public final class RemindersController {

    // <editor-fold defaultstate="collapsed" desc=" Singleton ">
    private static RemindersController instance;

    public synchronized static RemindersController getInstance() {
        if (instance == null) {
            instance = new RemindersController();
        }
        return instance;
    }
    // </editor-fold>

    private final List<Reminder> activeDailyReminders;
    private final List<Reminder> activeWeeklyReminders;
    private final List<Reminder> dailyRemindersToRemove;
    private final Utilities utils;
    
    private Thread weeklyRemindersThread;
    private Thread dailyRemindersThread;
    
    private RemindersController(){
        activeDailyReminders = new ArrayList<>();
        activeWeeklyReminders = new ArrayList<>();
        dailyRemindersToRemove = new ArrayList<>();
        utils = Utilities.getInstance();

        resetThreads();
    }
    
    public void stopController(){
        stopUpdateThreads();
        activeDailyReminders.clear();
        activeWeeklyReminders.clear();
    }
    
    public void resetThreads(){
        stopUpdateThreads();
        activeDailyReminders.clear();
        activeWeeklyReminders.clear();
        startUpdateThreads();
    }

    public void updateWeeklyCounter(Reminder reminder) {
        updateWeeklyCounter(reminder, utils.getDayOfWeek(utils.getCurrentDate()));
    }
    
    public synchronized void updateActiveReminders(Reminder reminder) {
        boolean isActive = Strings.DB_YES_FLAG.equals(reminder.getActive());
        if (Strings.DAILY.equals(reminder.getRepeatFrequency())) {
            if (isActive) {
                addToActiveList(reminder, activeDailyReminders);
            } else {
                removeFromActiveList(reminder, activeDailyReminders);
            }
        } else {
            if (isActive) {
                addToActiveList(reminder, activeWeeklyReminders);
                updateWeeklyCounters();
            } else {
                removeFromActiveList(reminder, activeWeeklyReminders);
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc=" Private methods ">
    private synchronized void startUpdateThreads() {
        Task weeklyUpdate = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                while (true) {
                    Platform.runLater(() -> {
                        synchronized(activeWeeklyReminders){
                            updateWeeklyCounters();
                        }
                    });
                    Thread.sleep(Strings.WEEKLY_TIMERS_UPDATE_DELAY_SECONDS * 1000);
                }
            }
        };
        
        Task dailyUpdate = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                while (true) {
                    Platform.runLater(() -> {
                        synchronized(activeDailyReminders){
                            updateDailyCounters();
                        }
                    });
                    Thread.sleep(Strings.DAILY_TIMERS_UPDATE_DELAY_SECONDS * 1000);
                }
            }
        };
        
        dailyRemindersThread = new Thread(dailyUpdate);
        dailyRemindersThread.setDaemon(true);
        dailyRemindersThread.start();
        
        weeklyRemindersThread = new Thread(weeklyUpdate);
        weeklyRemindersThread.setDaemon(true);
        weeklyRemindersThread.start();
    }

    private synchronized void stopUpdateThreads() {
        if (dailyRemindersThread != null && weeklyRemindersThread != null) {
            weeklyRemindersThread.interrupt();
            dailyRemindersThread.interrupt();

            try {
                weeklyRemindersThread.join();
                dailyRemindersThread.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(RemindersController.class.getName()).log(Level.SEVERE, null, ex);
                ex.printStackTrace();
            }
        }
    }

    private synchronized void updateWeeklyCounters(){
        int currentWeekDay = utils.getDayOfWeek(utils.getCurrentDate());
        
        for(Reminder reminder : activeWeeklyReminders){
            updateWeeklyCounter(reminder, currentWeekDay);
        }
    }
    
    // run this update only from thread with fixed time delay!!
    // otherwise counter will update every time method is run
    // *refactor
    private synchronized void updateDailyCounters(){
        dailyRemindersToRemove.clear();
        for (Reminder reminder : activeDailyReminders) {
            if (!reminder.getToRemind()) {
                if (reminder.getTimeInSeconds() > 0) {
                    reminder.updateTimeCounter();
                    reminder.setTimeInSeconds(reminder.getTimeInSeconds() - Strings.DAILY_TIMERS_UPDATE_DELAY_SECONDS);
                } else {
                    processReminder(reminder);
                    dailyRemindersToRemove.add(reminder);
                }
            }
        }
        activeDailyReminders.removeAll(dailyRemindersToRemove);
    }
    
    private synchronized void updateWeeklyCounter(Reminder reminder, int currentWeekDay) {
        List<Integer> diffs = new ArrayList<>();
        String days;
        int diff = 0;
        days = reminder.getWeekDays();
        diffs.clear();
        for (char c : days.toCharArray()) {
            diff = Character.getNumericValue(c) - currentWeekDay;
            diffs.add(diff < 0 ? diff + 7 : diff);
        }
        Collections.sort(diffs);
        diff = diffs.get(0);
        String valueToSet;
        if (diff == 0) {
            if (reminder.isTodayCompleted()) {
                if (diffs.size() == 1) {
                    diff = 7;
                } else {
                    diff = diffs.get(1);
                }
                valueToSet = diff + Strings.SPACE_BAR + (diff > 1 ? Strings.DAYS : Strings.DAY);
            } else {
                valueToSet = Strings.TODAY;
                processReminder(reminder);
            }
        } else {
            valueToSet = diff + Strings.SPACE_BAR + (diff > 1 ? Strings.DAYS : Strings.DAY);
        }
        reminder.setCounter(valueToSet);
    }

    private void processReminder(Reminder reminder){
        reminder.getReminderComponent().remindNow();
    }   

    private synchronized void removeFromActiveList(Reminder reminder, List list){
        if(list.contains(reminder)){
            list.remove(reminder);
        }
    }
    
    private synchronized void addToActiveList(Reminder reminder, List list){
        if(!list.contains(reminder)){
            list.add(reminder); 
        }
    }
    // </editor-fold>
}
