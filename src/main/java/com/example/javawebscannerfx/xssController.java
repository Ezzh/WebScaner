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

public class xssController {

    @FXML
    private Button xssBack;

    @FXML
    private TextArea xssOutput;

    @FXML
    private Button xssScan;

    @FXML
    private TextField xssUrl;

    @FXML
    void initialize(){
        xssBack.setOnAction(event -> {
            xssBack.getScene().getWindow().hide();

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
            stage.showAndWait();
        });

        xssScan.setOnAction(event -> {
            String url = xssUrl.getText();
            XssScan xssScan = new XssScan(url);
            try {
                xssOutput.setText(xssScan.XssScan());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

}
