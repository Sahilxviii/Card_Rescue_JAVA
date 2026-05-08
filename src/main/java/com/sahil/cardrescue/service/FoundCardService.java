package com.sahil.cardrescue.service;

import com.sahil.cardrescue.dto.CardResponse;
import com.sahil.cardrescue.dto.FoundCardRequest;
import com.sahil.cardrescue.entity.*;
import com.sahil.cardrescue.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FoundCardService {

    @Autowired
    private FoundCardRepository foundCardRepository;

    @Autowired
    private LostCardRepository lostCardRepository;

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private EmailService emailService;

    public CardResponse saveFoundCard(FoundCardRequest request, User user) {

        validateRequest(request);

        FoundCard foundCard = new FoundCard();
        foundCard.setUser(user);
        foundCard.setCardType(request.getCardType());
        foundCard.setBankName(request.getBankName());
        foundCard.setLast4Digits(request.getLast4Digits());
        foundCard.setStatus("OPEN");

        FoundCard saved = foundCardRepository.save(foundCard);

        matchFoundCard(saved);

        return mapToCardResponse(saved);
    }

    public List<CardResponse> getMyFoundCards(User user) {
        return foundCardRepository.findByUserId(user.getId())
                .stream()
                .map(this::mapToCardResponse)
                .toList();
    }

    private CardResponse mapToCardResponse(FoundCard foundCard) {
        return new CardResponse(
                foundCard.getId(),
                foundCard.getCardType(),
                foundCard.getBankName(),
                foundCard.getLast4Digits(),
                foundCard.getStatus()
        );
    }

    private void validateRequest(FoundCardRequest request) {

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

    private void matchFoundCard(FoundCard foundCard) {

        List<LostCard> candidates =
                lostCardRepository.findByCardTypeAndBankNameAndLast4DigitsAndStatus(
                        foundCard.getCardType(),
                        foundCard.getBankName(),
                        foundCard.getLast4Digits(),
                        "OPEN"
                );

        for (LostCard lost : candidates) {

            if (lost.getStatus().equals("OPEN") && foundCard.getStatus().equals("OPEN")) {

                Match match = new Match();
                match.setLostCardId(lost.getId());
                match.setFoundCardId(foundCard.getId());
                match.setMatchedAt(LocalDateTime.now());

                matchRepository.save(match);

                lost.setStatus("MATCHED");
                foundCard.setStatus("MATCHED");

                lostCardRepository.save(lost);
                foundCardRepository.save(foundCard);

                sendMatchEmails(lost, foundCard);

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