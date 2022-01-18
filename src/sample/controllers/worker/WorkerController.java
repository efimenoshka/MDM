package sample.controllers.worker;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import sample.tables.Worker;

import java.io.IOException;

public class WorkerController {
    @FXML
    private Label lb_name;
    @FXML
    private ToggleButton but_current_cheque;
    @FXML
    private ToggleButton but_all_cheque;
    @FXML
    private AnchorPane pane_container;

    private Worker worker;

    @FXML
    void initialize() {
        ToggleGroup group = new ToggleGroup();
        but_current_cheque.setToggleGroup(group);
        but_all_cheque.setToggleGroup(group);
        but_current_cheque.setSelected(true);

        showCurrentCheque();
        but_current_cheque.setOnAction(e -> showCurrentCheque());
        but_all_cheque.setOnAction(e -> showAllCheque());
    }

    private void showCurrentCheque() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/sample/views/worker-current-cheque-view.fxml"));
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        new Thread(() -> Platform.runLater(() -> ((WorkerCurrentChequeController) fxmlLoader.getController()).setWorker(worker))).start();

        AnchorPane child = fxmlLoader.getRoot();
        AnchorPane.setBottomAnchor(child, 0d);
        AnchorPane.setLeftAnchor(child, 0d);
        AnchorPane.setRightAnchor(child, 0d);
        AnchorPane.setTopAnchor(child, 0d);
        pane_container.getChildren().clear();
        pane_container.getChildren().add(child);
    }

    private void showAllCheque() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/sample/views/worker-all-cheque-view.fxml"));
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ((WorkerAllChequeController) fxmlLoader.getController()).setWorker(worker);
        AnchorPane child = fxmlLoader.getRoot();
        AnchorPane.setBottomAnchor(child, 0d);
        AnchorPane.setLeftAnchor(child, 0d);
        AnchorPane.setRightAnchor(child, 0d);
        AnchorPane.setTopAnchor(child, 0d);
        pane_container.getChildren().clear();
        pane_container.getChildren().add(child);
    }

    public void setWorker(Worker worker){
        this.worker = worker;
        lb_name.setText(worker.getFullName().split(" ")[1]);
    }
}
