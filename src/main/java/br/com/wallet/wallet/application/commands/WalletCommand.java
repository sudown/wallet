package br.com.wallet.wallet.application.commands;

public sealed interface WalletCommand permits CreateWalletCommand, DepositCommand {}