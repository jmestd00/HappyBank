package org.HappyBank.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public record Transaction(int transactionID, LocalDate transactionDate, BigDecimal transactionAmount, String transactionDescription,
                          Client transactionSenderClient, Client transactionReceiverClient) {
    private static DateTimeFormatter formater = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Transaction {\nID: " + transactionID + ", \nDate: " + transactionDate.format(formater) + ", \nAmount: " + transactionAmount.setScale(2, RoundingMode.HALF_UP) + ", \nDescription: "
        + transactionDescription + ", \nSender Client: " + transactionSenderClient.toString() + ", \nReceiver Client: " + transactionReceiverClient.toString() + "\n}");
        return sb.toString();
    }

}
