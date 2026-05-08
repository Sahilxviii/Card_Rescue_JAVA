package com.sahil.cardrescue.dto;

import java.time.LocalDateTime;

public class MatchResponse {

    private Long id;
    private Long lostCardId;
    private Long foundCardId;
    private LocalDateTime matchedAt;

    public MatchResponse(Long id, Long lostCardId, Long foundCardId, LocalDateTime matchedAt) {
        this.id = id;
        this.lostCardId = lostCardId;
        this.foundCardId = foundCardId;
        this.matchedAt = matchedAt;
    }

    public Long getId() {
        return id;
    }

    public Long getLostCardId() {
        return lostCardId;
    }

    public Long getFoundCardId() {
        return foundCardId;
    }

    public LocalDateTime getMatchedAt() {
        return matchedAt;
    }
}