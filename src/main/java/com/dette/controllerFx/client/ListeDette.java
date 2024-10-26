package com.dette.controllerFx.client;

import java.time.LocalDateTime;
import java.util.List;

import com.dette.entities.Article;
import com.dette.entities.Detail;
import com.dette.entities.Dette;
import com.dette.entities.Payement;
import com.dette.entities.UserConnect;
import com.dette.enums.Etat;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class ListeDette extends ClientController {

    // --------------------- INFOS CLIENT -------------------------
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

    // --------------------- PAYEMENT DETTE -------------------------
    @FXML
    private TableView<Payement> payementTable;
    @FXML
    private TableColumn<Payement, Integer> idColumnP;
    @FXML
    private TableColumn<Payement, LocalDateTime> dateColumnP;
    @FXML
    private TableColumn<Payement, Double> montantColumnP;
    @FXML
    private ComboBox<String> selectFiltre;

    private ObservableList<Payement> payementList;

    // ---------------------------------------------------------------

    public void initialize() {

        System.out.println(UserConnect.getUserConnecte().getClient());

        surnomFlied.setText(UserConnect.getUserConnecte().getClient().getSurnom());
        telField.setText(UserConnect.getUserConnecte().getClient().getTelephone());
        adresseField.setText(UserConnect.getUserConnecte().getClient().getAdresse());

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
        listeDette();
        submitSearchDette.setOnAction(event -> getArtPayDette(event));
    }

    private void listeDette() {
        detteList = FXCollections.observableArrayList();
        for (Dette detteb : dettes) {
            if (!detteb.getMontantRestant().equals(0.0)
                    && detteb.getEtatD().equals(Etat.accepter) && !detteb.getArchiver()) {
                detteList.add(detteb);
            }
        }
        detteTable.setItems(detteList);
        infosDette(detteList);
    }

    private void getArtPayDette(ActionEvent event) {

        try {
            String rechercheDette = searchDette.getText();
            Integer id = Integer.parseInt(rechercheDette);
            if (id == null || id <= 0) {
                showAlert(AlertType.ERROR, "Form Error!", "L'id doitt être positif.");
                return;
            }

            dette = detteService.getById(id);
            if (dette == null) {
                showAlert(AlertType.ERROR, "RECHERCHE", "aucune dette trouvé avec ce id");
                return;
            }
            if (!dette.getClientD().getId().equals(UserConnect.getUserConnecte().getClient().getId())) {
                showAlert(AlertType.ERROR, "Erreur", "Cette dette ne vous appartient pas.");
                return;
            }

            if (dette.getEtatD() != Etat.accepter) {
                showAlert(AlertType.ERROR, "Erreur", "Cette dette n'est pas acceptée.");
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
        for (Dette dette : detteAddition) {
            total += dette.getMontant();
            verser += dette.getMontantVerser();
        }
        montantField.setText(total + " FCFA");
        verserField.setText(verser + " FCFA");
        restantField.setText((total - verser) + " FCFA");
    }
}
