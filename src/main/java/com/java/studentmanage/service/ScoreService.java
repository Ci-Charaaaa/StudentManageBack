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

    //用于单表操作，比如增删改
    @Autowired
    private ScoreMapper scoreMapper;
    //这里用JdbcTemplate进行联表查询，因为baseMapper只能单表操作
    @Autowired
    private JdbcTemplate jdbcTemplate;

    //BASE_SELECT是联表查询的基础SQL
    private final String BASE_SELECT =
            "SELECT s.id, s.student_id, s.course_id, " +

             //分别从学生表和课程表拿data
            "st.name AS student_name, st.student_no, st.class_name, " +
            "c.name AS course_name, c.course_no, c.credit, " +

            "s.score, s.semester, s.exam_type, s.create_time, s.update_time " +
            "FROM score s LEFT JOIN student st ON s.student_id = st.id " +
            "LEFT JOIN course c ON s.course_id = c.id";

    //三表的分页查询
    public com.baomidou.mybatisplus.extension.plugins.pagination.Page<ScoreVo> page(int page, int pageSize,
            String studentName, String courseName, String semester, String examType,
            Long studentId, Long courseId) {

        //一个恒成立的条件，方便后面直接AND xxx拼接，不用判断是不是第一个条件
        StringBuilder where = new StringBuilder(" WHERE 1=1");
        //存储参数值，用于预编译，防止SQL注入
        List<Object> params = new ArrayList<>();

        //挨个进行判断，符合就拼接进去，最终返回一个完整的where
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

        //查总数据条数
        Long total = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM score s LEFT JOIN student st ON s.student_id = st.id LEFT JOIN course c ON s.course_id = c.id" + where,
                params.toArray(), Long.class);
        //如果为0/null,直接返回空
        if (total == null || total == 0) {
            com.baomidou.mybatisplus.extension.plugins.pagination.Page<ScoreVo> empty =
                    new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(page, pageSize);
            empty.setTotal(0);
            return empty;
        }

        //前面都过了，开始查询
        int offset = (page - 1) * pageSize;
        //这里把基础的和where相加，最后合并成需要的sql数据
        String sql = BASE_SELECT + where + " ORDER BY s.id DESC LIMIT ? OFFSET ?";
        //pageSize取多少条
        params.add(pageSize);
        //offset是跳过多少条
        params.add(offset);
        //映射成视图实例
        List<ScoreVo> records = jdbcTemplate.query(sql, params.toArray(),
                new BeanPropertyRowMapper<>(ScoreVo.class));
        //封装成前端要的视图数据返回
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
