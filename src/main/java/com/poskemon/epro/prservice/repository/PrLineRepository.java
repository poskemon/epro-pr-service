package com.poskemon.epro.prservice.repository;

import com.poskemon.epro.prservice.domain.dto.NeedByDateSearch;
import com.poskemon.epro.prservice.domain.dto.PrRetrieveReq;
import com.poskemon.epro.prservice.domain.dto.PrUpdateDTO;
import com.poskemon.epro.prservice.domain.dto.PurchaseUnitReq;
import com.poskemon.epro.prservice.domain.entity.PrHeader;
import com.poskemon.epro.prservice.domain.entity.PrLine;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PrLineRepository extends JpaRepository<PrLine, Long>, PrLineRepositoryCustom {
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

    @Query(value = "select pl, ph, i " +
        "from PrLine pl " +
        "join pl.prHeader ph " +
        "join pl.item i " +
        "where (:#{#prRetrieveReq.prNo} is null or ph.prNo = :#{#prRetrieveReq.prNo}) " +
        "and (:#{#prRetrieveReq.prTitle} is null or ph.prTitle = :#{#prRetrieveReq.prTitle}) " +
        "and (:#{#prRetrieveReq.itemNo} = -1L or i.itemNo = :#{#prRetrieveReq.itemNo}) " +
        "and (:#{#prRetrieveReq.requesterNo} = -1L or ph.requesterNo = :#{#prRetrieveReq.requesterNo}) " +
        "and (:#{#prRetrieveReq.buyerNo} = -1L or pl.buyerNo = :#{#prRetrieveReq.buyerNo}) " +
        "and (:#{#prRetrieveReq.prCreationDate} is null or ph.prCreationDate = :#{#prRetrieveReq.prCreationDate}) " +
        "and (:#{#prRetrieveReq.prStatus} is null or ph.prStatus = :#{#prRetrieveReq.prStatus}) " +
        "order by ph.prCreationDate desc")
    List<PrLine> findAllPr(@Param("prRetrieveReq") PrRetrieveReq prRetrieveReq);

    @Query(value = "select pl, ph " +
        "from PrLine pl " +
        "join pl.prHeader ph " +
        "where (:#{#prUpdateDTO.prNo} is null or ph.prNo = :#{#prUpdateDTO.prNo}) " +
        "and pl.prLine in (:#{#prUpdateDTO.prLines})")
    List<PrLine> findAllByPrNoAndPrLine(@Param("PrUpdateDTO") PrUpdateDTO prUpdateDTO);

    @Query(nativeQuery = true,
           value = "select * from (select max(pl.need_by_date) as needByDate, pl.rfq_no as rfqNo from pr_line pl group by pl.rfq_no) as A where A.rfqNo in (:rfqNos)")
    List<NeedByDateSearch> findAllByRfqNos(@Param("rfqNos") List<Long> rfqNos);

    @Query(value = "select distinct p.item.itemNo from PrLine p where p.rfqNo in (:rfqNos)")
    List<Long> findItemNoByRfqNo(List<Long> rfqNos);

    List<PrLine> findPrLinesByRfqNo(Long rfqNo);
}
