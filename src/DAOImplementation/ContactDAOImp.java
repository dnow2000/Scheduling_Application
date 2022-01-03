package DAOImplementation;

import DAOInterfaces.ContactDAO;
import Database.DBConnection;
import Database.DBQuery;
import Model.Appointment;
import Model.Contact;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;

public class ContactDAOImp implements ContactDAO {

        /**
         * fetches contact data from the database and creates a Contact object
         * @return observable list of Contact objects
         * @throws SQLException
         */
        @Override
        public ObservableList<Contact> getAllContacts() throws SQLException {
                ObservableList<Contact> allContacts = FXCollections.observableArrayList();
                Connection conn = DBConnection.getConnection();
                DBQuery.setStatement(conn);
                Statement statement = DBQuery.getStatement();

                String appointmentsSelect = "SELECT * FROM contacts";
                ResultSet rs = statement.executeQuery(appointmentsSelect);

                while (rs.next()) {
                        int contactId = rs.getInt("Contact_ID");
                        String contactName = rs.getString("Contact_Name");
                        String email = rs.getString("Email");

                        Contact newContact = new Contact(contactId, contactName, email);
                        allContacts.add(newContact);
                }
                return allContacts;
        }
}

