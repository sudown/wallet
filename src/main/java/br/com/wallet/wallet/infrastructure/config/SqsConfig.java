package br.com.wallet.wallet.infrastructure.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.config.SqsMessageListenerContainerFactory;
import io.awspring.cloud.sqs.support.converter.SqsMessagingMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

import java.time.Duration;

@Configuration
public class SqsConfig {

    @Bean
    public SqsMessageListenerContainerFactory<Object> defaultSqsListenerContainerFactory(
            SqsAsyncClient sqsAsyncClient,
            ObjectMapper objectMapper) {

        return SqsMessageListenerContainerFactory
                .<Object>builder()
                .configure(options -> options
                        .messageConverter(snsAwareSqsConverter(objectMapper))
                )
                .sqsAsyncClient(sqsAsyncClient)
                .build();
    }

    private SqsMessagingMessageConverter snsAwareSqsConverter(ObjectMapper objectMapper) {
        // Sobrescreve getPayloadToDeserialize para desembrulhar o envelope SNS
        return new SqsMessagingMessageConverter() {
            @Override
            protected Object getPayloadToDeserialize(
                    software.amazon.awssdk.services.sqs.model.Message message) {
                try {
                    JsonNode root = objectMapper.readTree(message.body());
                    // Se tiver campo "Message", é envelope SNS → extrai o conteúdo
                    if (root.has("Message")) {
                        return root.get("Message").asText();
                    }
                    // Mensagem raw direta (RawMessageDelivery=true)
                    return message.body();
                } catch (Exception e) {
                    return message.body();
                }
            }
        };
    }
}