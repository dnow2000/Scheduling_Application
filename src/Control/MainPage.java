package Control;

import DAOImplementation.AppointmentDAOImp;
import DAOImplementation.ContactDAOImp;
import DAOImplementation.CountryDAOImp;
import DAOImplementation.CustomerDAOImp;
import Model.Appointment;
import Model.Contact;
import Model.Customer;
import Model.Division;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.*;

public class MainPage  implements Initializable {

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
    public Button addAppointmentButton;
    public Button updateAppointmentButton;
    public Button deleteAppointmentButton;
    public TableColumn customerIDCol;
    public TableColumn nameCol;
    public TableColumn addressCol;
    public TableColumn postalCodeCol;
    public TableColumn phoneCol;
    public TableView customerTable;
    public TableColumn countryCol;
    public TableColumn stateProvidenceCol;
    public TableColumn divisionID;
    public RadioButton monthFilter;
    public RadioButton weekFilter;
    public Button clearButton;
    public Button addCustomerButton;
    public Button deleteCustomerButton;
    public Button updateCustomerButton;
    public ComboBox countryComboBox;
    public ComboBox divisionComboBox;
    public TableView contentTable;
    public ComboBox typeComboBox;
    public ComboBox monthComboBox;
    public TableColumn contactIdCol;
    public TableColumn contactNameCol;
    public TableColumn contactEmailCol;
    public TextField customerNameField;
    public TableColumn userIdCol;

    ObservableList<Appointment> appointments = FXCollections.observableArrayList();
    ObservableList<Customer> customers = FXCollections.observableArrayList();
    AppointmentDAOImp appointmentImp = new AppointmentDAOImp();
    CustomerDAOImp customerImp = new CustomerDAOImp();
    ContactDAOImp contactDAOImp = new ContactDAOImp();
    ObservableList<Contact> contacts = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    ///////////////////////////////////// tab initialization data //////////////////////////////////////////////////////

    /**
     * populate the appointment table with Appointment data <br>
     * lambda expression: iterate through appointments to populate type comboBox
     * @param event listens for appointment tab to be clicked
     * @throws SQLException
     */
    public void onAppointmentTab(Event event) throws SQLException {
        //Appointment Table
        appointments = appointmentImp.getAllAppointments();

        appointmentsTable.setItems(appointments);

        appointmentIDCol.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        contactCol.setCellValueFactory(new PropertyValueFactory<>("contact"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        startDateTimeCol.setCellValueFactory(new PropertyValueFactory<>("startTimeAndDate"));
        endDateTimeCol.setCellValueFactory(new PropertyValueFactory<>("endTimeAndDate"));
        customerIdCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        userIdCol.setCellValueFactory(new PropertyValueFactory<>("userID"));

        //type combo box
        ObservableList<String> types = FXCollections.observableArrayList();
        appointmentImp.getAllAppointments().forEach(appointment -> types.add(appointment.getType()));
        types.add("All");
        typeComboBox.setItems(types);

        //month combo box
        monthComboBox.getItems().addAll("January", "February", "March", "April", "May", "June", "July", "August", "September",
                "October", "November", "December", "All");
    }

    /**
     * populate the customer table with data
     * @param event listens for customer tab to be clicked
     * @throws SQLException
     */
    public void onCustomersTab(Event event) throws SQLException {
        //Customer Table **note to self: use the models not the dao
        customers = customerImp.getAllCustomers();
        customerTable.setItems(customers);

        customerIDCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        postalCodeCol.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        stateProvidenceCol.setCellValueFactory(new PropertyValueFactory<>("stateOrProvince"));
        countryCol.setCellValueFactory(new PropertyValueFactory<>("country"));
        divisionID.setCellValueFactory(new PropertyValueFactory<>("divisionId"));

        //Customer Window combo boxes
        countryComboBox.getItems().addAll("U.S", "UK", "Canada", "All");


    }

    /**
     * populate the contact table with appointment data
     * @param event listens for contact tab to be clicked
     * @throws SQLException
     */
    public void onContactTab(Event event) throws SQLException {
        contacts = contactDAOImp.getAllContacts();
        contentTable.setItems(contacts);
        contactIdCol.setCellValueFactory(new PropertyValueFactory<>("contactId"));
        contactNameCol.setCellValueFactory(new PropertyValueFactory<>("contact"));
        contactEmailCol.setCellValueFactory(new PropertyValueFactory<>("email"));

    }


    /**
     * function that changes the window
     * @param actionEvent listens for any button that changes the scene
     * @param page fxml page to be shown
     * @param title title of the scene
     * @param width width of the page
     * @param height height of the page
     * @throws IOException
     */
    //change the scene
    public void changeScene(ActionEvent actionEvent, String page, String title, int width, int height) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(page)));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, width, height);
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * changes scene to the add appointment window
     * @param actionEvent listens for add appointment button to be clicked
     * @throws SQLException An exception that provides information on a database access error or other errors.
     * @throws IOException Signals that an I/O exception of some sort has occurred
     */
    //////////////////////////// Appointments section ///////////////////////////////////////////////////////////////////
    public void onAddAppointment(ActionEvent actionEvent) throws SQLException, IOException {
        changeScene(actionEvent, "../sample/addAppointment.fxml", "Add Appointment", 760, 460);
    }

    /**
     * passes selected appointment to update Appointment controller.
     * changes scene to the update Appointment window
     * @param actionEvent listens for update Appointment button to be clicked
     * @throws IOException Signals that an I/O exception of some sort has occurred
     */
    public void onUpdateAppointment(ActionEvent actionEvent) throws IOException {
        try {
            Appointment app = (Appointment) appointmentsTable.getSelectionModel().getSelectedItem();
            UpdateAppointment.passDataToModify(app);
            changeScene(actionEvent, "../sample/updateAppointment.fxml", "Update Appointment", 760, 460);
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "please select an appointment to update");
            alert.showAndWait();
        }
    }

    /**
     * deletes selected appointment
     * @param actionEvent listens for the delete appointment button to be clicked
     * @throws SQLException An exception that provides information on a database access error or other errors.
     */
    public void onDeleteAppointment(ActionEvent actionEvent) throws SQLException {
        try {
            Appointment app = (Appointment) appointmentsTable.getSelectionModel().getSelectedItem();
            appointmentImp.deleteAppointment(app.getAppointmentID());
            appointments.remove(app);

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Appointment deleted");
            alert.showAndWait();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "please select an appointment to delete");
            alert.showAndWait();
        }
    }


    //Filtering components for appointments
    int calendarMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
    int calendarYear = Calendar.getInstance().get(Calendar.YEAR);

    /**
     * filter appointments by month
     * @param actionEvent listens for the month radio button to be clicked
     */
    public void onMonthFilter(ActionEvent actionEvent) {
        ObservableList<Appointment> appointmentsThisWeek = FXCollections.observableArrayList();

        if (weekFilter.isSelected()) {
            weekFilter.setSelected(false);
        }

        for (Appointment appointment : appointments) {

            int startAppointmentMonth = dateInfo(appointment.getStartTimeAndDate().toString(), 1);
            int startAppointmentYear = dateInfo(appointment.getStartTimeAndDate().toString(), 0);


            if (calendarMonth == startAppointmentMonth && startAppointmentYear == calendarYear) {

                appointmentsThisWeek.add(appointment);
                appointmentsTable.setItems(appointmentsThisWeek);

            } else {
                appointmentsTable.setItems(appointmentsThisWeek);
            }


        }
    }

    /**
     *
     * @param str time and date to be formatted
     * @param i used to format the date
     * @return returns a day month or year
     */
    public Integer dateInfo(String str, int i) {
        return Integer.parseInt(str.split(" ")[0].split("-")[i]);
    }

    /**
     * filter appointments by week
     * @param actionEvent listens for week radio button to be clicked
     */
    // if start time and end time are close to each other
    public void onWeekFilter(ActionEvent actionEvent) {
        ObservableList<Appointment> appointmentsThisWeek = FXCollections.observableArrayList();

        if (monthFilter.isSelected()) {
            monthFilter.setSelected(false);
        }

        for (Appointment appointment : appointments) {
            int calendarDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
            int startAppointmentDay = dateInfo(appointment.getStartTimeAndDate().toString(), 2);
            int startAppointmentMonth = dateInfo(appointment.getStartTimeAndDate().toString(), 1);
            int startAppointmentYear = dateInfo(appointment.getStartTimeAndDate().toString(), 0);


            if (startAppointmentDay <= calendarDay + 6 && startAppointmentDay >= LocalDateTime.now().getDayOfMonth()) {
                if (startAppointmentMonth == calendarMonth && startAppointmentYear == calendarYear) {
                    appointmentsThisWeek.add(appointment);
                    appointmentsTable.setItems(appointmentsThisWeek);

                }
            } else {
                appointmentsTable.setItems(appointmentsThisWeek);
            }

        }
    }

    /**
     * clears all filters set in place
     * @param actionEvent listens for the on clear button to be pressed
     */
    public void onClear(ActionEvent actionEvent) {
        weekFilter.setSelected(false);
        monthFilter.setSelected(false);
        typeComboBox.setValue("");
        monthComboBox.setValue("");
        appointmentsTable.setItems(appointments);
    }




    ///////////////////////////////////////////// Customer Section //////////////////////////////////////////////////////

    /**
     * changes the scene to add customer window
     * @param actionEvent listens for add customer button to be clicked
     * @throws IOException Signals that an I/O exception of some sort has occurred
     */
    public void onAddCustomer(ActionEvent actionEvent) throws IOException {
        changeScene(actionEvent, "../sample/addCustomer.fxml", "Add Customer", 760, 460);
    }

    public void onDeleteCustomer(ActionEvent actionEvent) throws SQLException {
        try {
            Customer customer = (Customer) customerTable.getSelectionModel().getSelectedItem();
            customers.remove(customer);
            appointmentImp.getAllAppointments().removeIf(appointment -> appointment.getCustomerID() == customer.getCustomerId());
            customerImp.deleteCustomer(customer);

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "customer deleted");
            alert.showAndWait();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "please select a customer to delete");
            alert.showAndWait();
        }
    }

    /**
     * passes selected customer data to update customer controller
     * @param actionEvent listens for update customer button to be clicked
     * @throws IOException Signals that an I/O exception of some sort has occurred
     */
    public void onUpdateCustomer(ActionEvent actionEvent) throws IOException {
        try {
            Customer customer = (Customer) customerTable.getSelectionModel().getSelectedItem();
            UpdateCustomer.passDataToModify(customer);
            changeScene(actionEvent, "../sample/updateCustomer.fxml", "Update Appointment", 760, 460);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "please select a customer to update");
            alert.showAndWait();
        }
    }

    // Filtering components for customers
    ObservableList<Customer> filteredCustomers;

    /**
     * filters customers by country
     * @param actionEvent listens for country combo box to be clicked
     * @throws SQLException An exception that provides information on a database access error or other errors.
     */
    public void onChooseCountry(ActionEvent actionEvent) throws SQLException {
        filteredCustomers = FXCollections.observableArrayList();
        ObservableList<String> filteredDivisions = FXCollections.observableArrayList();
        CountryDAOImp countryDAOImp = new CountryDAOImp();
        ObservableList<Division> divisions = countryDAOImp.getCountryDivisions(countryComboBox.getValue().toString());
        for (Division division : divisions) {
            filteredDivisions.add(division.getDivision());
        }
        divisionComboBox.setItems(filteredDivisions);
        for (Customer customer : customers) {
            if (customer.getCountry().equals(countryComboBox.getValue())) {
                filteredCustomers.add(customer);
            }
            if (countryComboBox.getValue().equals("All")) {
                filteredCustomers = customers;
                break;
            }
        }
        customerTable.setItems(filteredCustomers);


    }

    /**
     * filters customers by division
     * @param actionEvent listens for division combo box to be clicked
     */
    public void onChooseDivision(ActionEvent actionEvent) {
        if (divisionComboBox.getValue() != null && !(divisionComboBox.getValue().equals("All"))) {
            ObservableList<Customer> filteredDivCustomers = FXCollections.observableArrayList();
            for (Customer customer : customers) {
                if (customer.getStateOrProvince().equals(divisionComboBox.getValue())) {
                    filteredDivCustomers.add(customer);
                }
            }
            customerTable.setItems(filteredDivCustomers);
        } else {
            customerTable.setItems(filteredCustomers);
        }

    }

    /**
     * filters appointments by type
     * @param actionEvent listens for type combo box to be clicked
     * @throws SQLException An exception that provides information on a database access error or other errors.
     */
    public void onChooseType(ActionEvent actionEvent) throws SQLException {
        ObservableList<Appointment> filteredTypeApp = FXCollections.observableArrayList();
        if (!typeComboBox.getValue().equals("All")) {
        for (Appointment appointment : appointments) {
            if (appointment.getType().equals(typeComboBox.getValue())) {
                filteredTypeApp.add(appointment);
            }
        }
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
                if (date.getMonth().toString().toLowerCase(Locale.ROOT).equals(monthComboBox.getValue().toString().toLowerCase(Locale.ROOT))) {
                    filteredMonthApp.add(appointment);
                }
            }
            appointmentsTable.setItems(filteredMonthApp);
        }else {
            appointmentsTable.setItems(appointments);
        }
    }

    /**
     * changes scene to contact schedule window
     * @param actionEvent listens for contact schedule button to be clicked
     * @throws IOException An exception that provides information on a database access error or other errors.
     */
    public void onViewContactSchedule(ActionEvent actionEvent) throws IOException {
        try {
            ContactSchedule.passSelectedContact((Contact) contentTable.getSelectionModel().getSelectedItem());
            changeScene(actionEvent, "../sample/contactSchedule.fxml", "Contact Schedule", 850, 460);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "please select a contact to view its schedule");
            alert.showAndWait();
        }
    }

    public void onViewCustomerAppointments(ActionEvent actionEvent) {
        try {
            AppointmentsByCustomer.passDataToModify((Customer) customerTable.getSelectionModel().getSelectedItem());
            changeScene(actionEvent, "../sample/appointmentsByCustomer.fxml", "Appointments By Customer", 850, 460);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "please select a customer");
            alert.showAndWait();
        }
    }

    public void onSearchCustomerName(KeyEvent keyEvent) {
        //variables used
        ObservableList<Customer> filteredList = FXCollections.observableArrayList();

        //search product table when user presses enter
        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            if (!customerNameField.getText().equals("")) {
                try {
                    for (Customer customer : customers) {
                        if (customer.getCustomerName().toLowerCase(Locale.ROOT)
                                .contains(customerNameField.getText().toLowerCase(Locale.ROOT))) {
                            filteredList.add(customer);
                        }
                    }
                    customerTable.setItems(filteredList);
                } catch (Exception e) {
                    // catches IndexOutOfBoundException when user types on empty field
                    // and NumberFormatException when user types a string instead of a number
//                filteredList = Inventory.lookupProduct(searchText);
                    customerTable.setItems(filteredList);
//
                }
            }else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "customer does not exist");
                alert.showAndWait();
            }




        }
    }
}
