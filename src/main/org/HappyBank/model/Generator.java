package org.HappyBank.model;

import org.HappyBank.model.repository.AccountRepositoryImpl;
import org.HappyBank.model.repository.CreditCardRepositoryImpl;

import java.math.BigInteger;
import java.util.Random;

/**
 * Clase de utilidad que genera los números de cuenta, de las tarjetas de crédito y sus CVV de manera única y estándar.
 */
public class Generator {
    /**
     * Repositorio de cuentas.
     */
    private AccountRepositoryImpl accountRepository;
    /**
     * Repositorio de tarjetas de crédito.
     */
    private CreditCardRepositoryImpl cardRepository;
    
    //Constants
    /**
     * Código de país.
     */
    private static final String COUNTRY_CODE = "ES";
    /**
     * Código de banco.
     */
    private static final String BANK_CODE = "0064";
    /**
     * Código de sucursal.
     */
    private static final String BRANCH_CODE = "0001";
    /**
     * Generador de números aleatorios.
     */
    private static final Random random = new Random();
    
    
    //Constructor
    /**
     * Constructor de la clase.
     */
    public Generator() {
        this.accountRepository = new AccountRepositoryImpl();
        this.cardRepository = new CreditCardRepositoryImpl();
    }
    
    
    //Setters
    /**
     * Establece el repositorio de cuentas.
     *
     * @param accountRepository Repositorio de cuentas.
     */
    public void setAccountRepository(AccountRepositoryImpl accountRepository) {
        this.accountRepository = accountRepository;
    }
    
    /**
     * Establece el repositorio de tarjetas de crédito.
     *
     * @param cardRepository Repositorio de tarjetas de crédito.
     */
    public void setCardRepository(CreditCardRepositoryImpl cardRepository) {
        this.cardRepository = cardRepository;
    }
    
    
    //IBAN
    /**
     * Genera un IBAN único.
     *
     * @return IBAN único.
     */
    public String generateUniqueIBAN() {
        String iban;
        
        do {
            String randomTwoDigits = generateRandomDigits(2);
            String accountNumber = generateRandomDigits(10);
            String partialIBAN = BANK_CODE + BRANCH_CODE + randomTwoDigits + accountNumber;
            String checkDigits = calculateCheckDigits(partialIBAN);
            iban = COUNTRY_CODE + checkDigits + " " + BANK_CODE + " " + BRANCH_CODE + " " + randomTwoDigits + " " + accountNumber;
        } while (accountRepository.isIBAN(iban));
        
        return iban;
    }
    
    /**
     * Genera una cadena de dígitos aleatorios.
     *
     * @param length Longitud de la cadena.
     * @return Cadena de dígitos aleatorios.
     */
    private static String generateRandomDigits(int length) {
        StringBuilder digits = new StringBuilder();
        for (int i = 0; i < length; i++) {
            digits.append(random.nextInt(10));
        }
        return digits.toString();
    }
    
    /**
     * Calcula los dígitos de control de un IBAN.
     *
     * @param partialIBAN IBAN parcial.
     * @return Dígitos de control.
     */
    private static String calculateCheckDigits(String partialIBAN) {
        String ibanWithoutCheckDigits = COUNTRY_CODE + "00" + partialIBAN;
        BigInteger numericIBAN = convertIBANToNumeric(ibanWithoutCheckDigits);
        BigInteger remainder = numericIBAN.mod(BigInteger.valueOf(97));
        BigInteger checkDigits = BigInteger.valueOf(98).subtract(remainder);
        return String.format("%02d", checkDigits.intValue());
    }
    
    /**
     * Convierte un IBAN a un número.
     *
     * @param iban IBAN.
     * @return Número.
     */
    private static BigInteger convertIBANToNumeric(String iban) {
        StringBuilder numericIBAN = new StringBuilder();
        for (int i = 0; i < iban.length(); i++) {
            char c = iban.charAt(i);
            if (Character.isDigit(c)) {
                numericIBAN.append(c);
            } else {
                
                int numericValue = c - 'A' + 10;
                numericIBAN.append(numericValue);
            }
        }
        return new BigInteger(numericIBAN.toString());
    }
    
    
    //CardNumber
    /**
     * Genera un número de tarjeta de crédito único.
     *
     * @return Número de tarjeta de crédito único.
     */
    public String generateUniqueCreditCard() {
        String cardNumber;
        
        do {
            cardNumber = generateCardNumber();
        } while (cardRepository.isNumber(cardNumber));
        return formatCardNumber(cardNumber);
    }
    
    /**
     * Genera un número de tarjeta de crédito.
     *
     * @return Número de tarjeta de crédito.
     */
    private static String generateCardNumber() {
        int length = 16;
        String prefix = "64";
        StringBuilder cardNumber = new StringBuilder(prefix);
        
        while (cardNumber.length() < length - 1) {
            cardNumber.append(random.nextInt(10));
        }
        
        int checkDigit = calculateLuhnCheckDigit(cardNumber.toString());
        cardNumber.append(checkDigit);
        
        return cardNumber.toString();
    }
    
    /**
     * Calcula el dígito de control de Luhn de un número.
     *
     * @param number Número.
     * @return Dígito de control de Luhn.
     */
    private static int calculateLuhnCheckDigit(String number) {
        int sum = 0;
        boolean alternate = true;
        
        for (int i = number.length() - 1; i >= 0; i--) {
            int digit = Character.getNumericValue(number.charAt(i));
            if (alternate) {
                digit *= 2;
                if (digit > 9) {
                    digit -= 9;
                }
            }
            sum += digit;
            alternate = !alternate;
        }
        return (10 - (sum % 10)) % 10;
    }
    
    /**
     * Formatea un número de tarjeta de crédito.
     *
     * @param cardNumber Número de tarjeta de crédito.
     * @return Número de tarjeta de crédito formateado.
     */
    private static String formatCardNumber(String cardNumber) {
        StringBuilder formattedCardNumber = new StringBuilder();
        for (int i = 0; i < cardNumber.length(); i++) {
            if (i > 0 && i % 4 == 0) {
                formattedCardNumber.append(" ");
            }
            formattedCardNumber.append(cardNumber.charAt(i));
        }
        return formattedCardNumber.toString();
    }
    
    
    //CVV
    /**
     * Genera un CVV.
     *
     * @return CVV.
     */
    public static String generateCVV() {
        Random random = new Random();
        int cvv = random.nextInt(900) + 100;
        return String.valueOf(cvv);
    }
}
