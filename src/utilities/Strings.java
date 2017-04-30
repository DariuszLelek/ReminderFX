/*
 * Copyright 2017 Dariusz.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package utilities;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Dariusz
 */
public class Strings {
    private static Strings instance;
    private final List<Integer> hours;
    private final List<Integer> minutes;
    
    public final static String APP_NAME = "ReminderFX";
    public final static String APP_VERSION = "1.0";
    
    public final static String WEEKLY = "Weekly";
    public final static String MONTHLY = "Monthly";
    public final static String YEARLY = "Yearly";
    public final static String ONCE = "Once";
    public final static String DAILY = "Daily";
    public final static String EVERYDAY = "Everyday";
    public final static String TODAY = "Today";
    public final static String[] DAY_LETTERS = {"M", "Tu", "W", "Th", "F", "S", "Su"};
    
    public final static String EMPTY_STRING = "";
    public final static String EMPTY_STRING_PLACE_HOLDER = "-";
    public final static String DATE_SEPARATOR = "-";
    public final static String TIME_SEPARATOR = ":";
    
    public final static String DB_YES_FLAG = "Y";
    public final static String DB_NO_FLAG = "N";
    public final static String DB_ZERO = "0";
    
    public final static String ZERO_TIME = "0:00";
    public final static String DAYS = "days";
    public final static String DAY = "day";
    public final static String SPACE_BAR = " ";
 
    public final static int WEEKLY_TIMERS_UPDATE_DELAY_SECONDS = 3600;          // 1hr
    public final static int DAILY_TIMERS_UPDATE_DELAY_SECONDS = 60;             // 1min
    
    public final static String COLOR_SELECTED_PANE = "-fx-background-color: #89b6ff";
    public final static String COLOR_ACTIVE_REMINDER = "-fx-background-color:#def2e3";
    public final static String COLOR_REMINDER_ZERO_TIME = "-fx-background-color:#ffd147";
    public final static String COLOR_PRIORITY_TASK = "-fx-background-color:#ffafaf";
    
    public final static String ADD_NEW_ITEM = "Add new item.";
    
    private Strings(){
        hours = new ArrayList<>();
        minutes = new ArrayList<>();
        
        
        for(int i = 1; i<25; i++){
            hours.add(i);
        }
        
        for(int i = 0; i<61; i+=10){
            minutes.add(i);
        }
    }

    public List<Integer> getHours() {
        return hours;
    }

    public List<Integer> getMinutes() {
        return minutes;
    }
    

    public synchronized static Strings getInstance() {
        if (instance == null) {
            instance = new Strings();
        }
        return instance;
    }
}
