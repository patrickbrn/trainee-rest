package com.valtech.azubi.bankanwendung.server.gui.Controller;

import com.valtech.azubi.bankanwendung.model.dto.LogInSession;
import com.valtech.azubi.bankanwendung.server.resources.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;

import java.net.URL;
import java.util.ResourceBundle;

public class Admin implements Initializable {
    @FXML
    TableView<LogInSession> sessionTable;
    @FXML
    Button btn_kick;
    @FXML
    Button btn_ban;
    @FXML
    Button btn_reload;
    @FXML
    ToggleButton tog_updown;

SessionManager sessionManager=SessionManager.getInstance();

public void fillSessionTable(){
//sessionTable.getItems().clear();
sessionTable.getItems().addAll(sessionManager.getLogInSessionList());
}

public void reload(){
    fillSessionTable();
}
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}