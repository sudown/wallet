package br.com.wallet.wallet.infrastructure.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "wallet_summary")
@Getter
@Setter
@NoArgsConstructor
public class WalletSummaryEntity {
    @Id
    private UUID id;
    private BigDecimal balance;
    private Instant lastUpdate;

    public WalletSummaryEntity(UUID id) {
        this.id = id;
        this.balance = BigDecimal.ZERO;
        this.lastUpdate = Instant.now();
    }
}
