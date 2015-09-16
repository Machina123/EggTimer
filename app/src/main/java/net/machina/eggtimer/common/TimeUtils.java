package net.machina.eggtimer.common;

/**
 * Created by Machina on 06.09.2015.
 */
public class TimeUtils {
    public static String getTimeString(double seconds) {
        String result = "";
        int minutes = (int) Math.floor(seconds / 60);
        int remainingSeconds = (int) (seconds % 60);
        result += minutes + "m ";
        result += ((remainingSeconds < 10 && remainingSeconds >= 0) ? "0" + remainingSeconds : remainingSeconds);
        result += "s";
        return result;
    }

    public static String getTimeString(int seconds) {
        return getTimeString((double) seconds);
    }
}
