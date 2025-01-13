package org.HappyBank.controller;

import org.HappyBank.model.BankService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class DatabaseBackUpController {
    private static final Logger logger = LogManager.getLogger(DatabaseBackUpController.class);
    private boolean backUp = false;
    private BankService bankService = new BankService();

    /**
     * Constructor de la clase vacío para poder hacer la copia de seguridad.
     */
    public DatabaseBackUpController() {

    }

    /**
     * Método que realiza una copia de seguridad de la base de datos si está activo en el .config.
     */
    public void doBackUp() {
        ensureBackupDirectoryExists();
        readConfig();
        if (backUp) {
            bankService.createBackup();
        }
    }

    /**
     * Método que lee la configuración del archivo options.config para ver si la opción de backup está activada.
     */
    private void readConfig() {
        Properties prop = new Properties();
        try {
            File file = new File("options.config");
            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                prop.load(fis);
                fis.close();
                backUp = Boolean.parseBoolean(prop.getProperty("bbddBackup"));
            }
        } catch (Exception e) {
            logger.error("Error al leer el archivo de configuración: " + e.getMessage());
        }
    }
    
    /**
     * Garantiza que la carpeta backup carpeta existe.
     * <br>
     * Si no existe, la crea.
     *
     */
    private void ensureBackupDirectoryExists() {
        File directory = new File("/backup/");
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                throw new RuntimeException("No se pudo crear la carpeta de respaldo en: " + "/etc/backup/");
            }
        }
    }
}
