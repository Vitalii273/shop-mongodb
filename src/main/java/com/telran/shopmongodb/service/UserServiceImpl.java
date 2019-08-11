package com.telran.shopmongodb.service;

import com.telran.shopmongodb.controller.dto.*;
import com.telran.shopmongodb.data.*;
import com.telran.shopmongodb.data.entity.*;
import lombok.val;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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

    @Autowired
    Mapper mapper;

    @Override
    public Optional<UserDto> addUserInfo(String email, String name, String phone) {
        if (!userRepository.existsById(email)) {
            UserEntity entity = new UserEntity(email, name, phone, 0.0, null, null, null);
            userRepository.save(entity);
            return Optional.of(mapper.map(entity));
        }
        return Optional.empty();
    }

    @Override
    public Optional<UserDto> getUserInfo(String email) {
        try {
            UserEntity entity = userRepository.findById(email).orElseThrow();
            return Optional.of(mapper.map(entity));
        } catch (NoSuchElementException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User profile[" + email + "] does not exist");
        }
    }

    @Override
    public List<ProductDto> getAllProducts() {
        return StreamSupport.stream(productRepository.findAll().spliterator(), false)
                .map(mapper::map)
                .collect(toList());
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        return StreamSupport.stream(categoryRepository.findAll().spliterator(), false)
                .map(mapper::map)
                .collect(toList());
    }

    @Override
    public List<ProductDto> getProductsByCategory(String categoryId) {
        return productRepository.findAllByCategory_Id(categoryId)
                .map(mapper::map)
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
        ShoppingCartEntity shoppingCart;
        if (userEntity.getShoppingCart() == null) {
            shoppingCart = new ShoppingCartEntity();
            shoppingCart.setDate(LocalDateTime.now());
            shoppingCart.setProducts(new ArrayList<>());
            userEntity.setShoppingCart(shoppingCart.getId());
            shoppingCartRepository.save(shoppingCart);
        } else {
            shoppingCart = shoppingCartRepository.findById(userEntity.getShoppingCart().toHexString()).orElseThrow();
        }
        val list = shoppingCart.getProducts();

        if (list == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        List<ProductOrderEntity> products = list
                .stream()
                .map(ObjectId::toHexString)
                .map(productOrderEntityRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(toList());

        ProductOrderEntity poe = products.stream()
                .filter(p -> p.getProductId().toHexString().equals(productId))
                .findAny()
                .orElse(null);
        if (poe == null) {
            poe = new ProductOrderEntity();
            poe.setProductId(productEntity.getId());
            poe.setCategory(productEntity.getCategory());
            poe.setName(productEntity.getName());
            poe.setPrice(productEntity.getPrice());
            poe.setCount(count);
            products.add(poe);
            poe.setShoppingCart(shoppingCart.getId());
            productOrderEntityRepository.save(poe);
            list.add(poe.getId());
            shoppingCart.setProducts(list);
            shoppingCartRepository.save(shoppingCart);
            userEntity.setShoppingCart(shoppingCart.getId());
            userRepository.save(userEntity);
        } else {
            poe.setCount(poe.getCount() + count);
            productOrderEntityRepository.save(poe);
        }
        ShoppingCartDto dto = mapper.map(shoppingCart);
        return Optional.of(dto);
    }


    @Override
    public Optional<ShoppingCartDto> removeProductFromCart(String userEmail, String productId, int count) {
        UserEntity userEntity = getUserEntity(userEmail);
        ShoppingCartEntity shoppingCart = shoppingCartRepository.findById(userEntity.getShoppingCart().toHexString()).orElseThrow();

        val list = shoppingCart.getProducts();

        ProductOrderEntity poe = getProductOrderEntity(productId, list);
        if (poe == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "products not found");
        } else {
            poe.setCount(poe.getCount() - count);
            if(poe.getCount()< 0){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "basket is empty");
            }
            productOrderEntityRepository.save(poe);
        }
        ShoppingCartDto dto = mapper.map(shoppingCart);
        return Optional.of(dto);
    }

    private ProductOrderEntity getProductOrderEntity(String productId, List<ObjectId> list) {
        List<ProductOrderEntity> products = list
                .stream()
                .map(ObjectId::toHexString)
                .map(productOrderEntityRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(toList());

        return products.stream()
                .filter(p -> p.getProductId().toHexString().equals(productId))
                .findAny()
                .orElse(null);
    }

    @Override
    public Optional<ShoppingCartDto> getShoppingCart(String userEmail) {
        UserEntity userEntity = getUserEntity(userEmail);
        ShoppingCartEntity shoppingCart = shoppingCartRepository.findById(userEntity.getShoppingCart().toHexString()).orElse(null);
        if (shoppingCart == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Shopping Cart of [" + userEmail + "]not found");
        }
        ShoppingCartDto dto = mapper.map(shoppingCart);
        return Optional.of(dto);
    }

    @Override
    public boolean clearShoppingCart(String userEmail) {
        UserEntity userEntity = getUserEntity(userEmail);
        ShoppingCartEntity shoppingCart = shoppingCartRepository.findById(userEntity.getShoppingCart().toHexString()).orElse(null);
        if (shoppingCart == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Shopping Cart of [" + userEmail + "]not found");
        }
        List<ProductOrderEntity> products = getProductOrderEntities(shoppingCart);
        for (ProductOrderEntity productOrderEntity : products) {
            productOrderEntity.setShoppingCart(null);
        }
        productOrderEntityRepository.saveAll(products);
        shoppingCart.setProducts(new ArrayList<>());
        shoppingCartRepository.save(shoppingCart);
        return true;
    }


    @Override
    public List<OrderDto> getOrders(String userEmail) {
        getUserEntity(userEmail);
        return orderRepository.findAllByOwner_Email(userEmail).map(mapper::map).collect(Collectors.toList());
    }

    @Override
    public Optional<OrderDto> checkout(String userEmail) {
        UserEntity userEntity = getUserEntity(userEmail);
        ShoppingCartEntity shoppingCart = shoppingCartRepository.findById(userEntity.getShoppingCart().toHexString()).orElse(null);

        if (shoppingCart == null || shoppingCart.getProducts() == null || shoppingCart.getProducts().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Shopping Cart or products order of [" + userEmail + "]not found");
        }

        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setOwner(userEntity);
        orderEntity.setStatus(OrderStatus.UNDELIVERED);
        orderRepository.save(orderEntity);

        List<ProductOrderEntity> products = getProductOrderEntities(shoppingCart);
        for (ProductOrderEntity productOrderEntity : products) {
            productOrderEntity.setOrder(orderEntity.getId());
            productOrderEntity.setShoppingCart(null);
        }
        productOrderEntityRepository.saveAll(products);

        orderEntity.setProducts(products.stream().map(x -> x.getId()).collect(toList()));
        orderEntity.setDate(shoppingCart.getDate());
        orderRepository.save(orderEntity);

        List<ObjectId> list = userEntity.getOrders();
        if (list == null) {
            list = new ArrayList<>();
            list.add(orderEntity.getId());
            userEntity.setOrders(list);
            userRepository.save(userEntity);
        }
        shoppingCart.setProducts(new ArrayList<>());
        shoppingCartRepository.save(shoppingCart);
        OrderDto dto = mapper.map(orderEntity);
        return Optional.of(dto);
    }

    private List<ProductOrderEntity> getProductOrderEntities(ShoppingCartEntity shoppingCart) {
        val list = shoppingCart.getProducts();
        if (list == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return list
                .stream()
                .map(ObjectId::toHexString)
                .map(productOrderEntityRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(toList());
    }

    private UserEntity getUserEntity(String userEmail) {
        UserEntity userEntity = userRepository.findById(userEmail).orElse(null);
        if (userEntity == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User [" + userEmail + "] does not have profile");
        }
        return userEntity;
    }

}
