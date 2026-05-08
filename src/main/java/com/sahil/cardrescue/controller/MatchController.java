package com.sahil.cardrescue.controller;

import com.sahil.cardrescue.dto.MatchResponse;
import com.sahil.cardrescue.entity.User;
import com.sahil.cardrescue.service.MatchService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/matches")
@CrossOrigin
public class MatchController {

    @Autowired
    private MatchService matchService;

    @GetMapping("/my")
    public List<MatchResponse> getMyMatches() {

        User user = (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        return matchService.getMyMatches(user);
    }
}