package com.dette.controllerFx.admin;

import java.io.IOException;

import com.dette.App;
import com.dette.controllerFx.Controller;

public class AdminController extends Controller {

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

}
