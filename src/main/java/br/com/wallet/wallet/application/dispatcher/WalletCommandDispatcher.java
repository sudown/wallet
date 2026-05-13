package br.com.wallet.wallet.application.dispatcher;

import br.com.wallet.wallet.application.commandHandlers.CommandHandler;
import br.com.wallet.wallet.application.commands.WalletCommand;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WalletCommandDispatcher {
    private final List<CommandHandler<? extends WalletCommand, ?>> handlers;

    public WalletCommandDispatcher(List<CommandHandler<? extends WalletCommand, ?>> handlers) {
        this.handlers = handlers;
    }

    @SuppressWarnings("unchecked")
    public <R> R execute(WalletCommand command) {
        // 1. Primeiro, encontramos o handler
        var handler = handlers.stream()
                .filter(h -> h.canHandle(command))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Nenhum handler para " + command.getClass()));

        // 2. Depois de encontrado, executamos.
        // Fazemos o cast para a interface genérica para chamar o handle.
        return ((CommandHandler<WalletCommand, R>) handler).handle(command);
    }
}