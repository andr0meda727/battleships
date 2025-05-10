module battleships {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens battleships to javafx.fxml;
    opens battleships.views to javafx.fxml;

    exports battleships;
    exports battleships.views;
    exports battleships.models;
    exports battleships.controllers;
}
