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

import javafx.scene.control.Label;
import utilities.Strings;

/**
 *
 * @author Dariusz
 */
public class StatusController {

    // <editor-fold defaultstate="collapsed" desc=" Singleton ">
    private static StatusController instance;

    public synchronized static StatusController getInstance() {
        if (instance == null) {
            instance = new StatusController();
        }
        return instance;
    }
    // </editor-fold>

    private Label lblStatusIndicator;
    private Label txtStatusDisplay;

    public enum Status{
        CHANGES_NOT_SAVED("Changes were made, data not saved."),
        COMPONENT_EXISTS("Failed to add component. Component with given name already exists.");
        
        private final String stringValue;
        
        Status (String value){
            this.stringValue = value;
        }
        
        String getStringValue(){
            return this.stringValue;
        }
    }  

    public void setLblStatusIndicator(Label lblStatusIndicator) {
        this.lblStatusIndicator = lblStatusIndicator;
    }

    public void setTxtStatusDisplay(Label txtStatusDisplay) {
        this.txtStatusDisplay = txtStatusDisplay;
    }
    
    public void setStatus(Status status){
        txtStatusDisplay.setText(status.getStringValue());
        lblStatusIndicator.setVisible(true);
    }
    
    public void clearStatus(){
        txtStatusDisplay.setText(Strings.EMPTY_STRING);
        lblStatusIndicator.setVisible(false);
    }
}
