package com.poskemon.epro.prservice.repository;

import com.poskemon.epro.prservice.domain.dto.PrUpdateDTO;
import com.poskemon.epro.prservice.domain.dto.PurchaseUnitReq;
import com.poskemon.epro.prservice.domain.dto.NeedByDateSearch;
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
        "and (:#{#purchaseUnitReq.prStatus} is null or ph.prStatus = :#{#purchaseUnitReq.prStatus}) and ph.prStatus != :#{#purchaseUnitReq.except} " +
        "and (:#{#purchaseUnitReq.category} is null or i.category = :#{#purchaseUnitReq.category}) " +
        "and (:#{#purchaseUnitReq.rfqNo} = -1L or pl.rfqNo = :#{#purchaseUnitReq.rfqNo}) order by pl.prLine, ph.prNo")
    List<PrLine> findAllPrWithParams(@Param("purchaseUnitReq") PurchaseUnitReq purchaseUnitReq);

    @Query(value = "select pl, ph " +
        "from PrLine pl " +
        "join pl.prHeader ph " +
        "where (:#{#prUpdateDTO.prNo} is null or ph.prNo = :#{#prUpdateDTO.prNo}) " +
        "and pl.prLine in (:#{#prUpdateDTO.prLines})")
    List<PrLine> findAllByPrNoAndPrLine(@Param("PrUpdateDTO") PrUpdateDTO prUpdateDTO);

    @Query(nativeQuery = true,
           value = "select * from (select max(pl.need_by_date) as needByDate, pl.rfq_no as rfqNo from pr_line pl group by pl.rfq_no) as A where A.rfqNo in (:rfqNos)")
    List<NeedByDateSearch> findAllByRfqNos(@Param("rfqNos") List<Long> rfqNos);
}
