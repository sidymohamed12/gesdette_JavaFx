package com.dette.controllerFx.admin;

import com.dette.entities.Article;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class ArticleTable extends AdminController {
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
    private ComboBox<String> filterComboBox;

    @FXML
    public void initialize() {
        // Liaison des colonnes aux propriétés du modèle User
        refColumn.setCellValueFactory(new PropertyValueFactory<>("ref"));
        libelleColumn.setCellValueFactory(new PropertyValueFactory<>("libelle"));
        qteStockColumn.setCellValueFactory(new PropertyValueFactory<>("qteStock"));
        prixColumn.setCellValueFactory(new PropertyValueFactory<>("prix"));

        listArticle();

        filterComboBox.getItems().addAll("All", "Available");
        filterComboBox.getSelectionModel().select("All");
        filterComboBox.setOnAction(event -> listeFiltrerDispo());

    }

    public void listArticle() {
        ObservableList<Article> uList = FXCollections.observableArrayList(articleService.findAll());
        articleTable.setItems(uList);
    }

    @FXML
    private void listeFiltrerDispo() {
        String selectedFilter = filterComboBox.getSelectionModel().getSelectedItem();
        ObservableList<Article> filteredArticles = FXCollections.observableArrayList();
        ObservableList<Article> articles = FXCollections.observableArrayList(articleService.findAll());

        if ("Available".equals(selectedFilter)) {

            for (Article article : articles) {
                if (article.getQteStock() > 0) {
                    filteredArticles.add(article);
                }
            }
            articleTable.setItems(filteredArticles);
        } else {
            articleTable.setItems(articles);
        }
    }

}
