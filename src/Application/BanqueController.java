package Application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class BanqueController {

    @FXML
    private BorderPane bp;

    @FXML
    private AnchorPane ap;

    @FXML
    private Button compteBtn;

    @FXML
    private Button clientBtn;

    @FXML
    void clientBtnSubmit(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Client.fxml"));
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
    void compteBtnSubmit(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Compte.fxml"));
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

}

