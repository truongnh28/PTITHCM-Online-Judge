package ptithcm.onlinejudge.helper;

import java.time.Instant;

public class TimeHelper {
    // input time: "yyyy-MM-dd HH:mm:ss"
    public static Instant convertStringToInstance(String inputTime) {
        String date = inputTime.substring(0, inputTime.indexOf(' '));
        String time = inputTime.substring(inputTime.indexOf(' '));
        String formatTime = date + "T" + time + ".00Z";
        return Instant.parse(formatTime);
    }
}
