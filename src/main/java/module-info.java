module YTC.Installer {

    requires javafx.controls;
    requires javafx.graphics;
    requires com.google.gson;

    opens de.spc.installer to javafx.fxml;
    exports de.spc.installer;

}