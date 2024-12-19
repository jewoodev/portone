package com.portone.domain.repository;

import java.util.List;

public interface BulkInsertRepository<T> {
    void bulkCreate(List<T> entities);
}
