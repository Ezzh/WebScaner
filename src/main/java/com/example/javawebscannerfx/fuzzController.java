package com.example.javawebscannerfx;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

public class fuzzController {
    ObservableList<String> outputLog1 = FXCollections.observableArrayList();

    @FXML
    private Button fuzzBack;

    @FXML
    private TextArea fuzzFirstOutput;

    @FXML
    private TextField fuzzMode;

    @FXML
    private Button fuzzScan;

    @FXML
    private TextArea fuzzSecondOutput;

    @FXML
    private TextField fuzzUrl;

    @FXML
    private TextField fuzzWordlist;

    @FXML
    public void backStepButton(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Controller controller = fxmlLoader.getController();
        //controller.initGroupData(groupData);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }


    @FXML
    void initialize(){
        fuzzScan.setOnAction(event -> {
            Fuzz fuzz = new Fuzz();
            try {
                fuzzFirstOutput.setText(fuzz.settings(fuzzUrl.getText(), fuzzWordlist.getText(), fuzzMode.getText()));
            } catch (Exception e) {
                fuzzFirstOutput.setText(e.toString());
            }

            if (fuzzMode.getText().equals("dir")) {
                try {
                    dirScan(fuzz.Url, fuzz.reader);
                } catch (Exception e) {
                    fuzzFirstOutput.setText(e.toString());
                }
            } else if (fuzzMode.getText().equals("subdomain")) {
                try {
                    subdomainScan(fuzz.Url, fuzz.reader);
                } catch (Exception e) {
                    fuzzFirstOutput.setText(e.toString());
                }
            }

        });
        /*outputLog1.addListener((ListChangeListener<String>) c -> {
            c.next();
            if (c.wasAdded()){
                fuzzFirstOutput.appendText(c.getList().get(c.getFrom()));
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });*/
        /*fuzzBack.setOnAction(event -> {
            fuzzBack.getScene().getWindow().hide();

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
        });*/
    }

    public void dirScan(String Url, BufferedReader reader) throws IOException {
        //---------Getting url and path to wordlist---------

        HttpRequest request = new HttpRequest(Url);

        String output1 = "404 NOT FOUND:\n";
        String output2 = "FOUND:\n";
        fuzzFirstOutput.setText(output1);
        fuzzSecondOutput.setText(output2);
        String line = "";
        while(!((line = reader.readLine()) == null)){
            request.directory = line;
            request.dir();
            if (!request.response.contains("404")){
                output2 = request.finalUrl + "\n";
                fuzzSecondOutput.appendText(output2);
                System.out.println("-----------------" + request.finalUrl);
            }else {
                output1 = request.finalUrl + "\n";
                System.out.println(request.finalUrl);
                fuzzFirstOutput.appendText(output1);
            }
        }
    }

    void subdomainScan(String Url, BufferedReader reader) throws IOException {
        HttpRequest request = new HttpRequest(Url);

        String output1 = "";
        String output2 = "";
        String line = "";
        while(!((line = reader.readLine()) == null)){
            request.domain = line;
            try {
                request.sub();
            } catch (UnknownHostException e) {
                System.out.println("** server can't find " + line + "." + Url + ": NXDOMAIN");
                output1 =  request.finalUrl + "\n";
                System.out.println(request.finalUrl);
                fuzzFirstOutput.appendText(output1);
                continue;
            }

            if (request.response == null){
                output1 =  request.finalUrl + "\n";
                System.out.println(request.finalUrl);
                fuzzFirstOutput.appendText(output1);
                continue;
            }

            if (!request.response.contains("404")){
                output2 = output2 + request.finalUrl + "\n";
                fuzzSecondOutput.setText(output2);
                System.out.println("-----------------" + request.finalUrl);
            }else {
                output1 =  request.finalUrl + "\n";
                System.out.println(request.finalUrl);
                fuzzFirstOutput.appendText(output1);
            }
        }
    }

}
