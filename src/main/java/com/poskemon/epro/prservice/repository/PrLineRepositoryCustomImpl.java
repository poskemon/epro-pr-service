package com.poskemon.epro.prservice.repository;

import com.poskemon.epro.prservice.domain.dto.PrHeaderInfo;
import com.poskemon.epro.prservice.domain.entity.PrLine;
import com.poskemon.epro.prservice.domain.entity.QPrHeader;
import com.poskemon.epro.prservice.domain.entity.QPrLine;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import java.util.List;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class PrLineRepositoryCustomImpl extends QuerydslRepositorySupport implements PrLineRepositoryCustom {

    public PrLineRepositoryCustomImpl() {
        super(PrLine.class);
    }

    QPrHeader prHeader = QPrHeader.prHeader;
    QPrLine prLine = QPrLine.prLine1;

    @Override
    public List<PrHeaderInfo> findPrInfo(Long prHeaderSeq) {
        return from(prHeader)
            .innerJoin(prLine).on(prHeader.prHeaderSeq.eq(prLine.prHeader.prHeaderSeq))
            .where(prHeader.prHeaderSeq.eq(prHeaderSeq))
            .orderBy(prLine.needByDate.desc())
            .select(Projections.constructor(PrHeaderInfo.class,
                                            prHeader.requesterNo.as("requesterNo"),
                                            prHeader.prNo,
                                            prHeader.prHeaderSeq,
                                            // MEMO (Querydsl 에서 포맷팅): Expressions.dateTemplate(String.class, "TO_CHAR(pr_creation_date, 'YYYY-MM-DD')").as("prCreateDate"),
                                            prHeader.prCreationDate.as("prCreationDateDb"),
                                            prLine.rfqNo,
                                            prLine.prLinePrice.as("price"),
                                            prLine.needByDate)
            ).fetch();
    }
}
