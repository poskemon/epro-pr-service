package com.poskemon.epro.prservice.repository;

import com.poskemon.epro.prservice.domain.dto.PurchaseUnitReq;
import com.poskemon.epro.prservice.domain.entity.PrHeader;
import com.poskemon.epro.prservice.domain.entity.PrLine;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrLineRepository extends JpaRepository<PrLine, Long> {
    List<PrLine> findAllByPrHeader(PrHeader prHeader);

    void deleteAllByPrHeader(PrHeader prHeader);

    @Query(value = "select pl, ph, i " +
        "from PrLine pl " +
        "join pl.prHeader ph " +
        "join pl.item i " +
        "where (:#{#purchaseUnitReq.prNo} is null or ph.prNo = :#{#purchaseUnitReq.prNo}) " +
        "and (:#{#purchaseUnitReq.prTitle} is null or ph.prTitle = :#{#purchaseUnitReq.prTitle}) " +
        "and (:#{#purchaseUnitReq.requesterNo} = -1L or ph.requesterNo = :#{#purchaseUnitReq.requesterNo}) " +
        "and (:#{#purchaseUnitReq.buyerNo} = -1L or pl.buyerNo = :#{#purchaseUnitReq.buyerNo}) " +
        "and (:#{#purchaseUnitReq.itemDescription} is null or i.itemDescription = :#{#purchaseUnitReq.itemDescription}) " +
        "and (:#{#purchaseUnitReq.spec} is null or i.spec = :#{#purchaseUnitReq.spec}) " +
        "and (:#{#purchaseUnitReq.prStatus} is null or ph.prStatus = :#{#purchaseUnitReq.prStatus}) " +
        "and (:#{#purchaseUnitReq.category} is null or i.category = :#{#purchaseUnitReq.category}) " +
        "and (:#{#purchaseUnitReq.rfqNo} = -1L or pl.rfqNo = :#{#purchaseUnitReq.rfqNo})")
    List<PrLine> findAllPrWithParams(@Param("purchaseUnitReq") PurchaseUnitReq purchaseUnitReq);
}
