
package com.interface_example.paymentGateway.dto;

/**
 * Represents the payment request received from the client.
 */
public class PaymentRequest {

    private double amount;
    private String method;
    private String upi;
    private String card;
    private String cvv;
    private String bank;

    public PaymentRequest() {
    }

    public PaymentRequest(
            double amount,
            String method,
            String upi,
            String card,
            String cvv,
            String bank) {

        this.amount = amount;
        this.method = method;
        this.upi = upi;
        this.card = card;
        this.cvv = cvv;
        this.bank = bank;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUpi() {
        return upi;
    }

    public void setUpi(String upi) {
        this.upi = upi;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    @Override
    public String toString() {
        return "PaymentRequest{" +
                "amount=" + amount +
                ", method='" + method + '\'' +
                ", upi='" + upi + '\'' +
                ", bank='" + bank + '\'' +
                '}';
    }
}