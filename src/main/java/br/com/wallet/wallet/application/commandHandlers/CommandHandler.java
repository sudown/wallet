package br.com.wallet.wallet.application.commandHandlers;

import br.com.wallet.wallet.application.commands.WalletCommand;

public interface CommandHandler<Command extends WalletCommand, Result> {
    Result handle(Command command);
    boolean canHandle(WalletCommand command);
}