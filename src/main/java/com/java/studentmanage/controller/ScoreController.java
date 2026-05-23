package com.java.studentmanage.controller;

import com.java.studentmanage.entity.R;
import com.java.studentmanage.entity.Score;
import com.java.studentmanage.entity.ScoreVo;
import com.java.studentmanage.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/scores")
public class ScoreController {

    @Autowired
    private ScoreService scoreService;

    //和学生一样，只是调用的方法换成自定义的多表联查的方法，这里的两个外键传参是用于跳转详情页时获取学生/课程表的data的
    @GetMapping
    public R<?> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String studentName,
            @RequestParam(required = false) String courseName,
            @RequestParam(required = false) String semester,
            @RequestParam(required = false) String examType,
            @RequestParam(required = false) Long studentId,
            @RequestParam(required = false) Long courseId) {
        return R.ok(scoreService.page(page, pageSize, studentName, courseName, semester, examType, studentId, courseId));
    }

    //后面和学生一样CRUD
    @GetMapping("/{id}")
    public R<?> get(@PathVariable Long id) {
        ScoreVo vo = scoreService.getById(id);
        return R.ok(vo);
    }

    @PostMapping
    public R<?> add(@RequestBody Score score) {
        if (score.getStudentId() == null) return R.error("学生不能为空");
        if (score.getCourseId() == null) return R.error("课程不能为空");
        if (score.getScore() == null || score.getScore() < 0 || score.getScore() > 100)
            return R.error("成绩需在0~100之间");
        if (score.getSemester() == null || score.getSemester().isEmpty()) return R.error("学期不能为空");
        if (score.getExamType() == null || score.getExamType().isEmpty()) return R.error("考试类型不能为空");
        boolean ok = scoreService.save(score);
        return ok ? R.ok() : R.error("录入失败");
    }

    @PutMapping("/{id}")
    public R<?> update(@PathVariable Long id, @RequestBody Score score) {
        score.setId(id);
        boolean ok = scoreService.update(score);
        return ok ? R.ok() : R.error("修改失败");
    }

    @DeleteMapping("/{id}")
    public R<?> delete(@PathVariable Long id) {
        boolean ok = scoreService.delete(id);
        return ok ? R.ok() : R.error("删除失败");
    }
}
