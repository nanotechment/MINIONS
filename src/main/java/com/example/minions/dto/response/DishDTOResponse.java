package com.example.minions.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@FieldDefaults(level =  AccessLevel.PRIVATE)
public class DishDTOResponse {
    Long dishId;

    String dishName;

    String description;

    Double price;
}
