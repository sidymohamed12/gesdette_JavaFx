package com.dette.controllerFx.boutiquier;

import java.time.LocalDateTime;
import java.util.List;

import com.dette.entities.Dette;
import com.dette.entities.User;
import com.dette.enums.Etat;
import com.dette.enums.Role;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class DetteTable extends BoutiquierController {

    @FXML
    private TableView<Dette> detteTable;
    @FXML
    private TableColumn<Dette, LocalDateTime> dateColumn;
    @FXML
    private TableColumn<Dette, Double> montantColumn;
    @FXML
    private TableColumn<Dette, Double> montantVerserColumn;
    @FXML
    private TableColumn<Dette, Double> montantRestantColumn;
    @FXML
    private TableColumn<Dette, String> clientColumn;
    @FXML
    private ComboBox<String> selectFiltre;
    @FXML
    private ComboBox<String> selectFiltre2;

    private List<Dette> dettes = detteService.findAll();
    private ObservableList<Dette> detteList;
    private ObservableList<Dette> filtreDette;

    public void initialize() {
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        montantColumn.setCellValueFactory(new PropertyValueFactory<>("montant"));
        montantVerserColumn.setCellValueFactory(new PropertyValueFactory<>("montantVerser"));
        montantRestantColumn.setCellValueFactory(new PropertyValueFactory<>("montantRestant"));
        clientColumn.setCellValueFactory(data -> {
            Dette dette = data.getValue();
            return new SimpleStringProperty(dette.getClientD().getSurnom());
        });

        listeDette();

        selectFiltre.getItems().addAll("all", "solder", "non-solder");
        selectFiltre.getSelectionModel().select("all");
        selectFiltre.setOnAction(event -> updateDetteTable(event));

        selectFiltre2.getItems().addAll("all", "encours", "annuler");
        selectFiltre2.getSelectionModel().select("all");
        selectFiltre2.setOnAction(event -> updateDetteTable(event));
    }

    private void listeDette() {
        detteList = FXCollections.observableArrayList(dettes);
        detteTable.setItems(detteList);
    }

    private ObservableList<Dette> filterDetteByEtat2(ObservableList<Dette> dettes, Etat etat) {
        if (etat == null) {
            return dettes;
        }

        ObservableList<Dette> filtered = FXCollections.observableArrayList();
        for (Dette dette : dettes) {
            if (dette.getEtatD() == etat) {
                filtered.add(dette);
            }
        }
        return filtered;
    }

    private ObservableList<Dette> filterDetteByEtat(ObservableList<Dette> dettes, String etat) {
        ObservableList<Dette> filtered = FXCollections.observableArrayList();
        for (Dette dette : dettes) {
            if ("solder".equals(etat) && dette.getMontant() == dette.getMontantVerser()) {
                filtered.add(dette);
            } else if ("non-solder".equals(etat) && dette.getMontant() != dette.getMontantVerser()) {
                filtered.add(dette);
            } else if ("all".equals(etat)) {
                filtered.add(dette);
            }
        }
        return filtered;
    }

    private void updateDetteTable(ActionEvent event) {

        String selectedEtat1 = selectFiltre.getSelectionModel().getSelectedItem();
        String selectedEtat2 = selectFiltre2.getSelectionModel().getSelectedItem();

        Etat etat = Etat.getEtat(selectedEtat2);
        filtreDette = filterDetteByEtat(detteList, selectedEtat1);

        filtreDette = filterDetteByEtat2(filtreDette, etat);

        detteTable.setItems(filtreDette);
    }

}

// private void filtrerDette(ActionEvent event) {
// String selected = selectFiltre.getSelectionModel().getSelectedItem();
// ObservableList<Dette> detteFiltres = FXCollections.observableArrayList();
// for (Dette dette : dettes) {
// if ("solder".equals(selected) && dette.getMontant() ==
// dette.getMontantVerser()) {
// detteFiltres.add(dette);
// } else if ("non-solder".equals(selected) && dette.getMontant() !=
// dette.getMontantVerser()) {
// detteFiltres.add(dette);
// } else if ("all".equals(selected)) {
// detteFiltres.add(dette);
// }
// }
// detteTable.setItems(detteFiltres);
// }

// private void filtrerDette2(ActionEvent event) {
// String selected = selectFiltre2.getSelectionModel().getSelectedItem();
// ObservableList<Dette> detteFiltres = FXCollections.observableArrayList();
// for (Dette dette : dettes) {
// if ("encours".equals(selected) && dette.getEtatD() == Etat.encours) {
// detteFiltres.add(dette);
// } else if ("annuler".equals(selected) && dette.getEtatD() == Etat.annuler) {
// detteFiltres.add(dette);
// } else if ("all".equals(selected)) {
// detteFiltres.add(dette);
// }
// }
// detteTable.setItems(detteFiltres);
// }