package br.com.wallet.wallet.domain.model;

import br.com.wallet.wallet.domain.events.*;
import br.com.wallet.wallet.infrastructure.entities.EventEntity;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class Wallet {
  private UUID id;
  private BigDecimal balance = BigDecimal.ZERO;

  private final List<WalletEvent> uncommittedChanges = new ArrayList<WalletEvent>();

  public void LoadFromHistory(List<WalletEvent> history){
      history.forEach(this::apply);
  }

    public void deposit(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor do depósito deve ser positivo");
        }

        var event = new MoneyDeposited(this.id, amount, Instant.now());

        applyAndStage(event);
    }

    public void create(UUID ownerId){
        applyAndStage(new WalletCreated(UUID.randomUUID(), ownerId, Instant.now()));
    }

    private void apply(WalletEvent event) {
        if (event instanceof WalletCreated e) {
            this.id = e.aggregateId();
        } else if (event instanceof MoneyDeposited e) {
            this.balance = this.balance.add(e.amount());
        } else if (event instanceof MoneyWithdrawn e) {
            this.balance = this.balance.subtract(e.amount());
        } else if (event instanceof DividendsReceived e) {
            this.balance = this.balance.add(e.amount());
        }
    }
  private void applyAndStage(WalletEvent event) {
    this.apply(event);
    this.uncommittedChanges.add(event);
  }

    public void markChangesAsCommitted() {
        this.uncommittedChanges.clear();
    }
}
