package sample.controllers.admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import sample.database.Database;
import sample.tables.Position;
import sample.tables.TypeOfService;

import java.util.ArrayList;
import java.util.Arrays;

public class AddTypeOfServiceController {
    @FXML
    private TextField tf_name;
    @FXML
    private TextField tf_time;
    @FXML
    private TextField tf_cost;
    @FXML
    private ChoiceBox<Position> cb_position;
    @FXML
    private Button but_add;
    @FXML
    private Button but_cancel;

    private Database database = new Database();
    private AdminTypeOfServiceController parentController;
    private ObservableList<Position> listPosition = FXCollections.observableArrayList();
    private int globalId;

    @FXML
    void initialize() {
        this.listPosition.addAll(Position.Hair, Position.Manicure);
        cb_position.setItems(this.listPosition);

        but_add.setOnAction(e -> addTypeOfService());
        but_cancel.setOnAction(e -> close());
    }

    //обработка кнопки добавить
    private void addTypeOfService() {
        if (!isAllDate()) {
            showError();
            return;
        }

        TypeOfService tos = new TypeOfService(
                tf_name.getText().trim(),
                tf_time.getText().trim(),
                tf_cost.getText().trim(),
                cb_position.getValue().toString()
        );

        database.addTypeOfService(tos);
        close();
    }

    public void setParentController(AdminTypeOfServiceController parentController) {
        this.parentController = parentController;
    }

    private boolean isAllDate(){
        if (tf_name.getText().trim().isEmpty()) return false;
        try {
            int cost = Integer.parseInt(tf_cost.getText().trim());
            int time = Integer.parseInt(tf_time.getText().trim());
        } catch (Exception ex) {
            return false;
        }
        return cb_position.getValue() != null;
    }

    private void showError(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText("Проверьте введенные данные");
        alert.showAndWait();
    }

    private void close(){
        parentController.updateTable();
        but_add.getScene().getWindow().hide();
    }

    public void setUpdate(TypeOfService typeOfService) {
        globalId = typeOfService.getId();
        tf_name.setText(typeOfService.getName());
        tf_cost.setText(typeOfService.getPriceStr());
        tf_time.setText(typeOfService.getTime());
        int index = 0;
        for (int i = 0; i < Position.values().length; i++) {
            if (listPosition.get(i).toString().equals(typeOfService.getPosition())) index = i;
        }

        cb_position.setValue(listPosition.get(index));

        but_add.setText("Сохранить");
        but_add.setOnAction(e -> update());
    }

    private void update() {
        if (!isAllDate()) {
            showError();
            return;
        }

        TypeOfService tos = new TypeOfService(
                globalId,
                tf_name.getText().trim(),
                tf_time.getText().trim(),
                tf_cost.getText().trim(),
                cb_position.getValue().toString()
        );

        database.updateTypeOfService(tos);
        close();
    }
}
