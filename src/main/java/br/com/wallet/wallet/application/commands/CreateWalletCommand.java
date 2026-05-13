package br.com.wallet.wallet.application.commands;

import java.util.UUID;

public record CreateWalletCommand(UUID ownerId) implements WalletCommand {}