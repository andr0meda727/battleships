module battleships {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens battleships to javafx.fxml;
    opens battleships.models to javafx.fxml;
    opens battleships.controllers to javafx.fxml;

    exports battleships;
    exports battleships.models;
    exports battleships.controllers;
    exports battleships.enums;
    opens battleships.enums to javafx.fxml;
    exports battleships.models.bots;
    opens battleships.models.bots to javafx.fxml;
}
