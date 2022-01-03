package DAOInterfaces;

import Model.Appointment;
import Model.Contact;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.util.Hashtable;

public interface ContactDAO {
    ObservableList<Contact> getAllContacts() throws SQLException;
}
