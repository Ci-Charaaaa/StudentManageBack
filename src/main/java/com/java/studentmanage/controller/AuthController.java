package com.java.studentmanage.controller;

import com.java.studentmanage.entity.R;
import com.java.studentmanage.entity.User;
import com.java.studentmanage.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public R<?> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");
        if (username == null || password == null) {
            return R.error("用户名或密码不能为空");
        }
        Map<String, Object> result = userService.login(username, password);
        if (result == null) {
            return R.error("用户名或密码错误");
        }
        return R.ok(result);
    }

    @PostMapping("/register")
    public R<?> register(@RequestBody User user) {
        if (user.getUsername() == null || user.getUsername().length() < 3 || user.getUsername().length() > 20) {
            return R.error("用户名长度需在3-20之间");
        }
        if (user.getPassword() == null || user.getPassword().length() < 6 || user.getPassword().length() > 20) {
            return R.error("密码长度需在6-20之间");
        }
        if (user.getRealName() == null || user.getRealName().isEmpty()) {
            return R.error("真实姓名不能为空");
        }
        if (user.getEmail() == null || !user.getEmail().matches("^\\S+@\\S+\\.\\S+$")) {
            return R.error("邮箱格式不正确");
        }
        boolean success = userService.register(user);
        if (!success) {
            return R.error("用户名已存在");
        }
        return R.ok();
    }
}
