package br.com.wallet.wallet.application.commands;

import java.math.BigDecimal;
import java.util.UUID;

public record DepositCommand(UUID walletId, BigDecimal amount) implements WalletCommand {}