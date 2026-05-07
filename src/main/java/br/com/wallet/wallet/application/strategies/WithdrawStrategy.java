package br.com.wallet.wallet.application.strategies;

import br.com.wallet.wallet.application.abstractions.WalletProjectionStrategy;
import br.com.wallet.wallet.domain.events.MoneyWithdrawn;
import br.com.wallet.wallet.domain.events.WalletEvent;
import br.com.wallet.wallet.infrastructure.entities.WalletSummaryEntity;
import org.springframework.stereotype.Component;

@Component
public class WithdrawStrategy implements WalletProjectionStrategy<MoneyWithdrawn> {
    @Override
    public void apply(MoneyWithdrawn event, WalletSummaryEntity summary) {
        summary.withdraw(event.amount());
    }

    @Override
    public boolean canHandle(WalletEvent event) {
        return event instanceof MoneyWithdrawn;
    }
}
