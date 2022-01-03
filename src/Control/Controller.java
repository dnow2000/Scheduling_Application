package Control;

import DAOImplementation.AppointmentDAOImp;
import DAOImplementation.UserDAOImp;
import Database.DBConnection;
import Database.DBQuery;
import Model.Appointment;
import Model.Customer;
import Model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class Controller implements Initializable {

    public TextField userField;
    public Label userLabel;
    public Label passwordLabel;
    public TextField passField;
    public Button loginButton;
    public Label locationLabel;
    public Label loginLabel;


    Writer output = new BufferedWriter(new FileWriter("login_activity.txt", true));  //clears file every time

    public Controller() throws IOException {
    }

    /**
     * changes all the labels to french depending on location of the user
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       Boolean locationIsFrance = false;
        try {
            rb = ResourceBundle.getBundle("Nat", Locale.getDefault());
            locationIsFrance = Locale.getDefault().getLanguage().equals("fr");
        } catch (Exception e) {

        }


        if(locationIsFrance) {
            locationLabel.setText("location: " + TimeFunctions.getZonedTime(LocalDateTime.now()).getZone().toString());
            userLabel.setText(rb.getString("username"));
            userLabel.setMinWidth(150);
            userLabel.setTranslateX(-20);

            passwordLabel.setText(rb.getString("password"));
            passwordLabel.setMinWidth(150);
            passwordLabel.setTranslateX(-15);

            loginLabel.setText(rb.getString("login"));
            loginLabel.setMinWidth(150);
            loginLabel.setTranslateX(-15);

            loginButton.setText(rb.getString("login"));
            loginButton.setMinWidth(150);
            loginButton.setTranslateX(-15);

        }else {
            locationLabel.setText("location: " + TimeFunctions.getZonedTime(LocalDateTime.now()).getZone().toString());
        }
    }

    /**
     * Validates username and password
     * @param actionEvent listens for login button to be clicked
     * @throws SQLException An exception that provides information on a database access error or other errors.
     * @throws IOException Signals that an I/O exception of some sort has occurred
     */
    public void onLogin(ActionEvent actionEvent) throws SQLException, IOException {
        Boolean locationIsFrance = Locale.getDefault().getLanguage().equals("fr");

        String username = userField.getText();
        String password = passField.getText();

        Connection conn = DBConnection.getConnection();
        DBQuery.setStatement(conn);
        Statement statement = DBQuery.getStatement();

        String usersSelect = "SELECT * FROM users";
        ResultSet rs = statement.executeQuery(usersSelect);

        while (rs.next()) {
            if (rs.getString("User_Name").equals(username) && rs.getString("Password").equals(password)) {
                    output.append("User: " + rs.getString("User_Name") +", login attempt successful, Timestamp = " +
                            LocalDateTime.now().toString().replace("T", " ") + "\n");
                    output.close();
                    User user = new User(rs.getInt("User_ID"), username, password);
                    Appointment.setPassUser(user);
                    Customer.setPassUser(user);

                    Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../sample/mainPage.fxml")));
                    Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                    Scene scene = new Scene(root, 950, 460);
                    stage.setTitle("Main page");
                    stage.setScene(scene);
                    stage.show();

                    checkUpcomingAppointments();
                    break;

            }
            else if (locationIsFrance){
                output.append("login attempt Failed, Timestamp = " +
                        LocalDateTime.now().toString().replace("T", " ") + "\n");
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "le nom d'utilisateur ou le mot de passe n'existe pas");
                alert.showAndWait();
                break;
            } else {
                output.append("login attempt Failed, Timestamp = " +
                        LocalDateTime.now().toString().replace("T", " ") + "\n");
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "username or password does not exist");
                alert.showAndWait();
                break;
            }
        }


    }

    /**
     * shows upcoming appointments
     * @throws SQLException
     */
    public void checkUpcomingAppointments() throws SQLException {
        AppointmentDAOImp appointmentDAOImp = new AppointmentDAOImp();
        ObservableList<Appointment> appointments = appointmentDAOImp.getAllAppointments();
        ////////////////////////////////////// upcoming appointments //////////////////////////////////////////////////
        String displayApp = "";
        for (Appointment appointment : appointments) {
            LocalDateTime fifteenMinuteFrame =
                    TimeFunctions.sqlDateTimeFormat(LocalDateTime.now().toString().
                            replace("T", " ").substring(0, 16), "yyyy-MM-dd HH:mm").plusMinutes(15);

            LocalDateTime now = fifteenMinuteFrame.minusMinutes(15);
            LocalDateTime startTime = TimeFunctions.sqlDateTimeFormat(appointment.getStartTimeAndDate(), "yyyy-MM-dd HH:mm");

            if (startTime.isAfter(now) && startTime.isBefore(fifteenMinuteFrame)) {
                displayApp += "\nid: " + appointment.getAppointmentID() + "\nstart-date/time: " + appointment.getStartTimeAndDate() +
                        "\nend-date/time: " + appointment.getEndTimeAndDate() + "\n--------------------------";
            }

        }

        if (displayApp.length() == 0) {
            displayApp = "no upcoming appointments";
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"upcoming appointment(s):\n" +  displayApp);
        alert.showAndWait();
    }
}
