package sample.controllers.worker;

import com.microsoft.sqlserver.jdbc.SQLServerDataTable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.database.Database;
import sample.tables.Cheque;
import sample.tables.Status;
import sample.tables.TypeOfService;
import sample.tables.Worker;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WorkerAllChequeController {
    @FXML
    private TableView<Cheque> tv_order;
    @FXML
    private TextField tf_client;
    @FXML
    private ChoiceBox<TypeOfService> cb_type_of_service;
    @FXML
    private DatePicker date_picker;
    @FXML
    private ChoiceBox<Status> cb_status;

    private Worker curWorker;
    private Database database = new Database();
    private ObservableList<Cheque> listCheque = FXCollections.observableArrayList();
    private ObservableList<TypeOfService> listTypeOfService = FXCollections.observableArrayList();
    private Cheque curCheque;

    @FXML
    void initialize() {
        ArrayList<TypeOfService> listTypeOfService = database.getListTypeOfService();
        this.listTypeOfService.clear();
        this.listTypeOfService.add(new TypeOfService(0, "Все"));
        this.listTypeOfService.addAll(listTypeOfService);
        cb_type_of_service.setItems(this.listTypeOfService);
        cb_type_of_service.setValue(this.listTypeOfService.get(0));

        cb_status.setItems(FXCollections.observableArrayList(Status.END, Status.PROCESS, Status.ALL));
        cb_status.setValue(Status.ALL);

        createColumnServices();
        tv_order.setItems(listCheque);

        cb_type_of_service.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            filter();
        }));
        cb_status.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            filter();
        }));
        tf_client.textProperty().addListener((observable, oldValue, newValue) -> filter());

        TableView.TableViewSelectionModel<Cheque> selectionRow = tv_order.getSelectionModel();
        selectionRow.selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                curCheque = newValue;
            }
        });

        date_picker.setOnAction(e -> filter());
    }

    private void filter() {
        Predicate<Cheque> predicateTypeOfService = cheque -> cheque.getNameService().getId() == cb_type_of_service.getValue().getId();
        Predicate<Cheque> predicateCustomer = cheque -> cheque.getCustomer().getName().toLowerCase().contains(tf_client.getText().trim().toLowerCase());
        Predicate<Cheque> predicateDate = contract -> contract.getDate().getTime() == Date.from(date_picker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime();
        Predicate<Cheque> predicateStatus = contract -> contract.getStatus() == cb_status.getValue();

        Stream<Cheque> stream = listCheque.stream();

        if (cb_type_of_service.getValue().getId() != 0) stream = stream.filter(predicateTypeOfService);
        if (!tf_client.getText().isEmpty()) stream = stream.filter(predicateCustomer);
        if (date_picker.getValue() != null) stream = stream.filter(predicateDate);
        if (cb_status.getValue() != Status.ALL) stream = stream.filter(predicateStatus);

        tv_order.setItems(FXCollections.observableArrayList(stream.collect(Collectors.toList())));
    }

    private void createColumnServices() {
        tv_order.getColumns().clear();

        TableColumn<Cheque, Integer> idColumn = new TableColumn<>("№ заказа");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Cheque, Integer> serviceColumn = new TableColumn<>("Услуга");
        serviceColumn.setCellValueFactory(new PropertyValueFactory<>("nameService"));

        TableColumn<Cheque, Integer> clientColumn = new TableColumn<>("Клиент");
        clientColumn.setCellValueFactory(new PropertyValueFactory<>("customer"));

        TableColumn<Cheque, Integer> costColumn = new TableColumn<>("Стоимость");
        costColumn.setCellValueFactory(new PropertyValueFactory<>("cost"));

        TableColumn<Cheque, Integer> dateColumn = new TableColumn<>("Дата");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateStr"));

        TableColumn<Cheque, Integer> timeColumn = new TableColumn<>("Время");
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("timeStr"));

        TableColumn<Cheque, Integer> statusColumn = new TableColumn<>("Статус");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        tv_order.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tv_order.getColumns().addAll(idColumn, serviceColumn, clientColumn, costColumn, dateColumn, timeColumn, statusColumn);
    }

    public void setWorker(Worker worker){
        this.curWorker = worker;
        ArrayList<Cheque> chequeList = database.getListChequeByWorkerId(curWorker);
        listCheque.clear();
        this.listCheque.addAll(chequeList);
    }
}
