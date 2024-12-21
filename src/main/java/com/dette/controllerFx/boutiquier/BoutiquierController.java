package com.dette.controllerFx.boutiquier;

import java.io.IOException;

import com.dette.App;
import com.dette.controllerFx.Controller;

public class BoutiquierController extends Controller {

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

}
