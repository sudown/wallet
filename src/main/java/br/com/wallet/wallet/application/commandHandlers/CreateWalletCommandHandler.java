package br.com.wallet.wallet.application.commandHandlers;

import br.com.wallet.wallet.application.commands.CreateWalletCommand;
import br.com.wallet.wallet.application.commands.WalletCommand;
import br.com.wallet.wallet.domain.gateways.EventStoreRepository;
import br.com.wallet.wallet.domain.model.Wallet;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
@Transactional
public class CreateWalletCommandHandler implements CommandHandler<CreateWalletCommand, UUID> {

    private final EventStoreRepository eventStore;
    private final ApplicationEventPublisher eventPublisher;

    public CreateWalletCommandHandler(EventStoreRepository eventStore, ApplicationEventPublisher eventPublisher) {
        this.eventStore = eventStore;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public UUID handle(CreateWalletCommand cmd) {
        // 1. Instancia o agregado
        Wallet wallet = new Wallet();

        // 2. Executa a lógica de criação (isso deve gerar o aggregateId e o evento WalletCreated)
        wallet.create(cmd.ownerId());

        // 3. Persiste os eventos gerados no EventStore
        for (var event : wallet.getUncommittedChanges()) {
            eventStore.save(event);

            // 4. Publica internamente para o TransactionalEventListener disparar o SNS
            eventPublisher.publishEvent(event);
        }

        // 5. Limpa a lista de eventos pendentes do agregado
        wallet.markChangesAsCommitted();

        // Retorna o ID da nova carteira para o Controller
        return wallet.getId();
    }

    @Override
    public boolean canHandle(WalletCommand command) {
        return command instanceof CreateWalletCommand;
    }
}