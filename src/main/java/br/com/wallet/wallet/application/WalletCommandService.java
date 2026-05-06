package br.com.wallet.wallet.application;

import br.com.wallet.wallet.domain.events.WalletEvent;
import br.com.wallet.wallet.domain.gateways.EventStoreRepository;
import br.com.wallet.wallet.domain.model.Wallet;
import br.com.wallet.wallet.infrastructure.messaging.SnsEventPublisher;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@Transactional
public class WalletCommandService {
    private final EventStoreRepository eventStore;
    private final ApplicationEventPublisher applicationEventPublisher;

    public WalletCommandService(EventStoreRepository eventStore, ApplicationEventPublisher applicationEventPublisher) {
        this.eventStore = eventStore;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void handleDeposit(UUID walletId, BigDecimal amount) {
        var history = eventStore.findAllByAggregatedId(walletId);

        Wallet wallet = new Wallet();

        wallet.LoadFromHistory(history);

        wallet.deposit(amount);

        saveAndPublish(wallet);
    }

    public UUID createWallet(UUID ownerId) {
        Wallet wallet = new Wallet();

        wallet.create(ownerId);

        saveAndPublish(wallet);

        return wallet.getId();
    }

    private void saveAndPublish(Wallet wallet) {
        for (WalletEvent event : wallet.getUncommittedChanges()) {
            eventStore.save(event);
            applicationEventPublisher.publishEvent(event);
        }
        wallet.markChangesAsCommitted();
    }
}
