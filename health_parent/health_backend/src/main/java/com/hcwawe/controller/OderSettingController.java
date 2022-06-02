package com.hcwawe.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hcwawe.constant.MessageConstant;
import com.hcwawe.entity.Result;
import com.hcwawe.pojo.OrderSetting;
import com.hcwawe.service.OrderSettingService;
import com.hcwawe.utils.POIUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

//预约设置
@RestController
@RequestMapping("/ordersetting")
public class OderSettingController {
    @Reference
    private OrderSettingService orderSettingService;
//    文件上传实现数据批量导入
    @RequestMapping("/upload.do")
    public Result upload(@RequestParam("excelFile")MultipartFile excelFile){
        try {
            List<String[]> list = POIUtils.readExcel(excelFile);
            List<OrderSetting> data = new ArrayList<>();
            for (String[] strings : list) {
                String orderDate = strings[0];
                String number = strings[1];
                OrderSetting orderSetting = new OrderSetting(new Date(orderDate),Integer.parseInt(number));
                data.add(orderSetting);
            }
            orderSettingService.add(data);
            return new Result(true,MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
        } catch (IOException e) {
            e.printStackTrace();
//            文件解析失败
            return new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
    }
    //        根据年月来查询对应的预约信息
    @RequestMapping("/getOrderSettingByMonth.do")
    public Result getOrderSettingByMonth(String date){//date格式为yyyy-MM
        try{
            List<Map> list = orderSettingService.getOrderSettingByMonth(date);
            return new Result(true,MessageConstant.GET_ORDERSETTING_SUCCESS,list);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_ORDERSETTING_FAIL);
        }
    }
//    设置预约人数
    @RequestMapping("/editNumberByDate.do")
    public Result editNumberByDate(@RequestBody OrderSetting orderSetting){
        try {
            orderSettingService.editNumberByDate(orderSetting);
            return new Result(true,MessageConstant.ORDERSETTING_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(true,MessageConstant.ORDERSETTING_FAIL);
        }

    }
}
