package com.dette;

import java.io.IOException;

import org.mindrot.jbcrypt.BCrypt;

import com.dette.entities.User;
import com.dette.entities.UserConnect;
import com.dette.repository.JPA.ClientJPA;
import com.dette.repository.JPA.UserJPA;
import com.dette.services.UserService;

import javafx.fxml.FXML;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ConnexionController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Hyperlink guestLink;

    private UserService userService;
    ClientJPA clientJPA = new ClientJPA();
    private UserJPA userJPA;

    public ConnexionController() {
        this.userJPA = new UserJPA();
        this.userService = new UserService(userJPA);
    }

    @FXML
    public void initialize() {
    }

    @FXML
    private void handleLoginButtonAction(MouseEvent event) {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs", Alert.AlertType.ERROR);
            return;
        }

        try {
            User user = userService.getBy(email);
            UserConnect.setUserConnecte(user);
            if (user != null && BCrypt.checkpw(password, user.getPassword())) {
                redirectToDashboard(user);
            } else {
                showAlert("Erreur", "Email ou mot de passe incorrect", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            showAlert("Erreur", "Une erreur est survenue lors de la connexion", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void redirectToDashboard(User user) {

        switch (user.getRole()) {
            case admin:
                System.out.println("acces à la vue admin");
                try {
                    App.setRoot("adminVue/listeUser.admin");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case boutiquier:
                System.out.println("acces à la vue boutiquier");

                try {
                    App.setRoot("boutiquierVue/listeClient");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case client:
                System.out.println("acces à la vue client");

                try {
                    App.setRoot("clientVue/listeDette");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

        }

    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void closeApp(MouseEvent event) {
        Stage stage = (Stage) loginButton.getScene().getWindow();
        stage.close();
    }

}
