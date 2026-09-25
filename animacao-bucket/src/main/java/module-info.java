module com.bucket.animacaobucket {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.bucket.animacaobucket to javafx.fxml;
    exports com.bucket.animacaobucket;
}