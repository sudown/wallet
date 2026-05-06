package br.com.wallet.wallet.domain.events;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.time.Instant;
import java.util.UUID;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type" // O Jackson vai ler/escrever esse campo no JSON
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = WalletCreated.class, name = "WalletCreated"),
        @JsonSubTypes.Type(value = MoneyDeposited.class, name = "MoneyDeposited"),
        @JsonSubTypes.Type(value = MoneyWithdrawn.class, name = "MoneyWithdrawn"),
        @JsonSubTypes.Type(value = DividendsReceived.class, name = "DividendsReceived")
})
public sealed interface WalletEvent  permits WalletCreated, MoneyDeposited, MoneyWithdrawn, DividendsReceived {
    UUID aggregateId();
    Instant occurredAt();
}
