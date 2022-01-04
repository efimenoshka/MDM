package sample.controllers.admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sample.MaskField;
import sample.database.Database;
import sample.tables.Cheque;
import sample.tables.Customer;
import sample.tables.TypeOfService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AdminCustomerController {
    @FXML
    private TableView<Customer> tv_customers;
    @FXML
    private Button but_add;
    @FXML
    private Button but_update;
    @FXML
    private TextField tf_name;
    @FXML
    private MaskField tf_telephone;

    private Database database = new Database();
    private ObservableList<Customer> listCustomer = FXCollections.observableArrayList();
    private Customer curCustomer;

    @FXML
    void initialize() {
        createColumnServices();
        ArrayList<Customer> listCustomer = database.getListCustomers();
        this.listCustomer.clear();
        this.listCustomer = FXCollections.observableArrayList(listCustomer);
        tv_customers.setItems(this.listCustomer);

        but_add.setOnAction(e -> showAdd());
        but_update.setOnAction(e -> showUpdate());

        tf_name.textProperty().addListener((observable, oldValue, newValue) -> {
            filter();
        });
        tf_telephone.textProperty().addListener((observable, oldValue, newValue) -> {
            filter();
        });
        TableView.TableViewSelectionModel<Customer> selectionRow = tv_customers.getSelectionModel();
        selectionRow.selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null)
                curCustomer = newValue;
        });
    }

    private void showUpdate() {
        FXMLLoader newLoader = new FXMLLoader();
        newLoader.setLocation(getClass().getResource("/sample/views/add-customer-view.fxml"));
        try {
            newLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ((AddCustomerController) newLoader.getController()).setParentController(this);
        ((AddCustomerController) newLoader.getController()).setUpdate(curCustomer);
        Parent root = newLoader.getRoot();
        Stage stage = new Stage();
        stage.setTitle("Добавление клиента");
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }

    private void showAdd() {
        FXMLLoader newLoader = new FXMLLoader();
        newLoader.setLocation(getClass().getResource("/sample/views/add-customer-view.fxml"));
        try {
            newLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ((AddCustomerController) newLoader.getController()).setParentController(this);
        Parent root = newLoader.getRoot();
        Stage stage = new Stage();
        stage.setTitle("Добавление клиента");
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }

    private void filter() {
        Predicate<Customer> predicateName = customer -> customer.getName().toLowerCase().contains(tf_name.getText().trim().toLowerCase());
        Predicate<Customer> predicatePhone = customer -> customer.getTelephone().toLowerCase().contains(tf_telephone.getPlainText().trim().toLowerCase());

        Stream<Customer> stream = listCustomer.stream();

        if (!tf_name.getText().isEmpty()) stream = stream.filter(predicateName);
        if (!tf_telephone.getPlainText().isEmpty()) stream = stream.filter(predicatePhone);

        tv_customers.setItems(FXCollections.observableArrayList(stream.collect(Collectors.toList())));
    }

    private void createColumnServices() {
        tv_customers.getColumns().clear();

        TableColumn<Customer, String> idColumn = new TableColumn<>("Код");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Customer, String> nameColumn = new TableColumn<>("Имя");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Customer, String> telephoneColumn = new TableColumn<>("Номер телефона");
        telephoneColumn.setCellValueFactory(new PropertyValueFactory<>("telephone"));

        tv_customers.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tv_customers.getColumns().addAll(idColumn, nameColumn, telephoneColumn );
    }

    public void updateTable() {
        ArrayList<Customer> customers = database.getListCustomers();
        listCustomer.clear();
        listCustomer = FXCollections.observableArrayList(customers);
        tv_customers.setItems(listCustomer);
    }
}
