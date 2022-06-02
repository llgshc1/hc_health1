package com.hcwawe.dao;

import com.hcwawe.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OderSettingDao {

        public void add(OrderSetting orderSetting);
        public void editNumberByOrderDate(OrderSetting orderSetting);
        public long findCountByOrderDate(Date orderDate);

    public List<OrderSetting> getOrderSettingByMonth(Map<String, String> map);

    public OrderSetting findByDate(Date orderDate);
    //更新已预约人数
    public void editReservationsByOrderDate(OrderSetting orderSetting);
}
