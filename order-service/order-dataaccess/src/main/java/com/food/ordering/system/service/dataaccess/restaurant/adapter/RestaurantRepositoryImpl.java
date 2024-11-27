package com.food.ordering.system.service.dataaccess.restaurant.adapter;

import com.food.ordering.system.order.service.domain.entity.Restaurant;
import com.food.ordering.system.order.service.domain.ports.output.repository.RestaurantRepository;
import com.food.ordering.system.service.dataaccess.restaurant.entity.RestaurantEntity;
import com.food.ordering.system.service.dataaccess.restaurant.mapper.RestaurantDataAccessMapper;
import com.food.ordering.system.service.dataaccess.restaurant.repository.RestaurantJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class RestaurantRepositoryImpl implements RestaurantRepository {

    private RestaurantJpaRepository restaurantJpaRepository;
    private RestaurantDataAccessMapper restaurantDataAccessMapper;

    @Override
    public Optional<Restaurant> findRestaurantInformation(Restaurant restaurant) {

        List<UUID> products = restaurantDataAccessMapper.restaurantToRestaurantProducts(restaurant);

        Optional<List<RestaurantEntity>> restaurantEntries = restaurantJpaRepository.findByRestaurantIdAndProductIdIn(
                restaurant.getId().getValue(), products);

        return restaurantEntries.map(restaurantDataAccessMapper::restaurantEntityToRestaurant);
    }
}
