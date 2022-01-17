package Application;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.UnaryOperator;

import static javax.xml.bind.DatatypeConverter.parseDouble;
import static javax.xml.bind.DatatypeConverter.parseInt;

public class CompteController {

    Connection conn=null;
    public CompteController() {
        conn= SingletonConnection.getConnection();
    }

    @FXML
    private Button btnSubmit;

    @FXML
    private TextField SoldeField;

    @FXML
    private TextField decouvertField;

    @FXML
    private TextField maxDecouvertField;

    @FXML
    private TextField maxRetraitField;

    @FXML
    private ComboBox<Integer> combo;

    @FXML
    private TableView<Compte> table;

    @FXML
    private TableColumn<Compte, Integer> ClientCol;

    @FXML
    private TableColumn<Compte, Integer> CompteCol;

    @FXML
    private TableColumn<Compte, Double> SoldeCol;

    @FXML
    private TableColumn<Compte, Double> decouvertCol;

    @FXML
    private TableColumn<Compte, Double> maxDecouvertCol;

    @FXML
    private TableColumn<Compte, Double> maxRetraitCol;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnLister;

    @FXML
    private Button btnLister1;

    @FXML
    private Button btnModify;

    @FXML
    private Button btnExit;

    @FXML
    private Button btnTransaction;

    @FXML
    private Button btnVirement;

    ObservableList<Compte> list = FXCollections.observableArrayList();

    @FXML
    void onDelete(ActionEvent event) throws SQLException {
        Alert alert= new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setContentText("Are you sure to delete ?");
        Optional<ButtonType> action = alert.showAndWait();
        if (action.get()==ButtonType.OK) {
            int idc;
            idc = table.getSelectionModel().getSelectedItem().getidcompte();
            table.getItems().remove(table.getSelectionModel().getSelectedItem());
            PreparedStatement x = (PreparedStatement) conn.prepareStatement("delete from compte where idcompte='" + idc + "'");
            x.execute();
        }
    }

    @FXML
    void onExit(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    void onModify(ActionEvent event) throws SQLException {
        int idcli;
        double sol;
        double dec;
        double mdec;
        double mret;
        idcli= table.getSelectionModel().getSelectedItem().getidcompte();
        sol=parseDouble(SoldeField.getText());
        dec=parseDouble(decouvertField.getText());
        mdec=parseDouble(maxDecouvertField.getText());
        mret=parseDouble(maxRetraitField.getText());
        if (sol>0) {
            PreparedStatement x = (PreparedStatement) conn.prepareStatement("update compte set solde='" + sol + "',decouvert=0 ,maxdecouvert='" + mdec + "',maxretrait='" + mret + "' where idcompte='" + idcli + "' ");
            x.execute();
        }
        else{
            PreparedStatement x = (PreparedStatement) conn.prepareStatement("update compte set solde='" + sol + "',decouvert=-solde ,maxdecouvert='" + mdec + "',maxretrait='" + mret + "' where idcompte='" + idcli + "' ");
            x.execute();
        }
         clearInput();
       list.clear();
        ResultSet rs = conn.createStatement().executeQuery("select * from compte order by solde desc");

        while (rs.next()){
            list.add(new Compte(rs.getInt(1),rs.getInt("idClient"),rs.getDouble("Solde"),rs.getDouble("Decouvert"),rs.getDouble("MaxDecouvert"),rs.getDouble("MaxRetrait")));
        }

        ClientCol.setCellValueFactory(new PropertyValueFactory<>("idClient"));
        CompteCol.setCellValueFactory(new PropertyValueFactory<>("idCompte"));
        SoldeCol.setCellValueFactory(new PropertyValueFactory<>("solde"));
        decouvertCol.setCellValueFactory(new PropertyValueFactory<>("decouvert"));
        maxDecouvertCol.setCellValueFactory(new PropertyValueFactory<>("MaxDecouvert"));
        maxRetraitCol.setCellValueFactory(new PropertyValueFactory<>("MaxRetrait"));

        table.setItems(list);
        table.getSelectionModel().select(0);
        table.setStyle("-fx-selection-bar-non-focused: #CCFF00;");
    }

    @FXML
    void OnSet(MouseEvent event) {
        double sol;
        int idC;
        double dec;
        double mdec;
        double mret;
        idC = table.getSelectionModel().getSelectedItem().getidclient();
        sol = table.getSelectionModel().getSelectedItem().getSolde();
        dec = table.getSelectionModel().getSelectedItem().getDecouvert();
        mdec = table.getSelectionModel().getSelectedItem().getMaxDecouvert();
        mret = table.getSelectionModel().getSelectedItem().getMaxRetrait();

        combo.setValue(idC);
        combo.setDisable(true);
        SoldeField.setText(String.valueOf(sol));
        decouvertField.setText(String.valueOf(dec));
        maxDecouvertField.setText(String.valueOf(mdec));
        maxRetraitField.setText(String.valueOf(mret));

    }


    @FXML
    void onList(ActionEvent event) throws SQLException {
        list.clear();
        ResultSet rs = conn.createStatement().executeQuery("select * from compte order by solde desc");

        while (rs.next()){
            list.add(new Compte(rs.getInt("IdCompte"),rs.getInt("idClient"),rs.getDouble("Solde"),rs.getDouble("Decouvert"),rs.getDouble("MaxDecouvert"),rs.getDouble("MaxRetrait")));
        }
        ClientCol.setCellValueFactory(new PropertyValueFactory<>("idclient"));
        CompteCol.setCellValueFactory(new PropertyValueFactory<>("idcompte"));
        SoldeCol.setCellValueFactory(new PropertyValueFactory<>("solde"));
        decouvertCol.setCellValueFactory(new PropertyValueFactory<>("decouvert"));
        maxDecouvertCol.setCellValueFactory(new PropertyValueFactory<>("MaxDecouvert"));
        maxRetraitCol.setCellValueFactory(new PropertyValueFactory<>("MaxRetrait"));
        table.setItems(list);
        table.getSelectionModel().select(0);
        table.setStyle("-fx-selection-bar-non-focused: #CCFF00;");
    }

    @FXML
    void onListDec(ActionEvent event) throws SQLException {
        list.clear();
        ResultSet rs = conn.createStatement().executeQuery("select * from compte where solde<0");
        while (rs.next()){
            list.add(new Compte(rs.getInt("idCompte"),rs.getInt("idClient"),rs.getDouble("Solde"),rs.getDouble("Decouvert"),rs.getDouble("MaxDecouvert"),rs.getDouble("MaxRetrait")));
        }
        ClientCol.setCellValueFactory(new PropertyValueFactory<>("idclient"));
        CompteCol.setCellValueFactory(new PropertyValueFactory<>("idCompte"));
        SoldeCol.setCellValueFactory(new PropertyValueFactory<>("solde"));
        decouvertCol.setCellValueFactory(new PropertyValueFactory<>("decouvert"));
        maxDecouvertCol.setCellValueFactory(new PropertyValueFactory<>("MaxDecouvert"));
        maxRetraitCol.setCellValueFactory(new PropertyValueFactory<>("MaxRetrait"));
        table.setItems(list);
    }
    @FXML
    List<Integer> onidclient () throws SQLException {

        List<Integer> options = new ArrayList<>();

        String query = "Select idclient from client";
        PreparedStatement st = (PreparedStatement) conn.prepareStatement(query);
        ResultSet rs = (ResultSet) st.executeQuery();
        while (rs.next()) {
            options.add(rs.getInt("idclient"));
        }
        return options;
    }


    public void initialize() throws SQLException {
        combo.setItems(FXCollections.observableArrayList(onidclient()));
    }


    @FXML
    void onSubmit(ActionEvent event) throws SQLException {
        if (this.isInputValid()) {
        Double solde = parseDouble(SoldeField.getText());
        Double dec = parseDouble(decouvertField.getText());
        Double maxdecouvert = parseDouble(maxDecouvertField.getText());
        Double maxretrait = parseDouble(maxRetraitField.getText());
        int selected = combo.getValue();


                PreparedStatement x = (PreparedStatement) conn.prepareStatement("insert into Compte(idclient,solde,decouvert,maxdecouvert,maxretrait) values(?,?,?,?,?)");
                x.setInt(1, selected);
                x.setDouble(2, solde);
                x.setDouble(3, dec);
                x.setDouble(4, maxdecouvert);
                x.setDouble(5, maxretrait);
                x.execute();
      /*  Compte c = new Compte(this.combo.getValue(), this.SoldeField.getText(), this.decouvertField.getText(),this.maxDecouvertField.getText(),this.maxRetraitField.getText());
        table.getItems().add(c);*/
                clearInput();
            }
        }


    @FXML
    void onTransaction(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Transaction.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            Image icon = new Image(getClass().getResourceAsStream("icon.png"));
            stage.getIcons().add(icon);
            stage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onVerser(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Virement.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            Image icon = new Image(getClass().getResourceAsStream("icon.png"));
            stage.getIcons().add(icon);
            stage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }


    }
       private void clearInput() {
        this.SoldeField.setText("");
        this.decouvertField.setText("");
        this.maxDecouvertField.setText("");
        this.maxRetraitField.setText("");
        this.combo.getSelectionModel().clearSelection();
        }


    private boolean isInputValid() {
        if (this.combo.getValue()==null || this.SoldeField.getText().isEmpty() || this.decouvertField.getText().isEmpty() || this.maxDecouvertField.getText().isEmpty() || this.maxRetraitField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Input error!");
            alert.setHeaderText(null);
            alert.setContentText("Les champs ne doivent pas etre vide!");
            alert.show();
            return false;
        }
        return true;
    }

    private boolean isInputValid1() {
        if (SoldeField.getText().matches("[^a-zA-Z]") || decouvertField.getText().matches("[^a-zA-Z]") || maxDecouvertField.getText().matches("[^a-zA-Z]") ||maxRetraitField.getText().matches("[^a-zA-Z]")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Input error!");
            alert.setHeaderText(null);
            alert.setContentText("Les champs doivent etre de type numérique!");
            alert.show();
            return false;
        }
        return true;
    }




}





