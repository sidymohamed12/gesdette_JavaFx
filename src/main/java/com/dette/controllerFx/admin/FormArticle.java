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
        if (qteStock.isEmpty() || libelle.isEmpty() || prix.isEmpty()) {
            showAlert(AlertType.ERROR, "Form Error!", "Please fill all the fields");
            return;
        }
        try {
            int qteStockInt = Integer.parseInt(qteStock);
            double prixDouble = Double.parseDouble(prix);

            if (qteStockInt <= 0 || prixDouble <= 0) {
                showAlert(AlertType.ERROR, "Form Error!", "La quantité et le prix doivent être positifs.");
                return;
            }
            Article article = new Article(libelle, Integer.parseInt(qteStock), Double.parseDouble(prix));
            articleService.create(article);
            showAlert(AlertType.INFORMATION, "Success", "article registered successfully!");
            App.setRoot("listeArticle");

        } catch (NumberFormatException e) {

            showAlert(AlertType.ERROR, "Form Error!",
                    "La quantité et le prix doivent être des valeurs numériques valides.");
        } catch (Exception e) {

            e.printStackTrace();
            showAlert(AlertType.ERROR, "Database Error", "Error while saving article: " + e.getMessage());

        }

    }
}
