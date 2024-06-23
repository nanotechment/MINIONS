package com.example.minions.repository;

import com.example.minions.entity.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DishRepository extends JpaRepository<Dish, String>{
    Dish findByDishName(String dishName);

    Dish findByDishId(Long dishId);
}
