package org.HappyBank;

import org.HappyBank.model.HappyBankException;
import org.HappyBank.view.LoginView;

/**
 * The entry point of the HappyBank application.
 */
public class Launch {
    /**
     * The main method that starts the application.
     *
     * @param args Command line arguments.
     * @throws HappyBankException If there is an error during the application startup.
     */
    public static void main(String[] args) throws HappyBankException {
        LoginView.main(args);
    }
}
