-- 创建数据库
CREATE DATABASE IF NOT EXISTS student_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE student_db;

-- 用户表
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
    `id`        BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `username`  VARCHAR(50)  NOT NULL UNIQUE,
    `password`  VARCHAR(100) NOT NULL,
    `real_name` VARCHAR(50)  NOT NULL,
    `email`     VARCHAR(100) NOT NULL,
    `create_time` DATETIME   DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 学生表
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student` (
    `id`          BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `student_no`  VARCHAR(20)  NOT NULL UNIQUE COMMENT '学号',
    `name`        VARCHAR(50)  NOT NULL COMMENT '姓名',
    `gender`      VARCHAR(4)   NOT NULL COMMENT '性别',
    `age`         INT                   COMMENT '年龄',
    `class_name`  VARCHAR(50)  NOT NULL COMMENT '班级',
    `phone`       VARCHAR(20)           COMMENT '手机号',
    `email`       VARCHAR(100)          COMMENT '邮箱',
    `address`     VARCHAR(200)          COMMENT '住址',
    `create_time` DATETIME     DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 插入默认管理员（密码: 123456）
INSERT INTO `user` (`username`, `password`, `real_name`, `email`)
VALUES ('admin', '123456', '管理员', 'admin@example.com');

-- 插入示例学生数据
INSERT INTO `student` (`student_no`, `name`, `gender`, `age`, `class_name`, `phone`, `email`, `address`, `create_time`) VALUES
('2024001', '张三', '男', 20, '计算机一班', '13800138001', 'zhangsan@example.com', '北京市海淀区', '2025-01-15 10:00:00'),
('2024002', '李四', '女', 19, '计算机一班', '13800138002', 'lisi@example.com', '上海市浦东新区', '2025-02-20 10:00:00'),
('2024003', '王五', '男', 21, '计算机二班', '13800138003', 'wangwu@example.com', '广州市天河区', '2025-03-10 10:00:00'),
('2024004', '赵六', '女', 20, '计算机二班', '13800138004', 'zhaoliu@example.com', '深圳市南山区', '2025-04-05 10:00:00'),
('2024005', '孙七', '男', 22, '软件工程一班', '13800138005', 'sunqi@example.com', '杭州市西湖区', '2025-05-12 10:00:00');

-- 课程表
DROP TABLE IF EXISTS `course`;
CREATE TABLE `course` (
    `id`           BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `course_no`    VARCHAR(20)  NOT NULL UNIQUE COMMENT '课程编号',
    `name`         VARCHAR(100) NOT NULL COMMENT '课程名称',
    `credit`       DECIMAL(3,1) NOT NULL DEFAULT 2.0 COMMENT '学分',
    `teacher`      VARCHAR(50)  NOT NULL COMMENT '授课教师',
    `classroom`    VARCHAR(100)          COMMENT '上课地点',
    `max_students` INT                   COMMENT '选课容量',
    `description`  TEXT                  COMMENT '课程描述',
    `create_time`  DATETIME     DEFAULT CURRENT_TIMESTAMP,
    `update_time`  DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 成绩表
DROP TABLE IF EXISTS `score`;
CREATE TABLE `score` (
    `id`          BIGINT  NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `student_id`  BIGINT  NOT NULL COMMENT '关联学生',
    `course_id`   BIGINT  NOT NULL COMMENT '关联课程',
    `score`       INT              COMMENT '成绩 0-100',
    `semester`    VARCHAR(20)      COMMENT '学期',
    `exam_type`   VARCHAR(10)      COMMENT '考试类型(期中/期末/补考)',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`student_id`) REFERENCES `student`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`course_id`) REFERENCES `course`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 插入示例课程
INSERT INTO `course` (`course_no`, `name`, `credit`, `teacher`, `classroom`, `max_students`) VALUES
('C001', 'Java程序设计', 4.0, '张教授', '教学楼301', 60),
('C002', '数据库原理', 3.0, '李教授', '教学楼302', 55),
('C003', 'Web开发技术', 3.5, '王教授', '实验楼201', 50),
('C004', '数据结构', 4.0, '赵教授', '教学楼303', 65),
('C005', '操作系统', 3.0, '孙教授', '教学楼304', 60);

-- 插入示例成绩
INSERT INTO `score` (`student_id`, `course_id`, `score`, `semester`, `exam_type`) VALUES
(1, 1, 85, '2024-2025-1', '期末'),
(1, 2, 78, '2024-2025-1', '期末'),
(2, 1, 92, '2024-2025-1', '期末'),
(2, 3, 88, '2024-2025-1', '期末'),
(3, 2, 65, '2024-2025-1', '期末'),
(3, 4, 72, '2024-2025-1', '期末'),
(4, 1, 58, '2024-2025-1', '期末'),
(4, 5, 82, '2024-2025-1', '期末'),
(5, 3, 95, '2024-2025-1', '期末'),
(5, 4, 90, '2024-2025-1', '期末');
