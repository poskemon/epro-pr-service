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
    @Query(nativeQuery = true, value = "update pr_header set pr_status = :prStatus where pr_no = :prNo")
    Integer changeStatus(@Param("prStatus") String prStatus, @Param("prNo") String prNo);

    PrHeader findByPrHeaderSeq(Long prHeaderSeq);
}
