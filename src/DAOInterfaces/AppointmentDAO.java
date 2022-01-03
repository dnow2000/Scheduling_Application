package DAOInterfaces;

import Model.Appointment;
import Model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.util.List;

public interface AppointmentDAO {
    public ObservableList<Appointment> getAllAppointments() throws SQLException;
    public void updateAppointment(Appointment appointment) throws SQLException;
    public void deleteAppointment(int id) throws SQLException;
    public void addAppointment(Appointment appointment) throws SQLException;

}
