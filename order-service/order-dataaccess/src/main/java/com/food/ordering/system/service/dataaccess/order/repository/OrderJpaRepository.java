package com.food.ordering.system.service.dataaccess.order.repository;

import com.food.ordering.system.service.dataaccess.order.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderJpaRepository extends JpaRepository<OrderEntity, UUID> {
    public Optional<OrderEntity> findByTrackingId(UUID trackingId);
}
