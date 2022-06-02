package com.hcwawe.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hcwawe.dao.MemberDao;
import com.hcwawe.pojo.Member;
import com.hcwawe.service.MemberService;
import com.hcwawe.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberDao memberDao;
    @Override
    public Member findByTelephone(String telephone) {
        return memberDao.findByTelephone(telephone);
    }

    @Override
    public void add(Member member) {
        String password = member.getPassword();
        if (password != null){
          password = MD5Utils.md5(password);
        }
        memberDao.add(member);
    }

    @Override
    public List<Integer> findMemberCountByMonth(List<String> months) {
        List<Integer> memberCount = new ArrayList<>();
        for (String month : months) {
            String date = month + ".30";
            Integer count = memberDao.findMemberCountBeforeDate(date);
            memberCount.add(count);
        }
        return memberCount;
    }
}
