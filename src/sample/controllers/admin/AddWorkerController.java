package sample.controllers.admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import sample.database.Database;
import sample.tables.Customer;
import sample.tables.Position;
import sample.tables.Worker;

public class AddWorkerController {
    @FXML
    private TextField tf_name;
    @FXML
    private TextField tf_email;
    @FXML
    private TextField tf_phone;
    @FXML
    private ChoiceBox<Position> cb_position;
    @FXML
    private TextField tf_login;
    @FXML
    private TextField tf_password;
    @FXML
    private Button but_add;
    @FXML
    private Button but_cancel;
    private Database database = new Database();
    private AdminWorkerController parentController;
    private int globalId;
    private ObservableList<Position> listPosition = FXCollections.observableArrayList();

    @FXML
    void initialize() {
        but_add.setOnAction(e -> add());
        but_cancel.setOnAction(e -> close());

        listPosition.addAll(Position.Hair, Position.Manicure, Position.Admin);
        cb_position.setItems(listPosition);
    }

    private void add() {
        if (!isAllDate()) {
            showError();
            return;
        }

        Worker worker = new Worker(
                tf_name.getText().trim(),
                tf_phone.getText(),
                tf_email.getText().trim(),
                cb_position.getValue().toString(),
                tf_password.getText().trim(),
                tf_login.getText().trim()
        );

        database.addWorker(worker);
        close();
    }

    public void setParentController(AdminWorkerController parentController) {
        this.parentController = parentController;
    }

    private boolean isAllDate(){
        if (tf_name.getText().trim().isEmpty()) return false;
        if (tf_email.getText().trim().isEmpty()) return false;
        if (cb_position.getValue() == null) return false;
        if (tf_login.getText().trim().isEmpty()) return false;
        if (tf_password.getText().trim().isEmpty()) return false;
        return tf_phone.getText().matches("^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$");
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

    public void setUpdate(Worker worker) {
        globalId = worker.getId();
        tf_name.setText(worker.getFullName());
        tf_phone.setText(worker.getTelephone());
        tf_email.setText(worker.getEmail());
        int index = 0;
        for (int i = 0; i < Position.values().length - 1; i++) {
            if (listPosition.get(i).toString().equals(worker.getPosition())) index = i;
        }
        cb_position.setValue(listPosition.get(index));
        tf_password.setText(worker.getPassword());
        tf_login.setText(worker.getLogin());

        but_add.setText("Сохранить");
        but_add.setOnAction(e -> update());
    }

    private void update() {
        if (!isAllDate()) {
            showError();
            return;
        }

        Worker worker = new Worker(
                globalId,
                tf_name.getText().trim(),
                tf_phone.getText(),
                tf_email.getText().trim(),
                cb_position.getValue().toString(),
                tf_password.getText().trim(),
                tf_login.getText().trim()
        );

        database.updateWorker(worker);

        close();
    }
}
