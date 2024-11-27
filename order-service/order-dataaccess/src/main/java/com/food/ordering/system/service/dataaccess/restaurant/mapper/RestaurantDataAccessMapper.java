package com.food.ordering.system.service.dataaccess.restaurant.mapper;

import com.food.ordering.system.domain.valueObject.Money;
import com.food.ordering.system.domain.valueObject.ProductId;
import com.food.ordering.system.domain.valueObject.RestaurantId;
import com.food.ordering.system.order.service.domain.entity.Product;
import com.food.ordering.system.order.service.domain.entity.Restaurant;
import com.food.ordering.system.service.dataaccess.restaurant.entity.RestaurantEntity;
import com.food.ordering.system.service.dataaccess.restaurant.exception.RestaurantDataAccessException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class RestaurantDataAccessMapper {

    public List<UUID> restaurantToRestaurantProducts(Restaurant restaurant) {
        return restaurant.getProducts().stream().map(product -> product.getId().getValue()).toList();
    }

    public Restaurant restaurantEntityToRestaurant(List<RestaurantEntity> restaurantEntities) {
        RestaurantEntity restaurantEntity = restaurantEntities.stream().findFirst().stream().findFirst().orElseThrow(
                () -> new RestaurantDataAccessException("Restaurant can not be found!"));

        List<Product> products = restaurantEntities.stream().map(entity -> new Product(new ProductId(entity.getProductId()),
                entity.getProductName(),
                new Money(entity.getProductPrice())))
                .toList();

        List<Restaurant> restaurants = restaurantEntities.stream().map(entity -> Restaurant.Builder.builder()
                .restaurantId(new RestaurantId(entity.getRestaurantId()))
                .active(entity.getRestaurantActive())
                .build()).toList();

        return Restaurant.Builder.builder()
                .restaurantId(new RestaurantId(restaurantEntity.getRestaurantId()))
                .products(products)
                .active(restaurantEntity.getRestaurantActive())
                .build();
    }

}
