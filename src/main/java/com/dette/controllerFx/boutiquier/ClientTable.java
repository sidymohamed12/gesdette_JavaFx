package com.dette.controllerFx.boutiquier;

import java.util.List;

import com.dette.entities.Client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;

public class ClientTable extends BoutiquierController {
    @FXML
    private TableView<Client> clientTable;
    @FXML
    private TableColumn<Client, Integer> idColumn;
    @FXML
    private TableColumn<Client, String> surnomColumn;
    @FXML
    private TableColumn<Client, String> telephoneColumn;
    @FXML
    private TableColumn<Client, String> adresseColumn;
    private ObservableList<Client> clientList;

    @FXML
    private ComboBox<String> filterClient;

    @FXML
    private TextField searchField;
    @FXML
    private Button searchButton;

    private List<Client> clients = clientService.findAll();

    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        surnomColumn.setCellValueFactory(new PropertyValueFactory<>("surnom"));
        telephoneColumn.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        adresseColumn.setCellValueFactory(new PropertyValueFactory<>("adresse"));

        listeClient();

        filterClient.getItems().addAll("all", "sans compte", "avec compte");
        filterClient.getSelectionModel().select("all");
        filterClient.setOnAction(event -> getClientByHavingUser(event));
        searchButton.setOnAction(event -> clientSearch(event));

    }

    public void listeClient() {
        clientList = FXCollections.observableArrayList(clients);
        clientTable.setItems(clientList);
    }

    private void clientSearch(ActionEvent event) {
        String recherche = searchField.getText();
        isEmpty(recherche, "CHAMP VIDE", "veuiller saisir le numero du client");

        try {
            Client client = clientService.getBy(recherche);

            if (client == null) {
                showAlert(AlertType.ERROR, "RECHERCHE", "aucun client trouvé avec ce numero");
                searchField.clear();
                return;
            } else {
                clientList = FXCollections.observableArrayList(client);
                clientTable.setItems(clientList);

            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Database Error", "Client nnot found" + e.getMessage());
        }

    }

    private void getClientByHavingUser(ActionEvent event) {
        String select = filterClient.getSelectionModel().getSelectedItem();
        ObservableList<Client> filtered = FXCollections.observableArrayList();
        for (Client client : clients) {
            if ("sans compte".equals(select) && client.getUser() == null) {
                filtered.add(client);
            } else if ("avec compte".equals(select) && client.getUser() != null) {
                filtered.add(client);
            } else if ("all".equals(select)) {
                filtered.add(client);
            }
        }
        clientTable.setItems(filtered);
    }

}
