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
import sample.database.Database;
import sample.tables.Cheque;
import sample.tables.Customer;
import sample.tables.TypeOfService;
import sample.tables.Worker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AdminChequeController {
    @FXML
    private TextField tf_client;
    @FXML
    private TextField tf_worker;
    @FXML
    private Button but_filter;
    @FXML
    private Button but_reset;
    @FXML
    private Button but_add_cheque;
    @FXML
    private Button but_delete;
    @FXML
    private TableView<Cheque> tv_order;
    @FXML
    private ChoiceBox<TypeOfService> cb_type_of_service;

    private Worker worker;
    private Database database = new Database();
    private Cheque curCheque = null;
    private ObservableList<Cheque> listCheque = FXCollections.observableArrayList();
    private final ObservableList<TypeOfService> listTypeOfService = FXCollections.observableArrayList();

    @FXML
    void initialize() {
        ArrayList<TypeOfService> listTypeOfService = database.getListTypeOfService();
        this.listTypeOfService.clear();
        this.listTypeOfService.add(new TypeOfService(0, "Все"));
        this.listTypeOfService.addAll(listTypeOfService);
        cb_type_of_service.setItems(this.listTypeOfService);
        cb_type_of_service.setValue(this.listTypeOfService.get(0));

        createColumnServices();
        ArrayList<Cheque> chequeList = database.getListCheque();
        listCheque.clear();
        listCheque = FXCollections.observableArrayList(chequeList);
        tv_order.setItems(listCheque);

        but_add_cheque.setOnAction(e -> addButCheque());
        but_delete.setOnAction(e -> deleteCheque());
        but_filter.setOnAction(e -> filter());
        but_reset.setOnAction(e -> resetFilterValue());

        TableView.TableViewSelectionModel<Cheque> selectionRow = tv_order.getSelectionModel();
        selectionRow.selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null)
                curCheque = newValue;
        });
    }

    private void resetFilterValue() {
        cb_type_of_service.setValue(this.listTypeOfService.get(0));
        tf_client.setText("");
        tf_worker.setText("");
        filter();
    }

    private void filter() {
        Predicate<Cheque> predicateTypeOfService = cheque -> cheque.getNameService().getId() == cb_type_of_service.getValue().getId();
        Predicate<Cheque> predicateCustomer = cheque -> cheque.getCustomer().getName().toLowerCase().contains(tf_client.getText().trim().toLowerCase());
        Predicate<Cheque> predicateWorker = cheque -> cheque.getWorker().getFullName().toLowerCase().contains(tf_worker.getText().trim().toLowerCase());

        Stream<Cheque> stream = listCheque.stream();

        if (cb_type_of_service.getValue().getId() != 0) stream = stream.filter(predicateTypeOfService);
        if (!tf_client.getText().isEmpty()) stream = stream.filter(predicateCustomer);
        if (!tf_worker.getText().isEmpty()) stream = stream.filter(predicateWorker);

        tv_order.setItems(FXCollections.observableArrayList(stream.collect(Collectors.toList())));
    }

    private void deleteCheque() {
        if (curCheque != null)
            database.deleteCheque(curCheque);
        updateTable();
    }

    private void createColumnServices() {
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
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("timeStr"));

        tv_order.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tv_order.getColumns().addAll(idColumn, serviceColumn, clientColumn, workerColumn, costColumn, dateColumn, timeColumn);
    }

    public void addButCheque() {
        FXMLLoader newLoader = new FXMLLoader();
        newLoader.setLocation(getClass().getResource("/sample/views/add-cheque-view.fxml"));
        try {
            newLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ((AddChequeController) newLoader.getController()).setParentController(this);
        Parent root = newLoader.getRoot();
        Stage stage = new Stage();
        stage.setTitle("Добавление заказа");
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }

    public void updateTable() {
        ArrayList<Cheque> cheques = database.getListCheque();
        listCheque.clear();
        listCheque = FXCollections.observableArrayList(cheques);
        tv_order.setItems(listCheque);
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }
}
