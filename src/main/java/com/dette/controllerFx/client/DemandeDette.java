package com.dette.controllerFx.client;

import java.time.LocalDateTime;
import java.util.List;

import com.dette.entities.Dette;
import com.dette.entities.UserConnect;
import com.dette.enums.Etat;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class DemandeDette extends ClientController {
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
    private ComboBox<String> selectFiltre;

    private List<Dette> dettes = detteService.detteOfClient(UserConnect.getUserConnecte().getClient());
    private ObservableList<Dette> detteList;

    public void initialize() {
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        montantColumn.setCellValueFactory(new PropertyValueFactory<>("montant"));
        montantVerserColumn.setCellValueFactory(new PropertyValueFactory<>("montantVerser"));
        montantRestantColumn.setCellValueFactory(new PropertyValueFactory<>("montantRestant"));

        listeDette();

        selectFiltre.getItems().addAll("all", "encours", "annuler");
        selectFiltre.getSelectionModel().select("all");
        selectFiltre.setOnAction(event -> filtrerDette(event));
    }

    private void listeDette() {
        detteList = FXCollections.observableArrayList();
        for (Dette dette : dettes) {
            if (dette.getEtatD() != Etat.accepter) {
                detteList.add(dette);
            }
        }
        detteTable.setItems(detteList);
    }

    private void filtrerDette(ActionEvent event) {
        String selected = selectFiltre.getSelectionModel().getSelectedItem();
        ObservableList<Dette> detteFiltres = FXCollections.observableArrayList();
        for (Dette dette : detteList) {
            if ("encours".equals(selected) && dette.getEtatD() == Etat.encours) {
                detteFiltres.add(dette);
            } else if ("annuler".equals(selected) && dette.getEtatD() == Etat.annuler) {
                detteFiltres.add(dette);
            } else if ("all".equals(selected)) {
                detteFiltres.add(dette);
            }
        }
        detteTable.setItems(detteFiltres);
    }

}
