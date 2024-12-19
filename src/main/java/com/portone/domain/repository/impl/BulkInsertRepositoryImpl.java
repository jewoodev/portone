package com.portone.domain.repository.impl;

import com.portone.domain.entity.OrderedProduct;
import com.portone.domain.repository.BulkInsertRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class BulkInsertRepositoryImpl implements BulkInsertRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void bulkCreate(List entities) {
        int batchSize = 50;
        for (int i = 0; i < entities.size(); i++) {
            entityManager.persist(entities.get(i));
            if (i > 0 && i % batchSize == 0) {
                entityManager.flush();
                entityManager.clear();
            }
        }
        entityManager.flush();
        entityManager.clear();
    }
}
