package br.com.wallet.wallet.domain.gateways;

import br.com.wallet.wallet.domain.events.WalletEvent;

import java.util.List;
import java.util.UUID;

public interface EventStoreRepository {
    void save(WalletEvent event);
    List<WalletEvent> findAllByAggregatedId(UUID aggregatedId);
}
