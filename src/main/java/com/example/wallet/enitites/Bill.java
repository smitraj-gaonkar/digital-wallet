package com.example.wallet.enitites;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.example.wallet.enums.BillStatus;
import com.example.wallet.enums.UtilityType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    
    Long consumerId;
    UtilityType utilityType;
    Date generateDate;
    Double amount;
    BillStatus billStatus;
    Date paymentDate;
    Long merchantId;
    String merchantName;
    // @OneToOne
    // @JoinColumn(name = "merchant_id", referencedColumnName = "id")
    // MerchantAccount merchant;
    

    public Bill(
        Long consumerId, 
        UtilityType utilityType,
        Date generateDate, 
        Double amount, 
        BillStatus billStatus, 
        Date paymentDate, 
        Long merchantId,
        String merchantName
        // MerchantAccount merchant
        ) {
            this.consumerId = consumerId;
            this.utilityType = utilityType;
            this.generateDate = generateDate;
            this.amount = amount;
            this.billStatus = billStatus;
            this.paymentDate = paymentDate;
            this.merchantId = merchantId;
            this.merchantName = merchantName;
    }
    
}
