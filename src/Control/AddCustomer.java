package Control;

import DAOImplementation.CustomerDAOImp;
import DAOImplementation.CountryDAOImp;
import Model.Appointment;
import Model.Customer;
import Model.Division;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import Control.ReturnToMainPage;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.SimpleFormatter;

public class AddCustomer implements Initializable {

    public Button cancelButton;
    public Button addCustomerButton;
    public TextField customerIdField;
    public ComboBox countryComboBox;
    public ComboBox divisionComboBox;
    public TextField phoneNumField;
    public TextField postalCodeField;
    public TextField addressField;
    public TextField customerNameField;

    CountryDAOImp countryDAOImp = new CountryDAOImp();
    ObservableList<Division> divisions = FXCollections.observableArrayList();

    /**
     * returns to main page
     * @param actionEvent listens for cancel button to be clicked
     * @throws IOException Signals that an I/O exception of some sort has occurred
     */
    public void onCancel(ActionEvent actionEvent) throws IOException {
        ReturnToMainPage returnToMainPage = new ReturnToMainPage();
        returnToMainPage.returnToMain(actionEvent);
    }


    /**
     * saves customer to the customer table and database
     * @param actionEvent listens for add customer button to be clicked
     * @throws SQLException An exception that provides information on a database access error or other errors.
     * @throws IOException Signals that an I/O exception of some sort has occurred
     */
    public void onAddCustomer(ActionEvent actionEvent) throws SQLException, IOException {
        if (Validation.validCustomerFields(customerNameField, addressField, postalCodeField, phoneNumField,
                divisionComboBox, countryComboBox).equals("no conflict")) {
            CustomerDAOImp customerDAOImp = new CustomerDAOImp();
            divisions = countryDAOImp.getCountryDivisions(countryComboBox.getValue().toString());
            int divId = 0;

            int list = customerDAOImp.getAllCustomers().size();
            int genId = customerDAOImp.getAllCustomers().get(list - 1).getCustomerId() + 1;

            for (Division division : divisions) {
                if (division.getDivision().equals(divisionComboBox.getValue())) {
                    divId = division.getDivisionId();
                }
            }

            String phone = phoneNumField.getText();
            String formattedPhone = String.format("%s-%s-%s", phone.substring(0, 3), phone.substring(3, 6), phone.substring(6, 10));

            Customer newCustomer = new Customer(
                    genId,
                    customerNameField.getText(),
                    addressField.getText(),
                    postalCodeField.getText(),
                    formattedPhone,
                    divisionComboBox.getValue().toString(),
                    countryComboBox.getValue().toString(),
                    divId

            );

            customerDAOImp.addCustomer(newCustomer);
            ReturnToMainPage returnToMainPage = new ReturnToMainPage();
            returnToMainPage.returnToMain(actionEvent);
        }else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, Validation.validCustomerFields(
                    customerNameField, addressField, postalCodeField, phoneNumField,
                    divisionComboBox, countryComboBox
            ));
            alert.showAndWait();
        }
    }

    /**
     * populates the division combo box with states/regions depending on
     * what is in the country combo box.
     * @param actionEvent listens for the country comboBox to be clicked
     * @throws SQLException An exception that provides information on a database access error or other errors
     */
    public void onChooseCountry(ActionEvent actionEvent) throws SQLException {
        divisions = countryDAOImp.getCountryDivisions(countryComboBox.getValue().toString());
        ObservableList<String> divStr = FXCollections.observableArrayList();
        for (Division division: divisions) {
            divStr.add(division.getDivision());
        }
        divisionComboBox.setItems(divStr);
    }

    /**
     * initializes country combo box with country data
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        countryComboBox.getItems().addAll("U.S", "UK", "Canada");
        Boolean isUk = Locale.getDefault().getCountry().equals("GB");
        if (isUk) {
            addressField.setPromptText("123 ABC Street Town");
        }
    }
}
