package com.leyou.user.service;

import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.utils.NumberUtils;
import com.leyou.item.pojo.User;
import com.leyou.user.mapper.UserMapper;
import com.leyou.user.utils.CodecUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author: oyyb
 * @data: 2020-04-09
 * @version: 1.0.0
 * @descript:
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String KEY_PREFIX = "user:verify:phone";

    public Boolean checkData(String data, Integer type) {
        User record = new User();
        //判断数据类型
        switch (type){
            case 1:
                record.setUsername(data);
                break;
            case 2:
                record.setPhone(data);
                break;
            default:
                throw new LyException(ExceptionEnum.INVALID_USER_DATA_TYPE);

        }

        return userMapper.selectCount(record) == 0;

    }


    public void sendCode(String phone) {
        //生成key
        String key = KEY_PREFIX+phone;
        //生成验证码
        String code = NumberUtils.generateCode(6);

        Map<String,String> map = new HashMap<>();
        map.put("phone",phone);
        map.put("code",code);
        amqpTemplate.convertAndSend("ly.sms.exchange","sms.verify.code",map);

        //保存验证码
        redisTemplate.opsForValue().set(key,code,5, TimeUnit.MINUTES);


    }

    public void register(User user, String code) {
        //查询redis的验证码
        String redisCode = redisTemplate.opsForValue().get(KEY_PREFIX + user.getPhone());
        //1.校验验证码
        if (!StringUtils.equals(code,redisCode)){
            return;
        }
        //2.生成盐
        String salt = CodecUtils.generateSalt();
        user.setSalt(salt);
        //3.加盐加密
        user.setPassword(CodecUtils.md5Hex(user.getPassword(),salt));

        //4.新增用户
        user.setId(null);
        user.setCreated(new Date());

        userMapper.insertSelective(user);
    }

    public User queryUser(String username, String password) {
        User record = new User();
        record.setUsername(username);

        User user = this.userMapper.selectOne(record);
        //判断用户是否为空
        if (user == null){
            return null;
        }

        //获取盐,对用户输入的密码加盐加密
        password = CodecUtils.md5Hex(password,user.getSalt());

        //和数据库中的密码比较
        if (StringUtils.equals(password,user.getPassword())){
            return user;
        }
        return null;
    }
}
