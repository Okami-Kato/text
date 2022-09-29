package com.epam.controller;

import static javafx.scene.control.Alert.AlertType.WARNING;

import java.util.Optional;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.config.TextProperties;
import com.epam.service.DictionaryService;
import com.epam.service.dto.WordDto;

@Component
public class FxController {

    @Autowired
    private DictionaryService service;

    @Autowired
    private TextProperties properties;

    public ObservableList<WordDto> elements = FXCollections.observableArrayList();

    @FXML
    private TableColumn<WordDto, Integer> word;

    @FXML
    private TableColumn<WordDto, Integer> frequency;

    @FXML
    private TableView<WordDto> tableView;

    @FXML
    private TextField searchView;

    @FXML
    void initialize() {
        initTable();

        searchView.textProperty().addListener((observable, oldValue, newValue) -> {
            tableView.setItems(elements.stream()
                .filter(item -> item.getValue().matches("^%s.*".formatted(newValue)))
                .collect(Collectors.toCollection(FXCollections::observableArrayList))
            );
        });
    }

    @FXML
    void addWord() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add new word");
        dialog.setContentText("New word:");
        Optional<String> newWord = dialog.showAndWait();
        newWord.map(String::toLowerCase)
            .ifPresent(word -> {
                try {
                    validateWord(word);
                    WordDto dto = service.save(word);
                    elements.add(dto);
                    tableView.setItems(elements);
                } catch (IllegalArgumentException e) {
                    showAlert(e);
                }
            });
    }

    @FXML
    void editWord() {
        WordDto wordToEdit = tableView.getSelectionModel().getSelectedItem();
        if (wordToEdit != null) {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Edit word");
            dialog.setContentText("New word:");
            Optional<String> newWord = dialog.showAndWait();

            newWord.map(String::toLowerCase)
                .ifPresent(word -> {
                    try {
                        validateWord(word);
                        service.update(wordToEdit.getValue(), word);
                        updateTable();
                    } catch (IllegalArgumentException e) {
                        showAlert(e);
                    }
                });
        }
    }

    @FXML
    void deleteWord() {
        WordDto wordToDelete = tableView.getSelectionModel().getSelectedItem();
        if (wordToDelete != null) {
            Alert alert = new Alert(WARNING);
            alert.setTitle("Delete word");
            alert.setContentText("Are you sure you want to delete word \"%s\"?".formatted(wordToDelete.getValue()));
            alert.setHeaderText(null);
            Optional<ButtonType> buttonType = alert.showAndWait();
            if (buttonType.isPresent() && buttonType.get() == ButtonType.OK) {
                service.delete(wordToDelete.getValue());
                tableView.getItems().remove(wordToDelete);
                alert.close();
            }
        }
    }

    private void updateTable() {
        tableView.getItems().clear();
        elements.addAll(service.findAll());
        tableView.setItems(elements);
    }

    private void validateWord(String word) {
        if (!word.matches("^[\\w]*['-]*[\\w]+|[\\w]+['-]*[\\w]*$")) {
            throw new IllegalArgumentException("Invalid input");
        }
    }

    private void initTable() {
        word.setCellValueFactory(new PropertyValueFactory("value"));
        frequency.setCellValueFactory(new PropertyValueFactory("frequency"));
        updateTable();
    }

    private void showAlert(Exception e) {
        Alert alert = new Alert(WARNING);
        alert.setContentText(e.getMessage());
        alert.setHeaderText(null);
        Optional<ButtonType> buttonType = alert.showAndWait();
        if (buttonType.isPresent() && buttonType.get() == ButtonType.OK) {
            alert.close();
        }
    }
}
