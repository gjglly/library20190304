/*
Navicat MySQL Data Transfer

Source Server         : gtlkFast
Source Server Version : 50622
Source Host           : 193.112.51.199:3306
Source Database       : gtlk_sit_test

Target Server Type    : MYSQL
Target Server Version : 50622
File Encoding         : 65001

Date: 2019-01-30 10:08:49
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for lib_book_comment
-- ----------------------------
DROP TABLE IF EXISTS `book_comment`;
CREATE TABLE `book_comment` (
  `COMMENT_ID` varchar(64) NOT NULL COMMENT '评论编号',
  `BOOK_ID` varchar(64) DEFAULT NULL COMMENT '图书编号',
  `CONTENT` varchar(1024) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '评论内容',
  `user_id` varchar(64) DEFAULT NULL COMMENT '评论人编号',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '评论时间',
  `status` varchar(8) DEFAULT NULL COMMENT '评论状态，0：正常，1：删除',
  `person_name` varchar(200) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '真实姓名',
  `head_pic` varchar(200) DEFAULT NULL COMMENT '用户头像',
  PRIMARY KEY (`COMMENT_ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='活动评论表';
