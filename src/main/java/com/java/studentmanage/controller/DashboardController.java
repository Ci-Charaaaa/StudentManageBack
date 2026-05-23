package com.java.studentmanage.controller;

import com.java.studentmanage.entity.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class DashboardController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    //
    @GetMapping("/dashboard")
    public R<?> dashboard() {

        //执行8条聚合SQL，一次性返回首页需要的所有统计数字
        //按顺序分别是学生数，男数，女数，本月新增数，课程数，成绩数，平均成绩，及格率
        Long total = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM student", Long.class);
        Long maleCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM student WHERE gender='男'", Long.class);
        Long femaleCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM student WHERE gender='女'", Long.class);
        Long monthNewCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM student WHERE create_time >= DATE_SUB(CURDATE(), INTERVAL 1 MONTH)", Long.class);
        Long courseCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM course", Long.class);
        Long scoreCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM score", Long.class);
        Double avgScore = jdbcTemplate.queryForObject("SELECT ROUND(AVG(score), 1) FROM score", Double.class);
        Long passCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM score WHERE score >= 60", Long.class);
        //计算及格率
        double passRate = (scoreCount != null && scoreCount > 0)
                ? Math.round((double) passCount / scoreCount * 1000) / 10.0 : 0.0;

        //创建表，用来存上面查完的结果，然后把统计数据加进去再返回
        Map<String, Object> data = new HashMap<>();
        data.put("total", total);
        data.put("maleCount", maleCount);
        data.put("femaleCount", femaleCount);
        data.put("monthNewCount", monthNewCount);
        data.put("courseCount", courseCount);
        data.put("scoreCount", scoreCount);
        data.put("avgScore", avgScore);
        data.put("passRate", passRate);

        return R.ok(data);
    }
}
