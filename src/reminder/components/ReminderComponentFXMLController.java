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
package reminder.components;

import hibernate.config.Reminder;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import reminder.dataControllers.ReminderDataController;
import reminder.dataControllers.RemindersController;
import reminder.dataControllers.StatusController;
import utilities.AudioController;
import utilities.Strings;

/**
 * FXML Controller class
 *
 * @author Dariusz
 */
public class ReminderComponentFXMLController implements Initializable {
    @FXML
    private Label txtName;
    @FXML
    private RadioButton radioActive;
    @FXML
    private Label txtCounter;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private Label lblRepeat;
    @FXML
    private Button completeButton;
    
    private Stage mainStage;
    private Reminder reminder;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        reminder = ReminderDataController.getInstance().getPendingReminder();
        txtName.setText(reminder.getName());
        lblRepeat.setText(!Strings.EMPTY_STRING.equals(reminder.getWeekDays()) ? utilities.Utilities.getInstance().getDayLetters(reminder.getWeekDays()) : Strings.EMPTY_STRING_PLACE_HOLDER);
        radioActive.setSelected(Strings.DB_YES_FLAG.equals(reminder.getActive()));
        //txtCounter.textProperty().bind(reminder.Counter);
        
        reminder.setReminderComponent(this);
        reminder.resetTimeInSeconds();
        txtCounter.setText(reminder.getCounter());
        
        RemindersController.getInstance().updateActiveReminders(reminder);
        updatePanelVisibility();
    }    

    @FXML
    private void btnRadioActive(ActionEvent event) {
        updatePanelVisibility();
        reminder.setToRemind(false);
        reminder.resetTimeInSeconds();
        
        if(!completeButton.isDisabled() && !radioActive.isSelected()){
            completeButton.setDisable(true);
        }
        
        ReminderDataController.getInstance().setActive(reminder.getName(), radioActive.isSelected());
        ReminderDataController.getInstance().sortComponents();
        RemindersController.getInstance().updateActiveReminders(reminder);
    }
    
    private void updatePanelVisibility(){
        boolean active = radioActive.isSelected();
        txtCounter.setDisable(!active);
        rootPane.setStyle(active ? Strings.COLOR_ACTIVE_REMINDER : Strings.EMPTY_STRING);
    }

    @FXML
    private void btnDelete(ActionEvent event) {
        ReminderDataController.getInstance().removeComponent(txtName.getText());
        StatusController.getInstance().setStatus(StatusController.Status.CHANGES_NOT_SAVED);
    }

    @FXML
    private void btnComplete(ActionEvent event) {
        completeButton.setDisable(true);
        reminder.setToRemind(false);
        reminder.resetTimeInSeconds();
        
        if(!reminder.isDaily()){
            reminder.setLastTimeCompleted(utilities.Utilities.getInstance().getCurrentDate());
            RemindersController.getInstance().updateWeeklyCounter(reminder);
            StatusController.getInstance().setStatus(StatusController.Status.CHANGES_NOT_SAVED);
        }
        
        rootPane.setStyle(radioActive.isSelected() ? Strings.COLOR_ACTIVE_REMINDER : Strings.EMPTY_STRING);
        RemindersController.getInstance().updateActiveReminders(reminder);
    }
    
    public void setCounter(String counter) {
        txtCounter.setText(counter);
    }
    
    public void remindNow(){
        reminder.setTimeInSeconds(0);
        reminder.updateTimeCounter();
        reminder.setToRemind(true);
        completeButton.setDisable(false);
        rootPane.setStyle(Strings.COLOR_REMINDER_ZERO_TIME);
        
        if(reminder.isDaily()){
            AudioController.getInstance().playNotification();
        }

        bringMainStageToFront();
    }
    
    private void bringMainStageToFront() {
        setMainStage();
        if (mainStage != null) {
            if(mainStage.isIconified()){
                mainStage.setIconified(false);
            }
            mainStage.setAlwaysOnTop(true);
            mainStage.toFront();
            mainStage.requestFocus();
            mainStage.setAlwaysOnTop(false);
        } else {
            setMainStage();
        }
    }
    
    private void setMainStage(){
        if(mainStage == null && rootPane.getScene() != null){
            mainStage = (Stage) rootPane.getScene().getWindow();
        }
    }
}
