package com.dette.controllerFx.client;

import java.io.IOException;

import com.dette.App;
import com.dette.controllerFx.Controller;

public class ClientController extends Controller {

    public void showClientMenu() {
        System.out.println("1-  Lister ses dettes non soldées avec l’option de voir les articles ou les paiements");
        System.out.println("2-  Faire une demande de Dette");
        System.out.println("3-  Filtrer demandes de dette par état(En Cours, ou Annuler)");
        System.out.println("4-  Envoyer une relance pour une demande de dette annuler");
        System.out.println("5- Quitter");
    }

    public void loadDemandeClient() {
        try {
            App.setRoot("clientVue/listedemandeDette");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadDetteClient() {
        try {
            App.setRoot("clientVue/listeDette");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadDetteForm() {
        try {
            App.setRoot("clientVue/formDette");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadDetteRelance() {
        try {
            App.setRoot("clientVue/RelancerDette");
        } catch (Exception e) {
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
