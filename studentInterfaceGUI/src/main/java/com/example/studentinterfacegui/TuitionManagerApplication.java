package com.example.studentinterfacegui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.TextArea;

import java.io.IOException;

/**
 * TuitionManagerApplication is the class that launches the GUI for managing
 * the tuition of a particular university
 * @authors Arnold Nguyen, Sofia Juliani
 */
public class TuitionManagerApplication extends Application {

    /**
     * Creates a new Scene, the root of the GUI, and loads the FXML file
     * @param stage object necessary for launching the GUI
     * @throws IOException if the .fxml file does not exist
     */
    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(TuitionManagerApplication.class.getResource("TuitionManagerView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 700);
        stage.setTitle("Tuition Manager Graphical User Interface");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Utilizes the launch() method to initiate the GUI for user interaction
     * @param args optional input, not needed to launch the GUI
     */
    public static void main(String[] args) {
        launch();
    }
}