package com.sahil.cardrescue.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "lost_cards")
public class LostCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "card_type", nullable = false, length = 255)
    private String cardType;

    @Column(name = "bank_name", nullable = false, length = 255)
    private String bankName;

    @Column(name = "last4_digits", nullable = false, length = 4)
    private String last4Digits;

    @Column(name = "status")
    private String status = "OPEN";

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getLast4Digits() {
        return last4Digits;
    }

    public void setLast4Digits(String last4Digits) {
        this.last4Digits = last4Digits;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}