package com.sahil.cardrescue.repository;

import com.sahil.cardrescue.entity.FoundCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoundCardRepository extends JpaRepository<FoundCard, Long> {

    List<FoundCard> findByCardTypeAndBankNameAndLast4DigitsAndStatus(
            String cardType,
            String bankName,
            String last4Digits,
            String status
    );

    List<FoundCard> findByUserId(Long userId);

    void deleteByUserId(Long userId);
}