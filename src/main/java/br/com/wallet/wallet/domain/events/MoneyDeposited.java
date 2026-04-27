package br.com.wallet.wallet.domain.events;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record MoneyDeposited(
        UUID aggregateId,
        BigDecimal amount,
        Instant occurredAt
) implements WalletEvent {}
