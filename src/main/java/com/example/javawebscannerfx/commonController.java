package com.example.javawebscannerfx;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class commonController {

    @FXML
    private Button commonBack;


    @FXML
    private TextArea commonOutput;

    @FXML
    private Button commonScan;

    @FXML
    private TextField commonUrl;

    @FXML
    void initialize(){
        commonScan.setOnAction(event -> {
            String url = commonUrl.getText();
            CommonScan common = new CommonScan(url);
            try {
                common.commonScan();
                commonOutput.setText(common.OPTIONS + "\n" + common.Headers + "\n" + common.robots + "\n" + common.notFoundPage);
            } catch (IOException e) {
                commonOutput.setText(e.toString());
            }
        });

        commonBack.setOnAction(event -> {
            commonBack.getScene().getWindow().hide();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("hello-view.fxml"));
            try {
                loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        });
    }

}
