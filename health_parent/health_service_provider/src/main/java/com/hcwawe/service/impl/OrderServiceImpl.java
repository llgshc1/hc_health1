package com.hcwawe.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hcwawe.constant.MessageConstant;
import com.hcwawe.dao.MemberDao;
import com.hcwawe.dao.OrderDao;
import com.hcwawe.entity.Result;
import com.hcwawe.pojo.Member;
import com.hcwawe.pojo.Order;
import com.hcwawe.pojo.OrderSetting;
import com.hcwawe.service.OrderService;
import com.hcwawe.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.hcwawe.dao.OderSettingDao;

import java.util.Date;
import java.util.List;
import java.util.Map;
@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {
    @Autowired
  private OderSettingDao oderSettingDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private OrderDao orderDao;
    @Override
//    体检预约的方法
    public Result order(Map map) throws Exception {
//        选择日期是否已经设置可预约
        String orderDate = (String) map.get("orderDate");
        OrderSetting orderSetting = oderSettingDao.findByDate(DateUtils.parseString2Date(orderDate));
        if(orderSetting == null){
//            没有设置 无法预约
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }
//        看该日期是否约满
        int number = orderSetting.getNumber();
        int reservations = orderSetting.getReservations();
        if (reservations >= number){
            return  new Result(false,MessageConstant.ORDER_FULL);
        }
//        看用户是否重复预约 同一用户同一天同一套餐
        String telephone = (String) map.get("telephone");
        Member member = memberDao.findByTelephone(telephone);
        if(member != null){
//            判断是否重复
            Integer id = member.getId();//会员id
            Date order_Date = DateUtils.parseString2Date(orderDate);//预约日期
            String setmealId = (String) map.get("setmealId");
            Order order =new Order(id,order_Date,Integer.parseInt(setmealId));

            List<Order> list = orderDao.findByCondition(order);
            if(list.size() > 0 && list !=null){
//                重复预约了
                return new Result(false,MessageConstant.HAS_ORDERED);
            }

        }else {
//            不是会员
            member = new Member();
            member.setPhoneNumber(telephone);
            member.setName((String) map.get("name"));
            member.setPhoneNumber(telephone);
            member.setIdCard((String) map.get("idCard"));
            member.setSex((String) map.get("sex"));
            member.setRegTime(new Date());
//            完成注册
            memberDao.add(member);
        }
//        检查用户是否为会员 是就直接预约没有的话先自动注册

//        预约成功 更新已经预约的人数
        Order order = new Order();
        order.setId(member.getId());
        order.setOrderDate(DateUtils.parseString2Date(orderDate));
        order.setOrderType((String) map.get("orderType"));
        order.setOrderStatus(Order.ORDERSTATUS_NO);
        int setmealId = Integer.parseInt((String) map.get("setmealId"));
        order.setSetmealId(setmealId);//套餐id
        orderDao.add(order);
        orderSetting.setReservations(orderSetting.getReservations()+1);
        oderSettingDao.editNumberByOrderDate(orderSetting);
        return new Result(true,MessageConstant.ORDER_SUCCESS,order.getId());
    }

    @Override
    public Map findById(Integer id) throws Exception {
        Map map = orderDao.findById4Detail(id);
        if(map != null){
            //处理日期格式
            Date orderDate = (Date) map.get("orderDate");
            map.put("orderDate",DateUtils.parseDate2String(orderDate));
        }
        return map;
    }
}
