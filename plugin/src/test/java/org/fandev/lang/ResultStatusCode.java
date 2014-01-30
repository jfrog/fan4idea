package org.fandev.lang;

/**
 * @author Dror Bereznitsky
 * @date Feb 25, 2009 6:30:36 PM
 */
public enum ResultStatusCode {
    OK("OK"),
    EXCEPTION("Exception"),
    TIMEOUT("Timeout"),
    PARSING_ERROR("Parsing error");

    private String description;

    private ResultStatusCode(String description) {
        this.description = description;
    }


    @Override
    public String toString() {
        return description;
    }
}
