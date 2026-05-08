package com.sahil.cardrescue.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.sahil.cardrescue.dto.CardResponse;
import com.sahil.cardrescue.dto.FoundCardRequest;
import com.sahil.cardrescue.entity.User;
import com.sahil.cardrescue.service.FoundCardService;

import java.util.List;

@RestController
@RequestMapping("/found")
@CrossOrigin
public class FoundCardController {

    @Autowired
    private FoundCardService foundCardService;

    @PostMapping
    public CardResponse createFoundCard(@RequestBody FoundCardRequest request) {

        User user = getCurrentUser();

        return foundCardService.saveFoundCard(request, user);
    }

    @GetMapping("/my")
    public List<CardResponse> getMyFoundCards() {

        User user = getCurrentUser();

        return foundCardService.getMyFoundCards(user);
    }

    private User getCurrentUser() {
        return (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }
}