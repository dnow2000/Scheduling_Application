package DAOImplementation;

import DAOInterfaces.CountryDAO;
import Database.DBConnection;
import Database.DBQuery;
import Model.Division;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

import java.sql.*;
import java.util.Hashtable;

public class CountryDAOImp implements CountryDAO {

    /**
     * fetches division data from the database
     * @param countryVal string
     * @return returns and observable list of divisions
     * @throws SQLException
     */
    @Override
    public ObservableList<Division> getCountryDivisions(String countryVal) throws SQLException {
        ObservableList<Division> divisionList = FXCollections.observableArrayList();
        Hashtable <String, String> my_dict = new Hashtable<String, String>();
        my_dict.put("U.S", "1");
        my_dict.put("UK", "2");
        my_dict.put("Canada", "3");

        //query
        Connection conn = DBConnection.getConnection();
        DBQuery.setStatement(conn);
        Statement statement = DBQuery.getStatement();

        String countrySelect = "SELECT * FROM first_level_divisions WHERE COUNTRY_ID = " + my_dict.get(countryVal);
        ResultSet rs = statement.executeQuery(countrySelect);
        while (rs.next()) {
            String division = rs.getString("Division");
            int divisionId = rs.getInt("Division_ID");
            String createDate = rs.getString("Create_Date");
            String createdBy = rs.getString("Created_By");
            String lastUpdate = rs.getString("Last_Update");
            String lastUpdatedBy = rs.getString("Last_Updated_By");
            int countryId = rs.getInt("Country_ID");

            Division newDivision = new Division(divisionId, division, createDate, createdBy, lastUpdate, lastUpdatedBy, countryId);
            divisionList.add(newDivision);
        }
        return divisionList;
    }

}
