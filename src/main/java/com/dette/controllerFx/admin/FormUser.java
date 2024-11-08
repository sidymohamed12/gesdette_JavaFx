package com.dette.controllerFx.admin;

import com.dette.App;
import com.dette.entities.User;
import com.dette.enums.Role;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class FormUser extends AdminController {
    @FXML
    private TextField loginField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ComboBox<String> roleComboBox;

    @FXML
    private Button submitButton;

    @FXML
    public void initialize() {

        roleComboBox.getItems().addAll("admin", "boutiquier");
        submitButton.setOnAction(event -> handlesubmitButtonAction(event));

    }

    @FXML
    private void handlesubmitButtonAction(ActionEvent event) {

        String email = loginField.getText();
        String password = passwordField.getText();
        String role = roleComboBox.getValue();

        if (email.isEmpty() || password.isEmpty() || role == null) {
            showAlert(AlertType.ERROR, "Form Error!", "Please fill all the fields");
            return;
        }

        // Créer un nouvel objet User
        User newUser = new User(email, password, Role.getRole(role), true);
        try {
            userService.create(newUser);
            showAlert(AlertType.INFORMATION, "Success", "User registered successfully!");
            App.setRoot("listeUser");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Database Error", "Error while saving user: " + e.getMessage());
        }
    }
}
