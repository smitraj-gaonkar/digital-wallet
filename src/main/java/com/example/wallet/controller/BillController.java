package com.example.wallet.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.wallet.enitites.Bill;
import com.example.wallet.enitites.MerchantAccount;
import com.example.wallet.enitites.Offer;
import com.example.wallet.enitites.Transaction;
import com.example.wallet.enitites.UserAccount;
import com.example.wallet.enums.BillStatus;
import com.example.wallet.enums.Status;
import com.example.wallet.enums.TransactionType;
import com.example.wallet.service.BillService;
import com.example.wallet.service.MerchantAccountService;
import com.example.wallet.service.OfferService;
import com.example.wallet.service.TransactionService;
import com.example.wallet.service.UserAccountService;
import com.example.wallet.service.WalletService;

import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
public class BillController {

    @Autowired
    BillService billService;

    @Autowired
    UserAccountService userAccountService;

    @Autowired
    MerchantAccountService merchantAccountService;

    @Autowired
    WalletService walletService;

    @Autowired
    TransactionService transactionService;

    @Autowired
    OfferService offerService;

    @GetMapping("/bills")
    public ResponseEntity<?> getAll() {
        return new ResponseEntity<>(billService.getAll(), HttpStatus.OK);
    }

    @PutMapping("userAccount/{userAccountId}/addBill")
    public ResponseEntity<?> addBill(
        @PathVariable Long userAccountId, 
        // @RequestParam UtilityType utilityType,
        // @RequestParam String merchantName,
        @RequestParam Long consumerId) {
        
        Optional<UserAccount> userAccount = userAccountService.getByUserAccountId(userAccountId);
        Optional<Bill> bill = billService.getByConsumerId(consumerId);

        if(!userAccount.isPresent()) {
            return new ResponseEntity<>("Invalid User! User account not found with id: " + userAccountId, HttpStatus.NOT_FOUND);
        }
        if(!bill.isPresent()) {
            return new ResponseEntity<>("Invalid consumer id: " + consumerId, HttpStatus.NOT_FOUND);
        }

        Set<Long> consumerIds = userAccount.get().getConsumerIds();
        consumerIds.add(consumerId);
        userAccount.get().setConsumerIds(consumerIds);
        userAccountService.create(userAccount.get());
        
        return new ResponseEntity<>("Bill details fetched & added successfully.", HttpStatus.OK);
        
    }

    @GetMapping("userAccount/{userAccountId}/fetchBill")
    public ResponseEntity<?> fetchBill(
        @PathVariable Long userAccountId) {
        
        Optional<UserAccount> userAccount = userAccountService.getByUserAccountId(userAccountId);
        if(!userAccount.isPresent()) {
            return new ResponseEntity<>("Invalid User! User account not found with id: " + userAccountId, HttpStatus.NOT_FOUND);
        }
        
        return new ResponseEntity<>(billService.getByConsumerIds(userAccount.get().getConsumerIds()), HttpStatus.OK);
        
    }

    @GetMapping("userAccount/{userAccountId}/fetchBill/{billStatus}")
    public ResponseEntity<?> fetchBillByBillStatus(
        @PathVariable Long userAccountId,
        @PathVariable String billStatus) {
        if(Arrays.stream(BillStatus.values()).noneMatch(status-> status.toString().equals(billStatus.toUpperCase()))) {
            return new ResponseEntity<>("Invalid bill status!", HttpStatus.NOT_FOUND);
        }
        BillStatus status = BillStatus.valueOf(billStatus.toUpperCase());
        Optional<UserAccount> userAccount = userAccountService.getByUserAccountId(userAccountId);
        if(!userAccount.isPresent()) {
            return new ResponseEntity<>("Invalid User! User account not found with id: " + userAccountId, HttpStatus.NOT_FOUND);
        }
        
        return new ResponseEntity<>(billService.getByConsumerIdsAndBillStatus(userAccount.get().getConsumerIds(), status), HttpStatus.OK);
        
    }

    @PutMapping("userAccount/{userAccountId}/payBill/{consumerId}")
    public ResponseEntity<?> payBill(
        @PathVariable Long userAccountId,
        @PathVariable Long consumerId,
        @RequestParam String offerCode) {

        Status status = Status.FAILED;
        String info = "BILL PAYMENT. Consumer id: " + consumerId;
        String message = "";
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        
        Optional<UserAccount> userAccount = userAccountService.getByUserAccountId(userAccountId);
        if(!userAccount.isPresent()) {
            message = "Invalid User! User account not found with id: " + userAccountId;
            httpStatus = HttpStatus.NOT_FOUND;
        } else {
            Optional<Bill> bill = billService.getByConsumerIdAndStatus(consumerId, BillStatus.UNPAID);
            if(!bill.isPresent()) {
                message = "No due bill found for consumer id: " + consumerId;
                httpStatus = HttpStatus.NOT_FOUND;
            } else if(bill.get().getAmount()>userAccount.get().getWallet().getBalance()) {
                message = "Insufficient Balance in wallet. Available balance: " + userAccount.get().getWallet().getBalance();
                httpStatus = HttpStatus.BAD_REQUEST;
            } else {
                Double discount = 0.00;
                String discountMessage = "";
                if(offerCode!=null) {
                    if(offerCode!="") {
                        Optional<Offer> appliedOffer = offerService.getAll().stream().filter(offer -> offer.getCode().equals(offerCode)).findFirst();
                        Date todaysDate = new Date();
                        if( appliedOffer.isPresent() && 
                            todaysDate.compareTo(appliedOffer.get().getValidFrom())>=0 &&
                            todaysDate.compareTo(appliedOffer.get().getValidTo())<=0  ) {
                            
                            discount = (bill.get().getAmount() * appliedOffer.get().getCashbackPercent())/100.00;
                            discountMessage = " Offer Code: " + offerCode + ". Instant cashback of amount rs." + discount + " offer applied.";
                        }
                    }
                }

                MerchantAccount merchantAccount = merchantAccountService.getByName(bill.get().getMerchantName());
                walletService.transferToWallet(userAccount.get(), merchantAccount, bill.get().getAmount()-discount);
                billService.updateBillStatusAndPaymentDate(new Date(), BillStatus.PAID, bill.get().getId());
                status = Status.SUCCESS;
                message = "Bill paid successfully." + discountMessage;
                httpStatus = HttpStatus.OK;

                transactionService.create(new Transaction(
                    new Date(),
                    TransactionType.CREDIT,
                    bill.isPresent() ? bill.get().getAmount():0.0,
                    status,
                    info,
                    message,
                    merchantAccount.getId(),
                    merchantAccount.getWallet().getId()
                ));
            }
            
            transactionService.create(new Transaction(
                new Date(),
                TransactionType.DEBIT,
                bill.isPresent() ? bill.get().getAmount():0.0,
                status,
                info,
                message,
                userAccount.get().getId(),
                userAccount.get().getWallet().getId()
            ));
        }
        return new ResponseEntity<>(message, httpStatus);
    }
    
}
