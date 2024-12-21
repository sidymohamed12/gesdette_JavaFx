package com.dette.controllerFx.client;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

import com.dette.App;
import com.dette.entities.Article;
import com.dette.entities.Detail;
import com.dette.entities.Dette;
import com.dette.entities.UserConnect;
import com.dette.enums.Etat;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;

public class FormDette extends ClientController {

    @FXML
    private ComboBox<Article> selectArticle;

    @FXML
    private TextField qteAchete;

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

    // --------------------- DETAIL ARTICLE DETTE -------------------------
    @FXML
    private TableView<Detail> detailTable;
    @FXML
    private TableColumn<Detail, Double> totalColumn;
    @FXML
    private TableColumn<Detail, Double> qteVenduColumn;

    @FXML
    private Button buttonAjoutArticle;

    @FXML
    private Button submitDette;

    private Dette dette;

    private ObservableList<Article> articles = FXCollections.observableArrayList();
    private ObservableList<Article> articlesDetail = FXCollections.observableArrayList();
    private ObservableList<Detail> details = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        refColumn.setCellValueFactory(new PropertyValueFactory<>("ref"));
        libelleColumn.setCellValueFactory(new PropertyValueFactory<>("libelle"));
        qteStockColumn.setCellValueFactory(new PropertyValueFactory<>("qteStock"));
        prixColumn.setCellValueFactory(new PropertyValueFactory<>("prix"));

        qteVenduColumn.setCellValueFactory(new PropertyValueFactory<>("qteVendu"));
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("montantVendu"));

        loadArticles();
        buttonAjoutArticle.setOnAction(event -> addArticleToDette());
        submitDette.setOnAction(event -> submitDette());
    }

    private void loadArticles() {
        articles.clear();
        articles.addAll(articleService.findAll());

        // Définir le StringConverter pour afficher seulement les libellés
        selectArticle.setConverter(new StringConverter<Article>() {
            @Override
            public String toString(Article article) {
                return article != null ? article.getLibelle() : "";
            }

            @Override
            public Article fromString(String string) {
                return null;
            }
        });

        selectArticle.setItems(articles);
    }

    private void addArticleToDette() {
        if (dette == null) {
            dette = new Dette();
            dette.setMontant(0.0);
            dette.setMontantVerser(0.0);
            dette.setDate(LocalDateTime.now());
            dette.setArchiver(false);
            dette.setEtatD(Etat.encours);
            dette.setClientD(UserConnect.getUserConnecte().getClient());
        }

        Article article = selectArticle.getValue();

        isNull(article, "Erreur Article Null", "Veuillez sélectionner un article.");

        int qte;
        try {
            qte = Integer.parseInt(qteAchete.getText());
            if (qte > article.getQteStock() || qte <= 0) {
                showAlert(AlertType.ERROR, "Erreur", "Quantité non valide.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert(AlertType.ERROR, "Erreur", "Veuillez entrer une quantité valide.");
            return;
        }

        double montantArticle = qte * article.getPrix();

        // Vérifier si l'article est déjà présent dans la liste des détails
        Optional<Detail> existingDetail = details.stream()
                .filter(detail -> detail.getArticle().equals(article))
                .findFirst();

        if (existingDetail.isPresent()) {
            Detail detail = existingDetail.get();
            detail.setQteVendu(detail.getQteVendu() + qte);
            detail.setMontantVendu(detail.getMontantVendu() + montantArticle);
        } else {
            Detail detail = new Detail();
            detail.setQteVendu(qte);
            detail.setMontantVendu(montantArticle);
            detail.setArticle(article);
            detail.setDette(dette);

            articlesDetail.add(article);
            details.add(detail);
        }

        dette.setMontant(dette.getMontant() + montantArticle);

        articleTable.setItems(FXCollections.observableArrayList(articlesDetail));
        detailTable.setItems(FXCollections.observableArrayList(details));
        qteAchete.clear();
        selectArticle.getSelectionModel().clearSelection();
    }

    private void submitDette() {
        try {

            isNull(details, "Erreur d'articles", "Aucun article ajouté à la dette.");

            details.forEach(detail -> {
                if (detail.getId() != null) {
                    detailService.modifier(detail);
                } else {
                    detailService.create(detail);
                }
            });

            if (dette.getId() != null) {
                detteService.modifier(dette);
            } else {
                detteService.create(dette);
            }
            showAlert(AlertType.CONFIRMATION, "Succès", "La dette a été enregistrée avec succès.");

            App.setRoot("clientVue/listeDemandeDette");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
