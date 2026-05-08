package com.sahil.cardrescue.service;

import com.sahil.cardrescue.dto.CardResponse;
import com.sahil.cardrescue.dto.LostCardRequest;
import com.sahil.cardrescue.entity.*;
import com.sahil.cardrescue.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LostCardService {

    @Autowired
    private LostCardRepository lostCardRepository;

    @Autowired
    private FoundCardRepository foundCardRepository;

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private EmailService emailService;

    public CardResponse saveLostCard(LostCardRequest request, User user) {

        validateRequest(request);

        LostCard lostCard = new LostCard();
        lostCard.setUser(user);
        lostCard.setCardType(request.getCardType());
        lostCard.setBankName(request.getBankName());
        lostCard.setLast4Digits(request.getLast4Digits());
        lostCard.setStatus("OPEN");

        LostCard saved = lostCardRepository.save(lostCard);

        matchLostCard(saved);

        return mapToCardResponse(saved);
    }

    public List<CardResponse> getMyLostCards(User user) {
        return lostCardRepository.findByUserId(user.getId())
                .stream()
                .map(this::mapToCardResponse)
                .toList();
    }

    private CardResponse mapToCardResponse(LostCard lostCard) {
        return new CardResponse(
                lostCard.getId(),
                lostCard.getCardType(),
                lostCard.getBankName(),
                lostCard.getLast4Digits(),
                lostCard.getStatus()
        );
    }

    private void validateRequest(LostCardRequest request) {

        if (request.getCardType() == null || request.getCardType().isBlank()) {
            throw new RuntimeException("Card type is required");
        }

        if (request.getBankName() == null || request.getBankName().isBlank()) {
            throw new RuntimeException("Bank name is required");
        }

        if (request.getLast4Digits() == null || !request.getLast4Digits().matches("\\d{4}")) {
            throw new RuntimeException("Last 4 digits must be exactly 4 numbers");
        }
    }

    private void matchLostCard(LostCard lostCard) {

        List<FoundCard> candidates =
                foundCardRepository.findByCardTypeAndBankNameAndLast4DigitsAndStatus(
                        lostCard.getCardType(),
                        lostCard.getBankName(),
                        lostCard.getLast4Digits(),
                        "OPEN"
                );

        for (FoundCard found : candidates) {

            if (found.getStatus().equals("OPEN") && lostCard.getStatus().equals("OPEN")) {

                Match match = new Match();
                match.setLostCardId(lostCard.getId());
                match.setFoundCardId(found.getId());
                match.setMatchedAt(LocalDateTime.now());

                matchRepository.save(match);

                found.setStatus("MATCHED");
                lostCard.setStatus("MATCHED");

                foundCardRepository.save(found);
                lostCardRepository.save(lostCard);

                sendMatchEmails(lostCard, found);

                break;
            }
        }
    }

    private void sendMatchEmails(LostCard lostCard, FoundCard foundCard) {

        User lostUser = lostCard.getUser();
        User foundUser = foundCard.getUser();

        String subject = "Card Rescue - Card Match Found";

        String lostUserBody =
                "Hello " + lostUser.getName() + ",\n\n" +
                "Good news! A matching found card report has been submitted.\n\n" +
                "Card Type: " + lostCard.getCardType() + "\n" +
                "Bank Name: " + lostCard.getBankName() + "\n" +
                "Last 4 Digits: " + lostCard.getLast4Digits() + "\n\n" +
                "Please login to Card Rescue to check your match details.\n\n" +
                "Regards,\nCard Rescue Team";

        String foundUserBody =
                "Hello " + foundUser.getName() + ",\n\n" +
                "Thank you! Your found card report has matched with a lost card report.\n\n" +
                "Card Type: " + foundCard.getCardType() + "\n" +
                "Bank Name: " + foundCard.getBankName() + "\n" +
                "Last 4 Digits: " + foundCard.getLast4Digits() + "\n\n" +
                "Please keep the card safe and wait for the owner verification process.\n\n" +
                "Regards,\nCard Rescue Team";

        emailService.sendMatchNotification(lostUser.getEmail(), subject, lostUserBody);
        emailService.sendMatchNotification(foundUser.getEmail(), subject, foundUserBody);
    }
}