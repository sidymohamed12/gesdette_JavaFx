package com.dette.controllerFx.admin;

import com.dette.enums.Role;

import java.util.ArrayList;
import java.util.List;

import com.dette.App;
import com.dette.entities.Client;
import com.dette.entities.User;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class LinkClientUser extends AdminController {
    @FXML
    private TableView<Client> clientTable;
    @FXML
    private TableColumn<Client, String> surnomColumn;
    @FXML
    private TableColumn<Client, String> telephoneColumn;
    @FXML
    private TableColumn<Client, String> adresseColumn;

    @FXML
    private TextField loginField;
    @FXML
    private TextField passwordField;

    @FXML
    private TextField searchField;
    @FXML
    private Button searchButton;

    @FXML
    private Button submitButton;

    private ObservableList<Client> clList;

    private Client client;

    public void initialize() {
        surnomColumn.setCellValueFactory(new PropertyValueFactory<>("surnom"));
        telephoneColumn.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        adresseColumn.setCellValueFactory(new PropertyValueFactory<>("adresse"));

        listeClient();

        searchButton.setOnAction(event -> clientSearch(event));
        submitButton.setOnAction(event -> createUser(event));
    }

    public void listeClient() {
        List<Client> clients = new ArrayList<>();
        for (Client client : clientService.findAll()) {
            if (client.getUser() == null) {
                clients.add(client);
            }
        }
        clList = FXCollections.observableArrayList(clients);
        clientTable.setItems(clList);
    }

    private void clientSearch(ActionEvent event) {
        String recherche = searchField.getText();

        isEmpty(recherche, "ERREUR RECHERCHE", "veuillez saisir le telephone du client");

        try {
            client = clientService.getBy(recherche);
            System.out.println(client);
            if (client != null) {
                ObservableList<Client> foundClient = FXCollections.observableArrayList(client);
                clientTable.setItems(foundClient);
                showAlert(AlertType.INFORMATION, "Success", "client trouvé !");
            } else {
                showAlert(AlertType.ERROR, "Not Found", "Aucun client trouvé avec ce telephone.");
                clientTable.setItems(clList);
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Database Error", "Client nnot found" + e.getMessage());

        }
    }

    private void createUser(ActionEvent event) {

        isNull(client, "ERREUR SUBMIT", "Veuillez d'abord rechercher un client");
        isNull(client.getUser(), "ERREUR SUBMIT", "Veuillez sélectionner un client qui n'a pas de compte");

        try {
            String login = loginField.getText();
            String password = passwordField.getText();

            isEmpty(login, "ERREUR RECHERCHE", "veuillez remplir tous les champs");
            isEmpty(password, "ERREUR RECHERCHE", "veuillez remplir tous les champs");
            isNull(client, "ERREUR RECHERCHE", "veuillez remplir tous les champs");

            User user = new User(login, password, Role.client, true, client);

            userService.create(user);
            client.setUser(user);
            clientService.modifier(client);
            showAlert(AlertType.INFORMATION, "Success", "compte créé pour le client avec success");
            App.setRoot("adminVue/listeUser.admin");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Database Error", "Error while saving user: " + e.getMessage());
        }
    }

}
