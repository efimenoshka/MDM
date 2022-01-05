package sample.controllers.admin;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import sample.MaskField;
import sample.database.Database;
import sample.tables.Customer;
import sample.tables.Position;
import sample.tables.TypeOfService;

public class AddCustomerController {
    @FXML
    private TextField tf_name;
    @FXML
    private MaskField tf_phone;
    @FXML
    private Button but_add;
    @FXML
    private Button but_cancel;

    private final Database database = new Database();
    private AdminCustomerController parentController;
    private int globalId;

    @FXML
    void initialize() {
        but_add.setOnAction(e -> add());
        but_cancel.setOnAction(e -> close());
    }

    private void add() {
        if (!isAllDate()) {
            showError();
            return;
        }

        Customer customer = new Customer(
                tf_name.getText().trim(),
                tf_phone.getText()
        );

        database.addCustomer(customer);
        close();
    }

    public void setParentController(AdminCustomerController parentController) {
        this.parentController = parentController;
    }

    private boolean isAllDate(){
        if (tf_name.getText().trim().isEmpty()) return false;
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

    public void setUpdate(Customer customer) {
        globalId = customer.getId();
        tf_name.setText(customer.getName());
        tf_phone.setText(customer.getTelephone());

        but_add.setText("Сохранить");
        but_add.setOnAction(e -> update());
    }

    private void update() {
        if (!isAllDate()) {
            showError();
            return;
        }

        Customer tos = new Customer(
                globalId,
                tf_name.getText().trim(),
                tf_phone.getText()
        );

        database.updateCustomer(tos);
        close();
    }
}
