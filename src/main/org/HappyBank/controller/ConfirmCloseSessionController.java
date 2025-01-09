package org.HappyBank.controller;

import org.HappyBank.view.ViewFactory;

/**
 * Controller for the confirmation window to close the session.
 */
public class ConfirmCloseSessionController {
    private ViewFactory viewFactory;

    /**
     * Initializes the viewFactory instance.
     */
    public void initialize() {
        viewFactory = viewFactory.getInstance(null);
    }

    /**
     * Method that closes the session and shows the login view.
     */
    public void close() {
        viewFactory.closeLegend();
        viewFactory.showLoginView();
        viewFactory.closePopup();
    }

    /**
     * Method that closes the confirmation window.
     */
    public void decline() {
        viewFactory.closePopup();
    }
}
