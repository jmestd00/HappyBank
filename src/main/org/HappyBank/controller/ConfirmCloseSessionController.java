package org.HappyBank.controller;

import org.HappyBank.view.ViewFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Controlador para la vista de confirmación de cierre de sesión.
 */
public class ConfirmCloseSessionController {
private static final Logger logger = LogManager.getLogger(ConfirmCloseSessionController.class.getName());
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
        logger.info("Se ha cancelado el cierre de sesión.");
        viewFactory.closePopup();
    }
}
