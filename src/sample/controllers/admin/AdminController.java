package sample.controllers.admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sample.database.Database;
import sample.tables.Cheque;
import sample.tables.TypeOfService;
import sample.tables.Worker;

import java.io.IOException;
import java.util.ArrayList;

public class AdminController {
    @FXML private ToggleButton but_service;
    @FXML private ToggleButton but_worker;
    @FXML private ToggleButton but_client;
    @FXML private ToggleButton but_cheque;
    @FXML private Label lb_name;
    @FXML private AnchorPane pane_container;

    private Worker worker;
    private Database database = new Database();
    private ObservableList<Cheque> listCheque = FXCollections.observableArrayList();

    @FXML void initialize(){
        showChequePane();
        ToggleGroup group = new ToggleGroup();
        but_service.setToggleGroup(group);
        but_worker.setToggleGroup(group);
        but_client.setToggleGroup(group);
        but_cheque.setToggleGroup(group);
        but_cheque.setSelected(true);
        but_cheque.setOnAction(e -> showChequePane());
        but_service.setOnAction(e -> showServicePane());
        but_client.setOnAction(e -> showCustomerPane());
        but_worker.setOnAction(e -> showWorkerPane());
    }

    private void showWorkerPane() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/sample/views/admin-worker-view.fxml"));
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        AnchorPane child = fxmlLoader.getRoot();
        AnchorPane.setBottomAnchor(child, 0d);
        AnchorPane.setLeftAnchor(child, 0d);
        AnchorPane.setRightAnchor(child, 0d);
        AnchorPane.setTopAnchor(child, 0d);
        pane_container.getChildren().clear();
        pane_container.getChildren().add(child);
    }

    private void showCustomerPane() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/sample/views/admin-customers-view.fxml"));
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        AnchorPane child = fxmlLoader.getRoot();
        AnchorPane.setBottomAnchor(child, 0d);
        AnchorPane.setLeftAnchor(child, 0d);
        AnchorPane.setRightAnchor(child, 0d);
        AnchorPane.setTopAnchor(child, 0d);
        pane_container.getChildren().clear();
        pane_container.getChildren().add(child);
    }

    private void showServicePane() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/sample/views/admin-type-of-service-view.fxml"));
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        AnchorPane child = fxmlLoader.getRoot();
        AnchorPane.setBottomAnchor(child, 0d);
        AnchorPane.setLeftAnchor(child, 0d);
        AnchorPane.setRightAnchor(child, 0d);
        AnchorPane.setTopAnchor(child, 0d);
        pane_container.getChildren().clear();
        pane_container.getChildren().add(child);
    }


    private void showChequePane() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/sample/views/admin-cheque-view.fxml"));
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        AnchorPane child = fxmlLoader.getRoot();
        AnchorPane.setBottomAnchor(child, 0d);
        AnchorPane.setLeftAnchor(child, 0d);
        AnchorPane.setRightAnchor(child, 0d);
        AnchorPane.setTopAnchor(child, 0d);
        pane_container.getChildren().clear();
        pane_container.getChildren().add(child);
    }

    public void setWorker(Worker worker){
        this.worker = worker;
        lb_name.setText(worker.getFullName().split(" ")[1]);
    }
}
