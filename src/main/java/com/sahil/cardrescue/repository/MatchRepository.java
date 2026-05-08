package com.sahil.cardrescue.repository;

import com.sahil.cardrescue.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {

    List<Match> findByLostCardIdInOrFoundCardIdIn(
            List<Long> lostCardIds,
            List<Long> foundCardIds
    );
}