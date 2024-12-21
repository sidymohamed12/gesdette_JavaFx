package com.dette.controllerFx.boutiquier;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.dette.App;
import com.dette.entities.Article;
import com.dette.entities.Detail;
import com.dette.entities.Dette;
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

public class TraitementDette extends BoutiquierController {

    // --------------------- RECHERCDE DETTE -------------------------

    @FXML
    private TextField searchDetteField;
    @FXML
    private Button submitSearchDette;

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

    private List<Dette> dettes = detteService.findAll();
    private ObservableList<Dette> detteList;
    private Dette dette;

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

    @FXML
    private Button accepter;
    @FXML
    private Button refuser;

    // ---------------------------------------------------------------

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

        submitSearchDette.setOnAction(event -> searchDette(event));
        accepter.setOnAction(event -> update(event, Etat.accepter));
        refuser.setOnAction(event -> update(event, Etat.annuler));

    }

    public void listeDette() {
        List<Dette> ldettes = new ArrayList<Dette>();
        for (Dette dette : dettes) {
            if (dette.getEtatD() == Etat.encours) {
                ldettes.add(dette);
            }
        }
        detteList = FXCollections.observableArrayList(ldettes);
        detteTable.setItems(detteList);
    }

    private void searchDette(ActionEvent event) {
        String recherche = searchDetteField.getText();
        isEmpty(recherche, "CHAMP VIDE", "veuiller saisir l'id de la dette");

        Integer id = Integer.parseInt(recherche);
        isPositif(id, "CHAMP RECHERCHE", "l'id est nom valide.");

        try {
            dette = detteService.getById(id);
            if (dette != null && dette.getEtatD() == Etat.encours) {
                detteList = FXCollections.observableArrayList(dette);
                detteTable.setItems(detteList);

                List<Article> articles = articleService.getArticlesDette(dette);
                List<Detail> details = detailService.getDetailOfArticleDette(articles, dette);

                articleList = FXCollections.observableArrayList(articles);
                detailList = FXCollections.observableArrayList(details);

                articleTable.setItems(articleList);
                detailTable.setItems(detailList);
            } else {
                showAlert(AlertType.ERROR, "RECHERCHE", "aucune demande de dette trouvé avec ce id");
                dette = null;
                return;
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Database Error", e.getMessage());
        }
    }

    private void update(ActionEvent event, Etat etat) {

        isNull(dette, "ERREUR UPDATE", "veuilllez d'abord rechercher une dette");

        try {
            if (dette.getEtatD() != etat) {
                dette.setEtatD(etat);
                detteService.modifier(dette);
                App.setRoot("boutiquierVue/listeDette");
            } else {
                showAlert(AlertType.ERROR, "Error", "cette dette est deja " + etat.name());
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Database Error", e.getMessage());
        }
    }

}
