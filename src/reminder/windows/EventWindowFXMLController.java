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

import hibernate.config.Event;
import java.net.URL;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import reminder.dataControllers.EventDataController;
import reminder.dataControllers.StatusController;
import utilities.Strings;
import utilities.Utilities;

/**
 * FXML Controller class
 *
 * @author Dariusz
 */
public class EventWindowFXMLController implements Initializable {

    @FXML
    private TextField txtName;
    @FXML
    private TextField txtPlace;
    @FXML
    private DatePicker fieldDate;
    @FXML
    private ChoiceBox<Integer> fieldHours;
    @FXML
    private ChoiceBox<Integer> fieldMinutes;
    @FXML
    private TextArea txtComments;
    @FXML
    private RadioButton btnRepeat;
    @FXML
    private ToggleGroup repeatGroup;
    @FXML
    private RadioButton btnOnlyOnce;
    @FXML
    private HBox repeatTogglePanel;
    @FXML
    private ToggleButton btnWeekly;
    @FXML
    private ToggleGroup repeatToggle;
    @FXML
    private ToggleButton btnMonthly;
    @FXML
    private ToggleButton btnYearly;
    
    private String repeatRatio;
    private utilities.Utilities utils;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        utils = Utilities.getInstance();
        fieldHours.setItems(FXCollections.observableArrayList(Strings.getInstance().getHours()));
        fieldMinutes.setItems(FXCollections.observableArrayList(Strings.getInstance().getMinutes()));
        
        updateToggleButtonsPanel();
    }    

    @FXML
    private void toggleRepeat(ActionEvent event) {
        updateToggleButtonsPanel();
    }

    @FXML
    private void toggleOnlyOnce(ActionEvent event) {
        updateToggleButtonsPanel();
    }
    
    private void updateToggleButtonsPanel(){
        if(btnRepeat.isSelected()){
            repeatTogglePanel.setDisable(false);
        }else{
            repeatTogglePanel.setDisable(true);
            btnWeekly.setSelected(false);
            btnMonthly.setSelected(false);
            btnYearly.setSelected(false);
        }
    }
    
    private boolean validateWindow(){
        boolean result = true;
        result &= !Strings.EMPTY_STRING.equals(txtName.getText());
        result &= fieldDate.getValue() != null;
        
        if(result && Date.from(fieldDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()).compareTo(utils.getCurrentDate()) < 0){
            fieldDate.setValue(null);
            result = false;
        }
        
        if(result && btnRepeat.isSelected()){
            result &= (btnWeekly.isSelected() || btnMonthly.isSelected() || btnYearly.isSelected());
        }
        
        return result;
    }
    
    private void closeWindow(){
        Stage stage = (Stage) txtName.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void btnOk(ActionEvent event) {
        if (validateWindow()) {
            if (!EventDataController.getInstance().componentExists(txtName.getText())) {
                int hours = fieldHours.getSelectionModel().getSelectedItem() != null ? fieldHours.getSelectionModel().getSelectedItem() : 0;
                int minutes = fieldMinutes.getSelectionModel().getSelectedItem() != null ? fieldMinutes.getSelectionModel().getSelectedItem() : 0;

                repeatRatio = btnWeekly.isSelected() ? Strings.WEEKLY
                        : (btnMonthly.isSelected() ? Strings.MONTHLY
                        : (btnYearly.isSelected() ? Strings.YEARLY : Strings.ONCE));
                Event eventObject;
                eventObject = new Event(txtName.getText(), txtPlace.getText(),
                        Date.from(fieldDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                        utils.getCurrentDate(),
                        utils.getFormattedTime(hours, minutes),
                        txtComments.getText(), repeatRatio);
                EventDataController.getInstance().insertComponent(eventObject);
                EventDataController.getInstance().sortComponents();

                StatusController.getInstance().setStatus(StatusController.Status.CHANGES_NOT_SAVED);
            }else{
                StatusController.getInstance().setStatus(StatusController.Status.COMPONENT_EXISTS);
            }
            closeWindow();
        }
    }

    @FXML
    private void btnCancel(ActionEvent event) {
        closeWindow();
    }
}
