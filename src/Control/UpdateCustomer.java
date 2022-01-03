package Control;

import DAOImplementation.CountryDAOImp;
import DAOImplementation.CustomerDAOImp;
import Model.Appointment;
import Model.Customer;
import Model.Division;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UpdateCustomer implements Initializable {
    public Button cancelButton;
    public ComboBox countryComboBox;
    public ComboBox divisionComboBox;
    public TextField NameField;
    public TextField addressField;
    public TextField postalCodeField;
    public TextField phoneNumberField;
    public TextField IdField;

    CustomerDAOImp customerDAOImp = new CustomerDAOImp();
    CountryDAOImp countryDAOImp = new CountryDAOImp();
    private static Customer dataToModify = null;

    public static void passDataToModify(Customer customer) {dataToModify = customer;}

    /**
     * Initiate page with customer data
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        NameField.setText(dataToModify.getCustomerName());
        addressField.setText(dataToModify.getAddress());
        postalCodeField.setText(dataToModify.getPostalCode());
        phoneNumberField.setText(dataToModify.getPhone());
        countryComboBox.setValue(dataToModify.getCountry());
        divisionComboBox.setValue(dataToModify.getStateOrProvince());
        IdField.setText(dataToModify.getCustomerId() + "");

        countryComboBox.getItems().addAll("U.S", "UK", "Canada", "All");

        try {
            populateDivisionBox();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * returns to the main page
     * @param actionEvent
     * @throws IOException
     */
    public void onCancel(ActionEvent actionEvent) throws IOException {
        ReturnToMainPage returnToMainPage = new ReturnToMainPage();
        returnToMainPage.returnToMain(actionEvent);
    }

    public void onUpdateCustomer(ActionEvent actionEvent) throws SQLException, IOException {
        if (Validation.validCustomerFields(NameField, addressField, postalCodeField, phoneNumberField,
                divisionComboBox, countryComboBox).equals("no conflict")) {
            ObservableList<Division> divisions = countryDAOImp.getCountryDivisions(countryComboBox.getValue().toString());

            int divId = 0;
            for (Division division : divisions) {
                if (division.getDivision().equals(divisionComboBox.getValue())) {
                    divId = division.getDivisionId();
                }
            }
            Customer newCustomer = new Customer(Integer.parseInt(IdField.getText()),
                    NameField.getText(), addressField.getText(),
                    postalCodeField.getText(), phoneNumberField.getText(),
                    divisionComboBox.getValue().toString(), countryComboBox.getValue().toString(),
                    divId);

            customerDAOImp.updateCustomer(newCustomer);
            ReturnToMainPage returnToMainPage = new ReturnToMainPage();
            returnToMainPage.returnToMain(actionEvent);
        }else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, Validation.validCustomerFields(
                    NameField, addressField, postalCodeField, phoneNumberField,
                    divisionComboBox, countryComboBox
            ));
            alert.showAndWait();
        }

    }

    /**
     * populates the division comboBox depending on what country the user picks
     * @param actionEvent
     * @throws SQLException
     */
    public void onChooseCountry(ActionEvent actionEvent) throws SQLException {
        populateDivisionBox();
    }
    public void populateDivisionBox() throws SQLException {
        ObservableList<Division> divisions = countryDAOImp.getCountryDivisions(countryComboBox.getValue().toString());
        ObservableList<String> divStr = FXCollections.observableArrayList();
        for (Division division: divisions) {
            divStr.add(division.getDivision());
        }
        divisionComboBox.setItems(divStr);
    }
}
