package DAOImplementation;

import DAOInterfaces.UserDAO;
import Database.DBConnection;
import Database.DBQuery;
import Model.Appointment;
import Model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserDAOImp implements UserDAO {
    static ObservableList<User> allUsers = FXCollections.observableArrayList();

    /**
     * fetches customer data from the database
     * @return returns a list of users
     * @throws SQLException
     */
    @Override
    public ObservableList<User> getAllUsers() throws SQLException {
        Connection conn = DBConnection.getConnection();
        DBQuery.setStatement(conn);
        Statement statement = DBQuery.getStatement();

        String usersSelect = "SELECT * FROM users;";
        ResultSet rs = statement.executeQuery(usersSelect);

        while (rs.next()) {
            int userId = rs.getInt("User_ID");
            String username = rs.getString("User_Name");
            String password = rs.getString("Password");
            String createDate = rs.getString("Create_Date");
            String createdBy = rs.getString("Created_By");
            String lastUpdate = rs.getString("Last_Update");
            String lastUpdatedBy = rs.getString("Last_Updated_By");

            User user = new User(userId, username, password);
            allUsers.add(user);
        }
        return allUsers;
    }
}
