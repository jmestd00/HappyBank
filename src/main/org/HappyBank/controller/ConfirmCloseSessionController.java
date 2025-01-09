package org.HappyBank.controller;

import org.HappyBank.view.ViewFactory;

/**
 * Controlador para la vista de confirmación de cierre de sesión.
 */
public class ConfirmCloseSessionController {
    private ViewFactory viewFactory;

    /**
     * Metodo que inicializa la vista de confirmación de cierre de sesión.
     */
    public void initialize() {
        viewFactory = viewFactory.getInstance(null);
    }

    /**
     * Metodo que cierra la sesión del usuario cerrando también los diversos popups.
     */
    public void close() {
        viewFactory.closeLegend();
        viewFactory.showLoginView();
        viewFactory.closePopup();
    }

    /**
     * Metodo que cancela el cierre de sesión.
     */
    public void decline() {
        viewFactory.closePopup();
    }
}
