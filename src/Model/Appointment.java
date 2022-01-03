package Model;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;

public class Appointment {

    private static User passUser = null;
    public static void setPassUser(User user) {passUser = user;}

    private int appointmentID;
    private String title;
    private String description;
    private String location;
    private String contact;
    private String type;
    private String startTimeAndDate;
    private String endTimeAndDate;
    private int customerID;
    private int userID;
    //allAppointments (list)

    public Appointment(int appointmentID, String title, String description,
                    String location, String contact, String type,
                    String startTimeAndDate, String endTimeAndDate, int customerID, int userID) {
        this.appointmentID = appointmentID;
        this.title = title;
        this.description = description;
        this.location = location;
        this.contact = contact;
        this.type = type;
        this.startTimeAndDate = startTimeAndDate;
        this.endTimeAndDate = endTimeAndDate;
        this.customerID = customerID;
        this.userID = userID;

    }


    public int getAppointmentID() { return appointmentID;}
    public String getTitle() { return title;}
    public String getDescription() { return description;}
    public String getLocation() { return location;}
    public String getContact() { return contact;}
    public String getType() { return type;}
    public String getStartTimeAndDate() {return startTimeAndDate;}
    public String getEndTimeAndDate() { return endTimeAndDate;}
    public int getCustomerID() { return customerID;}
    public static User getUser() { return passUser; }
    public int getUserID() {return userID; }




}
