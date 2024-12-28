package org.HappyBank.model;

/**
 * This exception is thrown when there is an error in the HappyBank application.
 */
public class HappyBankException extends Exception {
    /**
     * Constructs a new HappyBankException with the specified detail message.
     *
     * @param message the detail message.
     */
    public HappyBankException(String message) {
        super(message);
    }
}
