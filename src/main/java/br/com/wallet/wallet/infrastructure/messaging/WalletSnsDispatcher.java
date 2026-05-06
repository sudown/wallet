package br.com.wallet.wallet.infrastructure.messaging;

import br.com.wallet.wallet.domain.events.WalletEvent;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class WalletSnsDispatcher {

    private final SnsEventPublisher snsPublisher;

    public WalletSnsDispatcher(SnsEventPublisher snsPublisher) {
        this.snsPublisher = snsPublisher;
    }

    @Async
    @Retryable(
            retryFor = { Exception.class },
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void dispatchToSns(WalletEvent event) {
        snsPublisher.publish(event);
    }

    @Recover
    public void recover(Exception e, WalletEvent event) {
        System.err.println("Falha definitiva ao publicar evento: " + event.aggregateId());
    }
}