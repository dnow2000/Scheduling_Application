package Control;
import Model.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Struct;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Repeated time manipulation code throughout the application
 */
public class TimeFunctions {

    public static ZonedDateTime getZonedTime(LocalDateTime ldt) {
        ZonedDateTime zdt = ldt.atZone(ZoneId.of(ZoneId.systemDefault().toString()));
        return zdt;
    }

    public static LocalDateTime convertToUTC(ZonedDateTime zdt) {
        ZonedDateTime utczdt = zdt.withZoneSameInstant(ZoneId.of("UTC"));
        return utczdt.toLocalDateTime();
    }

    public static String displayDateTimeFormat(String dateToFormat, String format) {
        if (dateToFormat.length() > 16 && format.length() <= 16) {
            dateToFormat = dateToFormat.substring(0, 16);
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        LocalDateTime localDateTime = LocalDateTime.parse(dateToFormat, formatter);
        return DateTimeFormatter.ofPattern(format).format(localDateTime);
    }

    public static LocalDateTime sqlDateTimeFormat(String dateToFormat, String format) {
        if (dateToFormat.length() == 15) {
            format = "yyyy-MM-dd H:mm";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return LocalDateTime.parse(dateToFormat, formatter);
    }


    public static String instantTime() {
        String[] inst = Instant.now().toString().split("T");
        return inst[0] + " " + inst[1].split("Z")[0].substring(0, 8);

    }
}
