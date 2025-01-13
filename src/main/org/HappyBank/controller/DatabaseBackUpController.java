package org.HappyBank.controller;

import org.HappyBank.model.BankService;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class DatabaseBackUpController {

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
        readConfig();
        if (backUp) {
            //bankService.createBackup();
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
            e.printStackTrace();
        }
    }


}
