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

    public Page<Student> page(int page, int pageSize, String name, String studentNo, String className, String gender) {
        Page<Student> p = new Page<>(page, pageSize);
        QueryWrapper<Student> qw = new QueryWrapper<>();
        if (name != null && !name.isEmpty()) qw.like("name", name);
        if (studentNo != null && !studentNo.isEmpty()) qw.like("student_no", studentNo);
        if (className != null && !className.isEmpty()) qw.like("class_name", className);
        if (gender != null && !gender.isEmpty()) qw.eq("gender", gender);
        qw.orderByDesc("id");
        return studentMapper.selectPage(p, qw);
    }

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
