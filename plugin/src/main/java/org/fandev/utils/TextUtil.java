package org.fandev.utils;

/**
 * @author Dror Bereznitsky
 * @date Jan 19, 2009 12:00:46 PM
 */
public class TextUtil {
    public static final String EMPTY_STRING = "";
    
    /**
     * @param s String
     * @return true if string is empty or equals null.
     */
    public static boolean isEmpty(final String s) {
        return (s == null || EMPTY_STRING.equals(s));
    }

    public static String getAsNotNull(final String str) {
        return str != null ? str : EMPTY_STRING;
    }
}
