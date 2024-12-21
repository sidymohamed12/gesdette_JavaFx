package com.dette.controllerFx.boutiquier;

import com.dette.App;
import com.dette.entities.Client;
import com.dette.entities.User;
import com.dette.enums.Role;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class FormClient extends BoutiquierController {
    @FXML
    private TextField loginField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField adresseField;
    @FXML
    private TextField telField;
    @FXML
    private TextField surnomField;
    @FXML
    private CheckBox checkBoxUser;
    @FXML
    private Button submitButton;

    public void initialize() {
        checkBoxUser.selectedProperty().addListener((observable, oldValue, newValue) -> {
            loginField.setDisable(!newValue);
            passwordField.setDisable(!newValue);
        });

        submitButton.setOnAction(event -> saveClient(event));
    }

    private void saveClient(ActionEvent event) {
        String adresse = adresseField.getText();
        String tel = telField.getText();
        String surnom = surnomField.getText();
        Boolean select = checkBoxUser.isSelected();

        isEmpty(adresse, "ERREUR SUBMIT", "Veuillez saisir le champ Adresse");
        isEmpty(tel, "ERREUR SUBMIT", "Veuillez saisir le champ Telephone");
        isEmpty(surnom, "ERREUR SUBMIT", "Veuillez saisir le champ Surnom");

        if (!select) {
            try {
                Client client = new Client(adresse, tel, surnom, null);
                clientService.create(client);
                App.setRoot("boutiquierVue/listeClient");

            } catch (Exception e) {
                e.printStackTrace();
                showAlert(AlertType.ERROR, "Database Error", "erreur de creation du client: " + e.getMessage());
            }

        } else {
            loginField.setDisable(false);
            passwordField.setDisable(false);
            String login = loginField.getText();
            String password = passwordField.getText();

            isEmpty(login, "ERREUR SUBMIT", "Veuillez saisir le champ Login");
            isEmpty(password, "ERREUR SUBMIT", "Veuillez saisir le champ Password");

            try {
                Client client = new Client(surnom, tel, adresse, null);
                clientService.create(client);
                User user = new User(login, password, Role.client, true, client); // si ça marche pas enlever client
                userService.create(user);

                client.setUser(user);
                clientService.modifier(client);
                App.setRoot("boutiquierVue/listeClient");
            } catch (Exception e) {
                e.printStackTrace();
                showAlert(AlertType.ERROR, "Database Error",
                        "erreur de creation du client avec user: " + e.getMessage());
            }
        }
    }
}
