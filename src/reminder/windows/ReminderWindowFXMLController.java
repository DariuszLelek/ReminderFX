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
package reminder.windows;

import hibernate.config.Reminder;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import reminder.dataControllers.ReminderDataController;
import reminder.dataControllers.StatusController;
import utilities.Strings;

/**
 * FXML Controller class
 *
 * @author Dariusz
 */
public class ReminderWindowFXMLController implements Initializable {

    @FXML
    private TextField txtName;
    @FXML
    private HBox panelHourly;
    @FXML
    private TextField txtHours;
    @FXML
    private TextField txtMinutes;
    @FXML
    private HBox panelWeekly;
    @FXML
    private CheckBox checkMonday;
    @FXML
    private CheckBox checkTuesday;
    @FXML
    private CheckBox checkWednesday;
    @FXML
    private CheckBox checkThursday;
    @FXML
    private CheckBox checkFriday;
    @FXML
    private CheckBox checkSaturday;
    @FXML
    private CheckBox checkSunday;
    @FXML
    private CheckBox checkEveryday;
    @FXML
    private RadioButton radioHourly;
    @FXML
    private RadioButton radioWeekly;
    
    private final ToggleGroup toggleGroup = new ToggleGroup();
    private Boolean[] weekdays;
    private CheckBox[] checkBoxes;
    
    private int hours, minutes;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        radioHourly.setToggleGroup(toggleGroup);
        radioWeekly.setToggleGroup(toggleGroup);
        
        radioHourly.setSelected(true);
        
        weekdays = new Boolean[7];
        checkBoxes = new CheckBox[7];
        
        checkBoxes[0] = checkMonday;
        checkBoxes[1] = checkTuesday;
        checkBoxes[2] = checkWednesday;
        checkBoxes[3] = checkThursday;
        checkBoxes[4] = checkFriday;
        checkBoxes[5] = checkSaturday;
        checkBoxes[6] = checkSunday;
        
        setMasks();
        updatePanelVisibility();
    }    

    // <editor-fold defaultstate="collapsed" desc=" FXML methods ">
    @FXML
    private void btnOk(ActionEvent event) {
        if (validateWindow()) {
            if (!ReminderDataController.getInstance().componentExists(txtName.getText())) {
                StringBuilder days = new StringBuilder();
                for (int i = 0; i < 7; i++) {
                    if (weekdays[i]) {
                        days.append(i + 1);
                    }
                }
                Reminder reminder = new Reminder(txtName.getText(), days.toString(), hours + ":" + minutes, Strings.DB_NO_FLAG,
                radioWeekly.isSelected() ? Strings.WEEKLY : Strings.DAILY);
                ReminderDataController.getInstance().insertComponent(reminder);
                ReminderDataController.getInstance().sortComponents();

                StatusController.getInstance().setStatus(StatusController.Status.CHANGES_NOT_SAVED);
            } else {
                StatusController.getInstance().setStatus(StatusController.Status.COMPONENT_EXISTS);
            }
            closeWindow();
        }
    }

    @FXML
    private void btnCancel(ActionEvent event) {
        closeWindow();
    }
    
    
    @FXML
    private void btnRadio(ActionEvent event) {
        updatePanelVisibility();
    }

    @FXML
    private void btnCheckDay(ActionEvent event) {
        if(checkEveryday.isSelected()){
            checkEveryday.setSelected(false);
        }
    }

    @FXML
    private void btnCheckEveryDay(ActionEvent event) {
        for(int i=0; i<checkBoxes.length; i++){
            checkBoxes[i].setSelected(checkEveryday.isSelected());
        }
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc=" Private methods ">
    private void setMasks(){
        txtHours.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                Integer value = null;
                try {
                    value = Integer.parseInt(txtHours.getText());
                } catch (NumberFormatException e) {

                } finally {
                    if (value == null || !(value > 0 && value < 24)) {
                        txtHours.setText(Strings.EMPTY_STRING);
                    }else{
                        hours = value;
                    }
                }
            }
        });

        txtMinutes.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                Integer value = null;
                try {
                    value = Integer.parseInt(txtMinutes.getText());
                } catch (NumberFormatException e) {

                } finally {
                    if (value == null || !(value > 0 && value < 60)) {
                        txtMinutes.setText(Strings.EMPTY_STRING);
                    }else{
                        minutes = value;
                    }
                }
            }
        });
    }

    private void updatePanelVisibility() {
        if (radioWeekly.isArmed() || radioWeekly.isPressed()) {
            panelHourly.setDisable(true);
            panelWeekly.setDisable(false);

            txtHours.setText(Strings.EMPTY_STRING);
            txtMinutes.setText(Strings.EMPTY_STRING);
        } else {
            panelHourly.setDisable(false);
            panelWeekly.setDisable(true);
            
            Arrays.asList(checkBoxes).forEach(x -> x.setSelected(false));
            checkEveryday.setSelected(false);
        }
    }
    
    private void updateWeekDayCheck(){
        for(int i=0; i<checkBoxes.length; i++){
            weekdays[i] = checkBoxes[i].isSelected();
        }
    }
    
    private void closeWindow(){
        Stage stage = (Stage) txtName.getScene().getWindow();
        stage.close();
    }
    
    private boolean validateWindow(){
        boolean result = !txtName.getText().isEmpty();
        
        if (result) {
            updateWeekDayCheck();
            if (radioHourly.isSelected()) {
                result &= !txtHours.getText().isEmpty() || !txtMinutes.getText().isEmpty();
            } else if (radioWeekly.isSelected()) {
                result &= Arrays.asList(weekdays).stream().anyMatch(x -> x);
            } else {
                result = false;
            }
        }
        
        return result;
    }
    // </editor-fold>
}
