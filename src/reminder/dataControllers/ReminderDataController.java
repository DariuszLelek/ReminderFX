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

import hibernate.config.Reminder;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import utilities.Strings;

/**
 *
 * @author Dariusz
 */
public class ReminderDataController extends ComponentUtil implements ComponentsControll{

    // <editor-fold defaultstate="collapsed" desc=" Singleton ">
    private static ReminderDataController instance;

    public synchronized static ReminderDataController getInstance() {
        if (instance == null) {
            instance = new ReminderDataController();
        }
        return instance;
    }
    // </editor-fold>

    private Reminder pendingReminder;
    private VBox containerReminder;
    private final Map<String, AnchorPane> components;
    private final Map<String, Reminder> componentsData;
    private boolean displayActiveOnly;
    
    private ReminderDataController() {
        components = new HashMap<>();
        componentsData = new TreeMap<>(); 
    }
    
    public Map<String, Reminder> getComponentsData() {
        return componentsData;
    }

    public Reminder getPendingReminder() {
        return pendingReminder;
    }
   
    
    public void setContainerReminders(VBox containerReminders) {
        this.containerReminder = containerReminders;
    }
    
    @Override
    public boolean componentExists(String name){
        return componentsData.containsKey(name);
    }
    
    public void insertComponent(Reminder reminder) {
        if (reminder != null && !componentExists(reminder.getName())) {
            this.pendingReminder = reminder;
            try {
                URL resource = this.getClass().getResource("/reminder/components/ReminderComponentFXML.fxml");
                Object obj = FXMLLoader.load(resource);
                if (obj != null && obj instanceof AnchorPane) {
                    AnchorPane anchorPane = (AnchorPane) obj;
                    anchorPane.prefWidthProperty().bind(containerReminder.widthProperty());
                    components.put(pendingReminder.getName(), anchorPane);
                    componentsData.put(pendingReminder.getName(), reminder);
                }
            } catch (IOException ex) {
                Logger.getLogger(ToDoDataController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        this.pendingReminder = null;
    }

    @Override
    public void loadComponents() {
        RemindersController.getInstance().resetThreads();
        clearCache();
        List<Reminder> reminders = DataProvider.getInstance().getAllEntities(Reminder.class);
        for (Reminder reminder : reminders) {
            insertComponent(reminder);
        }
        sortComponents();
    }
    
    @Override
    public void saveComponents() {
        List<Reminder> oldReminders = DataProvider.getInstance().getAllEntities(Reminder.class);
        deleteOldReminders(oldReminders);

        for (Reminder reminder : componentsData.values()) {
            hiberUtil.saveOrUpdateEntity(reminder);
        }
    }

    public void setActive(String name, Boolean active) {
        componentsData.get(name).setActive(active ? Strings.DB_YES_FLAG : Strings.DB_NO_FLAG);
    }

    public void setDisplayActiveOnly(boolean displayActiveOnly) {
        this.displayActiveOnly = displayActiveOnly;
        sortComponents();
    }

    private void deleteOldReminders(List<Reminder> oldReminders) {
        for (Reminder reminder : oldReminders) {
            if (!componentsData.keySet().contains(reminder.getName())) {
                hiberUtil.deleteEntity(reminder);
            }
        }
    }
    
    @Override
    public void sortComponents(){
        containerReminder.getChildren().clear();
        for(Reminder reminder : componentsData.values()){
            addComponentToDisplay(reminder, components.get(reminder.getName()));
        }
    }
    
    @Override
    protected void addComponentToDisplay(Object component, AnchorPane anchorPane) {
        if (component instanceof Reminder) {
            if (displayActiveOnly && Strings.DB_YES_FLAG.equals(((Reminder) component).getActive()) || !displayActiveOnly) {
                containerReminder.getChildren().add(anchorPane);
            }
        }
    }
    
    @Override
    public void removeComponent(String name){
        containerReminder.getChildren().remove(components.get(name));
        components.remove(name);
        componentsData.remove(name);
    }

    @Override
    protected void clearCache() {
        components.clear();
        componentsData.clear();
        containerReminder.getChildren().clear();
    }
}
