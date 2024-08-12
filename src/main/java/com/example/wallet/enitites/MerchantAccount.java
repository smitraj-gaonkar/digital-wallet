package com.example.wallet.enitites;

import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

import com.example.wallet.enums.UtilityType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@SequenceGenerator(name = "merchant_account_generator", sequenceName = "merchant_account_seq", allocationSize = 1)
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class MerchantAccount extends Account{

    // @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(unique = true)
    String name;

    @ElementCollection(targetClass = UtilityType.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "merchant_accounts_utility_types", joinColumns = @JoinColumn(name = "merchant_account_id"))
    @Column(name = "utility_type")
    Set<UtilityType> utilityType;

    @OneToOne
    @JoinColumn(name = "wallet_id", referencedColumnName = "id")
    private Wallet wallet;

    public MerchantAccount(String name, Set<UtilityType> utilityType) {
        this.name = name;
        this.utilityType = utilityType;
    }
    
    
}
