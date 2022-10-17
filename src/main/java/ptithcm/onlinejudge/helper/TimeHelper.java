package ptithcm.onlinejudge.helper;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class TimeHelper {
    private static final String DATE_FORMATTER = "yyyy-MM-dd HH:mm:ss";
    // input time: "yyyy-MM-dd HH:mm:ss"
    public static Instant convertStringToInstance(String inputTime) {
        String date = inputTime.substring(0, inputTime.indexOf(' '));
        String time = inputTime.substring(inputTime.indexOf(' ') + 1);
        String formatTime = date + "T" + time + ".00Z";
        return Instant.parse(formatTime);
    }

    public static String convertLocalDateTimeToString(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMATTER);
        return localDateTime.format(formatter);
    }

    public static String convertInstantToString(Instant instant) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMATTER).withZone(ZoneId.of("UTC+7"));
        return formatter.format(instant);
    }

    public static boolean nowIsAfter(Instant time) {
        Instant instantNow = Instant.now();
        return instantNow.isAfter(time);
    }

    public static String convertStringFormToDateFormatter(String time) {
        return time.replace("T", " ") + ":00";
    }
}
