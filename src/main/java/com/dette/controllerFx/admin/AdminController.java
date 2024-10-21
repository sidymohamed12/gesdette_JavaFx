package com.dette.controllerFx.admin;

import java.io.IOException;

import com.dette.App;
import com.dette.repository.JPA.ArticleJPA;
import com.dette.repository.JPA.ClientJPA;
import com.dette.repository.JPA.UserJPA;
import com.dette.services.ArticleService;
import com.dette.services.ClientService;
import com.dette.services.UserService;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class AdminController {
    private UserJPA userJPA = new UserJPA();
    protected UserService userService = new UserService(userJPA);
    private ArticleJPA articleJPA = new ArticleJPA();
    protected ArticleService articleService = new ArticleService(articleJPA);
    private ClientJPA clientJPA = new ClientJPA();
    protected ClientService clientService = new ClientService(clientJPA);

    public void loadUserList() {
        try {
            App.setRoot("adminVue/listeUser.admin");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadArticleList() {
        try {
            App.setRoot("adminVue/listeArticle");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadAddUser() {
        try {
            App.setRoot("adminVue/addUser");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadAddArticle() {
        try {
            App.setRoot("adminVue/addArticle");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadUpdateArticle() {
        try {
            App.setRoot("adminVue/updateArticle");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadSetEtat() {
        try {
            App.setRoot("adminVue/setEtatUser");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadLinkClientUser() {
        try {
            App.setRoot("adminVue/linkClientUser");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deconnected() {
        try {
            App.setRoot("connexion");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
