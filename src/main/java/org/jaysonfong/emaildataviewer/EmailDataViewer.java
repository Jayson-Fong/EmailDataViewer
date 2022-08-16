package org.jaysonfong.emaildataviewer;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Optional;

public class EmailDataViewer extends Application {

    private TableView headerTable;
    private ObservableMap<String, String> headersMap = FXCollections.observableHashMap();

    @Override
    public void start(final Stage stage) throws Exception {
        final BorderPane rootNode = new BorderPane();

        // Add MenuBar
        final MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(new Menu("Email Data Viewer [1.0-SNAPSHOT]"));
        rootNode.setTop(menuBar);

        // Add Header Table
        this.headerTable = new TableView();
        rootNode.setCenter(this.headerTable);
        this.headerTable.setEditable(true);

        TableColumn headersColumn = new TableColumn("Headers");

        TableColumn nameColumn = new TableColumn("Name");
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        nameColumn.setCellValueFactory(
                new PropertyValueFactory("name")
        );

        TableColumn valueColumn = new TableColumn("Value");
        valueColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        valueColumn.setCellValueFactory(
                new PropertyValueFactory("value")
        );

        headersColumn.getColumns().addAll(nameColumn, valueColumn);
        this.headerTable.getColumns().add(headersColumn);

        // Add Email File Selection
        final Button fileSelectionButton = new Button("Choose Email File");
        fileSelectionButton.setOnMouseClicked(e -> {
            FileChooser fileChooser = new FileChooser();
            final File emailFile = fileChooser.showOpenDialog(stage);
            final Optional<MimeMessage> mimeMessageOptional = this.getMimeMessageFromFile(emailFile);
            this.headerTable.getItems().clear();
            this.headersMap.clear();
            if (mimeMessageOptional.isPresent()) {
                final MimeMessage mimeMessage = mimeMessageOptional.get();
                try {
                    Enumeration<javax.mail.Header> headers = mimeMessage.getAllHeaders();
                    while (headers.hasMoreElements()) {
                        javax.mail.Header header = headers.nextElement();
                        this.headerTable.getItems().add(header);
                        this.headersMap.put(header.getName(), header.getValue());
                    }
                } catch (MessagingException messagingException) {
                    this.headerTable.getItems().clear();
                    this.headersMap.clear();
                }
            }
        });

        HBox bottomBar = new HBox();
        rootNode.setBottom(fileSelectionButton);

        stage.setScene(new Scene(rootNode, 720, 500));
        stage.setTitle("Email Header Viewer");
        stage.show();
    }

    private final Optional<MimeMessage> getMimeMessageFromFile(final File file) {
        if (file == null) {
            return Optional.empty();
        }

        try {
            final InputStream fileInputStream = new FileInputStream(file);
            final Session mailSession = Session.getDefaultInstance(System.getProperties(), null);
            final MimeMessage mimeMessage = new MimeMessage(mailSession, fileInputStream);
            return Optional.of(mimeMessage);
        } catch (final FileNotFoundException | MessagingException exception) {
            return Optional.empty();
        }
    }

}
