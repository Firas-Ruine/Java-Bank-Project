package Application;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TransactionController {
    Connection conn=null;
    public TransactionController() {
        conn= SingletonConnection.getConnection();
    }
    @FXML
    private ComboBox<Integer> combo2;

    @FXML
    private TextField montanttf;

    @FXML
    private Button retraitBtn;

    @FXML
    private Button versementBtn;

    @FXML
    List<Integer> onCombo2 () throws SQLException {

        List<Integer> options = new ArrayList<>();

        String query = "Select idcompte from Compte";
        PreparedStatement st = (PreparedStatement) conn.prepareStatement(query);
        ResultSet rs = (ResultSet) st.executeQuery();
        while (rs.next()) {
            options.add(rs.getInt("idcompte"));

        }
        return options;
    }

    public void initialize() throws SQLException {

        combo2.setItems(FXCollections.observableArrayList(onCombo2()));

    }

    @FXML
    void onRetrait(ActionEvent event) throws SQLException {
        if (this.isInputValid()) {
            double solde = 0;
            double MaxDec = 0;
            double nvsolde = 0;
            int id = combo2.getValue();
            PreparedStatement x1 = (PreparedStatement) conn.prepareStatement("select solde,maxdecouvert,maxretrait from compte where idcompte=?");
            x1.setInt(1, id);
            ResultSet rs = x1.executeQuery();
            double MaxRet = 0;
            while (rs.next()) {
                Compte c = new Compte();
                c.setSolde(rs.getDouble("solde"));
                c.setMaxDecouvert(rs.getInt("maxdecouvert"));
                c.setMaxRetrait(rs.getInt("maxretrait"));
                solde= c.getSolde();
                MaxDec = c.getMaxDecouvert();
                MaxRet = c.getMaxRetrait();
            }
            nvsolde = solde;
            Double montant = Double.parseDouble(montanttf.getText());
            if (montant <= (solde + MaxDec) && montant <= MaxRet) {
                nvsolde = solde - montant;
                PreparedStatement x = (PreparedStatement) conn.prepareStatement("update compte set solde=? where idcompte=?");
                x.setDouble(1, nvsolde);
                x.setInt(2, id);
                x.execute();
            } else {

                Alert alert = new Alert(Alert.AlertType.ERROR, "Retrait échoué", ButtonType.OK);
                alert.showAndWait();
            }

            if (nvsolde < 0) {
                PreparedStatement x = (PreparedStatement) conn.prepareStatement("update compte set solde='" + nvsolde + "',decouvert=-solde where idcompte='" + id + "'");
                x.execute();
            } else {
                PreparedStatement x = (PreparedStatement) conn.prepareStatement("update compte set solde='" + nvsolde + "',decouvert=0 where idcompte='" + id + "'");
                x.execute();
            }
        }
    }


    @FXML
    void onVerser(ActionEvent event) throws SQLException {
        if (this.isInputValid()) {
            double solde = 0;
            int id = combo2.getValue();
            PreparedStatement x1 = (PreparedStatement) conn.prepareStatement("select solde from compte where idcompte='" + id + "'");
            ResultSet rs = x1.executeQuery();
            while (rs.next()) {
                Compte c = new Compte();
                c.setSolde(rs.getDouble("solde"));
                solde = c.getSolde();
            }
            Double montant = Double.parseDouble(montanttf.getText()) + solde;

            PreparedStatement x = (PreparedStatement) conn.prepareStatement("update compte set solde=? where idcompte='" + id + "'");
            x.setDouble(1, montant);
            x.execute();
        if (montant < 0) {
            PreparedStatement x2 = (PreparedStatement) conn.prepareStatement("update compte set solde='" + montant + "',decouvert=-solde where idcompte='" + id + "'");
            x2.execute();
        } else {
            PreparedStatement x2 = (PreparedStatement) conn.prepareStatement("update compte set solde='" + montant + "',decouvert=0 where idcompte='" + id + "'");
            x2.execute();
        }
    }}

    private boolean isInputValid() {
        if (this.combo2.getValue()==null || this.montanttf.getText().isEmpty()) {
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
