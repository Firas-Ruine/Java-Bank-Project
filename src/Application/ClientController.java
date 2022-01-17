package Application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import static javax.xml.bind.DatatypeConverter.parseDouble;
import static javax.xml.bind.DatatypeConverter.parseInt;

public class ClientController {

    Connection conn = null;

    public ClientController() {
        conn = SingletonConnection.getConnection();
    }

    @FXML
    private Button btnSubmit;


    @FXML
    private TextField NomField;

    @FXML
    private TextField PrenomField;

    @FXML
    private TextField AdresseField;

    @FXML
    private TableView<Client> table;

    @FXML
    private TableColumn<Client, Integer> idCol;

    @FXML
    private TableColumn<Client, String> NomCol;

    @FXML
    private TableColumn<Client, String> PrenomCol;

    @FXML
    private TableColumn<Client, String> adresseCol;

    @FXML
    private Button btnImport;

    @FXML
    private Button btnmodify;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnExit;
    ObservableList<Client> list = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        System.out.println("Initializing...");

    }
     @FXML
    void OnSet(MouseEvent event) {
         String n;
         String p;
         String a;

        n = table.getSelectionModel().getSelectedItem().getNom();
        p = table.getSelectionModel().getSelectedItem().getPrenom();
        a = table.getSelectionModel().getSelectedItem().getAdresse();

        NomField.setText(String.valueOf(n));
        PrenomField.setText(String.valueOf(p));
        AdresseField.setText(String.valueOf(a));


    }

    @FXML
    void onDelete(ActionEvent event) throws SQLException {
        Alert alert= new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setContentText("Are you sure to delete ?");
        Optional<ButtonType> action = alert.showAndWait();
        if (action.get()==ButtonType.OK) {
            int idcli;
            idcli = table.getSelectionModel().getSelectedItem().getidclient();
            table.getItems().remove(table.getSelectionModel().getSelectedItem());
            PreparedStatement x = (PreparedStatement) conn.prepareStatement("delete from client where idclient='" + idcli + "'");
            x.execute();
        }
    }

    @FXML
    void onExit(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    void onImport(ActionEvent event) throws SQLException {
        list.clear();
        ResultSet rs = conn.createStatement().executeQuery("select * from client");

        while (rs.next()){
            list.add(new Client(rs.getInt("idClient"),rs.getString("Nom"),rs.getString("Prenom"),rs.getString("Adresse")));
        }
        idCol.setCellValueFactory(new PropertyValueFactory<>("idClient"));
        NomCol.setCellValueFactory(new PropertyValueFactory<>("Nom"));
        PrenomCol.setCellValueFactory(new PropertyValueFactory<>("Prenom"));
        adresseCol.setCellValueFactory(new PropertyValueFactory<>("Adresse"));
        table.setItems(list);
    }

    @FXML
    void onModify(ActionEvent event) throws SQLException {
      String n;
        String p;
        String a;
         int idcli;
         idcli=table.getSelectionModel().getSelectedItem().getidclient();
        n=NomField.getText();
        p=PrenomField.getText();
        a=AdresseField.getText();
        
            PreparedStatement x = (PreparedStatement) conn.prepareStatement("update client set nom='" + n + "',prenom='" + p + "', adresse='" + a +"' where idclient='" + idcli + "' ");
            x.execute();
            clearInput();
        list.clear();
        ResultSet rs = conn.createStatement().executeQuery("select * from client");

        while (rs.next()){
            list.add(new Client(rs.getInt("idClient"),rs.getString("Nom"),rs.getString("Prenom"),rs.getString("Adresse")));
        }
        idCol.setCellValueFactory(new PropertyValueFactory<>("idClient"));
        NomCol.setCellValueFactory(new PropertyValueFactory<>("Nom"));
        PrenomCol.setCellValueFactory(new PropertyValueFactory<>("Prenom"));
        adresseCol.setCellValueFactory(new PropertyValueFactory<>("Adresse"));
        table.setItems(list);




    }

    @FXML
    void onSubmit(ActionEvent event) throws SQLException {
        if (this.isInputValid()) {
            String nom = NomField.getText();
            String prenom = PrenomField.getText();
            String adresse = AdresseField.getText();

            PreparedStatement x = (PreparedStatement) conn.prepareStatement("insert into Client(nom,prenom,adresse) values(?,?,?)");

            x.setString(1, nom);
            x.setString(2, prenom);
            x.setString(3, adresse);
            x.execute();

            Client c = new Client(this.PrenomField.getText(), this.NomField.getText(), this.AdresseField.getText());
            table.getItems().add(c);
            this.clearInput();
      /*  try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("versement.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }*/
        }


    }

    private void clearInput() {
        this.PrenomField.setText("");
        this.NomField.setText("");
        this.AdresseField.setText("");
    }

    private boolean isInputValid() {
        if (this.PrenomField.getText().isEmpty() || this.NomField.getText().isEmpty() || this.AdresseField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Input error!");
            alert.setHeaderText(null);
            alert.setContentText("Les champs ne doivent pas etre vide!");
            alert.show();
            return false;
        }
        return true;
    }



}





