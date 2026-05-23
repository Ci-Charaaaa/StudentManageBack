CREATE TABLE IF NOT EXISTS `user` (
    `id`          BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `username`    VARCHAR(50)  NOT NULL UNIQUE,
    `password`    VARCHAR(100) NOT NULL,
    `real_name`   VARCHAR(50)  NOT NULL,
    `email`       VARCHAR(100) NOT NULL,
    `create_time` DATETIME     DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `student` (
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

CREATE TABLE IF NOT EXISTS `course` (
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

CREATE TABLE IF NOT EXISTS `score` (
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
