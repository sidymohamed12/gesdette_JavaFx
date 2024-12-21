package com.dette.controllerFx.boutiquier;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

import com.dette.App;
import com.dette.entities.Article;
import com.dette.entities.Client;
import com.dette.entities.Detail;
import com.dette.entities.Dette;
import com.dette.enums.Etat;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;
import javafx.scene.control.Alert.AlertType;

public class AddDette extends BoutiquierController {

    @FXML
    private ComboBox<Article> selectArticle;

    @FXML
    private ComboBox<Client> selectClient;

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
    private Client client;

    private ObservableList<Client> clients = FXCollections.observableArrayList();
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
        loadClients();
        loadArticles();
        buttonAjoutArticle.setOnAction(event -> addArticleToDette());
        submitDette.setOnAction(event -> submitDette());
    }

    private void loadArticles() {
        articles.clear();
        articles.addAll(articleService.findAll());
        selectArticle.setConverter(new StringConverter<Article>() {
            @Override
            public String toString(Article articleD) {
                return articleD != null ? articleD.getLibelle() : "";
            }

            @Override
            public Article fromString(String string) {
                return null;
            }
        });
        selectArticle.setItems(articles);
    }

    private void loadClients() {
        clients.clear();
        clients.addAll(clientService.findAll());
        selectClient.setConverter(new StringConverter<Client>() {

            @Override
            public String toString(Client cl) {
                return cl != null ? cl.getSurnom() : "";
            }

            @Override
            public Client fromString(String arg0) {
                return null;
            }

        });
        selectClient.setItems(clients);
    }

    private void addArticleToDette() {
        if (dette == null) {
            dette = new Dette();
            dette.setMontant(0.0);
            dette.setMontantVerser(0.0);
            dette.setDate(LocalDateTime.now());
            dette.setArchiver(false);
            dette.setEtatD(Etat.accepter);
            dette.setClientD(client);
        }

        Article article = selectArticle.getValue();

        isNull(article, "ERREUR AJOUT ARTICLE", "Veuillez sélectionner un article.");
        isPositif(article.getQteStock(), "ERREUR AJOUT ARTICLE", "Veuillez sélectionner un article disponible.");

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

        article.setQteStock(article.getQteStock() - qte);
        articleService.modifier(article);
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
            dette.addDetail(detail);
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
            client = selectClient.getValue();
            if (details.isEmpty()) {
                showAlert(AlertType.ERROR, "Erreur", "Aucun article ajouté à la dette.");
                return;
            }

            isNull(client, "Erreur Submit", "Aucun client celectionné à la dette.");

            dette.setClientD(client);
            client.addDettes(dette);

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

            App.setRoot("boutiquierVue/listeDette");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
