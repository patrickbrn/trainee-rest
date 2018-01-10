package com.valtech.azubi.bankanwendung.server.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class LaunchServerControlGui extends Application {

    public static void main(String[] args) {
    launch(args);
    }
    @Override
    public void start(Stage primaryStage) {
        Parent root = null;


        URL url = getClass().getResource("/GUI/admin.fxml");

        try {
            root = FXMLLoader.load(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();

    }


}