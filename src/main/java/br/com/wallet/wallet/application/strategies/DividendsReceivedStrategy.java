package br.com.wallet.wallet.application.strategies;

import br.com.wallet.wallet.application.abstractions.WalletProjectionStrategy;
import br.com.wallet.wallet.domain.events.DividendsReceived;
import br.com.wallet.wallet.domain.events.WalletEvent;
import br.com.wallet.wallet.infrastructure.entities.WalletSummaryEntity;
import org.springframework.stereotype.Component;

@Component
public class DividendsReceivedStrategy implements WalletProjectionStrategy<DividendsReceived> {

    @Override
    public void apply(DividendsReceived event, WalletSummaryEntity summary) {
        summary.deposit(event.amount());
    }

    @Override
    public boolean canHandle(WalletEvent event) {
        return event instanceof DividendsReceived;
    }
}