package com.poskemon.epro.prservice.repository;

import com.poskemon.epro.prservice.domain.entity.PrHeader;
import com.poskemon.epro.prservice.domain.entity.PrLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrLineRepository extends JpaRepository<PrLine, Long> {
    List<PrLine> findAllByPrHeader(PrHeader prHeader);
}
