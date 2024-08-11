package com.example.wallet.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.wallet.enitites.Bill;
import com.example.wallet.enums.BillStatus;
import com.example.wallet.repository.BillRepository;

@Service
public class BillService {

    @Autowired
    BillRepository billRepository;

    public List<Bill> getAll() {
        return billRepository.findAll();
    }

    public Optional<Bill> getByConsumerId(Long consumerId) {
        return billRepository.findFirstByConsumerIdOrderByGenerateDateDesc(consumerId);
    }

    public Optional<Bill> getByConsumerIdAndStatus(Long consumerId, BillStatus billStatus) {
        return billRepository.findFirstByConsumerIdAndBillStatusOrderByGenerateDateDesc(consumerId, billStatus);
    }

    public List<Bill> getByConsumerIds(Set<Long> consumerIds) {
        return billRepository.findByConsumerIdIn(consumerIds);
    }

    public Object getByConsumerIdsAndBillStatus(Set<Long> consumerIds, BillStatus billStatus) {
        return billRepository.findByBillStatusAndConsumerIdIn(billStatus, consumerIds);
    }

    public void updateBillStatusAndPaymentDate(Date paymentDate, BillStatus billStatus, Long id) {
        billRepository.updateBillStatusAndPaymentDate(paymentDate, billStatus, id);
    }
  
}
