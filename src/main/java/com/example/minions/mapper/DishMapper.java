package com.example.minions.mapper;

import com.example.minions.dto.request.DishDTORequest;
import com.example.minions.dto.response.DishDTOResponse;
import com.example.minions.entity.Dish;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public interface DishMapper {
    List<DishDTOResponse> toDishDTO(List<Dish> dishes);

    Dish toDish(DishDTORequest dishDTORequest);

    DishDTOResponse toDishDTOResponse(Dish dish);
}
