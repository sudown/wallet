package br.com.wallet.wallet.domain.events;

import java.time.Instant;
import java.util.UUID;

public record WalletCreated(
        UUID aggregateId,
        UUID ownerId,
        Instant occurredAt
) implements WalletEvent {}