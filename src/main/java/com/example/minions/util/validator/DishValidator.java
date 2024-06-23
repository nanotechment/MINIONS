package com.example.minions.util.validator;

import com.example.minions.dto.request.DishDTORequest;
import com.example.minions.entity.Dish;
import com.example.minions.mapper.DishMapper;
import com.example.minions.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class DishValidator implements Validator {
    @Autowired
    DishMapper dishMapper;
    @Autowired
    DishService dishService;

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object target, Errors errors) {
        DishDTORequest dishDTORequest = (DishDTORequest) target;
        Dish dish = dishMapper.toDish(dishDTORequest);
        if(dishService.findByDishName(dish.getDishName()) != null){
            errors.rejectValue("dishName","error.dishName","Dish name already exist");
        }
        if(dish.getPrice() < 0){
            errors.rejectValue("price","error.price","Price must be bigger than 0");
        }
    }
}
