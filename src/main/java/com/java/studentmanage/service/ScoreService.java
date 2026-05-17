package com.java.studentmanage.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.java.studentmanage.entity.Score;
import com.java.studentmanage.entity.ScoreVo;
import com.java.studentmanage.mapper.ScoreMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ScoreService {

    @Autowired
    private ScoreMapper scoreMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final String BASE_SELECT =
            "SELECT s.id, s.student_id, s.course_id, " +
            "st.name AS student_name, st.student_no, st.class_name, " +
            "c.name AS course_name, c.course_no, c.credit, " +
            "s.score, s.semester, s.exam_type, s.create_time, s.update_time " +
            "FROM score s LEFT JOIN student st ON s.student_id = st.id " +
            "LEFT JOIN course c ON s.course_id = c.id";

    public com.baomidou.mybatisplus.extension.plugins.pagination.Page<ScoreVo> page(int page, int pageSize,
            String studentName, String courseName, String semester, String examType,
            Long studentId, Long courseId) {
        StringBuilder where = new StringBuilder(" WHERE 1=1");
        List<Object> params = new ArrayList<>();
        if (studentName != null && !studentName.isEmpty()) {
            where.append(" AND st.name LIKE ?");
            params.add("%" + studentName + "%");
        }
        if (courseName != null && !courseName.isEmpty()) {
            where.append(" AND c.name LIKE ?");
            params.add("%" + courseName + "%");
        }
        if (semester != null && !semester.isEmpty()) {
            where.append(" AND s.semester = ?");
            params.add(semester);
        }
        if (examType != null && !examType.isEmpty()) {
            where.append(" AND s.exam_type = ?");
            params.add(examType);
        }
        if (studentId != null) {
            where.append(" AND s.student_id = ?");
            params.add(studentId);
        }
        if (courseId != null) {
            where.append(" AND s.course_id = ?");
            params.add(courseId);
        }

        Long total = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM score s LEFT JOIN student st ON s.student_id = st.id LEFT JOIN course c ON s.course_id = c.id" + where,
                params.toArray(), Long.class);
        if (total == null || total == 0) {
            com.baomidou.mybatisplus.extension.plugins.pagination.Page<ScoreVo> empty =
                    new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(page, pageSize);
            empty.setTotal(0);
            return empty;
        }

        int offset = (page - 1) * pageSize;
        String sql = BASE_SELECT + where + " ORDER BY s.id DESC LIMIT ? OFFSET ?";
        params.add(pageSize);
        params.add(offset);

        List<ScoreVo> records = jdbcTemplate.query(sql, params.toArray(),
                new BeanPropertyRowMapper<>(ScoreVo.class));

        com.baomidou.mybatisplus.extension.plugins.pagination.Page<ScoreVo> result =
                new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(page, pageSize);
        result.setTotal(total);
        result.setRecords(records);
        return result;
    }

    public ScoreVo getById(Long id) {
        String sql = BASE_SELECT + " WHERE s.id = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(ScoreVo.class), id);
    }

    public boolean save(Score score) {
        return scoreMapper.insert(score) > 0;
    }

    public boolean update(Score score) {
        return scoreMapper.updateById(score) > 0;
    }

    public boolean delete(Long id) {
        return scoreMapper.deleteById(id) > 0;
    }
}
