package com.example.crawling.repository.impl;

import com.example.crawling.model.QStockHistory;
import com.example.crawling.model.StockHistory;
import com.example.crawling.repository.StockHistoryRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class StockHistoryRepositoryImpl implements StockHistoryRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<StockHistory> findHistoryByCode(String code) {
        QStockHistory h = QStockHistory.stockHistory;
        return queryFactory
                .selectFrom(h)
                .where(h.code.eq(code))
                .orderBy(h.tradeDate.asc())
                .fetch();
    }

    @Override
    public Optional<StockHistory> findLatestByCode(String code) {
        QStockHistory h = QStockHistory.stockHistory;
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(h)
                        .where(h.code.eq(code))
                        .orderBy(h.tradeDate.desc())
                        .fetchFirst()
        );
    }

    @Override
    public boolean existsByCodeAndDate(String code, LocalDate date) {
        QStockHistory h = QStockHistory.stockHistory;
        return queryFactory
                .selectOne()
                .from(h)
                .where(h.code.eq(code).and(h.tradeDate.eq(date)))
                .fetchFirst() != null;
    }

}
