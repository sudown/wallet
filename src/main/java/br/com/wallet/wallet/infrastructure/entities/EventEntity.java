package br.com.wallet.wallet.infrastructure.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "event_store")
@Getter
@Setter
public class EventEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID aggregateId;
    private String eventType;

    @Column(columnDefinition = "TEXT")
    private String payload;

    private Instant occurredAt;
}