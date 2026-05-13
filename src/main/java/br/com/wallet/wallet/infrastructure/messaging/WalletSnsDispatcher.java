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

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleAfterCommit(WalletEvent event) {
        // Apenas repassa para o método assíncrono
        // Isso garante que a transação principal termine em paz
        this.publishAsync(event);
    }

    @Async
    public void publishAsync(WalletEvent event) {
        try {
            snsPublisher.publish(event);
        } catch (Exception e) {
            // Logamos o erro, mas não deixamos ele subir
            // Se ele subir aqui, ele tenta "melar" uma requisição que já deu certo
            System.err.println("Erro assíncrono ao postar no SNS: " + e.getMessage());
        }
    }
}