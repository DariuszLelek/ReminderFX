/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reminder;

import hibernate.util.HibernateUtil;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import reminder.dataControllers.EventDataController;
import reminder.dataControllers.ReminderDataController;
import reminder.dataControllers.RemindersController;
import reminder.dataControllers.ToDoDataController;
import utilities.Strings;

/**
 *
 * @author Dariusz
 */
public class ReminderFX extends Application {
    public static void main(String[] args) throws IOException {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("reminder/ReminderFXML.fxml"));
        Scene scene = new Scene(root);

        primaryStage.setOnCloseRequest(event -> {
            RemindersController.getInstance().stopController();
            HibernateUtil.getInstance().stopConnectionProvider();
        });

        primaryStage.getIcons().add(new Image(("graphics/icon32.png")));
        primaryStage.setResizable(true);
        primaryStage.setTitle(Strings.APP_NAME + Strings.SPACE_BAR + Strings.APP_VERSION);
        primaryStage.setScene(scene);
        primaryStage.show();

        ReminderDataController.getInstance().loadComponents();
        ToDoDataController.getInstance().loadComponents();
        EventDataController.getInstance().loadComponents();
        EventDataController.getInstance().updateDisplayedEvents(EventDataController.Upcoming.TODAY);
    }
}
