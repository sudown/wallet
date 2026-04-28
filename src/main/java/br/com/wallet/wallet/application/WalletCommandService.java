package br.com.wallet.wallet.application;

import br.com.wallet.wallet.domain.events.WalletEvent;
import br.com.wallet.wallet.domain.gateways.EventStoreRepository;
import br.com.wallet.wallet.domain.model.Wallet;
import br.com.wallet.wallet.infrastructure.messaging.SnsEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@Transactional
public class WalletCommandService {
    private final EventStoreRepository eventStore;
    private final SnsEventPublisher snsPublisher;

    public WalletCommandService(EventStoreRepository eventStore, SnsEventPublisher snsPublisher) {
        this.eventStore = eventStore;
        this.snsPublisher = snsPublisher;
    }

    public void handleDeposit(UUID walletId, BigDecimal amount) {
        // 1. Carrega o histórico e reconstrói a Wallet
        var history = eventStore.findAllByAggregatedId(walletId);
        Wallet wallet = new Wallet();
        wallet.LoadFromHistory(history);

        // 2. Executa a regra de negócio
        wallet.deposit(amount);

        // 3. Salva os novos eventos gerados (changes) e publica
        for (WalletEvent event : wallet.getUncommittedChanges()) {
            eventStore.save(event);
            snsPublisher.publish(event); // Dispara para o Amazon SNS
        }
    }
}
