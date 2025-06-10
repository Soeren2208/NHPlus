package de.hitec.nhplus.controller;

import de.hitec.nhplus.datastorage.CaregiverDao;
import de.hitec.nhplus.datastorage.DaoFactory;
import de.hitec.nhplus.model.Caregiver;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

import java.sql.SQLException;
import java.time.LocalDate;

public class AllCaregiverController {
    @FXML
    private TableView<Caregiver> tableView;
    @FXML
    private TableColumn<Caregiver, Integer> columnCId;
    @FXML
    private TableColumn<Caregiver, String> columnFirstName;
    @FXML
    private TableColumn<Caregiver, String> columnSurName;
    @FXML
    private TableColumn<Caregiver, String> columnPhoneNumber;
    @FXML
    private TableColumn<Caregiver, String> columnStatus;
    @FXML
    private TextField textFieldFirstName;
    @FXML
    private TextField textFieldSurName;
    @FXML
    private TextField textFieldPhoneNumber;
    @FXML
    private Button buttonDelete;
    @FXML
    private Button buttonAdd;
    @FXML
    private ComboBox<String> filterBox;

    private final ObservableList<Caregiver> caregivers = FXCollections.observableArrayList();
    private CaregiverDao dao;

    public void initialize() {
        this.readAllAndShowInTableView();

        this.columnCId.setCellValueFactory(new PropertyValueFactory<>("cid"));

        this.columnFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        this.columnFirstName.setCellFactory(TextFieldTableCell.forTableColumn());

        this.columnSurName.setCellValueFactory(new PropertyValueFactory<>("surname"));
        this.columnSurName.setCellFactory(TextFieldTableCell.forTableColumn());

        this.columnPhoneNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        this.columnPhoneNumber.setCellFactory(TextFieldTableCell.forTableColumn());

        this.columnStatus.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getStatus())
        );
        filterBox.getItems().addAll("Alle", "Nur aktive", "Nur inaktive");
        filterBox.setValue("Nur aktive");
        filterBox.setOnAction(e -> readAllAndShowInTableView());
        this.tableView.setItems(this.caregivers);

        this.buttonDelete.setDisable(true);
        this.tableView.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, oldCaregiver,
                 newCaregiver) -> AllCaregiverController.this.buttonDelete.setDisable(newCaregiver == null)
        );
        textFieldPhoneNumber.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("[0-9+()\\-/\\s]*")) {
                return change;
            } else {
                return null;
            }
        }));
        readAllAndShowInTableView();

        this.buttonAdd.setDisable(true);
        textFieldFirstName.textProperty().addListener((observable, oldValue, newValue) -> checkAddButtonState());
        textFieldSurName.textProperty().addListener((observable, oldValue, newValue) -> checkAddButtonState());
        textFieldPhoneNumber.textProperty().addListener((observable, oldValue, newValue) -> checkAddButtonState());
    }

    private void checkAddButtonState() {
        boolean disable = textFieldFirstName.getText().trim().isEmpty()
                || textFieldSurName.getText().trim().isEmpty()
                || textFieldPhoneNumber.getText().trim().isEmpty();
        buttonAdd.setDisable(disable);
    }

    @FXML
    public void handleOnEditFirstname(TableColumn.CellEditEvent<Caregiver, String> event) {
        event.getRowValue().setFirstName(event.getNewValue());
        this.doUpdate(event);
    }

    @FXML
    public void handleOnEditSurname(TableColumn.CellEditEvent<Caregiver, String> event) {
        event.getRowValue().setSurname(event.getNewValue());
        this.doUpdate(event);
    }

    @FXML
    public void handleOnEditPhonenumber(TableColumn.CellEditEvent<Caregiver, String> event) {
        event.getRowValue().setPhoneNumber(event.getNewValue());
        this.doUpdate(event);
    }

    private void doUpdate(TableColumn.CellEditEvent<Caregiver, String> event) {
        try {
            this.dao.update(event.getRowValue());
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @FXML
    public void handleAdd() {
        String firstName = this.textFieldFirstName.getText();
        String surName = this.textFieldSurName.getText();
        String phoneNumber = this.textFieldPhoneNumber.getText();
        try {
            this.dao.create(new Caregiver(firstName, surName, phoneNumber, null));
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        readAllAndShowInTableView();
        clearTextfield();
    }

    private void readAllAndShowInTableView() {
        this.caregivers.clear();
        this.dao = DaoFactory.getDaoFactory().createCaregiverDAO();
        try {
            var all = this.dao.readAll();
            String filter = filterBox.getValue();

            if ("Nur aktive".equals(filter)) {
                this.caregivers.addAll(all.stream().filter(Caregiver::isActive).toList());
            } else if ("Nur inaktive".equals(filter)) {
                this.caregivers.addAll(all.stream().filter(c -> !c.isActive()).toList());
            } else {
                this.caregivers.addAll(all);
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    private void clearTextfield() {
        this.textFieldFirstName.clear();
        this.textFieldSurName.clear();
        this.textFieldPhoneNumber.clear();
        checkAddButtonState();
    }

    @FXML
    private void handleDelete() {
        Caregiver selected = tableView.getSelectionModel().getSelectedItem();
        if (selected != null && selected.isActive()) {
            selected.setInactiveSince(LocalDate.now());
        }
        if (selected != null) {
            try {
                dao.update(selected);
                readAllAndShowInTableView();
                showInfo("Pflegekraft als inaktiv markiert: " + selected.getFirstName() + " " + selected.getSurname());
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}
