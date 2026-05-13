package br.com.wallet.wallet.infrastructure.messaging;

import br.com.wallet.wallet.domain.events.WalletEvent;
import io.awspring.cloud.sns.core.SnsNotification;
import io.awspring.cloud.sns.core.SnsTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SnsEventPublisher {
    private final SnsTemplate snsTemplate;
    private final String topicArn;

    public SnsEventPublisher(SnsTemplate snsTemplate,
                             @Value("${events.sns.topic-arn}") String topicArn) {
        this.snsTemplate = snsTemplate;
        this.topicArn = topicArn;
    }

    public void publish(WalletEvent event) {
        SnsNotification<WalletEvent> notification = SnsNotification.builder(event)
                .groupId(event.aggregateId().toString())
                .deduplicationId(UUID.randomUUID().toString())
                .build();

        snsTemplate.sendNotification(topicArn, notification);
    }
}
