package com.dette.controllerFx.admin;

import com.dette.App;
import com.dette.entities.Article;

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

public class UpdateArticle extends AdminController {

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

    @FXML
    private TextField searchField;

    @FXML
    private TextField newQteField;

    @FXML
    private Button searchButton;

    @FXML
    private Button submitButton;

    private Article article;

    @FXML
    public void initialize() {

        refColumn.setCellValueFactory(new PropertyValueFactory<>("ref"));
        libelleColumn.setCellValueFactory(new PropertyValueFactory<>("libelle"));
        qteStockColumn.setCellValueFactory(new PropertyValueFactory<>("qteStock"));
        prixColumn.setCellValueFactory(new PropertyValueFactory<>("prix"));

        listArticle();

        searchButton.setOnAction(event -> articleSearch(event));
        submitButton.setOnAction(event -> updateArticle(event));
    }

    public void listArticle() {
        ObservableList<Article> uList = FXCollections.observableArrayList(articleService.findAll());
        articleTable.setItems(uList);
    }

    public void articleSearch(ActionEvent event) {
        String recherche = searchField.getText();
        isEmpty(recherche, "ERREUR RECHERCHE", "veuillez saisir la ref de l'article");

        try {
            article = articleService.getBy(recherche);
            if (article != null) {

                ObservableList<Article> foundArticle = FXCollections.observableArrayList(article);
                articleTable.setItems(foundArticle);
                showAlert(AlertType.INFORMATION, "Success", "Article chargé !");
            } else {
                showAlert(AlertType.ERROR, "Not Found", "Aucun article trouvé avec cette référence.");
                articleTable.setItems(FXCollections.observableArrayList());
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Database Error", "Not found the article " + e.getMessage());

        }

    }

    public void updateArticle(ActionEvent event) {

        isNull(article, "ERREUR SUBMIT",
                "Veuillez d'abord rechercher et sélectionner un article à mettre à jour.");

        String newQte = newQteField.getText();
        System.out.println(article);
        System.out.println(newQte);

        try {
            Integer qteInt = Integer.parseInt(newQte);
            isPositif(qteInt, "SUBMI ERREUR", "entrez une quantité positive");

            article.setQteStock(article.getQteStock() + qteInt);
            articleService.modifier(article);

            App.setRoot("adminVue/listeArticle");

        } catch (NumberFormatException e) {
            showAlert(AlertType.ERROR, "Form Error!",
                    "La quantité doit être numérique.");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Database Error", "Error MAJ article: " + e.getMessage());
        }
    }
}
