package utils;

public class Utils {
    public static boolean checkValidWaybill(String s) {
        return s.length() >= 10 && s.length() <= 50;
    }

}
