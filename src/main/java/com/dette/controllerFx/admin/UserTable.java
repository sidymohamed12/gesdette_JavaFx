package com.dette.controllerFx.admin;

import com.dette.entities.User;
import com.dette.enums.Role;

import java.util.List;
import java.util.ArrayList;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class UserTable extends AdminController {

    @FXML
    private TableView<User> userTable;

    @FXML
    private TableColumn<User, Integer> idColumn;

    @FXML
    private TableColumn<User, String> loginColumn;

    @FXML
    private TableColumn<User, String> roleColumn;

    @FXML
    private ComboBox<String> selectRole;

    @FXML
    private ComboBox<String> selectEtat;

    private ObservableList<User> uList;
    private ObservableList<User> filteredUsers;

    @FXML
    public void initialize() {

        loginColumn.setCellValueFactory(new PropertyValueFactory<>("login"));
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        roleColumn.setCellValueFactory(data -> {
            User user = data.getValue();
            return new SimpleStringProperty(user.getRole().name());
        });

        listeUser();

        selectRole.getItems().addAll("all", "admin", "boutiquier", "client");
        selectRole.getSelectionModel().select("all");
        selectRole.setOnAction(event -> updateUserTable());

        selectEtat.getItems().addAll("all", "activer", "desactiver");
        selectEtat.getSelectionModel().select("all");
        selectEtat.setOnAction(event -> updateUserTable());
    }

    public void listeUser() {
        List<User> users = new ArrayList<>();
        for (User user : userService.findAll()) {
            users.add(user);
        }
        uList = FXCollections.observableArrayList(users);
        userTable.setItems(uList);
    }

    private void updateUserTable() {
        // Obtenir la sélection actuelle
        String selectedRole = selectRole.getSelectionModel().getSelectedItem();
        String selectedEtat = selectEtat.getSelectionModel().getSelectedItem();

        Role role = Role.getRole(selectedRole);
        filteredUsers = filterUsersByRole(uList, role);

        filteredUsers = filterUsersByEtat(filteredUsers, selectedEtat);

        userTable.setItems(filteredUsers);
    }

    private ObservableList<User> filterUsersByRole(ObservableList<User> users, Role role) {
        if (role == null) {
            return users;
        }

        ObservableList<User> filteredUser = FXCollections.observableArrayList();
        for (User user : users) {
            if (user.getRole() == role) {
                filteredUser.add(user);
            }
        }
        return filteredUser;
    }

    private ObservableList<User> filterUsersByEtat(ObservableList<User> users, String etat) {
        ObservableList<User> filtered = FXCollections.observableArrayList();
        for (User user : users) {
            if ("activer".equals(etat) && user.getEtat()) {
                filtered.add(user);
            } else if ("desactiver".equals(etat) && !user.getEtat()) {
                filtered.add(user);
            } else if ("all".equals(etat)) {
                filtered.add(user);
            }
        }
        return filtered;
    }
}
