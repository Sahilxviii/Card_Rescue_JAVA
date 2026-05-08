package com.sahil.cardrescue.repository;

import com.sahil.cardrescue.entity.LostCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LostCardRepository extends JpaRepository<LostCard, Long> {

    List<LostCard> findByCardTypeAndBankNameAndLast4DigitsAndStatus(
            String cardType,
            String bankName,
            String last4Digits,
            String status
    );

    List<LostCard> findByUserId(Long userId);

    void deleteByUserId(Long userId);
}