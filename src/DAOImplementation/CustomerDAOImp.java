package DAOImplementation;

import DAOInterfaces.CustomerDAO;
import Database.DBConnection;
import Database.DBQuery;
import Model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.converter.DateTimeStringConverter;

import java.sql.*;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

public class CustomerDAOImp implements CustomerDAO{
    static ObservableList<Customer> allCustomers = FXCollections.observableArrayList();

    /**
     * fetches customer data from the database
     * @return returns an observable list of customers
     * @throws SQLException
     */
    @Override
    public ObservableList<Customer> getAllCustomers() throws SQLException {
        ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
            Connection conn = DBConnection.getConnection();
            DBQuery.setStatement(conn);
            Statement statement = DBQuery.getStatement();

            String query = "SELECT c.*, f.Division, co.Country FROM customers AS c\n" +
                    "JOIN first_level_divisions AS f ON c.Division_ID = f.Division_ID\n" +
                    "JOIN countries AS co ON co.Country_ID = f.COUNTRY_ID;";
            ResultSet rs = statement.executeQuery(query);

            if (allCustomers.size() == 0) {
                while (rs.next()) {
                    int customerId = rs.getInt("Customer_ID");
                    String customerName = rs.getString("Customer_Name");
                    String address = rs.getString("Address");
                    String postalCode = rs.getString("Postal_Code");
                    String phone = rs.getString("Phone");
                    int divisionId = rs.getInt("Division_ID");
                    String division = rs.getString("Division");
                    String country = rs.getString("Country");


                    Customer customer = new Customer(customerId, customerName, address, postalCode, phone, division, country, divisionId);
                    allCustomers.add(customer);

                }
            }

        return allCustomers;
    }

    /**
     * update customer in the database
     * @param customer Customer object
     * @throws SQLException
     */
    @Override
    public void updateCustomer(Customer customer) throws SQLException {
        String[] instant = Instant.now().toString().split("T");
        String dateTimeNow = instant[0] + " " + instant[1].split("Z")[0];

        Connection conn = DBConnection.getConnection();
        DBQuery.setStatement(conn);
        Statement statement = DBQuery.getStatement();

        String updateCustomer = "UPDATE customers\n" +
                "SET Customer_Name = " + "'" + customer.getCustomerName() + "', " +  "Address = "  + "'" + customer.getAddress() + "',\n " +
                "Postal_Code = " + "'" + customer.getPostalCode() + "', " + "Phone = " + "'" + customer.getPhone() + "',\n " +
                "Last_Update = " + "'" + dateTimeNow + "', \n" +
                "Last_Updated_BY = '" +  customer.getPassUser().getUsername() + "', Division_ID = " + "'" + customer.getDivisionId() + "'\n" +
                "WHERE Customer_ID = " + customer.getCustomerId() + ";";
        statement.executeUpdate(updateCustomer);
    }

    /**
     * deletes customer record from database
     * @param customer Customer object
     * @throws SQLException
     */
    @Override
    public void deleteCustomer(Customer customer) throws SQLException {
        Connection conn = DBConnection.getConnection();
        DBQuery.setStatement(conn);
        Statement statement = DBQuery.getStatement();

        String deleteAppointment = "DELETE FROM appointments WHERE Customer_ID = " + customer.getCustomerId() + ";";

        String deleteCustomer = "DELETE FROM customers WHERE Customer_ID = " + customer.getCustomerId() + ";";
        statement.executeUpdate(deleteAppointment);
        statement.execute(deleteCustomer);

    }

    /**
     * adds customer to the database
     * @param customer Customer object
     * @throws SQLException
     */
    @Override
    public void addCustomer(Customer customer) throws SQLException {
        Connection conn = DBConnection.getConnection();
        DBQuery.setStatement(conn);
        Statement statement = DBQuery.getStatement();

        String[] instant = Instant.now().toString().split("T");
        String dateTimeNow = instant[0] + " " + instant[1].split("Z")[0];

        String insert = "INSERT INTO customers (Customer_Name, Address, Postal_Code, Phone, Create_Date, Created_By, Last_Update, Last_Updated_By, Division_ID)\n" +
                "VALUES ( " + "'" + customer.getCustomerName() + "'" + ", " + "'" + customer.getAddress() + "'" + ", \n" +
                "'" + customer.getPostalCode() + "'" + ", " + "'" + customer.getPhone() + "'" + ", " + "('" + dateTimeNow + "')" + ", \n'" +
                customer.getPassUser().getUsername() + "', \n" +
                 "('" + dateTimeNow + "')" + ", '" + customer.getPassUser().getUsername() + "'," + customer.getDivisionId()  +")";

        statement.executeUpdate(insert);
        allCustomers.add(customer);
    }

}
