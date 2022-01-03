package DAOInterfaces;

import Model.Division;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

import java.sql.SQLException;

public interface CountryDAO {
    public ObservableList<Division> getCountryDivisions(String countryVal) throws SQLException;
}
