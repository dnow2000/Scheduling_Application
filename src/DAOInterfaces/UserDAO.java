package DAOInterfaces;

import Model.User;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public interface UserDAO {
    public ObservableList<User> getAllUsers() throws SQLException;
}
