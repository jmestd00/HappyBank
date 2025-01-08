package org.HappyBank.model;

import org.HappyBank.model.repository.AccountRepositoryImpl;
import org.HappyBank.model.repository.CreditCardRepositoryImpl;

import java.math.BigInteger;
import java.util.Random;

public class Generator {
    private static final String COUNTRY_CODE = "ES";
    private static final String BANK_CODE = "0064";
    private static final String BRANCH_CODE = "0001";
    private static final Random random = new Random();
    
    
    //IBAN
    public static String generateUniqueIBAN() {
        AccountRepositoryImpl accountRepository = new AccountRepositoryImpl();
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
    
    
    private static String generateRandomDigits(int length) {
        StringBuilder digits = new StringBuilder();
        for (int i = 0; i < length; i++) {
            digits.append(random.nextInt(10));
        }
        return digits.toString();
    }
    
    
    private static String calculateCheckDigits(String partialIBAN) {
        String ibanWithoutCheckDigits = COUNTRY_CODE + "00" + partialIBAN;
        BigInteger numericIBAN = convertIBANToNumeric(ibanWithoutCheckDigits);
        BigInteger remainder = numericIBAN.mod(BigInteger.valueOf(97));
        BigInteger checkDigits = BigInteger.valueOf(98).subtract(remainder);
        return String.format("%02d", checkDigits.intValue());
    }
    
    
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
    public static String generateUniqueCreditCard() {
        CreditCardRepositoryImpl cardRepository = new CreditCardRepositoryImpl();
        String cardNumber;
        
        do {
            cardNumber = generateCardNumber();
        } while (cardRepository.isNumber(cardNumber));
        return formatCardNumber(cardNumber);
    }
    
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
    public static String generateCVV() {
        Random random = new Random();
        int cvv = random.nextInt(900) + 100;
        return String.valueOf(cvv);
    }
}
