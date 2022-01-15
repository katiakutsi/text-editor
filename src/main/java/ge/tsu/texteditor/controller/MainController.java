package ge.tsu.texteditor.texteditor.controller;

import ge.tsu.texteditor.texteditor.db.model.OpenedFile;
import ge.tsu.texteditor.texteditor.db.repository.OpenedFileRepository;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Slf4j
public class MainController implements Initializable {

    private OpenedFileRepository openedFileRepository = new OpenedFileRepository();

    private Stage stage;

    private final FileChooser fileOpenChooser;
    private final DirectoryChooser directoryChooser;

    private Path chosenFile = null;
    private List<File> selectedFiles = new ArrayList<>();
    private HashMap<File, Tab> openedTabs = new HashMap<>();

    public MainController() {
        fileOpenChooser = new FileChooser();
        directoryChooser = new DirectoryChooser();
    }

    @FXML
    private TreeView treeView;

    @FXML
    private MenuBar menuBar;

    @FXML
    private TabPane tabPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        menuBar.useSystemMenuBarProperty().set(true);
        treeView.setShowRoot(false);
        TabPane.TabClosingPolicy tabClosingPolicy = TabPane.TabClosingPolicy.SELECTED_TAB;
        tabPane.setTabClosingPolicy(tabClosingPolicy);
    }

    public void selectItem(MouseEvent mouseEvent) {
        TreeItem<String> selectedItem = (TreeItem<String>) treeView.getSelectionModel().getSelectedItem();

        Path selectedItemPath = Path.of(getFullPathForSelectedTreeItem(selectedItem));
        File selectedFile = selectedItemPath.toFile();

        if (selectedItem != null) {
            try {
                SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();

                if (!selectedFiles.contains(selectedFile)) {
                    Tab tab = new Tab(selectedItem.getValue());

                    if (getMimeType(selectedFile).equals("image")) {
                        ImageView imageView = new ImageView();
                        imageView.setFitWidth(tabPane.getWidth());
                        imageView.setPreserveRatio(true);
                        Image image = new Image(selectedFile.toURI().toString());
                        imageView.setImage(image);
                        tab.setContent(imageView);
                    } else {
                        TextArea textArea = new TextArea();
                        textArea.setText(Files.readString(selectedFile.toPath()));
                        tab.setContent(textArea);
                    }

                    tabPane.getTabs().add(tab);
                    selectionModel.select(tab);

                    openedTabs.put(selectedFile, tab);

                    tab.setOnCloseRequest(new EventHandler<Event>() {
                        @Override
                        public void handle(Event arg0) {
                            selectedFiles.remove(getFullPathForSelectedTreeItem(selectedItem));
                        }
                    });

                    tab.setOnSelectionChanged(new EventHandler<Event>() {
                        @Override
                        public void handle(Event event) {
//                            treeView.setSelectionModel();
                        }
                    });

                    selectedFiles.add(selectedFile);
                    log.info("Opened file: " + selectedFile.getName());
                    dataBaseStuff(selectedFile);
                } else {
                    for (Tab tab : tabPane.getTabs()) {
                        if (tab.getText().equals(selectedFile.getName())) {
                            selectionModel.select(tab);
                        }
                    }
                }
            } catch (IOException e) {
                log.error("Can't read file: " + selectedFile.getName());
            }
        }
    }

    public void open(ActionEvent actionEvent) {
        File selectedFolder = directoryChooser.showDialog(stage);
        if (selectedFolder != null) {
            chosenFile = selectedFolder.toPath();
            displayFileTree(selectedFolder);
        }
    }

    @SneakyThrows
    public void save(ActionEvent actionEvent) {

        for(Map.Entry<File, Tab> entry: openedTabs.entrySet()) {
            File file = entry.getKey();
            Tab tab = entry.getValue();

            TextArea textArea = (TextArea) tab.getContent();
            Files.writeString(file.toPath(), textArea.getText());
        }
    }

    private void displayFileTree(File folder) {
        TreeItem<String> rootItem = new TreeItem<>(folder.getName());

        List<File> fileList = List.of(folder.listFiles());

        for(File file: fileList) {
            makeFileTree(file, rootItem);
        }

        treeView.setRoot(rootItem);
    }

    private void makeFileTree(File file, TreeItem parent) {
        if (!file.getName().equals(".DS_Store")) {
            if (file.isDirectory()) {
                TreeItem<String> treeItem = new TreeItem<>(file.getName());
                parent.getChildren().add(treeItem);
                for (File fileEntry : file.listFiles()) {
                    makeFileTree(fileEntry, treeItem);
                }
            } else {
                parent.getChildren().add(new TreeItem<>(file.getName()));
            }
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private String getMimeType(File file) {
        String mimetype = new MimetypesFileTypeMap().getContentType(file);
        return mimetype.split("/")[0];
    }

    private String getFullPathForSelectedTreeItem(TreeItem item) {
        StringBuilder pathBuilder = new StringBuilder();
        for (item = (TreeItem<String>) treeView.getSelectionModel().getSelectedItem();
             item != null;
             item = item.getParent()) {

            pathBuilder.insert(0, item.getValue());
            pathBuilder.insert(0, "/");
        }
        String path = pathBuilder.toString();
        return chosenFile.getParent() + path;
    }

    @SneakyThrows
    private void dataBaseStuff(File file) {
        openedFileRepository.createTable();
        openedFileRepository.save(new OpenedFile(file.getName()));
    }

}