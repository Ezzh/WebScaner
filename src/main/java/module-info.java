module com.example.javawebscannerfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.httpcomponents.httpclient;
    requires org.apache.httpcomponents.httpcore;


    opens com.example.javawebscannerfx to javafx.fxml;
    exports com.example.javawebscannerfx;
}