package com.java.studentmanage.controller;

import com.java.studentmanage.entity.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/api")
public class VisualizationController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/visualization")
    public R<?> visualization() {
        Map<String, Object> data = new HashMap<>();
        data.put("studentTrend", getStudentTrend());
        data.put("studentCumulative", getStudentCumulative());
        data.put("maleCount", jdbcTemplate.queryForObject("SELECT COUNT(*) FROM student WHERE gender='男'", Long.class));
        data.put("femaleCount", jdbcTemplate.queryForObject("SELECT COUNT(*) FROM student WHERE gender='女'", Long.class));
        data.put("scoreDistribution", getScoreDistribution());
        data.put("courseAvg", getCourseAvg());
        return R.ok(data);
    }

    private Map<String, List<String>> getStudentTrend() {
        List<String> labels = new ArrayList<>();
        List<String> values = new ArrayList<>();
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(
                "SELECT DATE_FORMAT(create_time, '%Y-%m') AS m, COUNT(*) AS cnt FROM student " +
                "WHERE create_time >= DATE_SUB(CURDATE(), INTERVAL 11 MONTH) " +
                "GROUP BY m ORDER BY m");
        for (Map<String, Object> row : rows) {
            labels.add((String) row.get("m"));
            values.add(row.get("cnt").toString());
        }
        Map<String, List<String>> result = new HashMap<>();
        result.put("labels", labels);
        result.put("values", values);
        return result;
    }

    private Map<String, List<String>> getStudentCumulative() {
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(
                "SELECT DATE_FORMAT(create_time, '%Y-%m') AS m FROM student " +
                "WHERE create_time >= DATE_SUB(CURDATE(), INTERVAL 11 MONTH) " +
                "ORDER BY create_time");
        Set<String> monthSet = new LinkedHashSet<>();
        for (Map<String, Object> row : rows) {
            monthSet.add((String) row.get("m"));
        }
        List<String> labels = new ArrayList<>(monthSet);
        List<String> values = new ArrayList<>();
        long cum = 0;
        for (String m : labels) {
            Long cnt = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM student WHERE DATE_FORMAT(create_time, '%Y-%m') <= ?", Long.class, m);
            cum = cnt != null ? cnt : 0;
            values.add(String.valueOf(cum));
        }
        Map<String, List<String>> result = new HashMap<>();
        result.put("labels", labels);
        result.put("values", values);
        return result;
    }

    private Map<String, List<String>> getScoreDistribution() {
        List<String> labels = Arrays.asList("0-59", "60-69", "70-79", "80-89", "90-100");
        List<String> values = new ArrayList<>();
        values.add(String.valueOf(jdbcTemplate.queryForObject("SELECT COUNT(*) FROM score WHERE score < 60", Long.class)));
        values.add(String.valueOf(jdbcTemplate.queryForObject("SELECT COUNT(*) FROM score WHERE score BETWEEN 60 AND 69", Long.class)));
        values.add(String.valueOf(jdbcTemplate.queryForObject("SELECT COUNT(*) FROM score WHERE score BETWEEN 70 AND 79", Long.class)));
        values.add(String.valueOf(jdbcTemplate.queryForObject("SELECT COUNT(*) FROM score WHERE score BETWEEN 80 AND 89", Long.class)));
        values.add(String.valueOf(jdbcTemplate.queryForObject("SELECT COUNT(*) FROM score WHERE score >= 90", Long.class)));
        Map<String, List<String>> result = new HashMap<>();
        result.put("labels", labels);
        result.put("values", values);
        return result;
    }

    private Map<String, List<String>> getCourseAvg() {
        List<String> labels = new ArrayList<>();
        List<String> values = new ArrayList<>();
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(
                "SELECT c.name, ROUND(AVG(s.score), 1) AS avg_score FROM score s " +
                "LEFT JOIN course c ON s.course_id = c.id " +
                "GROUP BY s.course_id ORDER BY avg_score DESC");
        for (Map<String, Object> row : rows) {
            labels.add((String) row.get("name"));
            Object avg = row.get("avg_score");
            values.add(avg != null ? avg.toString() : "0");
        }
        Map<String, List<String>> result = new HashMap<>();
        result.put("labels", labels);
        result.put("values", values);
        return result;
    }
}
