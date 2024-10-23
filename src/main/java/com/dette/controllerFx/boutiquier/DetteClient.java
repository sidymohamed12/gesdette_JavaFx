package com.dette.controllerFx.boutiquier;

import java.time.LocalDateTime;
import java.util.List;

import com.dette.entities.Article;
import com.dette.entities.Client;
import com.dette.entities.Detail;
import com.dette.entities.Dette;
import com.dette.entities.Payement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;

public class DetteClient extends BoutiquierController {

    // --------------------- INFOS RECHERCHE CLIENT -------------------------

    @FXML
    private TextField searchClient;
    @FXML
    private Button submitSearchClient;
    private Client client;

    // --------------------- INFOS AVANT TABLEAU DETTE -------------------------
    @FXML
    private TextField surnomFlied;
    @FXML
    private TextField telField;
    @FXML
    private TextField adresseField;
    @FXML
    private TextField montantField;
    @FXML
    private TextField verserField;
    @FXML
    private TextField restantField;

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

    private List<Dette> dettes;
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

    // --------------------- PAYEMENT DETTE -------------------------
    @FXML
    private TableView<Payement> payementTable;
    @FXML
    private TableColumn<Payement, Integer> idColumnP;
    @FXML
    private TableColumn<Payement, LocalDateTime> dateColumnP;
    @FXML
    private TableColumn<Payement, Double> montantColumnP;

    private ObservableList<Payement> payementList;

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

        idColumnP.setCellValueFactory(new PropertyValueFactory<>("id"));
        montantColumnP.setCellValueFactory(new PropertyValueFactory<>("montant"));
        dateColumnP.setCellValueFactory(new PropertyValueFactory<>("date"));

        submitSearchClient.setOnAction(event -> getDetteClientBySearch(event));
        submitSearchDette.setOnAction(event -> getArtPayDette(event));
    }

    private void getDetteClientBySearch(ActionEvent event) {

        String rechercheClient = searchClient.getText();
        if (rechercheClient.isEmpty()) {
            showAlert(AlertType.ERROR, "CHAMP VIDE", "veuiller saisir un numero de téléphone");
            return;
        }

        try {
            client = clientService.getBy(rechercheClient);

            if (client == null) {
                showAlert(AlertType.ERROR, "RECHERCHE", "aucun client trouvé avec ce numero");
                return;
            }
            surnomFlied.setText(client.getSurnom());
            telField.setText(client.getTelephone());
            adresseField.setText(client.getAdresse());

            dettes = detteService.detteOfClient(client);
            detteList = FXCollections.observableArrayList(dettes);
            detteTable.setItems(detteList);
            infosDette(dettes);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Database Error", "Client/dettes nnot found" + e.getMessage());
        }
    }

    private void getArtPayDette(ActionEvent event) {
        if (dette == null && client == null) {
            showAlert(AlertType.ERROR, "Error",
                    "Veuillez d'abord rechercher un client puis recher une de ses dettes.");
            return;
        }

        String rechercheDette = searchDette.getText();
        if (rechercheDette.isEmpty()) {
            showAlert(AlertType.ERROR, "CHAMP VIDE", "veuiller saisir l'id de la dette");
            return;
        }

        Integer id = Integer.parseInt(rechercheDette);
        if (id == null || id <= 0) {
            showAlert(AlertType.ERROR, "Form Error!", "L'id doitt être positif.");
            return;
        }

        try {
            dette = detteService.getById(id);
            if (dette == null) {
                showAlert(AlertType.ERROR, "RECHERCHE", "aucune dette trouvé avec ce id");
                return;
            }
            List<Article> articles = articleService.getArticlesDette(dette);
            List<Payement> payments = payementService.getPayementsDette(dette);
            List<Detail> details = detailService.getDetailOfArticleDette(articles, dette);

            articleList = FXCollections.observableArrayList(articles);
            payementList = FXCollections.observableArrayList(payments);
            detailList = FXCollections.observableArrayList(details);

            articleTable.setItems(articleList);
            payementTable.setItems(payementList);
            detailTable.setItems(detailList);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Database Error", e.getMessage());
        }
    }

    private void infosDette(List<Dette> detteAddition) {
        Double total = 0.0;
        Double verser = 0.0;
        Double restant = 0.0;
        for (Dette dette : detteAddition) {
            total += dette.getMontant();
            verser += dette.getMontantVerser();
            restant += dette.getMontantRestant();
        }
        montantField.setText(total + " FCFA");
        verserField.setText(verser + " FCFA");
        restantField.setText(restant + " FCFA");
    }
}
