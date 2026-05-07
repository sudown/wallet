package br.com.wallet.wallet.application.strategies;

import br.com.wallet.wallet.application.abstractions.WalletProjectionStrategy;
import br.com.wallet.wallet.domain.events.WalletCreated;
import br.com.wallet.wallet.domain.events.WalletEvent;
import br.com.wallet.wallet.infrastructure.entities.WalletSummaryEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class WalletCreatedStrategy implements WalletProjectionStrategy<WalletCreated> {

    @Override
    public void apply(WalletCreated event, WalletSummaryEntity summary) {
        if (summary.getBalance() == null) {
            summary.deposit(BigDecimal.ZERO);
        }
    }

    @Override
    public boolean canHandle(WalletEvent event) {
        return event instanceof WalletCreated;
    }
}