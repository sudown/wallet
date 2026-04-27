package br.com.wallet.wallet.domain.events;

import java.time.Instant;
import java.util.UUID;

public sealed interface WalletEvent  permits WalletCreated, MoneyDeposited, MoneyWithdrawn, DividendsReceived {
    UUID aggregateId();
    Instant occurredAt();
}
