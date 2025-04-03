package com.nimbleways.springboilerplate.common.infra.database.jparepositories;

import com.nimbleways.springboilerplate.common.infra.database.entities.PurchaseDbEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaPurchaseRepository extends JpaRepository<PurchaseDbEntity, UUID> {
    Optional<PurchaseDbEntity> findByuserId(UUID userId);
    Iterable<PurchaseDbEntity> findByUserIdNot(UUID id);
}
