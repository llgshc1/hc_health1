package com.hcwawe.jobs;

import com.hcwawe.constant.RedisConstant;
import com.hcwawe.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.Set;

//自定义job 定时清理垃圾文件
public class ClearImgJob {
    @Autowired
    private JedisPool jedisPool;
    public void clearImg(){
//        根据Redis中保存的两个set集合得差值 删除垃圾图片
        Set<String> set = jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);

        if (set!=null){
            for (String picName : set) {
//                从服务器当中删除
                QiniuUtils.deleteFileFromQiniu(picName);
//                从redis中删除
                jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES,picName);
            }
        }

    }
}
