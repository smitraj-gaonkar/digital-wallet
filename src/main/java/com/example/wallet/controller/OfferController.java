package com.example.wallet.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.wallet.service.OfferService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class OfferController {

    @Autowired
    OfferService offerService;

    @GetMapping("/offers")
    public ResponseEntity<?> getAll() {
        return new ResponseEntity<>(offerService.getAll(), HttpStatus.OK);
    }
    
    
}
