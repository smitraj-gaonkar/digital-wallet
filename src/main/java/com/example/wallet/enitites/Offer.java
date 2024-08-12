package com.example.wallet.enitites;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class Offer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String code;

    String region;

    Date validFrom;

    Date validTo;

    Double cashbackPercent;

    public Offer( 
        String code,
        String region, 
        Date validFrom, 
        Date validTo, 
        Double cashbackPercent) {
            this.code = code;
            this.region = region;
            this.validFrom = validFrom;
            this.validTo = validTo;
            this.cashbackPercent = cashbackPercent;
    }
    
}
