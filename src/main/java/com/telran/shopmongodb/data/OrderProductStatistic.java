package com.telran.shopmongodb.data;

import com.telran.shopmongodb.data.entity.OrderStatistic;

import java.util.List;
import java.util.Map;

public interface OrderProductStatistic {
    List<OrderStatistic> getStat();
    List<Map> getCount();

}


