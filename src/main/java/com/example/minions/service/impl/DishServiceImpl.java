package com.example.minions.service.impl;

import com.example.minions.dto.request.DishDTORequest;
import com.example.minions.dto.response.DishDTOResponse;
import com.example.minions.entity.Dish;
import com.example.minions.mapper.DishMapper;
import com.example.minions.repository.DishRepository;
import com.example.minions.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DishServiceImpl implements DishService {

    @Autowired
    DishRepository dishRepository;
    @Autowired
    DishMapper dishMapper;

    @Override
    public Dish findByDishName(String dishName) {
        return dishRepository.findByDishName(dishName);
    }

    @Override
    public Dish findById(Long id) {
        Dish dish = dishRepository.findByDishId(id);
        return dish;
    }

    @Override
    public List<Dish> findAll() {
        return List.of();
    }

    @Override
    public void deleteDish(Long id) {
        Dish dish = dishRepository.findByDishId(id);
        dishRepository.delete(dish);
    }
    @Override
    public DishDTOResponse updateDish(DishDTORequest dishDTORequest, Long id) {
        Dish dish = dishMapper.toDish(dishDTORequest);

        dish.setDishId(id);
        dish.setImage(dishRepository.findByDishId(id).getImage());
        dishRepository.save(dish);
        return dishMapper.toDishDTOResponse(dish);
    }

    @Override
    public DishDTOResponse createDish(DishDTORequest dishDTORequest) {
        Dish dish = dishMapper.toDish(dishDTORequest);
        dishRepository.save(dish);
        return dishMapper.toDishDTOResponse(dish);
    }
    @Override
    public DishDTOResponse uploadImage(String imagePath, String dishName) {
        Dish dish = dishRepository.findByDishName(dishName);
        if (dish != null) {
            dish.setImage(imagePath);
            dishRepository.save(dish);
            return dishMapper.toDishDTOResponse(dish);
        }
        return null;
    }
}
