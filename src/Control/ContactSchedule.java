package Control;

import DAOImplementation.AppointmentDAOImp;
import Model.Appointment;
import Model.Contact;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class ContactSchedule implements Initializable {
    public TableView contactSchedTable;
    public TableColumn appointmentIdCol;
    public TableColumn titleCol;
    public TableColumn descriptionCol;
    public TableColumn locationCol;
    public TableColumn contactCol;
    public TableColumn typeCol;
    public TableColumn startCol;
    public TableColumn endCol;
    public TableColumn customerIdCol;
    public Label contactLabel;
    public Button goBackButton;
    AppointmentDAOImp appointmentDAOImp = new AppointmentDAOImp();
    ObservableList<Appointment> appointments = FXCollections.observableArrayList();

    private static Contact selectedContact = null;

    public static void passSelectedContact(Contact contact) {selectedContact = contact;}


    /**
     * initializes contact table with appointment data
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        contactLabel.setText(selectedContact.getContact() + "'s Schedule:");

        try {
            for (Appointment app : appointmentDAOImp.getAllAppointments()) {
                if (app.getContact().equals(selectedContact.getContact())) {
                    appointments.add(app);
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        contactSchedTable.setItems(orderByStartDates(appointments));

        appointmentIdCol.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        contactCol.setCellValueFactory(new PropertyValueFactory<>("contact"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        startCol.setCellValueFactory(new PropertyValueFactory<>("startTimeAndDate"));
        endCol.setCellValueFactory(new PropertyValueFactory<>("endTimeAndDate"));
        customerIdCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));
    }

    /**
     * Orders the contact schedule table according to appointment start dates
     * (most recent to oldest)
     * @param appointments observable list of appointments
     * @return returns a list of appointments
     */
    public ObservableList<Appointment> orderByStartDates(ObservableList<Appointment> appointments) {
        ObservableList<Appointment> orderedApps = FXCollections.observableArrayList();
        List<LocalDateTime> ldt = new ArrayList<>();
        Hashtable<String, Appointment> appointmentByDate = new Hashtable<>();

        for (Appointment appointment: appointments) {
           ldt.add(TimeFunctions.sqlDateTimeFormat(appointment.getStartTimeAndDate(), "yyyy-MM-dd HH:mm"));
           appointmentByDate.put(appointment.getStartTimeAndDate(), appointment);
        }

        Collections.sort(ldt);
        int i = 0;
        while (i <= ldt.size() -1) {
            orderedApps.add(appointmentByDate.get(ldt.get(i).toString().replace("T", " ")));
            i++;
        }

        return orderedApps;
       }

    public void goBack(ActionEvent actionEvent) throws IOException {
        ReturnToMainPage returnToMainPage = new ReturnToMainPage();
        returnToMainPage.returnToMain(actionEvent);
    }
}

