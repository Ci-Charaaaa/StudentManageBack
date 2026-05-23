package com.java.studentmanage.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.studentmanage.entity.Course;
import com.java.studentmanage.entity.R;
import com.java.studentmanage.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    //与学生类是一样的
    @GetMapping
    public R<?> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String courseNo,
            @RequestParam(required = false) String teacher) {
        Page<Course> p = courseService.page(page, pageSize, name, courseNo, teacher);
        return R.ok(p);
    }

    @GetMapping("/{id}")
    public R<?> get(@PathVariable Long id) {
        Course course = courseService.getById(id);
        if (course == null) return R.error("课程不存在");
        return R.ok(course);
    }

    @PostMapping
    public R<?> add(@RequestBody Course course) {
        if (course.getCourseNo() == null || course.getCourseNo().isEmpty()) return R.error("课程编号不能为空");
        if (course.getName() == null || course.getName().length() < 2 || course.getName().length() > 50)
            return R.error("课程名称长度需在2-50之间");
        if (course.getCredit() == null || course.getCredit() < 0.5 || course.getCredit() > 20)
            return R.error("学分需在0.5~20之间");
        if (course.getTeacher() == null || course.getTeacher().isEmpty()) return R.error("授课教师不能为空");
        if (course.getMaxStudents() != null && (course.getMaxStudents() < 1 || course.getMaxStudents() > 500))
            return R.error("选课容量需在1~500之间");
        boolean ok = courseService.save(course);
        return ok ? R.ok() : R.error("添加失败");
    }

    @PutMapping("/{id}")
    public R<?> update(@PathVariable Long id, @RequestBody Course course) {
        course.setId(id);
        boolean ok = courseService.update(course);
        return ok ? R.ok() : R.error("修改失败");
    }

    @DeleteMapping("/{id}")
    public R<?> delete(@PathVariable Long id) {
        boolean ok = courseService.delete(id);
        return ok ? R.ok() : R.error("删除失败");
    }
}
