package com.poskemon.epro.prservice.repository;

import com.poskemon.epro.prservice.domain.entity.PrHeader;

import javax.transaction.Transactional;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface PrHeaderRepository extends JpaRepository<PrHeader, Long> {
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "update pr_header set pr_status = :prStatus where pr_header_seq = :prHeaderSeq")
    void changeStatus(@Param("prStatus") String prStatus, @Param("prHeaderSeq") Long prHeaderSeq);

    PrHeader findByPrHeaderSeq(Long prHeaderSeq);
    PrHeader findByPrNo(String prNo);
}
