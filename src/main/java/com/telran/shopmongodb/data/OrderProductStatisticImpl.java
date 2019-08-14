package com.telran.shopmongodb.data;

import com.telran.shopmongodb.data.entity.OrderStatistic;
import com.telran.shopmongodb.data.entity.ProductOrderEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;

import java.util.List;
import java.util.Set;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;

public class OrderProductStatisticImpl implements OrderProductStatistic {
    @Autowired
    MongoTemplate template;

    @Override
    public List<OrderStatistic> getStat() {
        TypedAggregation<ProductOrderEntity> aggregation = Aggregation.newAggregation(
                ProductOrderEntity.class,
                group("category")
                        .sum("price").as("totalPrice")
                        .addToSet("name").as("productName"),
                project("totalPrice", "productName").and("_id").as("id")
        );
        AggregationResults<OrderStatistic> results = template.aggregate(aggregation, "product_order", OrderStatistic.class);
        return results.getMappedResults();
    }
}
