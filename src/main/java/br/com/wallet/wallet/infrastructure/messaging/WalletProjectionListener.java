package br.com.wallet.wallet.infrastructure.messaging;

import br.com.wallet.wallet.application.abstractions.WalletProjectionStrategy;
import br.com.wallet.wallet.domain.events.DividendsReceived;
import br.com.wallet.wallet.domain.events.MoneyDeposited;
import br.com.wallet.wallet.domain.events.MoneyWithdrawn;
import br.com.wallet.wallet.domain.events.WalletEvent;
import br.com.wallet.wallet.infrastructure.entities.WalletSummaryEntity;
import br.com.wallet.wallet.infrastructure.persistence.WalletSummaryRepository;
import io.awspring.cloud.sqs.annotation.SqsListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
public class WalletProjectionListener {
    private final List<WalletProjectionStrategy<? extends WalletEvent>> strategies;
    private final WalletSummaryRepository summaryRepository;

    public WalletProjectionListener(List<WalletProjectionStrategy<? extends WalletEvent>> strategies, WalletSummaryRepository summaryRepository) {
        this.strategies = strategies;
        this.summaryRepository = summaryRepository;
    }


    @SqsListener("${events.sqs.queue-url}")
    public void handle(@Payload WalletEvent event) {
        var summary = summaryRepository.findById(event.aggregateId())
                .orElseGet(() -> new WalletSummaryEntity(event.aggregateId()));

        strategies.stream()
                .filter(s -> s.canHandle(event))
                .findFirst()
                .ifPresent(s -> ((WalletProjectionStrategy<WalletEvent>) s).apply(event, summary));

        summaryRepository.save(summary);
    }
}