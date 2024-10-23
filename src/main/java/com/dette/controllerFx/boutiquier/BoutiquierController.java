package com.dette.controllerFx.boutiquier;

import java.io.IOException;

import com.dette.App;
import com.dette.repository.JPA.ArticleJPA;
import com.dette.repository.JPA.ClientJPA;
import com.dette.repository.JPA.DetailJPA;
import com.dette.repository.JPA.DetteJPA;
import com.dette.repository.JPA.PayementJPA;
import com.dette.repository.JPA.UserJPA;
import com.dette.services.ArticleService;
import com.dette.services.ClientService;
import com.dette.services.DetailService;
import com.dette.services.DetteService;
import com.dette.services.PayementService;
import com.dette.services.UserService;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class BoutiquierController {

    private ClientJPA clientJPA = new ClientJPA();
    private DetteJPA detteJPA = new DetteJPA();
    private PayementJPA payementJPA = new PayementJPA();
    private ArticleJPA articleJPA = new ArticleJPA();
    private DetailJPA detailJPA = new DetailJPA();
    private UserJPA userJPA = new UserJPA();

    protected ClientService clientService = new ClientService(clientJPA);
    protected DetteService detteService = new DetteService(detteJPA);
    protected PayementService payementService = new PayementService(payementJPA);
    protected ArticleService articleService = new ArticleService(articleJPA);
    protected DetailService detailService = new DetailService(detailJPA);
    protected UserService userService = new UserService(userJPA);

    public void menu() {
        System.out.println("1- Créer un client");
        System.out.println("2- Lister les clients ayant un compte (avec cumul des montants dus) et pouvoir filtrer");
        System.out.println("3- Lister les clients sans un compte");
        System.out.println("4- Rechercher un client par son téléphone");
        System.out.println("5- Créer une dette pour un client");
        System.out.println("6- Enregistrer un paiement pour une dette");
        System.out.println(
                "7- Lister les dettes non soldées d'un client avec l'option de voir les articles ou les paiements");
        System.out.println(
                "8- Lister les demandes de dette (filtrer par En Cours ou Annuler) , voir les articles d'une dette et enfin valider ou refuser la dette.");
        System.out.println("9- Quitter");
    }

    public void loadDetteListe() {
        try {
            App.setRoot("boutiquierVue/listeDette");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadDetteForm() {
        try {
            App.setRoot("boutiquierVue/formDette");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadDetteClient() {
        try {
            App.setRoot("boutiquierVue/detteClient");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadTraitement() {
        try {
            App.setRoot("boutiquierVue/traitementDette");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadClientListe() {
        try {
            App.setRoot("boutiquierVue/listeClient");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadClientForm() {
        try {
            App.setRoot("boutiquierVue/formClient");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadPayementForm() {
        try {
            App.setRoot("boutiquierVue/fairePayement");
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
