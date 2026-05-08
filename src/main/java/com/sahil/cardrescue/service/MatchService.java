package com.sahil.cardrescue.service;

import com.sahil.cardrescue.dto.MatchResponse;
import com.sahil.cardrescue.entity.FoundCard;
import com.sahil.cardrescue.entity.LostCard;
import com.sahil.cardrescue.entity.Match;
import com.sahil.cardrescue.entity.User;
import com.sahil.cardrescue.repository.FoundCardRepository;
import com.sahil.cardrescue.repository.LostCardRepository;
import com.sahil.cardrescue.repository.MatchRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatchService {

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private LostCardRepository lostCardRepository;

    @Autowired
    private FoundCardRepository foundCardRepository;

    public List<MatchResponse> getMyMatches(User user) {

        List<Long> lostCardIds = lostCardRepository.findByUserId(user.getId())
                .stream()
                .map(LostCard::getId)
                .toList();

        List<Long> foundCardIds = foundCardRepository.findByUserId(user.getId())
                .stream()
                .map(FoundCard::getId)
                .toList();

        return matchRepository.findByLostCardIdInOrFoundCardIdIn(lostCardIds, foundCardIds)
                .stream()
                .map(this::mapToMatchResponse)
                .toList();
    }

    private MatchResponse mapToMatchResponse(Match match) {
        return new MatchResponse(
                match.getId(),
                match.getLostCardId(),
                match.getFoundCardId(),
                match.getMatchedAt()
        );
    }
}