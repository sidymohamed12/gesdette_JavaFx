package com.dette.controllerFx.boutiquier;

import java.time.LocalDateTime;
import java.util.List;

import com.dette.entities.Article;
import com.dette.entities.Client;
import com.dette.entities.Detail;
import com.dette.entities.Dette;
import com.dette.entities.Payement;
import com.dette.enums.Etat;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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
    @FXML
    private ComboBox<String> selectFiltre;

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

        selectFiltre.getItems().addAll("all", "solder", "non-solder");
        selectFiltre.getSelectionModel().select("all");
        selectFiltre.setOnAction(event -> filtreDette(event));

        submitSearchClient.setOnAction(event -> getDetteClientBySearch(event));
        submitSearchDette.setOnAction(event -> getArtPayDette(event));
    }

    private void getDetteClientBySearch(ActionEvent event) {

        String rechercheClient = searchClient.getText();
        isEmpty(rechercheClient, "CHAMP VIDE", "veuiller saisir le numero de téléphone du client");

        try {
            client = clientService.getBy(rechercheClient);

            isNull(client, " ERREUR RECHERCHE", "aucun client trouvé avec ce numero");

            surnomFlied.setText(client.getSurnom());
            telField.setText(client.getTelephone());
            adresseField.setText(client.getAdresse());

            dettes = detteService.detteOfClient(client);
            detteList = FXCollections.observableArrayList();
            for (Dette detteB : dettes) {
                if (detteB.getEtatD() == Etat.accepter) {
                    detteList.add(detteB);
                }
            }
            detteTable.setItems(detteList);
            infosDette(detteList);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Database Error", "Client/dettes nnot found" + e.getMessage());
        }
    }

    private void getArtPayDette(ActionEvent event) {
        isNull(dette, "ERREUR LISTE ARTICLE & PAYEMENT",
                "Veuillez d'abord rechercher une dette d'un client.");

        isNull(client, "ERREUR LISTE ARTICLE & PAYEMENT", "Veuillez d'abord rechercher un client");

        String rechercheDette = searchDette.getText();
        isEmpty(rechercheDette, "CHAMP VIDE", "veuiller saisir l'id de la dette");

        try {
            Integer id = Integer.parseInt(rechercheDette);
            isPositif(id, "Form Error!", "L'id doitt être positif.");

            dette = detteService.getById(id);
            isNull(dette, "ERREUR RECHERCHE", "aucune dette trouvé avec ce id pour ce client");

            if (!dette.getClientD().getId().equals(client.getId())) {
                showAlert(AlertType.ERROR, "Erreur", "Cette dette n'appartient pas au client recherché.");
                return;
            }

            if (dette.getEtatD() != Etat.accepter) {
                showAlert(AlertType.ERROR, "Erreur", "La dette de ce client n'est pas acceptée.");
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

    private void filtreDette(ActionEvent event) {
        String selected = selectFiltre.getSelectionModel().getSelectedItem();
        ObservableList<Dette> detteFiltres = FXCollections.observableArrayList();
        for (Dette detteb : detteList) {
            if ("solder".equals(selected) && detteb.getMontant().equals(detteb.getMontantVerser())) {
                detteFiltres.add(detteb);
            } else if ("non-solder".equals(selected) && !detteb.getMontantRestant().equals(0.0)) {
                detteFiltres.add(detteb);
            } else if ("all".equals(selected)) {
                detteFiltres.add(detteb);
            }
        }
        detteTable.setItems(detteFiltres);
    }
}
