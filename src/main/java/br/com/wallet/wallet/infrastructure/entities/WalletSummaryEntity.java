package br.com.wallet.wallet.infrastructure.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "wallet_summary")
@Getter
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

    public void withdraw(BigDecimal amount){
        if(amount.compareTo(this.balance) > 0){
            throw new IllegalArgumentException("You cannot withdraw more than your balance.");
        }
        this.balance = amount;
        this.lastUpdate = Instant.now();
    }

    public void deposit(BigDecimal amount){
        if(amount.compareTo(BigDecimal.ZERO) < 0){
            throw new IllegalArgumentException("You cannot deposit negative values.");
        }
        this.balance = amount;
        this.lastUpdate = Instant.now();
    }
}
