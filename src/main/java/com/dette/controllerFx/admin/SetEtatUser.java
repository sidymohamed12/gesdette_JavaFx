package com.dette.controllerFx.admin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.dette.App;
import com.dette.entities.User;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class SetEtatUser extends AdminController {

    @FXML
    private CheckBox toggleSwitch;

    @FXML
    private TableView<User> userTable;

    @FXML
    private TableColumn<User, Integer> idColumn;

    @FXML
    private TableColumn<User, String> loginColumn;

    @FXML
    private TableColumn<User, String> roleColumn;

    private ObservableList<User> uList;

    @FXML
    private TextField searchField;

    @FXML
    private Button searchButton;

    @FXML
    private Button submitButton;

    private User user;

    @FXML
    public void initialize() {
        loginColumn.setCellValueFactory(new PropertyValueFactory<>("login"));
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        roleColumn.setCellValueFactory(data -> {
            User user = data.getValue();
            return new SimpleStringProperty(user.getRole().name());
        });

        listeUser();

        toggleSwitch.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            if (isSelected) {
                System.out.println("Toggle is ON");
            } else {
                System.out.println("Toggle is OFF");
            }
        });
        searchButton.setOnAction(event -> userSearch(event));
        submitButton.setOnAction(event -> changeEtat(event));
    }

    public void listeUser() {
        List<User> users = new ArrayList<>();
        for (User user : userService.findAll()) {
            users.add(user);
        }
        uList = FXCollections.observableArrayList(users);
        userTable.setItems(uList);
    }

    public void userSearch(ActionEvent event) {

        String recherche = searchField.getText();
        isEmpty(recherche, "ERREUR RECHERCHE", "veuillez saisir le login du user");

        try {
            user = userService.getBy(recherche);
            if (user != null) {

                ObservableList<User> foundArticle = FXCollections.observableArrayList(user);
                userTable.setItems(foundArticle);
                toggleSwitch.setSelected(user.getEtat());
                showAlert(AlertType.INFORMATION, "Success", "User chargé !");
            } else {
                showAlert(AlertType.ERROR, "Not Found", "Aucun user trouvé avec ce login.");
                userTable.setItems(FXCollections.observableArrayList());
                toggleSwitch.setSelected(false);
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Database Error", "User NOT FOUND " + e.getMessage());
            toggleSwitch.setSelected(false);
        }

    }

    public void changeEtat(ActionEvent event) {

        isNull(user, "ERREUR SUBMIT", "Veuillez d'abord rechercher un utilisateur.");

        boolean etatUser = user.getEtat();
        boolean switchEtat = toggleSwitch.isSelected();

        if (etatUser && switchEtat) {
            showAlert(AlertType.ERROR, "Info", "L'état de l'utilisateur est déjà activé.");
            return;
        }

        if (!etatUser && !switchEtat) {
            showAlert(AlertType.ERROR, "Info", "L'état de l'utilisateur est déjà desactivé.");
            return;
        }

        if (etatUser && !switchEtat) {

            user.setEtat(false);
            userService.modifier(user);
            showAlert(AlertType.INFORMATION, "Success", "L'état de l'utilisateur a été désactivé.");
            try {
                App.setRoot("adminVue/listeUser.admin");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (!etatUser && switchEtat) {
            user.setEtat(true);
            userService.modifier(user);
            showAlert(AlertType.INFORMATION, "Success", "L'état de l'utilisateur a été activé.");
            try {
                App.setRoot("adminVue/listeUser.admin");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        showAlert(AlertType.INFORMATION, "Info", "Aucun changement d'état nécessaire.");
    }
}
