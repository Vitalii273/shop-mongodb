package com.telran.shopmongodb.service;

import com.telran.shopmongodb.data.*;
import com.telran.shopmongodb.data.entity.CategoryEntity;
import com.telran.shopmongodb.data.entity.ProductEntity;
import com.telran.shopmongodb.data.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

@Service
@Transactional(isolation = Isolation.READ_COMMITTED)
public class AdminServiceImpl implements AdminService {
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
    public String addCategory(String categoryName) {
        if (!categoryRepository.existsById(categoryName)) {
            CategoryEntity entity = new CategoryEntity();
            entity.setName(categoryName);
            categoryRepository.save(entity);
            return entity.getId().toHexString();
        }
        return null;
    }

    @Override
    public String addProduct(String productName, BigDecimal price, String categoryId) {
        CategoryEntity ce = categoryRepository.findById(categoryId).orElse(null);
        if (ce == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category with categoryId " + categoryId + " does not exist");
        }
        ProductEntity entity = new ProductEntity();
        entity.setName(productName);
        entity.setCategory(ce);
        entity.setPrice(price.doubleValue());
        productRepository.save(entity);
        return entity.getId().toHexString();
    }

    @Override
    public boolean removeProduct(String productId) {
        ProductEntity pe = productRepository.findProductEntityById(productId);
        if (pe != null) {
            productRepository.delete(pe);
            return true;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "product with id [" + productId + "] does not exist");
    }

//    @Override
//    public boolean removeCategory(String categoryId) {
//        try {
//            categoryRepository.deleteById(categoryId);
//        } catch (IllegalArgumentException e) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "category with id [" + categoryId + "] does not exist");
//        }
//        return false;
//    }

    @Override
    public boolean updateCategory(String categoryId, String categoryName) {
        CategoryEntity ce = categoryRepository.findById(categoryId).orElse(null);
        if (ce != null) {
            ce.setName(categoryName);
            categoryRepository.save(ce);
            return true;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "category with id [" + categoryId + "] does not exist");
    }

    @Override
    public boolean changeProductPrice(String productId, BigDecimal price) {
        ProductEntity pe = productRepository.findProductEntityById(productId);
        if (pe != null) {
            pe.setPrice(price.doubleValue());
            productRepository.save(pe);
            return true;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "product with id [" + productId + "] does not exist");
    }

    @Override
    public boolean addBalance(String userEmail, BigDecimal balance) {
        UserEntity ue = userRepository.findById(userEmail).orElse(null);
        if (ue != null) {
            ue.setBalance(ue.getBalance().add(balance));
            userRepository.save(ue);
            return true;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user with email [" + userEmail + "] does not exist");
    }
}
