package com.sahil.cardrescue.dto;

public class CardResponse {

    private Long id;
    private String cardType;
    private String bankName;
    private String last4Digits;
    private String status;

    public CardResponse(Long id, String cardType, String bankName, String last4Digits, String status) {
        this.id = id;
        this.cardType = cardType;
        this.bankName = bankName;
        this.last4Digits = last4Digits;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getCardType() {
        return cardType;
    }

    public String getBankName() {
        return bankName;
    }

    public String getLast4Digits() {
        return last4Digits;
    }

    public String getStatus() {
        return status;
    }
}