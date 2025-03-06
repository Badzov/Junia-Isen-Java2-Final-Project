module app {
    requires javafx.controls;
    requires javafx.fxml;
	requires transitive javafx.graphics;
	requires java.sql;
	requires javafx.base;

    opens app to javafx.fxml;
    opens app.view to javafx.fxml;
    opens models to org.assertj.core;
    
    exports app;
}