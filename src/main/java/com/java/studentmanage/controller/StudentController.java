package com.java.studentmanage.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.studentmanage.entity.R;
import com.java.studentmanage.entity.Student;
import com.java.studentmanage.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping
    public R<?> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String studentNo,
            @RequestParam(required = false) String className,
            @RequestParam(required = false) String gender) {
        Page<Student> p = studentService.page(page, pageSize, name, studentNo, className, gender);
        return R.ok(p);
    }

    @GetMapping("/{id}")
    public R<?> get(@PathVariable Long id) {
        Student student = studentService.getById(id);
        if (student == null) return R.error("学生不存在");
        return R.ok(student);
    }

    @PostMapping
    public R<?> add(@RequestBody Student student) {
        if (student.getStudentNo() == null || student.getStudentNo().isEmpty()) return R.error("学号不能为空");
        if (student.getName() == null || student.getName().length() < 2 || student.getName().length() > 20)
            return R.error("姓名长度需在2-20之间");
        if (student.getGender() == null || (!student.getGender().equals("男") && !student.getGender().equals("女")))
            return R.error("性别必须为男或女");
        if (student.getClassName() == null || student.getClassName().isEmpty()) return R.error("班级不能为空");
        if (student.getAge() != null && (student.getAge() < 1 || student.getAge() > 150))
            return R.error("年龄需在1-150之间");
        if (student.getPhone() != null && !student.getPhone().isEmpty()
                && !student.getPhone().matches("1[3-9]\\d{9}"))
            return R.error("手机号格式不正确");
        boolean ok = studentService.save(student);
        return ok ? R.ok() : R.error("添加失败");
    }

    @PutMapping("/{id}")
    public R<?> update(@PathVariable Long id, @RequestBody Student student) {
        student.setId(id);
        boolean ok = studentService.update(student);
        return ok ? R.ok() : R.error("修改失败");
    }

    @DeleteMapping("/{id}")
    public R<?> delete(@PathVariable Long id) {
        boolean ok = studentService.delete(id);
        return ok ? R.ok() : R.error("删除失败");
    }
}
