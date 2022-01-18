package sample.controllers.admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sample.MaskField;
import sample.database.Database;
import sample.tables.Customer;
import sample.tables.Position;
import sample.tables.TypeOfService;
import sample.tables.Worker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AdminWorkerController {
    @FXML
    private TableView<Worker> tv_worker;
    @FXML
    private Button but_add;
    @FXML
    private Button but_update;
    @FXML
    private TextField tf_name;
    @FXML
    private MaskField tf_telephone;
    @FXML
    private ChoiceBox<Position> cb_position;

    private Database database = new Database();
    private ObservableList<Worker> listWorker = FXCollections.observableArrayList();
    private ObservableList<Position> listPosition = FXCollections.observableArrayList();
    private Worker curWorker;

    @FXML
    void initialize() {
        createColumnServices();
        ArrayList<Worker> listWorker = database.getListWorkers();
        this.listWorker.clear();
        this.listWorker = FXCollections.observableArrayList(listWorker);
        tv_worker.setItems(this.listWorker);

        ArrayList<Position> listPosition = new ArrayList<>(Arrays.asList(Position.values()));
        this.listPosition.addAll(listPosition);
        cb_position.setItems(this.listPosition);
        cb_position.setValue(this.listPosition.get(0));

        tf_name.textProperty().addListener((observable, oldValue, newValue) -> {
            filter();
        });
        tf_telephone.textProperty().addListener((observable, oldValue, newValue) -> {
            filter();
        });
        cb_position.valueProperty().addListener(((observable, oldValue, newValue) -> filter()));

        TableView.TableViewSelectionModel<Worker> selectionRow = tv_worker.getSelectionModel();
        selectionRow.selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                curWorker = newValue;
                but_update.setDisable(false);
            } else but_update.setDisable(true);
        });

        but_add.setOnAction(e -> showAdd());
        but_update.setOnAction(e -> showUpdate());
    }


    private void showUpdate() {
        FXMLLoader newLoader = new FXMLLoader();
        newLoader.setLocation(getClass().getResource("/sample/views/add-worker-view.fxml"));
        try {
            newLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ((AddWorkerController) newLoader.getController()).setParentController(this);
        ((AddWorkerController) newLoader.getController()).setUpdate(curWorker);
        Parent root = newLoader.getRoot();
        Stage stage = new Stage();
        stage.setTitle("Добавление клиента");
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }

    private void showAdd() {
        FXMLLoader newLoader = new FXMLLoader();
        newLoader.setLocation(getClass().getResource("/sample/views/add-worker-view.fxml"));
        try {
            newLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ((AddWorkerController) newLoader.getController()).setParentController(this);
        Parent root = newLoader.getRoot();
        Stage stage = new Stage();
        stage.setTitle("Добавление работника");
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }
    private void filter() {
        Predicate<Worker> predicateName = worker -> worker.getFullName().toLowerCase().contains(tf_name.getText().trim().toLowerCase());
        Predicate<Worker> predicatePhone = customer -> customer.getTelephone().toLowerCase().contains(tf_telephone.getPlainText().trim().toLowerCase());
        Predicate<Worker> predicatePosition = customer -> customer.getPosition().equals(cb_position.getValue().toString());

        Stream<Worker> stream = listWorker.stream();

        if (!tf_name.getText().isEmpty()) stream = stream.filter(predicateName);
        if (!tf_telephone.getPlainText().isEmpty()) stream = stream.filter(predicatePhone);
        if (cb_position.getValue() != Position.All) stream = stream.filter(predicatePosition);

        tv_worker.setItems(FXCollections.observableArrayList(stream.collect(Collectors.toList())));
    }

    private void createColumnServices() {
        tv_worker.getColumns().clear();

        TableColumn<Worker, String> idColumn = new TableColumn<>("Код");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Worker, String> nameColumn = new TableColumn<>("Имя");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));

        TableColumn<Worker, String> telephoneColumn = new TableColumn<>("Номер телефона");
        telephoneColumn.setCellValueFactory(new PropertyValueFactory<>("telephone"));

        TableColumn<Worker, String> emailColumn = new TableColumn<>("Почта");
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<Worker, String> positionColumn = new TableColumn<>("Должность");
        positionColumn.setCellValueFactory(new PropertyValueFactory<>("position"));

        tv_worker.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tv_worker.getColumns().addAll(idColumn, nameColumn, telephoneColumn, emailColumn, positionColumn);
    }

    public void updateTable() {
        ArrayList<Worker> customers = database.getListWorkers();
        listWorker.clear();
        listWorker = FXCollections.observableArrayList(customers);
        tv_worker.setItems(listWorker);
    }
}
