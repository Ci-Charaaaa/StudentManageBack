package com.java.studentmanage.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.studentmanage.entity.Student;
import com.java.studentmanage.mapper.StudentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

    @Autowired
    private StudentMapper studentMapper;

    //分页和条件查询
    public Page<Student> page(int page, int pageSize, String name, String studentNo, String className, String gender) {
        //新建一页，通过传参来取要的那一页
        Page<Student> p = new Page<>(page, pageSize);
        //QueryWrapper是MyBatis-Plus提供的条件包装器，用于动态拼接WHERE条件，防止SQL注入
        QueryWrapper<Student> qw = new QueryWrapper<>();
        //模糊匹配
        if (name != null && !name.isEmpty()) qw.like("name", name);
        if (studentNo != null && !studentNo.isEmpty()) qw.like("student_no", studentNo);
        if (className != null && !className.isEmpty()) qw.like("class_name", className);
        //精准匹配
        if (gender != null && !gender.isEmpty()) qw.eq("gender", gender);
        //排序
        qw.orderByDesc("id");
        return studentMapper.selectPage(p, qw);
    }
    //以下4个方法直接调用BaseMapper提供的方法
    public Student getById(Long id) {
        return studentMapper.selectById(id);
    }

    public boolean save(Student student) {
        return studentMapper.insert(student) > 0;
    }

    public boolean update(Student student) {
        return studentMapper.updateById(student) > 0;
    }

    public boolean delete(Long id) {
        return studentMapper.deleteById(id) > 0;
    }
}
