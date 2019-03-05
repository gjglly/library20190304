/*
Navicat MySQL Data Transfer

Source Server         : gtlk
Source Server Version : 50628
Source Host           : 59a616c12c7e0.gz.cdb.myqcloud.com:5797
Source Database       : gtlk

Target Server Type    : MYSQL
Target Server Version : 50628
File Encoding         : 65001

Date: 2019-01-30 09:29:17
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for lib_user_info
-- ----------------------------
DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info` (
  `user_id` varchar(64) CHARACTER SET utf8 NOT NULL COMMENT '用户编号',
  `userName` varchar(64) CHARACTER SET utf8 DEFAULT NULL COMMENT '登陆账号',
  `password` varchar(64) CHARACTER SET utf8 DEFAULT NULL COMMENT '登陆密码',
  `person_name` varchar(64) CHARACTER SET utf8 DEFAULT NULL COMMENT '真实姓名',
  `sex` tinyint(4) DEFAULT NULL COMMENT '性别：0：女 ，1：男',
  `address` varchar(200) CHARACTER SET utf8 DEFAULT NULL COMMENT '真实地址',
  `telephone` varchar(20) CHARACTER SET utf8 DEFAULT NULL COMMENT '电话号码',
  `email` varchar(100) CHARACTER SET utf8 DEFAULT NULL COMMENT '电子邮箱',
  `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '注册时间',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
