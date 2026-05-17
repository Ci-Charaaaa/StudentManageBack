package com.java.studentmanage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.java.studentmanage.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
