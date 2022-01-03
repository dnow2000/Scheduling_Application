package DAOInterfaces;

import Model.Appointment;
import Model.Customer;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public interface CustomerDAO {

    public ObservableList<Customer> getAllCustomers() throws SQLException;
    public void updateCustomer(Customer customer) throws SQLException;
    public void deleteCustomer(Customer customer) throws SQLException;
    public void addCustomer(Customer customer) throws SQLException;
}
