package com.telran.shopmongodb.controller;

import com.telran.shopmongodb.controller.dto.*;
import com.telran.shopmongodb.data.entity.OrderStatistic;
import com.telran.shopmongodb.service.AdminService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("admin")
public class AdminController {

    @Autowired
    AdminService service;


    @PostMapping("category")
    @ApiOperation(value = "Add product category, only for admin account")
    public AddUnitResponseDto addCategory(@RequestBody CategoryDto dto) {
        try {
            String id = service.addCategory(dto.getName());
            return AddUnitResponseDto.builder().id(id).build();
        } catch (Throwable throwable) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Category with name [" + dto.getName() + "] already exist");
        }
    }

    @PostMapping("product")
    @ApiOperation(value = "Add product, only for admin account")
    public AddUnitResponseDto addProduct(@RequestBody ProductDto dto) {
        String id = service.addProduct(dto.getName(), dto.getPrice(), dto.getCategory().getId());
        return AddUnitResponseDto.builder().id(id).build();
    }

    @DeleteMapping("product/{id}")
    @ApiOperation(value = "Remove product by Id, only for admin account")
    public RemoveResponseDto removeProductById(@PathVariable("id") String id) {
        service.removeProduct(id);
        return RemoveResponseDto.builder().id(id).build();
    }

//    @DeleteMapping("category/{id}")
//    public RemoveResponseDto removeCategoryById(@PathVariable("id") String id){
//        service.removeCategory(id);
//        return RemoveResponseDto.builder().id(id).build();
//    }

    @PutMapping("category")
    @ApiOperation(value = "Update category, only for admin account")
    public UpdateUnitResponseDto updateCategory(@RequestBody CategoryDto categoryDto) {
        service.updateCategory(categoryDto.getId(), categoryDto.getName());
        return UpdateUnitResponseDto.builder().id(categoryDto.getId()).name(categoryDto.getName()).build();
    }

    @PutMapping("product")
    @ApiOperation(value = "Change product price, only for admin account")
    public PriceResponseDto changeProductPrice(@RequestBody ProductDto productDto) {
        service.changeProductPrice(productDto.getId(), productDto.getPrice());
        return PriceResponseDto.builder().id(productDto.getId()).price(productDto.getPrice()).build();
    }

    @PutMapping("user")
    @ApiOperation(value = "Add user balance, only for admin account")
    public Optional<AddUserBalanceResponseDto> addBalance(@RequestBody UserDto userDto) {
        return service.addBalance(userDto.getEmail(), userDto.getBalance());
    }

    @GetMapping("stat")
    public List<OrderStatistic> getStat(){
        return service.getStat();
    }
}

