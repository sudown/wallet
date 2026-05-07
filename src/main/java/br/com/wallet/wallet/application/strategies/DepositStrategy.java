package br.com.wallet.wallet.application.strategies;

import br.com.wallet.wallet.application.abstractions.WalletProjectionStrategy;
import br.com.wallet.wallet.domain.events.MoneyDeposited;
import br.com.wallet.wallet.domain.events.WalletEvent;
import br.com.wallet.wallet.infrastructure.entities.WalletSummaryEntity;
import org.springframework.stereotype.Component;

@Component
public class DepositStrategy implements WalletProjectionStrategy<MoneyDeposited> {
    @Override
    public void apply(MoneyDeposited event, WalletSummaryEntity summary) {
        summary.deposit(event.amount());
    }

    @Override
    public boolean canHandle(WalletEvent event) {
        return event instanceof MoneyDeposited;
    }
}
