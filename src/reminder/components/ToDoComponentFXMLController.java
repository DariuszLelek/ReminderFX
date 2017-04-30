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

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import reminder.dataControllers.StatusController;
import reminder.dataControllers.ToDoDataController;
import reminder.dataobjects.ToDoDataTransfer;

/**
 * FXML Controller class
 *
 * @author Dariusz
 */
public class ToDoComponentFXMLController implements Initializable {
    @FXML
    private Label txtTaskName;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private Label txtOverdue;
    
    private ToDoDataTransfer tddt;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tddt = ToDoDataController.getInstance().getPendingToDo();
        txtTaskName.setText(tddt.getName());
        
        long hours = utilities.Utilities.getInstance().getHoursPasssed(tddt.getEnterDate());
        
        txtOverdue.setText(String.format("%02dd %02dh", hours/24 ,hours%24));
        
        if(tddt.getPriority() > 0){
            rootPane.setStyle("-fx-background-color:#ffafaf");
        }
    }    

    private void btnComplete(ActionEvent event) {
        ToDoDataController.getInstance().removeComponent(tddt.getName());
    }

    @FXML
    private void btnDelete(ActionEvent event) {
        ToDoDataController.getInstance().removeComponent(tddt.getName());
        StatusController.getInstance().setStatus(StatusController.Status.CHANGES_NOT_SAVED);
    }   
}
