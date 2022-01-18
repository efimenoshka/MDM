package sample.controllers.admin;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.MaskField;
import sample.database.Database;
import sample.tables.*;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AddChequeController {
    @FXML private ChoiceBox<TypeOfService> choice_service;
    @FXML
    private ChoiceBox<Worker> choice_worker;
    @FXML
    private TextField tf_name_client;
    @FXML
    private MaskField tf_telephone_client;
    @FXML
    private CheckBox check_new_client;
    @FXML
    private TableView<Customer> tv_clients;
    @FXML
    private Button but_new_order;
    @FXML
    private DatePicker dp_date;

    @FXML
    private TextField tf_time;

    private final Database database = new Database();
    private final ObservableList<TypeOfService> listTypeOfService = FXCollections.observableArrayList();
    private final ObservableList<Worker> listWorker = FXCollections.observableArrayList();
    private final ObservableList<Customer> listCustomer = FXCollections.observableArrayList();
    private Customer globalCustomer = null;
    private AdminChequeController parentController;
    private int globalId;


    @FXML void initialize() {
        ArrayList<TypeOfService> listTypeOfService = database.getListTypeOfService();
        this.listTypeOfService.clear();
        this.listTypeOfService.setAll(listTypeOfService);
        choice_service.setItems(this.listTypeOfService);

        ArrayList<Worker> listWorker = database.getListWorkers();
        this.listWorker.clear();
        this.listWorker.setAll(listWorker);
        choice_worker.setItems(this.listWorker);

        ChangeListener<TypeOfService> changeListener = (observableValue, oldValue, newValue) -> {
            if (newValue != null && newValue != oldValue) {
                ArrayList<Worker> listWorker1 = database.getListWorkersByPosition(newValue.getPosition());
                choice_worker.setItems(FXCollections.observableArrayList(listWorker1));
            }
        };
        choice_service.getSelectionModel().selectedItemProperty().addListener(changeListener);

        createColumnCustomers();

        ArrayList<Customer> customers = database.getListCustomers();
        listCustomer.addAll(customers);
        tv_clients.setItems(listCustomer);

        ChangeListener<String> changeListener1 = (observableValue, oldValue, newValue) -> {
            filterNameTelephoneCustomers();
        };

        tf_name_client.textProperty().addListener(changeListener1);
        tf_telephone_client.textProperty().addListener(changeListener1);

        // чекбокс нового клиента
        check_new_client.setOnAction(e -> tv_clients.setDisable(check_new_client.isSelected()));

        //автозаполнение полей
        TableView.TableViewSelectionModel<Customer> selectionRow = tv_clients.getSelectionModel();
        selectionRow.selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                tf_name_client.setText(newValue.getName());
                tf_telephone_client.setText(newValue.getTelephone());
                globalCustomer = newValue;
            }
        });



//        tv_clients.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
//            if (newSelection != null) {
//                tf_name_client.setText(newSelection.getName());
//                tf_telephone_client.setText(newSelection.getTelephone());
//                globalCustomer = newSelection;
//            }
//        });

        but_new_order.setOnAction(e -> addCheque());
    }

    private void createColumnCustomers(){
        tv_clients.getColumns().clear();

        TableColumn<Customer, String> nameCustomersColumn = new TableColumn<>("Имя");
        nameCustomersColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Customer, String> telephoneCustomersColumn = new TableColumn<>("Телефон");
        telephoneCustomersColumn.setCellValueFactory(new PropertyValueFactory<>("telephone"));

        tv_clients.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tv_clients.getColumns().addAll(nameCustomersColumn, telephoneCustomersColumn);
    }

    private void filterNameTelephoneCustomers(){
        ArrayList<Customer> newCustomer = new ArrayList<>(listCustomer);
        Stream<Customer> stream = newCustomer.stream();
        if (!tf_name_client.getText().trim().isEmpty()){
            stream = stream.filter(customer -> customer.getName().toLowerCase().contains(tf_name_client.getText().toLowerCase().trim()));
        }
        if (!tf_telephone_client.getText().trim().isEmpty()){
            stream = stream.filter(customer -> customer.getTelephone().toLowerCase().contains(tf_telephone_client.getPlainText().toLowerCase().trim()));
        }

        tv_clients.setItems(FXCollections.observableArrayList(stream.collect(Collectors.toList())));

    }

    //обработка кнопки добавить
    private void addCheque(){
        if (!isAllDate()) {
            showError();
            return;
        }

        if (check_new_client.isSelected()) {
            Customer customer = database.addGetNewCustomer(new Customer(tf_name_client.getText(), tf_telephone_client.getText()));
            Cheque cheque = new Cheque(
                    choice_service.getValue(),
                    customer,
                    choice_worker.getValue(),
                    Date.from(dp_date.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                    tf_time.getText(),
                    Status.PROCESS
            );
            database.addCheque(cheque);
            close();
        } else{
            Cheque cheque = new Cheque(
                    choice_service.getValue(),
                    globalCustomer,
                    choice_worker.getValue(),
                    Date.from(dp_date.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                    tf_time.getText(),
                    Status.PROCESS
            );
            database.addCheque(cheque);
            close();
        }
    }

    private boolean isAllDate(){
        if (choice_service.getValue() == null) return false;
        if (choice_worker.getValue() == null) return false;
        if (tf_time.getText() == null || tf_time.getText().trim().isEmpty()) return false;
        try {
            String[] arr = tf_time.getText().split(":");
            int a = Integer.parseInt(arr[0]);
            if (a < 0 || a > 23) return false;
            int b = Integer.parseInt(arr[1]);
            if (b < 0 || b > 59) return false;
        } catch (Exception ex) {
            return false;
        }
        if (dp_date.getValue() == null) return false;
        if (check_new_client.isSelected()){
           if (tf_name_client.getText() == null || tf_name_client.getText().trim().isEmpty()) return false;
            return tf_telephone_client.getText() != null && !tf_telephone_client.getText().trim().isEmpty();
        } else {
            return globalCustomer != null;
        }
    }

    private void showError(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText("Проверьте введенные данные");
        alert.showAndWait();
    }

    private void close(){
        FXMLLoader newLoader = new FXMLLoader();
        newLoader.setLocation(getClass().getResource("/sample/views/admin-cheque-view.fxml"));
        try {
            newLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        parentController.updateTable();
        but_new_order.getScene().getWindow().hide();
    }

    public void setParentController(AdminChequeController parentController) {
        this.parentController = parentController;
    }

    public void setUpdate(Cheque cheque) {
        globalId = cheque.getId();
        int indexService = 0;
        int indexWorker = 0;


        for (int i = 0; i < listTypeOfService.size() - 1; i++) {
            if (choice_service.getItems().get(i).getId() == cheque.getNameService().getId()) indexService = i;
        }

        choice_service.setValue(listTypeOfService.get(indexService));


        for (int i = 0; i < choice_worker.getItems().size(); i++) {
            if (choice_worker.getItems().get(i).getId() == cheque.getWorker().getId()) indexWorker = i;
        }

        globalCustomer = cheque.getCustomer();

        tf_name_client.setText(globalCustomer.getName());
        tf_telephone_client.setText(globalCustomer.getTelephone());

        dp_date.setValue(Instant.ofEpochMilli(cheque.getDate().getTime()).atZone(ZoneId.systemDefault()).toLocalDate());

        tf_time.setText(cheque.getTimeStr());

        choice_worker.setValue(choice_worker.getItems().get(indexWorker));

        but_new_order.setText("Сохранить");
        but_new_order.setOnAction(e -> update());


    }

    private void update() {
        if (!isAllDate()) {
            showError();
            return;
        }

        if (check_new_client.isSelected()) {
            Customer customer = database.addGetNewCustomer(new Customer(tf_name_client.getText(), tf_telephone_client.getText()));
            Cheque cheque = new Cheque(
                    globalId,
                    choice_service.getValue(),
                    customer,
                    choice_worker.getValue(),
                    Date.from(dp_date.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                    tf_time.getText().trim(),
                    Status.PROCESS.ordinal()
            );
            database.updateCheque(cheque);
        } else {
            Cheque cheque = new Cheque(
                    globalId,
                    choice_service.getValue(),
                    globalCustomer,
                    choice_worker.getValue(),
                    Date.from(dp_date.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                    tf_time.getText().trim(),
                    Status.PROCESS.ordinal()
            );
            database.updateCheque(cheque);
        }
        close();
    }
}