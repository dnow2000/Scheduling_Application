package DAOImplementation;

import Control.TimeFunctions;
import DAOInterfaces.AppointmentDAO;
import Database.DBConnection;
import Database.DBQuery;
import Model.Appointment;
import Model.Contact;
import Model.Customer;
import Model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.converter.DateTimeStringConverter;

import java.sql.*;
import java.time.*;
import java.util.Calendar;
import java.util.Date;

public class AppointmentDAOImp implements AppointmentDAO {
    static ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();

    private static Integer passContactId = null;
    public static void passContactId(Integer id) {passContactId = id;}


    /**
     * fetches appointment data from the database to be used in the application
     * @return returns an Observable list of appointments
     * @throws SQLException
     */
    @Override
    public ObservableList<Appointment> getAllAppointments() throws SQLException {
        ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
        Connection conn = DBConnection.getConnection();
        DBQuery.setStatement(conn);
        Statement statement = DBQuery.getStatement();

        String appointmentsSelect = "SELECT a.*, c.Contact_Name FROM appointments AS a\n" +
                "JOIN contacts AS c ON a.Contact_ID = c.Contact_ID;";
        ResultSet rs = statement.executeQuery(appointmentsSelect);

        if (allAppointments.size() <= 1) {
        while (rs.next()) {
            int appointmentID = rs.getInt("Appointment_ID");
            String title = rs.getString("Title");
            String description = rs.getString("Description");
            String location = rs.getString("Location");
            String contact = rs.getString("Contact_Name");
            String type = rs.getString("Type");
            Timestamp getStartTime = rs.getTimestamp("Start");
            Timestamp getEndTime = rs.getTimestamp("End");
            int customerID = rs.getInt("Customer_ID");
            int userID = rs.getInt("User_ID");

                String startTimeAndDate = TimeFunctions.displayDateTimeFormat(getStartTime.toString(), "yyyy-MM-dd HH:mm");
                String endTimeAndDate = TimeFunctions.displayDateTimeFormat(getEndTime.toString(), "yyyy-MM-dd HH:mm");

                ZonedDateTime zdt = TimeFunctions.sqlDateTimeFormat(startTimeAndDate, "yyyy-MM-dd HH:mm").atZone(ZoneId.systemDefault());
                ZonedDateTime zonedStartTime = zdt.withZoneSameInstant(ZoneId.of(ZoneId.systemDefault().toString()));
                ZonedDateTime zdt2 = TimeFunctions.sqlDateTimeFormat(endTimeAndDate, "yyyy-MM-dd HH:mm").atZone(ZoneId.systemDefault());
                ZonedDateTime zonedEndTime = zdt2.withZoneSameInstant(ZoneId.of(ZoneId.systemDefault().toString()));


                Appointment appointment = new Appointment(appointmentID, title, description, location, contact, type,
                        zonedStartTime.toLocalDateTime().toString().replace("T", " "),
                        zonedEndTime.toLocalDateTime().toString().replace("T", " "), customerID, userID);
                allAppointments.add(appointment);
            }
        }

        return allAppointments;
    }

    /**
     * updates appointment data in the database
     * @param appointment Appointment object
     * @throws SQLException
     */
    @Override
    public void updateAppointment(Appointment appointment) throws SQLException {
        Connection conn = DBConnection.getConnection();
        DBQuery.setStatement(conn);
        Statement statement = DBQuery.getStatement();

        ZonedDateTime zdtStart = TimeFunctions.getZonedTime(TimeFunctions.sqlDateTimeFormat(appointment.getStartTimeAndDate(), "yyyy-MM-dd HH:mm"));
        ZonedDateTime zdtEnd = TimeFunctions.getZonedTime(TimeFunctions.sqlDateTimeFormat(appointment.getEndTimeAndDate(), "yyyy-MM-dd HH:mm"));
        LocalDateTime utcStartTime = TimeFunctions.convertToUTC(zdtStart);
        LocalDateTime utcEndTime = TimeFunctions.convertToUTC(zdtEnd);

        String dateTimeNow = TimeFunctions.instantTime();

        String appointmentsSelect = "UPDATE appointments\n " +
                "SET Title = '" + appointment.getTitle() + "', Description = '" + appointment.getDescription() + "', Location = '" + appointment.getLocation() + "', \n" +
                "Type = '" + appointment.getType() + "', Start = '" + Timestamp.valueOf(utcStartTime) + "', End = '" + Timestamp.valueOf(utcEndTime) + "'," + "\n" +
                "Last_Update = " + "('" + dateTimeNow + "')" + ", Last_Updated_By = '" + appointment.getUser().getUsername() + "'," + "Customer_ID = " + appointment.getCustomerID() + ", User_ID = " + appointment.getUser().getUserId()  + ", \n" +
                "Contact_ID = " + passContactId + "\n" +
                "WHERE Appointment_ID = " + appointment.getAppointmentID() + ";";
        statement.executeUpdate(appointmentsSelect);


    }

    /**
     * deletes appointment from the database
     * @param appointmentId Integer
     * @throws SQLException
     */
    @Override
    public void deleteAppointment(int appointmentId) throws SQLException {
        Connection conn = DBConnection.getConnection();
        DBQuery.setStatement(conn);
        Statement statement = DBQuery.getStatement();

        String delete = "DELETE FROM appointments WHERE Appointment_ID = " + appointmentId;
        statement.executeUpdate(delete);
    }

    /**
     * inserts appointment to the database
     * @param newAppointment Appointment object
     * @throws SQLException
     */
    @Override
    public void addAppointment(Appointment newAppointment) throws SQLException {
        Connection conn = DBConnection.getConnection();
        DBQuery.setStatement(conn);
        Statement statement = DBQuery.getStatement();

        // Convert to utc
        ZonedDateTime zdtStart= TimeFunctions.getZonedTime(TimeFunctions.sqlDateTimeFormat(newAppointment.getStartTimeAndDate(), "yyyy-MM-dd HH:mm"));
        ZonedDateTime zdtEnd = TimeFunctions.getZonedTime(TimeFunctions.sqlDateTimeFormat(newAppointment.getEndTimeAndDate(), "yyyy-MM-dd HH:mm"));
        LocalDateTime utcStartTime = TimeFunctions.convertToUTC(zdtStart);
        LocalDateTime utcEndTime = TimeFunctions.convertToUTC(zdtEnd);
        LocalDateTime timeCreated = TimeFunctions.convertToUTC(TimeFunctions.getZonedTime(LocalDateTime.now()));


        String dateTimeNow = TimeFunctions.instantTime();

        String insert =  "INSERT INTO appointments (Title, Description, Location, Type, Start, End, Create_Date, Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID)\n" +
                "VALUES ( " + "'" + newAppointment.getTitle() + "'" + ", " + "'" + newAppointment.getDescription() + "'" + ", " + "'" + newAppointment.getLocation() + "'" + ", \n" +
                "'" + newAppointment.getType() + "'" + ", " + "('" + Timestamp.valueOf(utcStartTime) + "'), ('" + Timestamp.valueOf(utcEndTime) + "'), " + "('" + dateTimeNow + "')" + ",'" + newAppointment.getUser().getUsername() + "', ('" + dateTimeNow  + "'), \n'" +
                newAppointment.getUser().getUsername() + "'," + newAppointment.getCustomerID() + ", " + newAppointment.getUser().getUserId() + ", " + passContactId + ");";

        statement.executeUpdate(insert);

        allAppointments.add(newAppointment);
    }




}
