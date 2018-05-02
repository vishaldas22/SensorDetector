package excel.com.sensordetector.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CommonUtils {

    private static final String dateFormat = "dd/MM/yyyy";
    public static String getDateFromMillSec(long milliseconds)
    {
        Date date = new Date(milliseconds);
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.getDefault());
        return formatter.format(date);
    }

    public static String getTimeFromSeconds(long seconds) {

        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        seconds = seconds % 60;

        return putZeroBefore(hours) + " : " + putZeroBefore(minutes) + " : " + putZeroBefore(seconds);
    }

    private static String putZeroBefore(long number) {

        if (number == 0) {
            return "00";
        }

        if (number / 10 == 0) {
            return "0" + number;
        }

        return String.valueOf(number);
    }
}