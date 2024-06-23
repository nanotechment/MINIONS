package com.example.minions.dto.request;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class DishDTORequest {

    Long dishId;

    @NotBlank(message = "Cannot be blank")
    String dishName;

    @NotBlank(message = "Cannot be blank")
    String description;

    @PositiveOrZero(message = "At least 0 or more")
    Double price;
}
