module com.example.studentinterfacegui {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.studentinterfacegui to javafx.fxml;
    exports com.example.studentinterfacegui;
}