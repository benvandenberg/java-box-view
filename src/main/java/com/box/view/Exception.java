package com.box.view;

/**
 * Exception extends the default exception class.
 * It doesn't do anything fancy except be a unique kind of Exception.
 */
public class Exception extends java.lang.Exception {
    private static final long serialVersionUID = 1L;

    /**
     * A string representing the short form error code.
     *
     * @var code
     */
    private String code = null;

    /**
     * The constructor function for Exception
     *
     * @param message The long form error message.
     * @param code The short form error code.
     */
    public Exception(String message, String code) {
        super(message);
        this.code = code;
    }

    /**
     * Get the short form error code
     *
     * @return string The short form error code
     */
    public String getCode() {
        return code;
    }
}
