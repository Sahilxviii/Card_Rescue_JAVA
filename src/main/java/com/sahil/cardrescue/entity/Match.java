package com.sahil.cardrescue.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "matches")
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long lostCardId;
    private Long foundCardId;

    private LocalDateTime matchedAt;

    // getters & setters
    public Long getId() { return id; }

    public Long getLostCardId() { return lostCardId; }
    public void setLostCardId(Long lostCardId) { this.lostCardId = lostCardId; }

    public Long getFoundCardId() { return foundCardId; }
    public void setFoundCardId(Long foundCardId) { this.foundCardId = foundCardId; }

    public LocalDateTime getMatchedAt() { return matchedAt; }
    public void setMatchedAt(LocalDateTime matchedAt) { this.matchedAt = matchedAt; }
}