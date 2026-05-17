package com.java.studentmanage.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.java.studentmanage.entity.User;
import com.java.studentmanage.mapper.UserMapper;
import com.java.studentmanage.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public Map<String, Object> login(String username, String password) {
        User user = userMapper.selectOne(
                new QueryWrapper<User>().eq("username", username).eq("password", password));
        if (user == null) return null;
        String token = JwtUtil.generate(user.getId(), user.getUsername());
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("user", user);
        return result;
    }

    public boolean register(User user) {
        User exist = userMapper.selectOne(new QueryWrapper<User>().eq("username", user.getUsername()));
        if (exist != null) return false;
        return userMapper.insert(user) > 0;
    }
}
