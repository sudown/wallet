package br.com.wallet.wallet.infrastructure.messaging;

import br.com.wallet.wallet.domain.events.DividendsReceived;
import br.com.wallet.wallet.domain.events.MoneyDeposited;
import br.com.wallet.wallet.domain.events.MoneyWithdrawn;
import br.com.wallet.wallet.domain.events.WalletEvent;
import br.com.wallet.wallet.infrastructure.entities.WalletSummaryEntity;
import br.com.wallet.wallet.infrastructure.persistence.WalletSummaryRepository;
import org.springframework.stereotype.Component;
import io.awspring.cloud.sqs.annotation.SqsListener;

import java.time.Instant;

@Component
public class WalletProjectionListener {

    private final WalletSummaryRepository summaryRepository;

    public WalletProjectionListener(WalletSummaryRepository summaryRepository) {
        this.summaryRepository = summaryRepository;
    }

    @SqsListener("${events.sqs.queue-name}")
    public void handle(WalletEvent event) {
        // Buscamos a projeção (o estado atual mastigado)
        var summary = summaryRepository.findById(event.aggregateId())
                .orElseGet(() -> new WalletSummaryEntity(event.aggregateId()));

        // Atualizamos a projeção baseada no evento que chegou
        if (event instanceof MoneyDeposited e) {
            summary.setBalance(summary.getBalance().add(e.amount()));
        } else if (event instanceof MoneyWithdrawn e) {
            summary.setBalance(summary.getBalance().subtract(e.amount()));
        } else if (event instanceof DividendsReceived e) {
            summary.setBalance(summary.getBalance().add(e.amount()));
        }

        summary.setLastUpdate(Instant.now());
        summaryRepository.save(summary);
    }
}