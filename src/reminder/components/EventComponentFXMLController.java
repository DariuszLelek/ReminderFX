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

import hibernate.config.Event;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import reminder.dataControllers.EventDataController;
import utilities.Utilities;

/**
 * FXML Controller class
 *
 * @author Dariusz
 */
public class EventComponentFXMLController implements Initializable {
    @FXML
    private Label txtDate;
    @FXML
    private Label txtTime;
    @FXML
    private Label txtName;
    @FXML
    private Label txtComments;
    @FXML
    private Label txtLocation;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private Label txtRepeat;
    
    private Event eventObject;
    private final Utilities utils = Utilities.getInstance();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        eventObject = EventDataController.getInstance().getPendingEvent();
        txtDate.setText(eventObject.getEventDate()!= null ? utils.getFormattedDate(eventObject.getEventDate()) : utils.getEventString(null));
        txtTime.setText(utils.getEventString(eventObject.getEventTime()));
        txtName.setText(utils.getEventString(eventObject.getName()));
        txtRepeat.setText(utils.getEventString(eventObject.getRepeatRatio()));
        txtLocation.setText(utils.getEventString(eventObject.getLocation()));
        txtComments.setText(utils.getEventString(eventObject.getComments()));
    }    

    @FXML
    private void btnClick(MouseEvent event) {
        EventDataController.getInstance().processComponentClick(rootPane, eventObject.getName());
    }
}
