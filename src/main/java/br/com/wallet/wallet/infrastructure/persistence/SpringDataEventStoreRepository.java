package br.com.wallet.wallet.infrastructure.persistence;

import br.com.wallet.wallet.infrastructure.entities.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface SpringDataEventStoreRepository extends JpaRepository<EventEntity, Long> {
    List<EventEntity> findAllByAggregateIdOrderByIdAsc(UUID aggregateId);
}