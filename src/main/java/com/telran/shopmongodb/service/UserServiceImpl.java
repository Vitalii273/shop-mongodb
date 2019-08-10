package com.telran.shopmongodb.service;

import com.telran.shopmongodb.controller.dto.*;
import com.telran.shopmongodb.data.*;
import com.telran.shopmongodb.data.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.telran.shopmongodb.service.Mapper.map;
import static java.util.stream.Collectors.toList;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    ShoppingCartRepository shoppingCartRepository;
    @Autowired
    ProductOrderEntityRepository productOrderEntityRepository;

    @Override
    public Optional<UserDto> addUserInfo(String email, String name, String phone) {
        if (!userRepository.existsById(email)) {
            UserEntity entity = new UserEntity(email, name, phone, BigDecimal.ZERO, null, null, null);
            userRepository.save(entity);
            return Optional.of(map(entity));
        }
        return Optional.empty();
    }

    @Override
    public Optional<UserDto> getUserInfo(String email) {
        try {
            UserEntity entity = userRepository.findById(email).orElseThrow();
            return Optional.of(map(entity));
        } catch (NoSuchElementException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User profile[" + email + "] does not exist");
        }
    }

    @Override
    public List<ProductDto> getAllProducts() {
        return StreamSupport.stream(productRepository.findAll().spliterator(), false)
                .map(Mapper::map)
                .collect(toList());
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        return StreamSupport.stream(categoryRepository.findAll().spliterator(), false)
                .map(Mapper::map)
                .collect(toList());
    }

    @Override
    public List<ProductDto> getProductsByCategory(String categoryId) {
        return productRepository.findAllByCategory_Id(categoryId)
                .map(Mapper::map)
                .collect(toList());
    }
    @Override
    public Optional<ShoppingCartDto> addProductToCart(String userEmail, String productId, int count) {
        ProductEntity productEntity = productRepository.findProductEntityById(productId);
        UserEntity userEntity = getUserEntity(userEmail);
        if (userEntity == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User does not have a profile");
        }
        if (productEntity == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }
        ShoppingCartEntity shoppingCart = userEntity.getShoppingCart();
        if (shoppingCart == null) {
            shoppingCart = new ShoppingCartEntity();
            shoppingCart.setDate(Timestamp.valueOf(LocalDateTime.now()));
            shoppingCart.setProducts(new ArrayList<>());
            shoppingCartRepository.save(shoppingCart);
        }
        userEntity.setShoppingCart(shoppingCart);

        List<ProductOrderEntity> products = shoppingCart.getProducts();

        ProductOrderEntity poe = products.stream()
                .filter(p -> p.getProductId().equals(productId))
                .findAny()
                .orElse(null);
        if (poe == null) {
            poe = new ProductOrderEntity();
            poe.setProductId(productEntity.getId());
            poe.setCategory(productEntity.getCategory());
            poe.setName(productEntity.getName());
            poe.setPrice(productEntity.getPrice());
            poe.setCount(count);
            productOrderEntityRepository.save(poe);
            poe.setShoppingCart(shoppingCart);
            products.add(poe);
        } else {
            poe.setCount(poe.getCount() + count);
        }
        ShoppingCartDto dto = Mapper.map(shoppingCart);
        return Optional.of(dto);
    }

    @Override
    public Optional<ShoppingCartDto> removeProductFromCart(String userEmail, String productId, int count) {
        UserEntity userEntity = getUserEntity(userEmail);
        ShoppingCartEntity shoppingCart = userEntity.getShoppingCart();
        List<ProductOrderEntity> products = shoppingCart.getProducts();
        ProductOrderEntity poe = products.stream()
                .filter(p -> p.getProductId().equals(productId))
                .findAny()
                .orElse(null);
        if (poe == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } else {
            poe.setCount(poe.getCount() - count);
        }
        ShoppingCartDto dto = map(shoppingCart);
        return Optional.of(dto);
    }

    @Override
    public Optional<ShoppingCartDto> getShoppingCart(String userEmail) {
        UserEntity userEntity = getUserEntity(userEmail);
        ShoppingCartEntity shoppingCart = userEntity.getShoppingCart();
        if (shoppingCart == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Shopping Cart of [" + userEmail + "]not found");
        }
        ShoppingCartDto dto = map(shoppingCart);
        return Optional.of(dto);
    }

    @Override
    public boolean clearShoppingCart(String userEmail) {
        UserEntity userEntity = getUserEntity(userEmail);
        ShoppingCartEntity shoppingCart = userEntity.getShoppingCart();
        if (shoppingCart == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Shopping Cart of [" + userEmail + "]not found");
        }
        List<ProductOrderEntity> products = shoppingCart.getProducts();

        for (ProductOrderEntity productOrderEntity : products) {
            productOrderEntity.setShoppingCart(null);
        }
        userEntity.setShoppingCart(null);

        for (ProductOrderEntity productOrderEntity : products) {
            productOrderEntity.setShoppingCart(null);
        }
        shoppingCartRepository.delete(shoppingCart);
        return true;
    }

    private UserEntity getUserEntity(String userEmail) {
        UserEntity userEntity = userRepository.findById(userEmail).orElse(null);
        if (userEntity == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User [" + userEmail + "] does not have profile");
        }
        return userEntity;
    }

    @Override
    public List<OrderDto> getOrders(String userEmail) {
        getUserEntity(userEmail);
        return orderRepository.findAllByOwner_Email(userEmail).map(Mapper::map).collect(Collectors.toList());
    }

    @Override
    public Optional<OrderDto> checkout(String userEmail) {
        UserEntity userEntity = getUserEntity(userEmail);
        ShoppingCartEntity shoppingCart = userEntity.getShoppingCart();
        if (shoppingCart == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Shopping Cart of [" + userEmail + "]not found");
        }

        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setOwner(shoppingCart.getOwner());
        orderEntity.setStatus(OrderStatus.UNDELIVERED);

        List<ProductOrderEntity> products = shoppingCart.getProducts();
        for (ProductOrderEntity productOrderEntity : products) {
            productOrderEntity.setOrder(orderEntity);
            productOrderEntity.setShoppingCart(null);
        }

        orderEntity.setProducts(products);
        orderEntity.setDate(shoppingCart.getDate());
        orderRepository.save(orderEntity);
        userEntity.getOrders().add(orderEntity);
        clearShoppingCart(userEmail);
        OrderDto dto = map(orderEntity);
        return Optional.of(dto);
    }

}
