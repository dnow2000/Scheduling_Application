package Control;

import DAOImplementation.AppointmentDAOImp;
import Model.Appointment;
import Model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * validate the data for updating and adding appointments and customers
 */
public class Validation {
    static AppointmentDAOImp appointmentDAOImp = new AppointmentDAOImp();
    static ObservableList<Appointment> appointments;

    static {
        try {
            appointments = appointmentDAOImp.getAllAppointments();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * check that one customer's appointment does not have a time conflict with other appointments
     * @param newAppointment Appointment object
     * @return returns a string to be used for alert message box
     * @throws SQLException An exception that provides information on a database access error or other errors
     */
    // validation function
    public static String checkAppointmentConflict(Appointment newAppointment) throws SQLException {
        ObservableList<Appointment> customerApps = FXCollections.observableArrayList();
        LocalDateTime start = TimeFunctions.sqlDateTimeFormat(newAppointment.getStartTimeAndDate(), "yyyy-MM-dd HH:mm");
        LocalDateTime end = TimeFunctions.sqlDateTimeFormat(newAppointment.getEndTimeAndDate(), "yyyy-MM-dd HH:mm");

        //start date has to be before end date
        if (start.isAfter(end)) {
            return "the start date needs to be before the end date";
        }


        for (Appointment appointment : appointments) {
            if (appointment.getCustomerID() == newAppointment.getCustomerID()) {
                customerApps.add(appointment);
            }
        }

        for (Appointment custApp : customerApps) {
            LocalDateTime start2 = TimeFunctions.sqlDateTimeFormat(custApp.getStartTimeAndDate(), "yyyy-MM-dd HH:mm");
            LocalDateTime end2 = TimeFunctions.sqlDateTimeFormat(custApp.getEndTimeAndDate(), "yyyy-MM-dd HH:mm");


            if (custApp.getAppointmentID() != newAppointment.getAppointmentID()) {
                if (end.isAfter(start2) && end.isBefore(end2) || end.isEqual(start2) || end.isEqual(end2)) {
                    System.out.print("1");
                    return "the dates you have chosen conflict with other customer appointments";
                }
                if (start2.isAfter(start) && start2.isBefore(end) || start2.isEqual(start) || start2.isEqual(end)) {
                    System.out.print("2");
                    return "the dates you have chosen conflict with other customer appointments";
                }
                if (start2.isBefore(start) && end2.isAfter(end)) {
                    System.out.print("3");
                    return "the dates you have chosen conflict with other customer appointments";
                }
                if (start.isAfter(start2) && start.isBefore(end2)){
                    System.out.print("4");
                    return "the dates you have chosen conflict with other customer appointments";
                }
            }

        }
        return "no conflict";
    }

    /**
     * check that appointments are within the business hours of the company
     * @param newAppointment Appointment object
     * @return returns a string
     */
    public static String withinBusinessHours(Appointment newAppointment) {

        ZonedDateTime zdt = LocalDateTime.now().atZone(ZoneId.of("America/New_York"));
        ZonedDateTime startTimeZdt = null;
        ZonedDateTime endTimeZdt = null;
        LocalDateTime startLocalDateTime = TimeFunctions.sqlDateTimeFormat(newAppointment.getStartTimeAndDate(), "yyyy-MM-dd HH:mm");
        LocalDateTime endLocalDateTime = TimeFunctions.sqlDateTimeFormat(newAppointment.getEndTimeAndDate(), "yyyy-MM-dd HH:mm");

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

        if (startLocalDateTime.getHour() > zonedEndTime.getHour() || startLocalDateTime.getHour() < zonedStartTime.getHour()) {
            return "dates need to be within the business hours of 8:00am - 22:00pm";
        }else if (endLocalDateTime.getHour() > zonedEndTime.getHour() || endLocalDateTime.getHour() < zonedStartTime.getHour()) {
            return "dates need to be within the business hours of 8:00am - 22:00pm";
        }else {
            return "no conflict";
        }

    }

    /**
     * field validation for appointments
     * @param newAppointment Appointment object
     * @return returns a string
     */
    public static String appFieldValidation(Appointment newAppointment) {
        try {
            if (newAppointment.getDescription().equals("")) {return "fill in empty fields";}
            Integer.parseInt(newAppointment.getDescription());
            return "description needs to be alphanumeric";
        }catch (Exception ignored) {}
        try {
            if (newAppointment.getType().equals("")) {return "fill in empty fields";}
            Integer.parseInt(newAppointment.getType());
            return "type needs to be alphanumeric";
        }catch (Exception ignored) {}
        try {
            if (newAppointment.getLocation().equals("")) {return "fill in empty fields";}
            Integer.parseInt(newAppointment.getLocation());
            return "location needs to be alphanumeric";
        }catch (Exception ignored) {}
        try {
            if (newAppointment.getStartTimeAndDate().equals("")) {return "fill in empty fields";}
        }catch (Exception ignored) {}
        return "no conflict";
    }

    /**
     * function that calls other functions to validate appointments
     * @param newAppointment Appointment Object
     * @return
     * @throws SQLException
     */
    public static String appValidationHub(Appointment newAppointment) throws SQLException {
        if (!checkAppointmentConflict(newAppointment).equals("no conflict")) {
            return checkAppointmentConflict(newAppointment);
        } else if (!withinBusinessHours(newAppointment).equals("no conflict")){
            return withinBusinessHours(newAppointment);
        }else if (!appFieldValidation(newAppointment).equals("no conflict")) {
            return appFieldValidation(newAppointment);
        }else{
            return "no conflict";
        }
    }

    /**
     * validates the datepickers and comboboxes for an appointment. meant to put this in appFieldValidation() but
     * generates error if combo boxes and date pickers do not have data when creating an appointment object
     * @param startTime String value
     * @param startDate DatePicker
     * @param endDate DatePicker
     * @param endTime String
     * @param contact ComboBox
     * @param customer ComboBox
     * @return returns a string
     */
    public static String ValidComboBoxAndDates(String startTime, DatePicker startDate, DatePicker endDate,
                                               String endTime, ComboBox contact, ComboBox customer) {

        if (startDate.getValue() == null || contact.getValue() == null || endDate.getValue() == null
        || customer.getValue() == null) {
            return "fill in empty fields";
        }

        if (startTime.equals("") || endTime.equals("")) {
            return "fill in empty fields";
        }else {
            return "no conflict";
        }
    }
    /////////////////////////////////// Customer Validation ///////////////////////////////////////////////////////

    /**
     * validates customer fields
     * @param customerNameField TextField
     * @param addressField TextField
     * @param postalCodeField TextField
     * @param phoneNumField TextField
     * @param divisionComboBox ComboBox
     * @param countryComboBox ComboBox
     * @return returns a String
     */
    public static String validCustomerFields(TextField customerNameField, TextField addressField,
            TextField postalCodeField, TextField phoneNumField,
            ComboBox divisionComboBox, ComboBox countryComboBox) {

        if (!(customerNameField.getText().equals("") || addressField.getText().equals("") || postalCodeField.getText().equals(""))) {
            String phoneNum = phoneNumField.getText();
            if (phoneNumField.getText().contains("-")) {
                phoneNum = phoneNumField.getText().replace("-", "");
            }

            try {
                Long.parseLong(phoneNum);
            } catch (Exception ignored) {
                return "fill in a valid phone number. ex: 1234567891";
            }


            if (phoneNum.length() < 10) {
                return "fill in a valid phone number. ex: 1234567891";
            } else if (divisionComboBox.getValue() == null || countryComboBox.getValue() == null){
                return "fill in empty fields";
            }else {
                return "no conflict";
            }
        }else{
            return "fill in empty fields";
        }
    }
}
