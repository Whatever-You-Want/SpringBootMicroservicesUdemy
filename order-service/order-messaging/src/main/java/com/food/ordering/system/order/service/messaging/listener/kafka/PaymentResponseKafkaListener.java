package com.food.ordering.system.order.service.messaging.listener.kafka;

import com.food.ordering.system.kafka.consumer.KafkaConsumer;
import com.food.ordering.system.kafka.order.avro.model.PaymentResponseAvroModel;
import com.food.ordering.system.kafka.order.avro.model.PaymentStatus;
import com.food.ordering.system.order.service.domain.ports.input.message.listener.payment.PaymentResponsePaymentListener;
import com.food.ordering.system.order.service.messaging.mapper.OrderMessagingDataMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class PaymentResponseKafkaListener implements KafkaConsumer<PaymentResponseAvroModel> {

    private final PaymentResponsePaymentListener paymentResponsePaymentListener;
    private final OrderMessagingDataMapper orderMessagingDataMapper;

    public PaymentResponseKafkaListener(PaymentResponsePaymentListener paymentResponsePaymentListener, OrderMessagingDataMapper orderMessagingDataMapper) {
        this.paymentResponsePaymentListener = paymentResponsePaymentListener;
        this.orderMessagingDataMapper = orderMessagingDataMapper;
    }

    @Override
    @KafkaListener(id = "${kafka-consumer-config.payment-consumer-group-id}", topics = "${order-service.payment-response-topic-name}")
    public void recieve(@Payload List<PaymentResponseAvroModel> messages,
                        @Header(KafkaHeaders.RECEIVED_KEY) List<String> keys,
                        @Header(KafkaHeaders.PARTITION) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offset) {
        log.info("{} number of payment responses received with keys: {}, partitions: {} and offset: {}  ",
                messages.size(),
                keys.toString(),
                partitions.toString(),
                offset.toString());

        messages.stream().forEach(paymentResponseAvroModel -> {
            if (PaymentStatus.COMPLETED == paymentResponseAvroModel.getPaymentStatus()) {
                log.info("Processing successful payment for order id: {}", paymentResponseAvroModel.getOrderId());
                paymentResponsePaymentListener.PaymentCompleted(
                        orderMessagingDataMapper.paymentResponseAvroModelToPaymentResponse(paymentResponseAvroModel));
            } else if (PaymentStatus.FAILED == paymentResponseAvroModel.getPaymentStatus() ||
                    PaymentStatus.CANCELLED == paymentResponseAvroModel.getPaymentStatus()) {
                log.info("Processing unsuccessful payment for order id: {}", paymentResponseAvroModel.getOrderId());
                paymentResponsePaymentListener.PaymentCanceled(
                        orderMessagingDataMapper.paymentResponseAvroModelToPaymentResponse(paymentResponseAvroModel));


            }
        });


    }
}
