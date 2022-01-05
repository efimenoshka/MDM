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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sample.database.Database;
import sample.tables.TypeOfService;

import java.io.IOException;
import java.util.ArrayList;

public class AdminTypeOfServiceController {
    @FXML
    private TableView<TypeOfService> tv_type_of_service;
    @FXML
    private Button but_add;
    @FXML
    private Button but_update;

    private Database database = new Database();
    private ObservableList<TypeOfService> listTypeOfService = FXCollections.observableArrayList();
    private TypeOfService curTypeOfService;

    @FXML
    void initialize() {
        createColumnServices();
        ArrayList<TypeOfService> chequeList = database.getListTypeOfService();
        listTypeOfService.clear();
        listTypeOfService = FXCollections.observableArrayList(chequeList);
        tv_type_of_service.setItems(listTypeOfService);

        TableView.TableViewSelectionModel<TypeOfService> selectionRow = tv_type_of_service.getSelectionModel();
        selectionRow.selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null)
                curTypeOfService = newValue;
        });

        but_add.setOnAction(e -> showAddService());
        but_update.setOnAction(e -> showUpdateService());
    }

    private void showUpdateService() {
        FXMLLoader newLoader = new FXMLLoader();
        newLoader.setLocation(getClass().getResource("/sample/views/add-type-of-service-view.fxml"));
        try {
            newLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ((AddTypeOfServiceController) newLoader.getController()).setParentController(this);
        ((AddTypeOfServiceController) newLoader.getController()).setUpdate(curTypeOfService);
        Parent root = newLoader.getRoot();
        Stage stage = new Stage();
        stage.setTitle("Изменение услуги");
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }

    private void showAddService() {
        FXMLLoader newLoader = new FXMLLoader();
        newLoader.setLocation(getClass().getResource("/sample/views/add-type-of-service-view.fxml"));
        try {
            newLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ((AddTypeOfServiceController) newLoader.getController()).setParentController(this);
        Parent root = newLoader.getRoot();
        Stage stage = new Stage();
        stage.setTitle("Добавление услуги");
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }

    private void createColumnServices() {
        tv_type_of_service.getColumns().clear();

        TableColumn<TypeOfService, String> idColumn = new TableColumn<>("Услуга");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<TypeOfService, String> timeColumn = new TableColumn<>("Длительность, мин");
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));

        TableColumn<TypeOfService, String> priceColumn = new TableColumn<>("Цена");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("priceStr"));

        TableColumn<TypeOfService, String> positionColumn = new TableColumn<>("Должность");
        positionColumn.setCellValueFactory(new PropertyValueFactory<>("position"));

        tv_type_of_service.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tv_type_of_service.getColumns().addAll(idColumn, timeColumn, priceColumn, positionColumn);
    }

    public void updateTable() {
        ArrayList<TypeOfService> cheques = database.getListTypeOfService();
        listTypeOfService.clear();
        listTypeOfService = FXCollections.observableArrayList(cheques);
        tv_type_of_service.setItems(listTypeOfService);
    }

}
