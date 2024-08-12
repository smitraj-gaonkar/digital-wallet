package com.example.wallet.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.wallet.enitites.Offer;
import com.example.wallet.repository.OfferRepository;

@Service
public class OfferService {

    @Autowired
    OfferRepository offerRepository;

    public List<Offer> getAll() {
        return offerRepository.findAll();
    }
    
}
