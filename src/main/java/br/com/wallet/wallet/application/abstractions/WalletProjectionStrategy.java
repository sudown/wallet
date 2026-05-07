package br.com.wallet.wallet.application.abstractions;

import br.com.wallet.wallet.domain.events.WalletEvent;
import br.com.wallet.wallet.infrastructure.entities.WalletSummaryEntity;

public interface WalletProjectionStrategy<T extends WalletEvent> {
    void apply(T event, WalletSummaryEntity summary);
    boolean canHandle(WalletEvent event);
}
