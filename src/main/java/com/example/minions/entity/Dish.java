package com.example.minions.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@Entity
@Table(name = "DISH")
@ToString
@NoArgsConstructor
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Dish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DISH_ID")
    Long dishId;
    @Column(name = "DISH_NAME", nullable = false)
    String dishName;
    @Column(name = "DESCRIPTION")
    String description;
    @Column(name = "PRICE")
    Double price;
    @Column(name = "IMAGE")
    String image;
}
