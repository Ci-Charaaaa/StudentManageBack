package com.java.studentmanage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.java.studentmanage.entity.Student;
import org.apache.ibatis.annotations.Mapper;

@Mapper // 告诉Spring这是一个Mapper接口
public interface StudentMapper extends BaseMapper<Student> {
    // 继承了BaseMapper后，增删改查的方法已经自带了，这里什么都不用写
}