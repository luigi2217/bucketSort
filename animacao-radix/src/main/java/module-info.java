module com.fipp.animacao {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.fipp.animacao.app to javafx.fxml;
    exports com.fipp.animacao.app;
    exports com.fipp.animacao.util;
    exports com.fipp.animacao.view;
}