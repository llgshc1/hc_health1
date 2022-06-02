package com.hcwawe.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hcwawe.constant.MessageConstant;
import com.hcwawe.entity.Result;
import com.hcwawe.service.MemberService;
import com.hcwawe.utils.DateUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/report")
public class ReportController {
    @Reference
    private MemberService memberService;
    @RequestMapping("/getMemberReport.do")
    public Result getMemberReport(){
/*//        使用模拟数据 看能否转为echars
        Map<String,Object> map = new HashMap<>();
        List<String> list = new ArrayList<>();
        list.add("2022.05");
        list.add("2022.06");
        list.add("22022.07");
        List<Integer> list1 = new ArrayList<>();
        list1.add(10);
        list1.add(80);
        list1.add(180);
        map.put("months",list);
        map.put("memberCount",list1);

        return new Result(true, MessageConstant.GET_USERNAME_FAIL,map);*/
//        计算过去一年得十二个月份 calender对象 默认时间就是当前得系统时间
        Calendar calendar = Calendar.getInstance();
//        往前翻12个月
        calendar.add(Calendar.MONTH,-12);
        List<String> months = new ArrayList<>();
        Date time = calendar.getTime();
        for (int i = 0; i < 12; i++) {
            calendar.add(Calendar.MONTH,1);//每次往后退一个月
            Date date = calendar.getTime();
            months.add(new SimpleDateFormat("yyyy.MM").format(date));
        }
        Map<String,Object> map = new HashMap<>();
        map.put("months",months);
        List<Integer> memberCount = memberService.findMemberCountByMonth(months);
        map.put("memberCount",memberCount);
        return new Result(true,MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,map);
    }


}
