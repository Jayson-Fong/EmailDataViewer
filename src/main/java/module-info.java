module EmailDataViewer {
    requires javafx.base;
    requires javafx.graphics;
    requires javafx.controls;
    requires javax.mail;
    opens org.jaysonfong.emaildataviewer to javafx.graphics;
    exports org.jaysonfong.emaildataviewer;
}