package com.example.wallet.initializer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.wallet.enitites.BankAccount;
import com.example.wallet.enitites.Bill;
import com.example.wallet.enitites.MerchantAccount;
import com.example.wallet.enitites.Offer;
import com.example.wallet.enitites.Transaction;
import com.example.wallet.enums.BillStatus;
import com.example.wallet.enums.Status;
import com.example.wallet.enums.TransactionType;
import com.example.wallet.enums.UtilityType;
import com.example.wallet.repository.BankAccountRepository;
import com.example.wallet.repository.BillRepository;
import com.example.wallet.repository.MerchantAccountRepository;
import com.example.wallet.repository.OfferRepository;
import com.example.wallet.repository.TransactionRepository;
import com.example.wallet.service.WalletService;

@Component
public class DatabaseInitializer {

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private MerchantAccountRepository merchantAccountRepository;

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private WalletService walletService;

    @PostConstruct
    public void init() {
        bankAccountRepository.save(new BankAccount(Long.valueOf("9000001"), "Smith John", "AXIS110011", Double.valueOf(1000.50), Integer.valueOf(1234), null));
        bankAccountRepository.save(new BankAccount(Long.valueOf("9000002"), "John Doe", "HDFC2200022", Double.valueOf(2000.25), Integer.valueOf(1234), null));
        bankAccountRepository.save(new BankAccount(Long.valueOf("9000003"), "Alex Chase", "HDFC2200021", Double.valueOf(1540.25), Integer.valueOf(1234), null));
        bankAccountRepository.save(new BankAccount(Long.valueOf("9000004"), "David Pinkman", "AXIS1100012", Double.valueOf(2120.25), Integer.valueOf(1234), null));
        bankAccountRepository.save(new BankAccount(Long.valueOf("9000005"), "Smith John", "ICIC3300030", Double.valueOf(5000.25), Integer.valueOf(1234), null));


        MerchantAccount merchantAccount = new MerchantAccount();
        
        merchantAccount = merchantAccountRepository.save(new MerchantAccount("Tata", new HashSet<>(Arrays.asList(UtilityType.ELECTRICITY, UtilityType.DTH))));
        merchantAccount.setWallet(walletService.activateWallet(merchantAccount));
        merchantAccountRepository.save(merchantAccount);

        merchantAccount = merchantAccountRepository.save(new MerchantAccount("Adani", new HashSet<>(Arrays.asList(UtilityType.ELECTRICITY))));
        merchantAccount.setWallet(walletService.activateWallet(merchantAccount));
        merchantAccountRepository.save(merchantAccount);

        merchantAccount = merchantAccountRepository.save(new MerchantAccount("Mahanagar Gas", new HashSet<>(Arrays.asList(UtilityType.GAS))));
        merchantAccount.setWallet(walletService.activateWallet(merchantAccount));
        merchantAccountRepository.save(merchantAccount);

        merchantAccount = merchantAccountRepository.save(new MerchantAccount("Airtel", new HashSet<>(Arrays.asList(UtilityType.MOBILE, UtilityType.DTH, UtilityType.BROADBAND))));
        merchantAccount.setWallet(walletService.activateWallet(merchantAccount));
        merchantAccountRepository.save(merchantAccount);

        merchantAccount = merchantAccountRepository.save(new MerchantAccount("Jio", new HashSet<>(Arrays.asList(UtilityType.MOBILE, UtilityType.BROADBAND))));
        merchantAccount.setWallet(walletService.activateWallet(merchantAccount));
        merchantAccountRepository.save(merchantAccount);

        billRepository.save(new Bill(Long.valueOf(8000001), UtilityType.ELECTRICITY, new Date(), Double.valueOf(100), BillStatus.UNPAID, null, merchantAccountRepository.findByName("Tata").getId(),"Tata"));
        billRepository.save(new Bill(Long.valueOf(8000002), UtilityType.ELECTRICITY, new Date(), Double.valueOf(1800), BillStatus.UNPAID, null, merchantAccountRepository.findByName("Tata").getId(), "Tata"));
        billRepository.save(new Bill(Long.valueOf(8000003), UtilityType.ELECTRICITY, new Date(), Double.valueOf(1020), BillStatus.UNPAID, null, merchantAccountRepository.findByName("Tata").getId(), "Tata"));
        billRepository.save(new Bill(Long.valueOf(8000004), UtilityType.DTH, new Date(), Double.valueOf(250), BillStatus.UNPAID, null, merchantAccountRepository.findByName("Tata").getId(), "Tata"));
        billRepository.save(new Bill(Long.valueOf(8000005), UtilityType.DTH, new Date(), Double.valueOf(300), BillStatus.UNPAID, null, merchantAccountRepository.findByName("Tata").getId(), "Tata"));
        billRepository.save(new Bill(Long.valueOf(7000001), UtilityType.ELECTRICITY, new Date(), Double.valueOf(1000), BillStatus.UNPAID, null, merchantAccountRepository.findByName("Adani").getId(), "Adani"));
        billRepository.save(new Bill(Long.valueOf(7000002), UtilityType.ELECTRICITY, new Date(), Double.valueOf(1500), BillStatus.UNPAID, null, merchantAccountRepository.findByName("Adani").getId(), "Adani"));
        billRepository.save(new Bill(Long.valueOf(5000001), UtilityType.GAS, new Date(), Double.valueOf(1010), BillStatus.UNPAID, null, merchantAccountRepository.findByName("Mahanagar Gas").getId(), "Mahanagar Gas"));
        billRepository.save(new Bill(Long.valueOf(5000002), UtilityType.GAS, new Date(), Double.valueOf(900), BillStatus.UNPAID, null, merchantAccountRepository.findByName("Mahanagar Gas").getId(), "Mahanagar Gas"));
        billRepository.save(new Bill(Long.valueOf(4000001), UtilityType.MOBILE, new Date(), Double.valueOf(399), BillStatus.UNPAID, null, merchantAccountRepository.findByName("Airtel").getId(), "Airtel"));
        billRepository.save(new Bill(Long.valueOf(4000002), UtilityType.DTH, new Date(), Double.valueOf(350), BillStatus.UNPAID, null, merchantAccountRepository.findByName("Airtel").getId(), "Airtel"));
        billRepository.save(new Bill(Long.valueOf(4000003), UtilityType.BROADBAND, new Date(), Double.valueOf(200), BillStatus.UNPAID, null, merchantAccountRepository.findByName("Airtel").getId(), "Airtel"));
        billRepository.save(new Bill(Long.valueOf(3000001), UtilityType.MOBILE, new Date(), Double.valueOf(299), BillStatus.UNPAID, null, merchantAccountRepository.findByName("Jio").getId(), "Jio"));
        billRepository.save(new Bill(Long.valueOf(3000002), UtilityType.MOBILE, new Date(), Double.valueOf(299), BillStatus.UNPAID, null, merchantAccountRepository.findByName("Jio").getId(),"Jio"));
        billRepository.save(new Bill(Long.valueOf(3000003), UtilityType.BROADBAND, new Date(), Double.valueOf(300), BillStatus.UNPAID, null, merchantAccountRepository.findByName("Jio").getId(),"Jio"));
        
        Date validFromDate, validTodate;
        try {

            validFromDate = new SimpleDateFormat("dd-MMM-yyyy").parse("10-Aug-2024");
            validTodate = new SimpleDateFormat("dd-MMM-yyyy").parse("15-Aug-2024");
            offerRepository.save(new Offer("OFFXYZABC02", "INDIA", validFromDate, validTodate, 2.00));
            offerRepository.save(new Offer("OFFXYZABC05", "INDIA", validFromDate, validFromDate, 5.00));
            offerRepository.save(new Offer("OFFXYZABC10", "INDIA", validFromDate, validTodate, 10.00));
            offerRepository.save(new Offer("OFFXYZABC15", "INDIA", validFromDate, validFromDate, 15.00));
            offerRepository.save(new Offer("OFFXYZABC20", "INDIA", validFromDate, validTodate, 20.00));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        
        
        
        transactionRepository.save(
            new Transaction(
                Long.valueOf(0),
                new Date(),
                TransactionType.CREDIT,
                Double.valueOf(0),
                Status.SUCCESS,
                "INFO:",
                "MESSAGE",
                Long.valueOf(0),
                Long.valueOf(0)
            )
        );


    }
}