package Control;

import DAOImplementation.AppointmentDAOImp;
import DAOImplementation.ContactDAOImp;
import DAOImplementation.CustomerDAOImp;
import Model.Appointment;
import Model.Contact;
import Model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

public class UpdateAppointment implements Initializable {
    public Button cancelButton;
    public TextField appointmentIdField;
    public TextField titleField;
    public TextField descriptionField;
    public TextField locationField;
    public TextField contactField;
    public TextField typeField;
    public TextField startDateTimeField;
    public TextField endDateTimeField;
    public TextField customerIdField;
    public TextField endTime;
    public ComboBox contactComboBox;
    public TextField startTimeField;
    public DatePicker startDate;
    public DatePicker endDate;
    public ComboBox customerIdComboBox;
    public TextField userIdField;

    AppointmentDAOImp appointmentImp = new AppointmentDAOImp();
    ObservableList<Appointment> appointments = appointmentImp.getAllAppointments();
    ContactDAOImp contactDAOImp = new ContactDAOImp();

    private static Appointment dataToModify = null;

    public static void passDataToModify(Appointment appointment) {
        dataToModify = appointment;
    }

    public UpdateAppointment() throws SQLException {

    }

    public void onCancel(ActionEvent actionEvent) throws IOException {
        ReturnToMainPage returnToMainPage = new ReturnToMainPage();
        returnToMainPage.returnToMain(actionEvent);

    }

    /**
     * initializes the update Appointment page with data
     * forEach lambda expression to iterate through contacts and extract the contact name to
     * populate the contact comboBox table
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        LocalDateTime updateStart = TimeFunctions.sqlDateTimeFormat(dataToModify.getStartTimeAndDate(), "yyyy-MM-dd HH:mm");
        LocalDateTime updateEnd = TimeFunctions.sqlDateTimeFormat(dataToModify.getEndTimeAndDate(), "yyyy-MM-dd HH:mm");
        try {
            ObservableList<String> contacts = FXCollections.observableArrayList();
            contactDAOImp.getAllContacts().forEach(contact -> contacts.add(contact.getContact()));
            contactComboBox.setItems(contacts);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        // selected appointment data
        appointmentIdField.setText(dataToModify.getAppointmentID() + "");
        titleField.setText(dataToModify.getTitle());
        descriptionField.setText(dataToModify.getDescription());
        locationField.setText(dataToModify.getLocation());
        contactComboBox.setValue(dataToModify.getContact());
        typeField.setText(dataToModify.getType());
        startDate.setValue(updateStart.toLocalDate());
        startTimeField.setText(updateStart.toLocalTime().toString());
        customerIdComboBox.setValue(dataToModify.getCustomerID() + "");
        endDate.setValue(updateEnd.toLocalDate());
        endTime.setText(updateEnd.toLocalTime().toString());

        userIdField.setText(dataToModify.getUserID() + "");

    }

    /**
     * validates the user input and updates the appointment <br>
     * foreach lambda expression: iterate though contacts and pass contact id to AppointmentDAO
     * @param actionEvent
     * @throws SQLException
     * @throws IOException
     */
    public void onUpdateAppointment(ActionEvent actionEvent) throws SQLException, IOException {

        if (Validation.ValidComboBoxAndDates(startTimeField.getText(), startDate,
                endDate, endTime.getText(), contactComboBox, customerIdComboBox).equals("no conflict")) {

            ObservableList<Contact> contacts = contactDAOImp.getAllContacts();
            contacts.forEach(contact -> {
                if (contactComboBox.getValue().toString().equals(contact.getContact())) {
                    AppointmentDAOImp.passContactId(contact.getContactId());
                }
            });

            String start = startDate.getValue().toString() + " "
                    + startTimeField.getText();
            String end = endDate.getValue().toString() + " "
                    + endTime.getText();

            ZonedDateTime zdt = TimeFunctions.sqlDateTimeFormat(start, "yyyy-MM-dd HH:mm").atZone(ZoneId.systemDefault());
            ZonedDateTime zonedStartTime = zdt.withZoneSameInstant(ZoneId.of(ZoneId.systemDefault().toString()));
            ZonedDateTime zdt2 = TimeFunctions.sqlDateTimeFormat(end, "yyyy-MM-dd HH:mm").atZone(ZoneId.systemDefault());
            ZonedDateTime zonedEndTime = zdt2.withZoneSameInstant(ZoneId.of(ZoneId.systemDefault().toString()));

            Appointment updatedAppointment = new Appointment(
                    dataToModify.getAppointmentID(),
                    titleField.getText(),
                    descriptionField.getText(),
                    locationField.getText(),
                    contactComboBox.getValue().toString(),
                    typeField.getText(),
                    zonedStartTime.toLocalDateTime().toString().replace("T", " "),
                    zonedEndTime.toLocalDateTime().toString().replace("T", " "),
                    Integer.parseInt(customerIdComboBox.getValue().toString()),
                    dataToModify.getUserID()
            );
            if (Validation.appValidationHub(updatedAppointment).equals("no conflict")) {
                appointmentImp.updateAppointment(updatedAppointment);
                ReturnToMainPage returnToMainPage = new ReturnToMainPage();
                returnToMainPage.returnToMain(actionEvent);
            } else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, Validation.appValidationHub(updatedAppointment));
                alert.showAndWait();
            }
        }else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, Validation.ValidComboBoxAndDates(startTimeField.getText(), startDate,
                    endDate, endTime.getText(), contactComboBox, customerIdComboBox));
            alert.showAndWait();
        }
    }
}


