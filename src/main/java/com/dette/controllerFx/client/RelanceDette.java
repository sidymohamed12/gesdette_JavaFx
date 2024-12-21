package com.dette.controllerFx.client;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import com.dette.App;
import com.dette.entities.Article;
import com.dette.entities.Detail;
import com.dette.entities.Dette;
import com.dette.entities.UserConnect;
import com.dette.enums.Etat;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class RelanceDette extends ClientController {
    // --------------------- TABLEAU DETTE -------------------------

    @FXML
    private TableView<Dette> detteTable;
    @FXML
    private TableColumn<Dette, Integer> idColumn;
    @FXML
    private TableColumn<Dette, LocalDateTime> dateColumn;
    @FXML
    private TableColumn<Dette, Double> montantColumn;
    @FXML
    private TableColumn<Dette, Double> verserColumn;
    @FXML
    private TableColumn<Dette, Double> restantColumn;

    private List<Dette> dettes = detteService.detteOfClient(UserConnect.getUserConnecte().getClient());
    private ObservableList<Dette> detteList;
    private Dette dette;

    // --------------------- INFOS RECHERCHE DETTE -------------------------

    @FXML
    private TextField searchDette;
    @FXML
    private Button submitSearchDette;

    // --------------------- ARTICLE DETTE -------------------------
    @FXML
    private TableView<Article> articleTable;
    @FXML
    private TableColumn<Article, String> refColumn;
    @FXML
    private TableColumn<Article, String> libelleColumn;
    @FXML
    private TableColumn<Article, Integer> qteStockColumn;
    @FXML
    private TableColumn<Article, Double> prixColumn;
    private ObservableList<Article> articleList;

    // --------------------- DETAIL ARTICLE DETTE -------------------------
    @FXML
    private TableView<Detail> detailTable;
    @FXML
    private TableColumn<Detail, Double> totalColumn;
    @FXML
    private TableColumn<Detail, Double> qteVenduColumn;
    private ObservableList<Detail> detailList;

    // --------------------- RELANCEMENT DETTE -------------------------
    @FXML
    private Button resendDette;

    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        montantColumn.setCellValueFactory(new PropertyValueFactory<>("montant"));
        verserColumn.setCellValueFactory(new PropertyValueFactory<>("montantVerser"));
        restantColumn.setCellValueFactory(new PropertyValueFactory<>("montantRestant"));

        refColumn.setCellValueFactory(new PropertyValueFactory<>("ref"));
        libelleColumn.setCellValueFactory(new PropertyValueFactory<>("libelle"));
        qteStockColumn.setCellValueFactory(new PropertyValueFactory<>("qteStock"));
        prixColumn.setCellValueFactory(new PropertyValueFactory<>("prix"));

        qteVenduColumn.setCellValueFactory(new PropertyValueFactory<>("qteVendu"));
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("montantVendu"));
        listeDette();

        submitSearchDette.setOnAction(event -> getArtDette(event));

        resendDette.setOnAction(event -> resendDetteAnnuler());
    }

    private void listeDette() {
        detteList = FXCollections.observableArrayList();
        for (Dette detteL : dettes) {
            if (detteL.getEtatD().equals(Etat.annuler)) {
                detteList.add(dette);
            }
        }
        detteTable.setItems(detteList);
    }

    private void getArtDette(ActionEvent event) {

        try {
            Integer id = Integer.parseInt(searchDette.getText());
            isPositif(id, "ERREUR RECHERCHE : DETTE_ID NULL", "L'id doitt être positif.");

            dette = detteService.getById(id);
            isNull(dette, "RECHERCHE Null", "aucune dette trouvé avec ce id");

            if (!dette.getClientD().getId().equals(UserConnect.getUserConnecte().getClient().getId())) {
                showAlert(AlertType.ERROR, "Erreur", "Cette dette ne vous appartient pas.");
                return;
            }

            if (dette.getEtatD() != Etat.annuler) {
                showAlert(AlertType.ERROR, "Erreur", "Cette dette n'est pas annulée.");
                return;
            }

            List<Article> articles = articleService.getArticlesDette(dette);
            List<Detail> details = detailService.getDetailOfArticleDette(articles, dette);

            articleList = FXCollections.observableArrayList(articles);
            detailList = FXCollections.observableArrayList(details);

            articleTable.setItems(articleList);
            detailTable.setItems(detailList);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Database Error", e.getMessage());
        }
    }

    private void resendDetteAnnuler() {
        try {

            isNull(dette, "ERREUR RELANCEMENT",
                    "aucune dette selectionnée, veuillez d'abord faire une recherce");

            if (!dette.getClientD().getId().equals(UserConnect.getUserConnecte().getClient().getId())) {
                showAlert(AlertType.ERROR, "Erreur", "Cette dette ne vous appartient pas.");
                return;
            }
            if (dette.getEtatD().equals(Etat.accepter)) {
                dette.setEtatD(Etat.encours);
                detteService.modifier(dette);
                showAlert(AlertType.CONFIRMATION, "Succès", "La dette a été enregistrée avec succès.");
                App.setRoot("clientVue/listeDemandeDette");
            } else {
                showAlert(AlertType.ERROR, "SUBMIT", "Cette dette n'est pas annuler");
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
