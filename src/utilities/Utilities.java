package utilities;


import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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

/**
 *
 * @author Dariusz
 */
public class Utilities {
    private static Utilities instance;
    private final Calendar cal;
    private final DateFormat formatter;
    private final String[] dayLetters;

    private Utilities(){
        dayLetters = Strings.DAY_LETTERS;
        cal = Calendar.getInstance();
        formatter = new SimpleDateFormat("dd/MM/yyyy");
    }
    
    public Date getCurrentDateTime(){
        return new Timestamp(Calendar.getInstance().getTimeInMillis());
    }
    
    public Date getCurrentDate(){
        Date date = getCurrentDateTime();
        return getDateOnly(date);
    }
    
    public Date getDateOnly(Date date){
        try {
            date = formatter.parse(formatter.format(date));
        } catch (ParseException ex) {
            Logger.getLogger(Utilities.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            //logger
        }
        return date;
    }
    
    public long getHoursPasssed(Date startPeriod){
        long diff = getCurrentDateTime().getTime() - startPeriod.getTime();
        return diff / (60 * 60 * 1000);
    }
    
    public String getFormattedDate(Date date){
        StringBuilder sb = new StringBuilder();
        int monthDay = getMonth(date) + 1; // 0 based
        String month = monthDay < 10 ? Strings.DB_ZERO + monthDay : monthDay + Strings.EMPTY_STRING; 
        return sb.append(getDayOfMonth(date)).append(Strings.DATE_SEPARATOR).append(month).append(Strings.DATE_SEPARATOR).append(getYear(date)).toString();
    }
    
    public int getYear(Date date){
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }
    
    public int getMonth(Date date){
        cal.setTime(date);
        return cal.get(Calendar.MONTH);
    }
    
    public int getDayOfMonth(Date date){
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_MONTH);
    }
     
    public int getDayOfWeek(Date date){
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_WEEK) - 1 == 0 ? 7 : cal.get(Calendar.DAY_OF_WEEK);
    }
    
    public int getDaysToMonthEnd(){
        Date today = getCurrentDate(); 
        cal.setTime(today);
        
        cal.add(Calendar.MONTH, 1);  
        cal.set(Calendar.DAY_OF_MONTH, 1);  
        cal.add(Calendar.DATE, -1);
        
        Date lastDayOfMonth = cal.getTime();

        DateFormat sdf = new SimpleDateFormat("dd");
        
        return Integer.parseInt(sdf.format(lastDayOfMonth)) - Integer.parseInt(sdf.format(today));
    }
    
    public int getDaysToWeekEnd(){
        return 7 - getDayOfWeek(getCurrentDate());
    }
    
    public String getFormattedTime(int hours, int minutes){
        String result = Strings.EMPTY_STRING;
        if(hours > 0 || minutes > 0){
            result += hours + Strings.TIME_SEPARATOR + (minutes < 10 ? "0" + minutes :  minutes);
        }
        return result;
    }
    
    public String getFormattedTime(int seconds){
        String result = Strings.ZERO_TIME;
        if(seconds > 0){
            int hours = seconds / 3600;
            int minutes = (seconds % 3600) / 60;
            result = hours + Strings.TIME_SEPARATOR + (minutes < 10 ? "0" + minutes :  minutes);
        }
        return result;
    }
    
    public boolean dateInRange(Date date, int days){
        boolean result = false;
        
        date = getDateOnly(date);
        Date todayDate = getCurrentDate();
        
        cal.setTime(date);
        Calendar today = Calendar.getInstance();  
        today.setTime(todayDate);
        
        Calendar last = (Calendar) today.clone();
        last.add(Calendar.DAY_OF_YEAR, days);

        result = cal.compareTo(last) <= 0 && cal.compareTo(today) >= 0;
        return result;
    }
    
    public String getEventString(Object o){
        String result = Strings.EMPTY_STRING_PLACE_HOLDER;
        if(o instanceof String){
            String s = (String) o;
            if(!s.isEmpty()){
                result = s;
            }
        }
        return result;
    }
    
    public String getDayLetters(String dayNumbers){
        String result = "";
        StringBuilder days = new StringBuilder();
        List<Integer> daysList = new ArrayList<>();
        for(char c : dayNumbers.toCharArray()){
            daysList.add(Character.getNumericValue(c));
        }
        Collections.sort(daysList);
        
        daysList.forEach((day) -> {days.append(dayLetters[day - 1]).append(",");});
        
        result = days.toString();
        result = result.substring(0, result.length() - 1);
        result = daysList.size()== 7 ? Strings.EVERYDAY : result;
        return result;
    }
    
    public int getSeconds(String formattedTime){
        int result = 0;
        if(!formattedTime.isEmpty()){
            String[] split = formattedTime.split(Strings.TIME_SEPARATOR);
            result = (Integer.parseInt(split[0]) * 3600) + Integer.parseInt(split[1]) * 60;
        }
        return result;
    }
    
    public synchronized static Utilities getInstance() {
        if (instance == null) {
            instance = new Utilities();
        }
        return instance;
    }
}
