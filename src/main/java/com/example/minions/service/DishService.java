package com.example.minions.service;

import com.example.minions.dto.request.DishDTORequest;
import com.example.minions.dto.response.DishDTOResponse;
import com.example.minions.entity.Dish;

import java.util.List;

public interface DishService {
    Dish findByDishName(String dishName);

    Dish findById(Long id);

    List<Dish> findAll();

    void deleteDish(Long id);

    DishDTOResponse updateDish(DishDTORequest dishDTORequest, Long id);

    DishDTOResponse createDish(DishDTORequest dishDTORequest);

    DishDTOResponse uploadImage(String imagePath, String dishName);
}
