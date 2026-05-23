package com.java.studentmanage.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.studentmanage.entity.Course;
import com.java.studentmanage.mapper.CourseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CourseService {

    @Autowired
    private CourseMapper courseMapper;

    //和学生的分页是一样的
    public Page<Course> page(int page, int pageSize, String name, String courseNo, String teacher) {
        Page<Course> p = new Page<>(page, pageSize);
        QueryWrapper<Course> qw = new QueryWrapper<>();
        if (name != null && !name.isEmpty()) qw.like("name", name);
        if (courseNo != null && !courseNo.isEmpty()) qw.like("course_no", courseNo);
        if (teacher != null && !teacher.isEmpty()) qw.like("teacher", teacher);
        qw.orderByDesc("id");
        return courseMapper.selectPage(p, qw);
    }

    public Course getById(Long id) {
        return courseMapper.selectById(id);
    }

    public boolean save(Course course) {
        return courseMapper.insert(course) > 0;
    }

    public boolean update(Course course) {
        return courseMapper.updateById(course) > 0;
    }

    public boolean delete(Long id) {
        return courseMapper.deleteById(id) > 0;
    }
}
