package com.example.wallet.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.wallet.enitites.Bill;
import com.example.wallet.enums.BillStatus;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {

    Optional<Bill> findFirstByConsumerIdOrderByGenerateDateDesc(Long consumerId);

    Optional<Bill> findFirstByConsumerIdAndBillStatusOrderByGenerateDateDesc(Long consumerId, BillStatus billStatus);

    List<Bill> findByConsumerIdIn(Set<Long> consumerId);

    List<Bill> findByBillStatusAndConsumerIdIn(BillStatus billStatus, Set<Long> consumerId);

    @Transactional
    @Modifying
    @Query("UPDATE Bill b SET b.paymentDate = :paymentDate, b.billStatus = :billStatus WHERE b.id = :id")
    void updateBillStatusAndPaymentDate(Date paymentDate, BillStatus billStatus, Long id);
  
}
