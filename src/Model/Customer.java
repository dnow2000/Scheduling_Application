package Model;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.chrono.*;
import java.util.Date;

public class Customer {
    private static User passUser = null;
    public static void setPassUser(User user) {passUser = user;}

    private int customerId;
    private String customerName;
    private String address;
    private String postalCode;
    private String phone;
    private String stateOrProvince;
    private String country;
    private int divisionId;

    public Customer(int customerId, String customerName, String address,
                    String postalCode, String phone, String stateOrProvince,
                    String country,
                    int divisionId) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.stateOrProvince = stateOrProvince;
        this.country = country;
        this.divisionId = divisionId;

    }
    // getters
    public int getCustomerId() {return customerId;}
    public String getCustomerName() {return customerName;}
    public String getAddress() {return address;}
    public String getPostalCode() {return postalCode;}
    public String getPhone() {return phone;}
    public String getStateOrProvince() {return stateOrProvince;}
    public String getCountry() {return country;}
    public int getDivisionId() {return divisionId;}

    public User getPassUser() {return passUser;}
    // functions
    // associated appointments
    //delete customer ** must delete associated appointment(s) first



}
