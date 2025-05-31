package demo.core;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class convert {
    public Date stringToDate(String string) throws Exception {
        DateFormat format = new SimpleDateFormat("HH:mm:ss");
        return (Date) format.parse(string);
    }

    public String dateToString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.YYYY hh:mm:ss");
        return dateFormat.format(date);
    }

    public String dateToFormatDTOString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");
        return dateFormat.format(date);
    }
}
