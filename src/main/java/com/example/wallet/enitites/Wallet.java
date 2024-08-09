package com.example.wallet.enitites;

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
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Double balance;

    // @OneToOne
    // @JoinColumn
    private Long userId;
    // private UserAccount userAccount;
    
    public Wallet(Double balance, Long userId) {
        this.balance = balance;
        this.userId = userId;
    }

    public Double credit(Double amount) {
        this.balance += amount;
        return this.balance;
    }

    public Double debit(Double amount) {
        if(this.balance>=amount) {
            this.balance -= amount;
        }
        return this.balance;
    }
}
