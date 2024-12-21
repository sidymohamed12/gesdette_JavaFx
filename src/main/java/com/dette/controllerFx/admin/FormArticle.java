package com.dette.controllerFx.admin;

import com.dette.App;
import com.dette.entities.Article;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class FormArticle extends AdminController {
    @FXML
    private TextField libelleField;

    @FXML
    private TextField qteStockField;

    @FXML
    private TextField prixField;

    @FXML
    private Button submitButton;

    @FXML
    public void initialize() {

        submitButton.setOnAction(event -> enregistrementArticle(event));

    }

    public void enregistrementArticle(ActionEvent event) {

        String prix = prixField.getText();
        String qteStock = qteStockField.getText();
        String libelle = libelleField.getText();

        try {
            int qteStockInt = Integer.parseInt(qteStock);
            double prixDouble = Double.parseDouble(prix);

            isEmpty(libelle, "ERREUR FORMULAIRE", "Complétez les champs vides");
            isPositif(qteStockInt, "ERREUR FORMULAIRE", "La quantité doit être positif.");
            isPositif(prixDouble, "ERREUR FORMULAIRE", "Le prix doit être positif.");

            Article article = new Article(libelle, Integer.parseInt(qteStock), Double.parseDouble(prix));
            articleService.create(article);
            showAlert(AlertType.INFORMATION, "Success", "article registered successfully!");
            App.setRoot("adminVue/listeArticle");

        } catch (NumberFormatException e) {

            showAlert(AlertType.ERROR, "Form Error!",
                    "La quantité et le prix doivent être des valeurs numériques valides.");
        } catch (Exception e) {

            e.printStackTrace();
            showAlert(AlertType.ERROR, "Database Error", "Error while saving article: " + e.getMessage());

        }

    }
}
