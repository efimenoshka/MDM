package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sample.database.Database;
import sample.tables.Cheque;
import sample.tables.TypeOfService;
import sample.tables.Worker;

import java.io.IOException;
import java.util.ArrayList;

public class AdminController {
    @FXML private Button but_service;
    @FXML private Button but_worker;
    @FXML private Button but_client;
    @FXML private TextField tf_service;
    @FXML private TextField tf_client;
    @FXML private TextField tf_worker;
    @FXML private Button but_filter;
    @FXML private Button but_reset;
    @FXML private Button but_add_cheque;
    @FXML private Button but_change;
    @FXML private Button but_delete;
    @FXML private TableView<Cheque> tv_order;
    @FXML private Label lb_name;

    private Worker worker;
    private Database database = new Database();
    private ObservableList<Cheque> listCheque = FXCollections.observableArrayList();

    @FXML void initialize(){setChequePanel();}

    private void createColumnServices(){
        tv_order.getColumns().clear();

        TableColumn<Cheque, Integer> idColumn = new TableColumn<>("№ заказа");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Cheque, Integer> serviceColumn = new TableColumn<>("Услуга");
        serviceColumn.setCellValueFactory(new PropertyValueFactory<>("nameService"));

        TableColumn<Cheque, Integer> clientColumn = new TableColumn<>("Клиент");
        clientColumn.setCellValueFactory(new PropertyValueFactory<>("customer"));

        TableColumn<Cheque, Integer> workerColumn = new TableColumn<>("Работник");
        workerColumn.setCellValueFactory(new PropertyValueFactory<>("worker"));

        TableColumn<Cheque, Integer> costColumn = new TableColumn<>("Стоимость");
        costColumn.setCellValueFactory(new PropertyValueFactory<>("cost"));

        TableColumn<Cheque, Integer> dateColumn = new TableColumn<>("Дата");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateStr"));

        TableColumn<Cheque, Integer> timeColumn = new TableColumn<>("Время");
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));

        tv_order.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tv_order.getColumns().addAll(idColumn, serviceColumn, clientColumn, workerColumn, costColumn, dateColumn, timeColumn);
    }

    public void setWorker(Worker worker){
        this.worker = worker;
        lb_name.setText(worker.getFullName().split(" ")[1]);
    }

    public void setChequePanel(){
        createColumnServices();
        ArrayList<Cheque> chequeList = database.getListCheque();
        listCheque.clear();
        listCheque = FXCollections.observableArrayList(chequeList);
        tv_order.setItems(listCheque);

        but_add_cheque.setOnAction(e -> addButCheque());
    }

    public void addButCheque(){
        FXMLLoader newLoader = new FXMLLoader();
        newLoader.setLocation(getClass().getResource("/sample/views/add-cheque-view.fxml"));
        try {
            newLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Parent root = newLoader.getRoot();
        Stage stage = new Stage();
        stage.setTitle("Добавление заказа");
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }

    public void updateTable(){
        ArrayList<Cheque> cheques = database.getListCheque();
        listCheque.clear();
        listCheque = FXCollections.observableArrayList(cheques);
        tv_order.setItems(listCheque);
    }
}
