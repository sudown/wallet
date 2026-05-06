package br.com.wallet.wallet.application.dto;

import java.math.BigDecimal;

public record DepositRequest(BigDecimal amount) {}