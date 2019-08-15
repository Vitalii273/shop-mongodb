package com.telran.shopmongodb.data;

import com.telran.shopmongodb.data.entity.OrderStatistic;
import com.telran.shopmongodb.data.entity.ProductOrderEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;

import java.util.List;
import java.util.Map;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

public class OrderProductStatisticImpl implements OrderProductStatistic {
    @Autowired
    MongoTemplate template;

    @Override
    public List<OrderStatistic> getStat() {
        TypedAggregation<ProductOrderEntity> aggregation = Aggregation.newAggregation(
                ProductOrderEntity.class,
                group("category")
                        .sum("price").as("totalPrice")
                        .sum("count").as("totalCount")
                .addToSet("name").as("productName"),sort(Sort.Direction.DESC,"totalCount"),
                project("totalCount","totalPrice", "productName")
        );
        AggregationResults<OrderStatistic> results = template.aggregate(aggregation, "product_order", OrderStatistic.class);
        return results.getMappedResults();
    }

    @Override
    public List<Map> getCount() {
        return template.aggregate(
                newAggregation(group("name")
                                .sum("count").as("totalCount")
                                .sum("price").as("totalPrice")
                ),
                "product_order",
                Map.class
        ).getMappedResults();
    }
}

