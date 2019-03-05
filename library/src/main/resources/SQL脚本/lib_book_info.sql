/*
Navicat MySQL Data Transfer

Source Server         : gtlk
Source Server Version : 50628
Source Host           : 59a616c12c7e0.gz.cdb.myqcloud.com:5797
Source Database       : gtlk

Target Server Type    : MYSQL
Target Server Version : 50628
File Encoding         : 65001

Date: 2019-01-30 09:56:54
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for lib_book_info
-- ----------------------------
DROP TABLE IF EXISTS `book_info`;
CREATE TABLE `book_info` (
  `book_id` varchar(64) CHARACTER SET utf8 NOT NULL COMMENT '图书编号',
  `author` varchar(200) CHARACTER SET utf8 DEFAULT NULL COMMENT '作者',
  `publisher` varchar(100) CHARACTER SET utf8 DEFAULT NULL COMMENT '出版社',
  `book_name` varchar(64) CHARACTER SET utf8 DEFAULT NULL COMMENT '图书名称',
  `type` tinyint(4) DEFAULT NULL COMMENT '图书类型',
  `publish_time` datetime DEFAULT NULL COMMENT '发布时间',
  `summary` varchar(1024) CHARACTER SET utf8 DEFAULT NULL COMMENT '图书介绍',
  `publish_count` int(11) DEFAULT NULL COMMENT '出版数量',
  `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `sale_count` int(11) DEFAULT NULL COMMENT '销售数量',
  `cover_img` varchar(200) DEFAULT NULL,
  `comment_count` int(11) DEFAULT NULL COMMENT '评论次数',
  `collect_count` int(11) DEFAULT NULL COMMENT '收藏次数',
  `view_count` int(11) DEFAULT NULL COMMENT '查看次数',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `last_editor` varchar(64) CHARACTER SET utf8 DEFAULT NULL COMMENT '最后更新的用户编号',
  `ISBN` varchar(13) CHARACTER SET utf8 DEFAULT NULL COMMENT '国籍标准图书编码',
  PRIMARY KEY (`book_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
