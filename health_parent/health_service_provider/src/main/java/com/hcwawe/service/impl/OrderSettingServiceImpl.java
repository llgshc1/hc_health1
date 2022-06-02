package com.hcwawe.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hcwawe.dao.OderSettingDao;
import com.hcwawe.pojo.OrderSetting;
import com.hcwawe.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {
    @Autowired
    private OderSettingDao oderSettingDao;

    @Override
    public void add(List<OrderSetting> list) {
//        判断集合是否为空
        if (list != null && list.size() > 0) {
            for (OrderSetting orderSetting : list) {
                //            调用方法 看日期是否已经有预约
                long countByOrderDate = oderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());

//                count = 0 该天没有预约
                if (countByOrderDate > 0) {
//                    有预约 对预约数据进行修改
                    oderSettingDao.editNumberByOrderDate(orderSetting);
                } else {
                    oderSettingDao.add(orderSetting);
                }
            }
        }
    }

    //根据年月来查询预约人数
    @Override
    public List<Map> getOrderSettingByMonth(String date) {
        String begin = date + "-" + "1";
        String end = "";
        int year = Integer.parseInt(date.substring(0, 4));
        String month = date.substring(5);
        if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
            switch (month) {
                case "1":
                case "3":
                case "5":
                case "7":
                case "8":
                case "12":
                case "10":
                    end = date + "-31";
                    break;
                case "2":
                    end = date + "-29";
                    break;
                default:
                    end = date + "-30";
            }
        } else {
            switch (month) {
                case "1":
                case "3":
                case "5":
                case "7":
                case "8":
                case "12":
                case "10":
                    end = date + "-31";
                    break;
                case "2":
                    end = date + "-28";
                    break;
                default:
                    end = date + "-30";
            }

        }
        Map<String,String> map = new HashMap<>();
        map.put("begin",begin);
        map.put("end",end);
        List<OrderSetting> list = oderSettingDao.getOrderSettingByMonth(map);
        List<Map> result = new ArrayList<>();
        if (list != null && list.size() > 0 ){
            for (OrderSetting orderSetting : list) {
                Map<String,Object> m = new HashMap<>();
                m.put("data",orderSetting.getOrderDate().getDate());
                m.put("number",orderSetting.getNumber());
                m.put("reservations",orderSetting.getReservations());
                result.add(m);
            }
        }
        return result;
    }
//根据日期设置对应得预约设置语句
    @Override
    public void editNumberByDate(OrderSetting orderSetting) {
//        根据日期进行查询
        long countByOrderDate = oderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
        if(countByOrderDate > 0){
//            已经预约
            oderSettingDao.editNumberByOrderDate(orderSetting);
        }else {
            oderSettingDao.add(orderSetting);
        }
    }
}
