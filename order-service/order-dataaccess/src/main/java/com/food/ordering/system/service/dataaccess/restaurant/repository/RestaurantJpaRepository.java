package com.food.ordering.system.service.dataaccess.restaurant.repository;

import com.food.ordering.system.service.dataaccess.restaurant.entity.RestaurantEntityId;
import com.food.ordering.system.service.dataaccess.restaurant.entity.RestaurantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RestaurantJpaRepository extends JpaRepository<RestaurantEntity, RestaurantEntityId> {

    public Optional<List<RestaurantEntity>> findByRestaurantIdAndProductIdIn (UUID restaurantId, List<UUID> productIds);
}
