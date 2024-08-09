package com.example.wallet.enitites;

import javax.persistence.*;

import lombok.*;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class BankAccount {

    @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountNumber;             //Account number should only contain digits
    
    private String accountHolderName;       //Account holder’s name should contain only alphabets and spaces
    
    private String bankCode;                //Bank code should start with 4 alphabets followed by 6 to 8 digits
    
    @Column(precision = 10, scale = 2)
    private Double balance;                 //Amount should be greater than 0 and can contain 2 digits after the decimal
    
    private Integer verificationPin;

    // @JoinColumn(name = "user_id")
    private Long userId;

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
