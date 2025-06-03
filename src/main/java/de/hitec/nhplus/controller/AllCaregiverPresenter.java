package de.hitec.nhplus.controller;

import de.hitec.nhplus.datastorage.CaregiverDao;
import de.hitec.nhplus.datastorage.DaoFactory;
import de.hitec.nhplus.datastorage.PatientDao;
import de.hitec.nhplus.model.Caregiver;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

import java.sql.SQLException;

public class AllCaregiverPresenter {

    @FXML
    private TableView<Caregiver> tableView;

    @FXML
    private TableColumn<Caregiver, Integer> colId;

    @FXML
    private TableColumn<Caregiver, String> colSurname;

    @FXML
    private TableColumn<Caregiver, String> colFirstName;

    @FXML
    private TableColumn<Caregiver, String> colTelephone;

    @FXML
    private TextField txfSurname;

    @FXML
    private TextField txfFirstName;

    @FXML
    private TextField txfTelephone;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnDelete;

    private final ObservableList<Caregiver> caregivers = FXCollections.observableArrayList();
    private CaregiverDao dao;

    /**
     * When <code>initialize()</code> gets called, all fields are already initialized. For example from the FXMLLoader
     * after loading an FXML-File. At this point of the lifecycle of the Controller, the fields can be accessed and
     * configured.
     */
    public void initialize() {
        this.readAllAndShowInTableView();

        this.colId.setCellValueFactory(new PropertyValueFactory<>("cgID"));

        // CellValueFactory to show property values in TableView
        this.colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        // CellFactory to write property values from with in the TableView
        this.colFirstName.setCellFactory(TextFieldTableCell.forTableColumn());

        this.colSurname.setCellValueFactory(new PropertyValueFactory<>("surname"));
        this.colSurname.setCellFactory(TextFieldTableCell.forTableColumn());

        this.colTelephone.setCellValueFactory(new PropertyValueFactory<>("telNumber"));
        this.colTelephone.setCellFactory(TextFieldTableCell.forTableColumn());

        //Anzeigen der Daten
        this.tableView.setItems(this.caregivers);

        this.btnDelete.setDisable(true);
        this.tableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Caregiver>() {

            @Override
            public void changed(ObservableValue<? extends Caregiver> observableValue, Caregiver oldCaregiver, Caregiver newCaregiver) {;
                AllCaregiverPresenter.this.btnDelete.setDisable(newCaregiver == null);
            }
        });

        this.btnAdd.setDisable(true);
        ChangeListener<String> inputNewCaregiverListener = (observableValue, oldText, newText) ->
        this.txfSurname.textProperty().addListener(inputNewCaregiverListener);
        this.txfFirstName.textProperty().addListener(inputNewCaregiverListener);
        this.txfTelephone.textProperty().addListener(inputNewCaregiverListener);
    }

    /**
     * When a cell of the column with first names was changed, this method will be called, to persist the change.
     *
     * @param event Event including the changed object and the change.
     */
    @FXML
    public void handleOnEditFirstname(TableColumn.CellEditEvent<Caregiver, String> event) {
        event.getRowValue().setFirstName(event.getNewValue());
        this.doUpdate(event);
    }

    /**
     * When a cell of the column with surnames was changed, this method will be called, to persist the change.
     *
     * @param event Event including the changed object and the change.
     */
    @FXML
    public void handleOnEditSurname(TableColumn.CellEditEvent<Caregiver, String> event) {
        event.getRowValue().setSurname(event.getNewValue());
        this.doUpdate(event);
    }

    /**
     * When a cell of the column with care levels was changed, this method will be called, to persist the change.
     *
     * @param event Event including the changed object and the change.
     */
    @FXML
    public void handleOnEditTelephone(TableColumn.CellEditEvent<Caregiver, String> event) {
        event.getRowValue().setTelNumber(event.getNewValue());
        this.doUpdate(event);
    }

    /**
     * Updates a patient by calling the method <code>update()</code> of {@link PatientDao}.
     *
     * @param event Event including the changed object and the change.
     */
    private void doUpdate(TableColumn.CellEditEvent<Caregiver, String> event) {
        try {
            this.dao.update(event.getRowValue());
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Reloads all patients to the table by clearing the list of all patients and filling it again by all persisted
     * patients, delivered by {@link PatientDao}.
     */
    private void readAllAndShowInTableView() {
        this.caregivers.clear();
        this.dao = DaoFactory.getDaoFactory().createCaregiverDAO();
        try {
            this.caregivers.addAll(this.dao.readAll());
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * This method handles events fired by the button to delete patients. It calls {@link PatientDao} to delete the
     * patient from the database and removes the object from the list, which is the data source of the
     * <code>TableView</code>.
     */
    @FXML
    public void handleDelete() {
        Caregiver selectedItem = this.tableView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            try {
                DaoFactory.getDaoFactory().createCaregiverDAO().deleteById(selectedItem.getCgID());
                this.tableView.getItems().remove(selectedItem);
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
    }

    /**
     * This method handles the events fired by the button to add a caregiver. It collects the data from the
     * <code>TextField</code>s, creates an object of class <code>Caregiver</code> of it and passes the object to
     * {@link CaregiverDao} to persist the data.
     */
    @FXML
    public void handleAdd() {
        String surname = this.txfSurname.getText();
        String firstName = this.txfFirstName.getText();
        String telephone = this.txfTelephone.getText();
        try {
            this.dao.create(new Caregiver(firstName, surname, telephone));
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        readAllAndShowInTableView();
        clearTextfields();
    }

    /**
     * Clears all contents from all <code>TextField</code>s.
     */
    private void clearTextfields() {
        this.txfFirstName.clear();
        this.txfSurname.clear();
        this.txfTelephone.clear();
    }
}
