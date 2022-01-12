package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.controllers.admin.AdminController;
import sample.controllers.worker.WorkerController;
import sample.database.Database;
import sample.tables.Worker;

import java.io.IOException;

public class LoginController {
    @FXML
    private TextField tf_login;

    @FXML
    private PasswordField tf_password;

    @FXML
    private Button but_enter;

    Database database = new Database();

    @FXML
    void initialize(){
        but_enter.setOnAction(this::logIn);
    }

    private void errorWithoutPassword(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("");
        alert.setHeaderText("Ошибка в вводе данных");
        alert.setContentText("Введите корректные данные");
        alert.showAndWait();
    }

    private void logIn(ActionEvent actionEvent) {
        but_enter.getScene().getWindow().hide();
        Worker worker = database.getWorker(tf_login.getText(), tf_password.getText());
        if (worker == null){
            errorWithoutPassword();
        } else {
            String workerPosition = worker.getPosition();
            if ("Администратор".equals(workerPosition)) {
                FXMLLoader newLoader = new FXMLLoader();
                newLoader.setLocation(getClass().getResource("/sample/views/admin-view.fxml"));
                try {
                    newLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Parent root = newLoader.getRoot();
                Stage stage = new Stage();
                stage.setTitle("Администратор");
                stage.setScene(new Scene(root));
                ((AdminController) newLoader.getController()).setWorker(worker);
                stage.showAndWait();
            } else {
                FXMLLoader newLoader = new FXMLLoader();
                newLoader.setLocation(getClass().getResource("/sample/views/worker-view.fxml"));
                try {
                    newLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Parent root = newLoader.getRoot();
                Stage stage = new Stage();
                stage.setTitle("Работник");
                stage.setScene(new Scene(root));
                ((WorkerController) newLoader.getController()).setWorker(worker);
                stage.showAndWait();
            }
        }
    }
}
