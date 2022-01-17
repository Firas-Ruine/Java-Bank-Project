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
import java.util.Optional;

public class VirementController {
    Connection conn=null;
    public VirementController() {
        conn= SingletonConnection.getConnection();
    }
    @FXML
    private TextField iddestesttf;

    @FXML
    private TextField montanttf;
    @FXML
    private ComboBox<Integer> combo;
    @FXML
    private ComboBox<Integer> combo1;
    @FXML
    private Button envoieBtn;

    @FXML
    private TextField idexpxptf;

    @FXML
    List<Integer> onCombo1 () throws SQLException {

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

        combo.setItems(FXCollections.observableArrayList(onCombo1()));
        combo1.setItems(FXCollections.observableArrayList(onCombo1()));

    }

    @FXML
    void onSubmit(ActionEvent event) throws SQLException {
        if (this.isInputValid()) {
        double soldexp = 0;
        double soldedest = 0;
        double Maxdec = 0;
        double nvsolderet = 0;
        double nvsoldevers = 0;

        int idd = combo.getValue();
        int ide = combo1.getValue();



            PreparedStatement x1 = (PreparedStatement) conn.prepareStatement("select solde,maxdecouvert from compte where idcompte='" + ide + "'");
            PreparedStatement x3 = (PreparedStatement) conn.prepareStatement("select solde from compte where idcompte='" + idd + "'");
            ResultSet rs = x1.executeQuery();
            while (rs.next()) {
                Compte c = new Compte();
                c.setSolde(rs.getDouble("solde"));
                c.setMaxDecouvert(rs.getDouble("maxdecouvert"));
                soldexp = c.getSolde();
                Maxdec = c.getMaxDecouvert();
            }
            ResultSet rs1 = x3.executeQuery();
            while (rs1.next()) {
                Compte c = new Compte();
                c.setSolde(rs1.getDouble("solde"));
                soldedest = c.getSolde();
            }
            nvsolderet = soldexp;
            Double montant = Double.parseDouble(montanttf.getText());
            if (montant <= (soldexp + Maxdec)) {
                nvsolderet = soldexp - montant;
                nvsoldevers = soldedest + montant;
                PreparedStatement x = (PreparedStatement) conn.prepareStatement("update compte set solde='" + nvsolderet + "'where idcompte='" + ide + "'");
                x.execute();
                PreparedStatement x2 = (PreparedStatement) conn.prepareStatement("update compte set solde='" + nvsoldevers + "'where idcompte='" + idd + "'");
                x2.execute();

            } else {

                Alert alert = new Alert(Alert.AlertType.ERROR, "Virement echoué", ButtonType.OK);
                alert.showAndWait();
            }
            if (nvsolderet < 0) {
                PreparedStatement x = (PreparedStatement) conn.prepareStatement("update compte set solde='" + nvsolderet + "',decouvert=-solde where idcompte='" + ide + "'");
                x.execute();
            } else {
                PreparedStatement x = (PreparedStatement) conn.prepareStatement("update compte set solde='" + nvsolderet + "',decouvert=0 where idcompte='" + ide + "'");
                x.execute();
            }
            if (nvsoldevers >= 0) {
                PreparedStatement x2 = (PreparedStatement) conn.prepareStatement("update compte set solde='" + nvsoldevers + "',decouvert=0 where idcompte='" + idd + "'");
                x2.execute();

            } else {
                PreparedStatement x2 = (PreparedStatement) conn.prepareStatement("update compte set solde='" + nvsoldevers + "',decouvert=-solde where idcompte='" + idd + "'");
                x2.execute();
            }
        }
    }

    private boolean isInputValid() {
        if ( this.montanttf.getText().isEmpty()||combo.getValue()==null || combo1.getValue()==null) {
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




