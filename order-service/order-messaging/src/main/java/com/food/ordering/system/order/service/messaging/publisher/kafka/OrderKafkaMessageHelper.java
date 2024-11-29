package com.food.ordering.system.order.service.messaging.publisher.kafka;

import com.food.ordering.system.kafka.order.avro.model.RestaurantApprovalRequestAvroModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.support.SendResult;

import java.util.function.BiConsumer;

@Slf4j
class OrderKafkaMessageHelper {

    static <T> BiConsumer<SendResult<String, T>, ? super Throwable> getKafkaCallback(
            String paymentRequestTopicName,
            T requestAvroModel,
            String orderId,
            String avroModelName) {
        return (result, throwable) -> {
            if (throwable != null) {
                log.error("Error while sending {} message {} to topic {}",
                        avroModelName,
                        requestAvroModel.toString(),
                        paymentRequestTopicName,
                        throwable);
            } else {
                RecordMetadata recordMetadata = result.getRecordMetadata();
                log.info("Received successful response from Kafka for order id: {} Topic: {} " +
                                "Partition: {} Offset: {} Timestamp: {}",
                        orderId,
                        recordMetadata.topic(),
                        recordMetadata.partition(),
                        recordMetadata.offset(),
                        recordMetadata.timestamp());
            }
        };
    }
}
