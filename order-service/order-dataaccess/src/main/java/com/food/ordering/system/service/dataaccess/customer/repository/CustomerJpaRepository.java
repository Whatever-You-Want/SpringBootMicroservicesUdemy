package com.food.ordering.system.service.dataaccess.customer.repository;

import com.food.ordering.system.order.service.domain.entity.Customer;
import com.food.ordering.system.service.dataaccess.customer.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerJpaRepository extends JpaRepository<CustomerEntity, UUID> {
//    public Optional<CustomerEntity> findCustomerById(UUID id);
}
