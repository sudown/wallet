package br.com.wallet.wallet.infrastructure.messaging;

import br.com.wallet.wallet.domain.events.WalletEvent;
import io.awspring.cloud.sns.core.SnsTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SnsEventPublisher {
    private final SnsTemplate snsTemplate;
    private final String topicName;

    public SnsEventPublisher(SnsTemplate snsTemplate,
                             @Value("${events.sns.topic-name}") String topicName) {
        this.snsTemplate = snsTemplate;
        this.topicName = topicName;
    }

    public void publish(WalletEvent event) {
        snsTemplate.sendNotification(topicName, event, event.getClass().getSimpleName());
    }
}
