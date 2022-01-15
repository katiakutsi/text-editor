module ge.tsu.texteditor {
    requires javafx.controls;
    requires javafx.fxml;
    requires lombok;
    requires org.apache.commons.io;
    requires activation;
    requires mime.types;
    requires slf4j.api;
    requires com.h2database;
    requires java.sql;
    requires java.naming;
    requires javafx.media;

    opens ge.tsu.texteditor to javafx.fxml;
    opens ge.tsu.texteditor.controller to javafx.fxml;

    exports ge.tsu.texteditor;
    exports ge.tsu.texteditor.controller;
}