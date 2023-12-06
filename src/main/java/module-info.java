module com.crisis.ihm.ihm {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires org.bouncycastle.provider;

    opens com.crisis.ihm to javafx.fxml;
    exports com.crisis.ihm;
}