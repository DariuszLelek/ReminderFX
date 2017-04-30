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
package reminder.dataControllers;

import hibernate.config.Event;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import utilities.Strings;
import reminder.dataobjects.NameDatePair;
import reminder.dataobjects.NameDatePairComparator;

/**
 *
 * @author Dariusz
 */
public class EventDataController extends ComponentUtil implements ComponentsControll{

    // <editor-fold defaultstate="collapsed" desc=" Singleton ">
    private static EventDataController instance;

    public synchronized static EventDataController getInstance() {
        if (instance == null) {
            instance = new EventDataController();
        }
        return instance;
    }
    // </editor-fold>

    private Event pendingEvent;
    private VBox containerEvent;
    private Map<NameDatePair, AnchorPane> components;
    private final Map<String, Event> componentsData;
    private String selectedPaneName;
    private AnchorPane selectedPane;
    private NameDatePairComparator datePairComparator;

    public BooleanProperty EventSelected;

    public enum Upcoming {
        TODAY, WEEK, MONTH, ALL
    }

    private EventDataController(){
        components = new LinkedHashMap<>();
        componentsData = new TreeMap<>();
        EventSelected = new SimpleBooleanProperty();
        datePairComparator = new NameDatePairComparator();
    }

    // <editor-fold defaultstate="collapsed" desc=" Public methods ">
    public Event getPendingEvent() {
        return pendingEvent;
    }

    public String getSelectedPaneName() {
        return selectedPaneName;
    }

    public void setContainerEvent(VBox containerEvent) {
        this.containerEvent = containerEvent;
    }

    @Override
    public boolean componentExists(String name) {
        return componentsData.containsKey(name);
    }

    public void insertComponent(Event event) {
        if (event != null && !componentExists(event.getName())) {
            this.pendingEvent = event;
            try {
                URL resource = this.getClass().getResource("/reminder/components/EventComponentFXML.fxml");
                Object obj = FXMLLoader.load(resource);
                if (obj != null && obj instanceof AnchorPane) {
                    AnchorPane anchorPane = (AnchorPane) obj;
                    anchorPane.prefWidthProperty().bind(containerEvent.widthProperty());
                    containerEvent.getChildren().add(anchorPane);
                    components.put(new NameDatePair(pendingEvent.getName(), pendingEvent.getEventDate()), anchorPane);
                    componentsData.put(pendingEvent.getName(), event);
                }
            } catch (IOException ex) {
                Logger.getLogger(ToDoDataController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        this.pendingEvent = null;
    }

    @Override
    public void loadComponents() {
        clearCache();
        List<Event> events = DataProvider.getInstance().getAllEntities(Event.class);

        events.stream().filter((event) -> (validateEventDate(event))).forEachOrdered((event) -> {
            insertComponent(event);
        });
        
        sortComponents();
    }

    @Override
    public void saveComponents() {
        List<Event> oldEvents = DataProvider.getInstance().getAllEntities(Event.class);
        deleteOldEvents(oldEvents);

        componentsData.values().forEach((event) -> {
            hiberUtil.saveOrUpdateEntity(event);
        });
    }

    public void processComponentClick(AnchorPane rootPane, String name) {
        if (selectedPaneName == null || !selectedPaneName.equals(name)) {
            EventSelected.set(true);
            selectedPaneName = name;
            if (selectedPane != null) {
                selectedPane.setStyle(Strings.EMPTY_STRING);
            }
            rootPane.setStyle(Strings.COLOR_SELECTED_PANE);
            selectedPane = rootPane;
        }
    }

    public void clearSelectedComponent() {
        EventSelected.set(false);
        selectedPaneName = Strings.EMPTY_STRING;
        if (selectedPane != null) {
            selectedPane.setStyle(Strings.EMPTY_STRING);
        }
        selectedPane = null;
    }

    public void updateDisplayedEvents(Upcoming upcoming) {
        int dayCounter = 0;
        switch (upcoming) {
            case TODAY:
                break;
            case WEEK:
                dayCounter = utilities.Utilities.getInstance().getDaysToWeekEnd();
                break;
            case MONTH:
                dayCounter = utilities.Utilities.getInstance().getDaysToMonthEnd();
                break;
            default:
            case ALL:
                dayCounter = Integer.MAX_VALUE;
                break;
        }

        clearSelectedComponent();
        containerEvent.getChildren().clear();
        for (Map.Entry<NameDatePair, AnchorPane> pair : components.entrySet()) {
            if (utilities.Utilities.getInstance().dateInRange(pair.getKey().getDate(), dayCounter)) {
                containerEvent.getChildren().add(pair.getValue());
            }
        }
    }

    public void deleteSelected() {
        if (selectedPane != null) {
            containerEvent.getChildren().remove(selectedPane);
            components.remove(new NameDatePair(selectedPaneName, null));
            componentsData.remove(selectedPaneName);
            selectedPane = null;
            selectedPaneName = Strings.EMPTY_STRING;
            EventSelected.set(false);

            StatusController.getInstance().setStatus(StatusController.Status.CHANGES_NOT_SAVED);
        }
    }

    @Override
    public void removeComponent(String name) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void sortComponents() {
        List<NameDatePair> elements = new ArrayList<>();
        elements.addAll(components.keySet());
        Collections.sort(elements, datePairComparator);
        Map<NameDatePair, AnchorPane> sortedMap = new LinkedHashMap<>();
        for (NameDatePair element : elements) {
            sortedMap.put(element, components.get(element));
        }
        components.clear();
        components = sortedMap;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc=" Private methods ">
    @Override
    protected void clearCache() {
        clearSelectedComponent();
        components.clear();
        componentsData.clear();
        containerEvent.getChildren().clear();
    }

    private boolean validateEventDate(Event event) {
        boolean result = true;
        Date eventDate = event.getEventDate();
        Date currentDate = utils.getCurrentDate();
        if (eventDate.compareTo(utils.getDateOnly(currentDate)) < 0) {
            if (Strings.ONCE.equals(event.getRepeatRatio())) {
                deleteEvent(event);
                result = false;
            } else {
                // try update event date
                Calendar cal = Calendar.getInstance();
                switch (event.getRepeatRatio()) {
                    case Strings.WEEKLY:
                        currentDate = utils.getCurrentDate();
                        int eventDayOfWeek = utils.getDayOfWeek(eventDate);
                        int currentDayOfWeek = utils.getDayOfWeek(currentDate);
                        int diff = eventDayOfWeek - currentDayOfWeek;
                        cal.set(utils.getYear(currentDate), utils.getMonth(currentDate), utils.getDayOfMonth(currentDate) + diff);
                        setNewEventDate(cal.getTime(), event);
                        result = true;
                        break;
                    case Strings.MONTHLY:
                        currentDate = utils.getCurrentDate();
                        int newMonth = utils.getMonth(currentDate);
                        int eventDay = utils.getDayOfMonth(eventDate);
                        int currentDay = utils.getDayOfMonth(currentDate);
                        newMonth = eventDay < currentDay ? newMonth + 1 : newMonth;
                        cal.set(utils.getYear(currentDate), newMonth, eventDay);
                        setNewEventDate(cal.getTime(), event);
                        result = true;
                        break;
                    case Strings.YEARLY:
                        // may cause problems if set to end of month and new year has less days / will switch to beginning of new month in that case
                        cal.set(utils.getYear(utils.getCurrentDate()) + 1, utils.getMonth(eventDate), utils.getDayOfMonth(eventDate));
                        setNewEventDate(cal.getTime(), event);
                        result = true;
                        break;
                    default:
                        // add logger
                        result = false;
                        throw new UnsupportedOperationException();
                }
            }
        }
        return result;
    }
    
    @Override
    protected void addComponentToDisplay(Object component, AnchorPane pane) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void setNewEventDate(Date newDate, Event event) {
        event.setEventDate(utils.getDateOnly(newDate));
        updateEvent(event);
    }

    private void deleteOldEvents(List<Event> oldEvenst) {
        oldEvenst.stream().filter((event) -> (!componentsData.keySet().contains(event.getName()))).forEachOrdered((event) -> {
            deleteEvent(event);
        });
    }

    private void updateEvent(Event event) {
        hiberUtil.updateEntity(event);
    }

    private void deleteEvent(Event event) {
        hiberUtil.deleteEntity(event);
    }
    // </editor-fold>
}
