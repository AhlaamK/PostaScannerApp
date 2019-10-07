package utils;

import org.apache.commons.lang3.StringUtils;

public class Utils {
    public static boolean checkValidWaybill(String s){

        if (s.length() == 10 || s.length() == 12)
        {
            return StringUtils.isNumeric(s);
        }
        else if (s.length() == 18 || s.length() == 13)
        {
            return StringUtils.isAlphanumeric(s);
        }
        return false;
    }
}
