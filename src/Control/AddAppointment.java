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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import Control.ReturnToMainPage;
import javafx.util.converter.DateTimeStringConverter;
import sample.Main;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.*;
import java.util.List;

import static Control.Validation.*;

public class AddAppointment implements Initializable {
    public Button cancelButton;
    public TextField startTimeField;
    public Button addAppointmentButton;
    public TextField endTimeField;
    public ComboBox customerIdField;
    public TextField appointmentIdField;
    public TextField titleField;
    public TextField descriptionField;
    public TextField locationField;
    public TextField contactField;
    public TextField typeField;
    public TextField userIdField;
    public DatePicker startTimeDate;
    public DatePicker endTimeDate;
    public ComboBox contactComboBox;
    public Label businessHoursLabel;

    AppointmentDAOImp appointmentDAOImp = new AppointmentDAOImp();
   ContactDAOImp contactDAOImp = new ContactDAOImp();
    CustomerDAOImp customerDAOImp = new CustomerDAOImp();
    ObservableList<Customer> customers = customerDAOImp.getAllCustomers();
    ObservableList<Contact> contacts = contactDAOImp.getAllContacts();


    public AddAppointment() throws SQLException {

    }

    /**
     * returns to the main page
     * @param actionEvent listens for cancel button to be clicked
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     */
    public void onCancel(ActionEvent actionEvent) throws IOException {
        ReturnToMainPage returnToMainPage = new ReturnToMainPage();
        returnToMainPage.returnToMain(actionEvent);
    }

    /**
     * adds appointment to the appointment table and saves it to the database.<br>
     * lambda expression: iterates through contacts and pass the contact Id to appointment DAO
     * @param actionEvent listens for add appointment button to be clicked
     * @throws ParseException Signals that an error has been reached unexpectedly while parsing.
     * @throws SQLException An exception that provides information on a database access error or other errors.
     * @throws IOException signals that an I/O exception of some sort has occurred
     */
    public void onAddAppointment(ActionEvent actionEvent) throws ParseException, SQLException, IOException {
        if (Validation.ValidComboBoxAndDates(startTimeField.getText(), startTimeDate,
                endTimeDate, endTimeField.getText(), contactComboBox, customerIdField).equals("no conflict")) {

            String startDate = startTimeDate.getValue().toString() + " "
                    + startTimeField.getText();
            String endDate = endTimeDate.getValue().toString() + " "
                    + endTimeField.getText();

            ZonedDateTime zdt = TimeFunctions.sqlDateTimeFormat(startDate, "yyyy-MM-dd HH:mm").atZone(ZoneId.systemDefault());
            ZonedDateTime zonedStartTime = zdt.withZoneSameInstant(ZoneId.of(ZoneId.systemDefault().toString()));
            ZonedDateTime zdt2 = TimeFunctions.sqlDateTimeFormat(endDate, "yyyy-MM-dd HH:mm").atZone(ZoneId.systemDefault());
            ZonedDateTime zonedEndTime = zdt2.withZoneSameInstant(ZoneId.of(ZoneId.systemDefault().toString()));

            int autoId = appointmentDAOImp.getAllAppointments().size() + 1;
            int userId = 1;

            contacts.forEach(contact -> {
                if (contactComboBox.getValue().toString().equals(contact.getContact())) {
                    AppointmentDAOImp.passContactId(contact.getContactId());
                }

            });

                Appointment newAppointment = new Appointment(
                        autoId,
                        titleField.getText(),
                        descriptionField.getText(),
                        locationField.getText(),
                        contactComboBox.getValue().toString(),
                        typeField.getText(),
                        zonedStartTime.toLocalDateTime().toString().replace("T", " "),
                        zonedEndTime.toLocalDateTime().toString().replace("T", " "),
                        Integer.parseInt(customerIdField.getValue().toString()),
                        Appointment.getUser().getUserId()
                );

                if (Validation.appValidationHub(newAppointment).equals("no conflict")) {
                    appointmentDAOImp.addAppointment(newAppointment);
                    appointments.add(newAppointment);
                    ReturnToMainPage returnToMainPage = new ReturnToMainPage();
                    returnToMainPage.returnToMain(actionEvent);
                } else {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, Validation.appValidationHub(newAppointment));
                    alert.showAndWait();
                }
            } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, Validation.ValidComboBoxAndDates(startTimeField.getText(), startTimeDate,
                    endTimeDate, endTimeField.getText(), contactComboBox, customerIdField));
            alert.showAndWait();
            }
    }


    /**
     * set local business hour label.
     * populate the combo boxes
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ZonedDateTime zdt = LocalDateTime.now().atZone(ZoneId.of("America/New_York"));
        ZonedDateTime startTimeZdt = null;
        ZonedDateTime endTimeZdt = null;

        int hour = zdt.getHour();
        int minute = zdt.getMinute();
        if (hour > 8) {
            startTimeZdt = zdt.minusHours(hour - 8);
        } else {
            startTimeZdt = zdt.plusHours(8 - hour);
        }
        if (hour > 22) {
            endTimeZdt = zdt.minusHours(hour - 22);
        } else { endTimeZdt = zdt.plusHours(22 - hour);}

        ZonedDateTime zonedStartTime = startTimeZdt.minusMinutes(minute).withZoneSameInstant(ZoneId.of(ZoneId.systemDefault().toString()));
        ZonedDateTime zonedEndTime = endTimeZdt.minusMinutes(minute).withZoneSameInstant(ZoneId.of(ZoneId.systemDefault().toString()));
        String businessText = "";

        if (zonedStartTime.getMinute() < 10 && zonedEndTime.getMinute() < 10) {
            businessText = "Business hours: " + zonedStartTime.getHour() + ":0" + zonedStartTime.getMinute()
                    + " - " + zonedEndTime.getHour() + ":0" + zonedEndTime.getMinute();
        } else if (zonedEndTime.getMinute() < 10) {
            businessText = "Business hours: " + zonedStartTime.getHour() + ":" + zonedStartTime.getMinute()
                    + " - " + zonedEndTime.getHour() + ":0" + zonedEndTime.getMinute();
        }else {
            businessText = "Business hours: " + zonedStartTime.getHour() + ":0" + zonedStartTime.getMinute()
                    + " - " + zonedEndTime.getHour() + ":" + zonedEndTime.getMinute();
        }
        businessHoursLabel.setText(businessText);



        for (Customer customer : customers) {
            customerIdField.getItems().add(customer.getCustomerId());
        }

        for (Contact contact: contacts) {
            contactComboBox.getItems().add(contact.getContact());
        }

    }


    public void onSelectCustomer(ActionEvent actionEvent) {
    }
}
