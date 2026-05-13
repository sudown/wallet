package br.com.wallet.wallet.application.commandHandlers;

import br.com.wallet.wallet.application.commands.DepositCommand;
import br.com.wallet.wallet.application.commands.WalletCommand;
import br.com.wallet.wallet.domain.gateways.EventStoreRepository;
import br.com.wallet.wallet.domain.model.Wallet;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class DepositCommandHandler implements CommandHandler<DepositCommand, Void> {
    private final EventStoreRepository eventStore;
    private final ApplicationEventPublisher eventPublisher;

    public DepositCommandHandler(EventStoreRepository eventStore, ApplicationEventPublisher eventPublisher) {
        this.eventStore = eventStore;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Void handle(DepositCommand cmd) {
        var history = eventStore.findAllByAggregatedId(cmd.walletId());
        Wallet wallet = new Wallet();
        wallet.setId(cmd.walletId());
        wallet.LoadFromHistory(history);

        wallet.deposit(cmd.amount());

        for (var event : wallet.getUncommittedChanges()) {
            eventStore.save(event);
            eventPublisher.publishEvent(event);
        }
        wallet.markChangesAsCommitted();
        return null;
    }

    @Override
    public boolean canHandle(WalletCommand command) {
        return command instanceof DepositCommand;
    }
}