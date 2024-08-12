package com.example.wallet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.wallet.enitites.Offer;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long>{
  
}
