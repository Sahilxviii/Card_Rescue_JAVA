package com.sahil.cardrescue.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.sahil.cardrescue.dto.CardResponse;
import com.sahil.cardrescue.dto.LostCardRequest;
import com.sahil.cardrescue.entity.User;
import com.sahil.cardrescue.service.LostCardService;

import java.util.List;

@RestController
@RequestMapping("/lost")
@CrossOrigin
public class LostCardController {

    @Autowired
    private LostCardService lostCardService;

    @PostMapping
    public CardResponse createLostCard(@RequestBody LostCardRequest request) {

        User user = getCurrentUser();

        return lostCardService.saveLostCard(request, user);
    }

    @GetMapping("/my")
    public List<CardResponse> getMyLostCards() {

        User user = getCurrentUser();

        return lostCardService.getMyLostCards(user);
    }

    private User getCurrentUser() {
        return (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }
}