package com.dette.controllerFx.boutiquier;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.dette.App;
import com.dette.entities.Dette;
import com.dette.entities.Payement;
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

public class AddPayement extends BoutiquierController {
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

    // --------------------------- PAYEMENT ------------------------
    @FXML
    private TextField montantPayement;
    @FXML
    private Button submitPayement;

    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        montantColumn.setCellValueFactory(new PropertyValueFactory<>("montant"));
        verserColumn.setCellValueFactory(new PropertyValueFactory<>("montantVerser"));
        restantColumn.setCellValueFactory(new PropertyValueFactory<>("montantRestant"));

        listeDette();

        submitSearchDette.setOnAction(event -> searchDette(event));
        submitPayement.setOnAction(event -> payement(event));

    }

    private void listeDette() {
        List<Dette> ldettes = new ArrayList<Dette>();
        for (Dette dette : dettes) {
            if (dette.getEtatD() == Etat.accepter && (dette.getMontant() != dette.getMontantVerser()
                    || dette.getMontantRestant() == 0) && dette.getArchiver() == false) {
                ldettes.add(dette);
            }
        }
        detteList = FXCollections.observableArrayList(ldettes);
        detteTable.setItems(detteList);
    }

    private void searchDette(ActionEvent event) {
        String recherche = searchDetteField.getText();
        if (recherche.isEmpty()) {
            showAlert(AlertType.ERROR, "CHAMP VIDE", "veuiller saisir l'id de la dette");
            return;
        }

        Integer id = Integer.parseInt(recherche);
        if (id < 0 || id == null) {
            showAlert(AlertType.ERROR, "Form Error!", "L'id doitt être positif.");
            return;
        }

        try {
            dette = detteService.getById(id);
            if (dette != null && dette.getEtatD() == Etat.accepter
                    && !dette.getMontant().equals(dette.getMontantVerser())
                    && dette.getArchiver() == false) {
                detteList = FXCollections.observableArrayList(dette);
                detteTable.setItems(detteList);
            } else {
                showAlert(AlertType.ERROR, "RECHERCHE", "aucune dette non-soldé accepté trouvé avec ce id");
                dette = null;
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, " Error", e.getMessage());
        }
    }

    private void payement(ActionEvent event) {

        isNull(dette, "RECHERCHE DETTE ERROR", "veuilllez d'abord rechercher une dette");

        String montant = montantPayement.getText();

        try {

            Double pay = Double.parseDouble(montant);
            isPositif(pay, "ERREUR FORMULAIRE", "Veuillez entrez un montant valide");
            if (pay > dette.getMontantRestant()) {
                showAlert(AlertType.ERROR, "ERREUR FORMULAIRE", "payement impossible verifier le montant saisie");
                return;
            } else {
                Payement payement = new Payement();
                payement.setDate(LocalDateTime.now());
                payement.setMontant(pay);
                payement.setDette(dette);
                payementService.create(payement);

                dette.addPayement(payement);
                dette.setMontantVerser(dette.getMontantVerser() + pay);
                if (dette.getMontantVerser() == dette.getMontant() || dette.getMontantRestant() == 0) {
                    dette.setArchiver(true);
                }
                detteService.modifier(dette);
                App.setRoot("boutiquierVue/listeDette");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Error", e.getMessage());
        }
    }
}
