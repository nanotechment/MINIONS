package com.example.minions.controller;

import com.example.minions.dto.request.DishDTORequest;
import com.example.minions.dto.response.ApiResponse;
import com.example.minions.dto.response.DishDTOResponse;
import com.example.minions.exception.MinionsException;
import com.example.minions.service.DishService;
import com.example.minions.service.ImageService;
import com.example.minions.util.validator.DishValidator;
import com.example.minions.util.validator.ValidatorUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;

import static com.example.minions.util.Constant.*;

@RestController
@CrossOrigin(origins = {"*"})
@RequestMapping(DISH_API)
public class DishController {

    @Autowired
    DishService dishService;
    @Autowired
    ImageService imageService;
    @Autowired
    DishValidator dishValidator;
    @Autowired
    ValidatorUtil validatorUtil;

    @GetMapping("/list")
    ResponseEntity<ApiResponse> getDish() {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.ok(dishService.findAll());
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_EMPLOYEE')")
    ResponseEntity<ApiResponse> createMovie(@RequestBody @Valid DishDTORequest dishRequest, BindingResult bindingResult) {
        ApiResponse apiResponse = new ApiResponse();
        dishValidator.validate(dishRequest, bindingResult);
        if (bindingResult.hasErrors()) {
            HashMap<String, String> hashMap = validatorUtil.toErrors(bindingResult.getFieldErrors());
            apiResponse.error(hashMap);
            return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
        }
        apiResponse.ok(dishService.createDish(dishRequest));
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_EMPLOYEE')")
    ResponseEntity<ApiResponse> updateMovie(@PathVariable Long id, @RequestBody @Valid DishDTORequest dishRequest, BindingResult bindingResult) {
        ApiResponse apiResponse = new ApiResponse();
        if (dishService.findById(id) != null) {
            if (bindingResult.hasErrors()) {
                HashMap<String, String> hashMap = validatorUtil.toErrors(bindingResult.getFieldErrors());
                apiResponse.error(hashMap);
                return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
            }
            apiResponse.ok(dishService.updateDish(dishRequest, id));
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        }
        throw MinionsException.badRequest("Id not found");
    }

    @PostMapping("/upload-image/{name}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_EMPLOYEE')")
    ResponseEntity<ApiResponse> uploadImage(@PathVariable String name,
                                            @RequestParam("image") MultipartFile dImage) throws IOException {
        ApiResponse apiResponse = new ApiResponse();
        if (dishService.findByDishName(name) != null) {
            String image = API_DOMAIN + imageService.saveImage(dImage, DISH_IMAGE);
            DishDTOResponse movieResponse = dishService.uploadImage(image, name);
            apiResponse.ok(movieResponse);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        }
        throw MinionsException.badRequest("Dish not found");
    }
}
