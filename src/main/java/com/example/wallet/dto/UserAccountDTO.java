package com.example.wallet.dto;

import com.example.wallet.enitites.UserAccount;

import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class UserAccountDTO {

    // private Long id;
    
    private String name;
    
    private String email;
    
    private Long mobile;
    
    private String password;
    
    // @OneToMany(mappedBy = "userAccount", cascade = CascadeType.ALL)
    // private List<BankAccount> bankAccounts;

    public UserAccount mapToUserAccount() {
        UserAccount userAccount = new UserAccount();
        userAccount.setName(this.getName());
        userAccount.setEmail(this.getEmail());
        userAccount.setMobile(this.getMobile());
        userAccount.setPassword(this.getPassword());
        return userAccount;
    }
    
}
