package br.com.wallet.wallet.infrastructure.repositories;

import br.com.wallet.wallet.domain.events.WalletEvent;
import br.com.wallet.wallet.domain.gateways.EventStoreRepository;
import br.com.wallet.wallet.infrastructure.entities.EventEntity;
import br.com.wallet.wallet.infrastructure.persistence.SpringDataEventStoreRepository;
import org.springframework.stereotype.Repository;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.UUID;

@Repository
public class JpaEventStoreRepository implements EventStoreRepository {

    private final SpringDataEventStoreRepository jpaRepository; // Injeção da interface que criamos agora
    private final ObjectMapper objectMapper;

    public JpaEventStoreRepository(SpringDataEventStoreRepository jpaRepository, ObjectMapper objectMapper) {
        this.jpaRepository = jpaRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public void save(WalletEvent event) {
        try {
            EventEntity entity = new EventEntity();
            entity.setAggregateId(event.aggregateId());
            entity.setOccurredAt(event.occurredAt());
            entity.setEventType(event.getClass().getName()); // Usar o nome completo ajuda na reconstrução

            String jsonPayload = objectMapper.writeValueAsString(event);
            entity.setPayload(jsonPayload);

            jpaRepository.save(entity);
        } catch (JacksonException ex) {
            throw new RuntimeException("Falha ao serializar evento", ex);
        }
    }

    @Override
    public List<WalletEvent> findAllByAggregatedId(UUID aggregatedId) {
        return jpaRepository.findAllByAggregateIdOrderByIdAsc(aggregatedId)
                .stream()
                .map(this::deserialize)
                .toList();
    }


    private WalletEvent deserialize(EventEntity entity) {
        try {
            Class<?> clazz = Class.forName(entity.getEventType());
            return (WalletEvent) objectMapper.readValue(entity.getPayload(), clazz);
        } catch (Exception e) {
            throw new RuntimeException("Falha ao reconstruir evento", e);
        }
    }
}