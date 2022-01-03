package Control;

import DAOImplementation.AppointmentDAOImp;
import Model.Appointment;
import Model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.Main;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.ResourceBundle;

public class AppointmentsByCustomer implements Initializable {

    private static Customer dataToModify = null;

    public TableView appointmentsTable;
    public TableColumn appointmentIDCol;
    public TableColumn titleCol;
    public TableColumn descriptionCol;
    public TableColumn locationCol;
    public TableColumn contactCol;
    public TableColumn typeCol;
    public TableColumn startDateTimeCol;
    public TableColumn endDateTimeCol;
    public TableColumn customerIdCol;
    public ComboBox typeComboBox;
    public ComboBox monthComboBox;
    public Label countLabel;

    public AppointmentsByCustomer() throws SQLException {
    }

    public static void passDataToModify(Customer customer) {dataToModify = customer;}
    AppointmentDAOImp appointmentDAOImp = new AppointmentDAOImp();
    ObservableList<Appointment> appointments = appointmentDAOImp.getAllAppointments();

    /**
     * Initialize table with appointment data <br>
     * lambda expression:iterates through appointments and add all the appointment types to an array.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<Appointment> custApps = FXCollections.observableArrayList();
        for (Appointment appointment : appointments) {
            if (appointment.getCustomerID() == dataToModify.getCustomerId()) {
                custApps.add(appointment);
            }
        }
        System.out.print(custApps.size());
        appointmentsTable.setItems(custApps);

        appointmentIDCol.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        contactCol.setCellValueFactory(new PropertyValueFactory<>("contact"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        startDateTimeCol.setCellValueFactory(new PropertyValueFactory<>("startTimeAndDate"));
        endDateTimeCol.setCellValueFactory(new PropertyValueFactory<>("endTimeAndDate"));
        customerIdCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));

        //type combo box
        ObservableList<String> types = FXCollections.observableArrayList();
        try {
            appointmentDAOImp.getAllAppointments().forEach(appointment -> types.add(appointment.getType()));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        types.add("All");
        typeComboBox.setItems(types);

        //month combo box
        monthComboBox.getItems().addAll("January", "February", "March", "April", "May", "June", "July", "August", "September",
                "October", "November", "December", "All");

    }

    /**
     * go back to main page
     * @param actionEvent
     * @throws IOException
     */
    public void onGoBack(ActionEvent actionEvent) throws IOException {
        ReturnToMainPage returnToMainPage = new ReturnToMainPage();
        returnToMainPage.returnToMain(actionEvent);
    }


    // Filtering components for customers
    // if type is in same month

    ObservableList<Customer> filteredCustomers;
    Boolean selectedMonth = false;
    /**
     * filters appointments by type
     * @param actionEvent listens for type combo box to be clicked
     * @throws SQLException An exception that provides information on a database access error or other errors.
     */
    public void onChooseType(ActionEvent actionEvent) throws SQLException {
       ObservableList<Appointment> filteredTypeApp = FXCollections.observableArrayList();
        if (!typeComboBox.getValue().equals("All")) {
            for (Appointment appointment : appointments) {
                LocalDateTime date = TimeFunctions.sqlDateTimeFormat( appointment.getStartTimeAndDate(), "yyyy-MM-dd HH:mm");
                if (monthComboBox.getValue() != null) {
                    selectedMonth = date.getMonth().toString().toLowerCase(Locale.ROOT).equals(monthComboBox.getValue().toString().toLowerCase(Locale.ROOT));
                }
                if (appointment.getType().equals(typeComboBox.getValue()) && selectedMonth ||
                monthComboBox.getValue() == null && appointment.getType().equals(typeComboBox.getValue())) {
                    filteredTypeApp.add(appointment);
                }
            }
            countLabel.setText("Total: " + filteredTypeApp.size());
            appointmentsTable.setItems(filteredTypeApp);
        }else {
            appointmentsTable.setItems(appointments);
        }
    }

    /**
     * filters appointments by month
     * @param actionEvent listens for month combo box to be clicked
     */
    public void onChooseMonth(ActionEvent actionEvent) {
        ObservableList<Appointment> filteredMonthApp = FXCollections.observableArrayList();

        if (!monthComboBox.getValue().equals("All")) {
            for (Appointment appointment : appointments) {
                LocalDateTime date = TimeFunctions.sqlDateTimeFormat( appointment.getStartTimeAndDate(), "yyyy-MM-dd HH:mm");
                selectedMonth = date.getMonth().toString().toLowerCase(Locale.ROOT).equals(monthComboBox.getValue().toString().toLowerCase(Locale.ROOT));
                if (selectedMonth && typeComboBox.getValue() == null || selectedMonth &&
                        appointment.getType().equals(typeComboBox.getValue())) {
                    filteredMonthApp.add(appointment);
                }
            }
            countLabel.setText("Total: " + filteredMonthApp.size());
            appointmentsTable.setItems(filteredMonthApp);
        }else {
            appointmentsTable.setItems(appointments);
        }
    }
}
