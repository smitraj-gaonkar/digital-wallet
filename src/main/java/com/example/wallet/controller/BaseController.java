package com.example.wallet.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class BaseController {

    @GetMapping("/")
    public ResponseEntity<String> baseContext() {
        return new ResponseEntity<>("Digital Wallet", HttpStatus.OK);
    }

}
