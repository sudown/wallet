package br.com.wallet.wallet.domain.events;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record DividendsReceived(
        UUID aggregateId,
        BigDecimal amount,
        String ticker,
        Instant occurredAt
) implements WalletEvent {}