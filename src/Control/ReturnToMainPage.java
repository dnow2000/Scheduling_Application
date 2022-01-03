package Control;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class ReturnToMainPage {

    /**
     * repeated code used to return to the main page
     * @param actionEvent listens for scene change
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     */
    public void returnToMain(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../sample/mainPage.fxml")));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 950, 460);
        stage.setTitle("Main page");
        stage.setScene(scene);
        stage.show();
    }
}
