package ru.dolya.deal.config;


import lombok.Getter;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import ru.dolya.deal.model.dto.EmailMessage;


import java.util.HashMap;
import java.util.Map;

@Configuration
@Getter
public class KafkaProducerConfig {

    @Value(value = "${spring.kafka.producer.bootstrap-servers}")
    private String bootstrapAddress;

    @Value(value = "${kafka.finish-registration}")
    private String finishRegistrationTopic;

    @Value(value = "${kafka.create-documents}")
    private String createDocumentsTopic;

    @Value(value = "${kafka.send-documents}")
    private String sendDocumentsTopic;

    @Value(value = "${kafka.send-ses}")
    private String sendSesTopic;

    @Value(value = "${kafka.credit-issued}")
    private String creditIssuedTopic;

    @Value(value = "${kafka.application-denied}")
    private String applicationDeniedTopic;

    @Bean
    public ProducerFactory<String, EmailMessage> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                bootstrapAddress);
        configProps.put(
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);
        configProps.put(
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, EmailMessage> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public NewTopic topicFinishRegistration() {
        return new NewTopic(finishRegistrationTopic, 1, (short) 1);
    }

    @Bean
    public NewTopic topicCreateDocuments() {
        return new NewTopic(createDocumentsTopic, 1, (short) 1);
    }

    @Bean
    public NewTopic topicSendDocuments() {
        return new NewTopic(sendDocumentsTopic, 1, (short) 1);
    }

    @Bean
    public NewTopic topicSendSes() {
        return new NewTopic(sendSesTopic, 1, (short) 1);
    }

    @Bean
    public NewTopic topicCreditIssued() {
        return new NewTopic(creditIssuedTopic, 1, (short) 1);
    }

    @Bean
    public NewTopic topicApplicationDenied() {
        return new NewTopic(applicationDeniedTopic, 1, (short) 1);
    }
}
