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

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import reminder.dataControllers.StatusController;
import reminder.dataControllers.ToDoDataController;
import reminder.dataobjects.ToDoDataTransfer;
import utilities.Strings;
import utilities.Utilities;

/**
 * FXML Controller class
 *
 * @author Dariusz
 */
public class ToDoWindowFXMLController implements Initializable {

    @FXML
    private TextField txtName;
    @FXML
    private ChoiceBox<String> choiceBox;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        choiceBox.setItems(FXCollections.observableArrayList("low", "high"));
    }

    @FXML
    private void btnOk(ActionEvent event) {
        if (!Strings.EMPTY_STRING.equals(txtName.getText()) && choiceBox.getSelectionModel().getSelectedItem() != null) {
            int priority = "low".equals(choiceBox.getSelectionModel().getSelectedItem()) ? 0 : 1;

            if (!ToDoDataController.getInstance().componentExists(txtName.getText())) {
                ToDoDataController.getInstance().insertComponent(new ToDoDataTransfer(txtName.getText(), priority, Utilities.getInstance().getCurrentDateTime()));
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

    private void closeWindow() {
        Stage stage = (Stage) txtName.getScene().getWindow();
        stage.close();
    }
}
