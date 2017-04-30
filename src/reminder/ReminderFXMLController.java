/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reminder;

import hibernate.config.Event;
import hibernate.config.Reminder;
import hibernate.config.Todo;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import reminder.dataControllers.DataProvider;
import reminder.dataControllers.EventDataController;
import reminder.dataControllers.ReminderDataController;
import reminder.dataControllers.RemindersController;
import reminder.dataControllers.StatusController;
import reminder.dataControllers.ToDoDataController;
import utilities.AudioController;
import utilities.Strings;

/**
 * FXML Controller class
 *
 * @author Dariusz
 */
public class ReminderFXMLController implements Initializable {
    @FXML
    private VBox containerToDo;
    @FXML
    private ScrollPane paneToDo;
    @FXML
    private ScrollPane paneReminders;
    @FXML
    private VBox containerReminder;
    @FXML
    private CheckBox chckPriorityOnly;
    @FXML
    private ToggleGroup eventGroup;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private VBox containerEvent;
    @FXML
    private ScrollPane paneEvent;
    @FXML
    private Button editButton;
    @FXML
    private Button deteleButton;
    @FXML
    private Label lblStatusIndicator;
    @FXML
    private Label txtStatusDisplay;
    @FXML
    private Label txtRemindersCounter;
    @FXML
    private Label txtToDoCounter;
    @FXML
    private Label txtEventsCounter;
    @FXML
    private CheckBox chckActiveOnly;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {        
        ToDoDataController.getInstance().setContainerToDo(containerToDo);
        ReminderDataController.getInstance().setContainerReminders(containerReminder);
        EventDataController.getInstance().setContainerEvent(containerEvent);
        
        paneReminders.setFitToHeight(true);
        paneReminders.setFitToWidth(true);
        paneToDo.setFitToHeight(true);
        paneToDo.setFitToWidth(true);      
        paneEvent.setFitToHeight(true);
        paneEvent.setFitToWidth(true);
        
        StatusController.getInstance().setLblStatusIndicator(lblStatusIndicator);
        StatusController.getInstance().setTxtStatusDisplay(txtStatusDisplay);
        StatusController.getInstance().clearStatus();
        
        updateCounters();
        
        editButton.disableProperty().bind(new BooleanBinding(){
                {
                   bind(EventDataController.getInstance().EventSelected);
                }
            
            @Override
            protected boolean computeValue() {
                return !EventDataController.getInstance().EventSelected.get();
            }
        });
        deteleButton.disableProperty().bind(new BooleanBinding(){
                {
                   bind(EventDataController.getInstance().EventSelected);
                }
            
            @Override
            protected boolean computeValue() {
                return !EventDataController.getInstance().EventSelected.get();
            }
        });
    }    

    @FXML
    private void btnAddReminder(ActionEvent event) {
        try {
            showStage(FXMLLoader.load(this.getClass().getResource("/reminder/windows/ReminderWindowFXML.fxml")));
        } catch (IOException ex) {
            Logger.getLogger(ReminderFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void btnAddToDo(ActionEvent event) {
        try {
            showStage(FXMLLoader.load(this.getClass().getResource("/reminder/windows/ToDoWindowFXML.fxml")));
        } catch (IOException ex) {
            Logger.getLogger(ReminderFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    private void btnAddEvent(ActionEvent event) {
        try {
            clearSelecteEventToggle();
            EventDataController.getInstance().clearSelectedComponent();
            showStage(FXMLLoader.load(this.getClass().getResource("/reminder/windows/EventWindowFXML.fxml")));
        } catch (IOException ex) {
            Logger.getLogger(ReminderFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void btnSave(ActionEvent event) {
        ReminderDataController.getInstance().saveComponents();
        ToDoDataController.getInstance().saveComponents();
        EventDataController.getInstance().saveComponents();
        StatusController.getInstance().clearStatus();
        updateCounters();
    }

    @FXML
    private void btnLoad(ActionEvent event) {
        ReminderDataController.getInstance().loadComponents();
        ToDoDataController.getInstance().loadComponents();
        EventDataController.getInstance().loadComponents();
        clearSelecteEventToggle();
        StatusController.getInstance().clearStatus();
        updateCounters();
    }

    @FXML
    private void btnPriorityOnly(ActionEvent event) {
        ToDoDataController.getInstance().setDisplayPriorityOnly(chckPriorityOnly.isSelected());
    }

    @FXML
    private void btnToday(ActionEvent event) {
         EventDataController.getInstance().updateDisplayedEvents(EventDataController.Upcoming.TODAY);
    }

    @FXML
    private void btnWeek(ActionEvent event) {
        EventDataController.getInstance().updateDisplayedEvents(EventDataController.Upcoming.WEEK);
    }

    @FXML
    private void btnMonth(ActionEvent event) {
        EventDataController.getInstance().updateDisplayedEvents(EventDataController.Upcoming.MONTH);
    }

    @FXML
    private void btnAll(ActionEvent event) {
        EventDataController.getInstance().updateDisplayedEvents(EventDataController.Upcoming.ALL);
    }

    @FXML
    private void btnEdit(ActionEvent event) {
        // TODO
    }

    @FXML
    private void btnDelete(ActionEvent event) {
        EventDataController.getInstance().deleteSelected();
    }
    
    @FXML
    private void btnActiveOnly(ActionEvent event) {
        ReminderDataController.getInstance().setDisplayActiveOnly(chckActiveOnly.isSelected());
    }

    // <editor-fold defaultstate="collapsed" desc=" Private methods ">
    private void showStage(Parent root) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(rootPane.getScene().getWindow());
        stage.setTitle(Strings.ADD_NEW_ITEM);
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();
    }

    private void updateCounters(){
        int remindersSize = DataProvider.getInstance().getTableCount(Reminder.class);
        int todosSize = DataProvider.getInstance().getTableCount(Todo.class);
        int eventsSize = DataProvider.getInstance().getTableCount(Event.class);
        txtRemindersCounter.setText(remindersSize > 0 ? "(" + remindersSize + ")" : Strings.EMPTY_STRING);
        txtEventsCounter.setText(eventsSize > 0 ? "(" + eventsSize + ")" : Strings.EMPTY_STRING);
        txtToDoCounter.setText(todosSize > 0 ? "(" + todosSize + ")" : Strings.EMPTY_STRING);
    }
    
    private void clearSelecteEventToggle() {
        if (eventGroup.getSelectedToggle() != null) {
            eventGroup.getSelectedToggle().setSelected(false);
        }
    }
    // </editor-fold>
}
