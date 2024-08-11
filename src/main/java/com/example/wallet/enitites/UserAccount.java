package com.example.wallet.enitites;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@SequenceGenerator(name = "user_account_generator", sequenceName = "user_account_seq", allocationSize = 1)
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class UserAccount extends Account{

    // @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    
    private String email;
    
    private Long mobile;
    
    private String password;
    
    @OneToMany(mappedBy = "userId", cascade = CascadeType.ALL)
    private List<BankAccount> bankAccounts = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "wallet_id", referencedColumnName = "id")
    private Wallet wallet;

    @ElementCollection(targetClass = Long.class)
    // @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_account_bill", joinColumns = @JoinColumn(name = "user_account_id"))
    @Column(name = "consumer_id")
    Set<Long> consumerIds = new HashSet<>();
 
}
