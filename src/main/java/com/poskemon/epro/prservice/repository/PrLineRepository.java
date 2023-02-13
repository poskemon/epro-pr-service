package com.poskemon.epro.prservice.repository;

import com.poskemon.epro.prservice.domain.entity.PrLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrLineRepository extends JpaRepository<PrLine, Long> {
}
