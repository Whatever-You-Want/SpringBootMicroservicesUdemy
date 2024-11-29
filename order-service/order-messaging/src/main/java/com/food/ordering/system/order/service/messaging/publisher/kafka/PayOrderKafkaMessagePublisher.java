package com.food.ordering.system.order.service.messaging.publisher.kafka;

import com.food.ordering.system.kafka.order.avro.model.RestaurantApprovalRequestAvroModel;
import com.food.ordering.system.kafka.order.avro.model.RestaurantApprovalResponseAvroModel;
import com.food.ordering.system.kafka.producer.service.KafkaProducer;
import com.food.ordering.system.order.service.domain.config.OrderServiceConfigData;
import com.food.ordering.system.order.service.domain.event.OrderPaidEvent;
import com.food.ordering.system.order.service.domain.ports.output.message.publisher.restaurentapproval.OrderPaidRestaurantRequestMessagePublisher;
import com.food.ordering.system.order.service.messaging.mapper.OrderMessagingDataMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.food.ordering.system.order.service.messaging.publisher.kafka.OrderKafkaMessageHelper.getKafkaCallback;

@Slf4j
@Component
public class PayOrderKafkaMessagePublisher implements OrderPaidRestaurantRequestMessagePublisher {
    private OrderMessagingDataMapper orderMessagingDataMapper;
    private OrderServiceConfigData orderServiceConfigData;
    private KafkaProducer<String, RestaurantApprovalRequestAvroModel> kafkaProducer;

    public PayOrderKafkaMessagePublisher(OrderMessagingDataMapper orderMessagingDataMapper,
                                            OrderServiceConfigData orderServiceConfigData,
                                            KafkaProducer<String, RestaurantApprovalRequestAvroModel> kafkaProducer) {

        this.orderMessagingDataMapper = orderMessagingDataMapper;
        this.orderServiceConfigData = orderServiceConfigData;
        this.kafkaProducer = kafkaProducer;
    }

    @Override
    public void publish(OrderPaidEvent domainEvent) {
        String orderId = domainEvent.getOrder().getId().getValue().toString();
        log.info("Received OrderPaidEvent for order id {}", orderId);

        try {
            RestaurantApprovalRequestAvroModel restaurantApprovalRequestAvroModel = orderMessagingDataMapper
                    .orderPaidEventToRestaurantApprovalRequest(domainEvent);
            kafkaProducer.send(orderServiceConfigData.getPaymentRequestTopicName(),
                    orderId,
                    restaurantApprovalRequestAvroModel,
                    getKafkaCallback(orderServiceConfigData.getRestaurantApprovalRequestTopicName(),
                            restaurantApprovalRequestAvroModel,
                            orderId,
                            "RestaurantApprovalRequestAvroModel"));

            log.info("RestaurantApprovalRequestAvroModel sent to Kafka for order id {}", orderId);
        } catch (Exception e) {
            log.error("Error while sending RestaurantApprovalRequestAvroModel to Kafka for order id {} with error {}", orderId, e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
