/*
Navicat MySQL Data Transfer

Source Server         : local
Source Server Version : 50140
Source Host           : localhost:3306
Source Database       : training

Target Server Type    : MYSQL
Target Server Version : 50140
File Encoding         : 65001

Date: 2013-02-28 09:05:56
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `t_bill`
-- ----------------------------
DROP TABLE IF EXISTS `t_bill`;
CREATE TABLE `t_bill` (
  `billId` int(11) NOT NULL AUTO_INCREMENT,
  `studentId` int(11) NOT NULL,
  `schoolId` int(11) NOT NULL,
  `userId` int(11) NOT NULL,
  `paymentId` int(11) DEFAULT NULL,
  `billType` int(11) NOT NULL,
  `billDate` date DEFAULT NULL,
  `pay` double NOT NULL,
  `favourable` double DEFAULT NULL,
  `payed` double NOT NULL,
  `note` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  `insertDate` date DEFAULT NULL,
  PRIMARY KEY (`billId`),
  KEY `FK_Reference_37` (`studentId`),
  KEY `FK_Reference_38` (`schoolId`),
  KEY `FK_Reference_39` (`userId`),
  KEY `FK_Reference_48` (`paymentId`),
  CONSTRAINT `FK_Reference_37` FOREIGN KEY (`studentId`) REFERENCES `t_student` (`studentId`),
  CONSTRAINT `FK_Reference_38` FOREIGN KEY (`schoolId`) REFERENCES `t_school` (`schoolId`),
  CONSTRAINT `FK_Reference_39` FOREIGN KEY (`userId`) REFERENCES `t_user` (`userId`),
  CONSTRAINT `FK_Reference_48` FOREIGN KEY (`paymentId`) REFERENCES `t_payment` (`paymentId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='消费单类型  0--退费，1--消费\r\n';

-- ----------------------------
-- Records of t_bill
-- ----------------------------

-- ----------------------------
-- Table structure for `t_billdetail`
-- ----------------------------
DROP TABLE IF EXISTS `t_billdetail`;
CREATE TABLE `t_billdetail` (
  `billDetailId` int(11) NOT NULL AUTO_INCREMENT,
  `billId` int(11) DEFAULT NULL,
  `billItemName` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `courseId` int(11) DEFAULT NULL,
  `feeItemId` int(11) DEFAULT NULL,
  `commodityId` int(11) DEFAULT NULL,
  `warehouseId` int(11) DEFAULT NULL,
  `productType` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `price` double DEFAULT NULL,
  `qty` int(11) DEFAULT NULL,
  `discount` double DEFAULT NULL,
  `discountAmount` double DEFAULT NULL,
  `unitName` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  PRIMARY KEY (`billDetailId`),
  KEY `FK_Reference_40` (`billId`),
  KEY `FK_Reference_41` (`courseId`),
  KEY `FK_Reference_42` (`feeItemId`),
  KEY `FK_Reference_43` (`commodityId`),
  KEY `FK_Reference_44` (`warehouseId`),
  CONSTRAINT `FK_Reference_40` FOREIGN KEY (`billId`) REFERENCES `t_bill` (`billId`),
  CONSTRAINT `FK_Reference_41` FOREIGN KEY (`courseId`) REFERENCES `t_course` (`courseId`),
  CONSTRAINT `FK_Reference_42` FOREIGN KEY (`feeItemId`) REFERENCES `t_feeitem` (`feeItemId`),
  CONSTRAINT `FK_Reference_43` FOREIGN KEY (`commodityId`) REFERENCES `t_commodity` (`commodityId`),
  CONSTRAINT `FK_Reference_44` FOREIGN KEY (`warehouseId`) REFERENCES `t_warehouse` (`warehouseId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='收费类型: 1课程，2收费项，3 商品\r\n状态：当收费类型是课程时，用到状态';

-- ----------------------------
-- Records of t_billdetail
-- ----------------------------

-- ----------------------------
-- Table structure for `t_class`
-- ----------------------------
DROP TABLE IF EXISTS `t_class`;
CREATE TABLE `t_class` (
  `classId` int(11) NOT NULL,
  `className` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `courseId` int(11) NOT NULL,
  `teacherId` int(11) NOT NULL,
  `lessons` int(11) DEFAULT NULL,
  `startDate` date NOT NULL,
  `timeRule` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  `classroomId` int(11) NOT NULL,
  `courseProgress` int(11) DEFAULT NULL,
  `planCount` int(11) NOT NULL,
  `stuCount` int(11) DEFAULT NULL,
  `note` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  `schoolId` int(11) NOT NULL,
  `classType` int(11) NOT NULL,
  `endDate` date DEFAULT NULL,
  `arrangeLessons` int(11) DEFAULT NULL,
  `lessonMinute` int(11) DEFAULT NULL,
  `lessonCommission` int(11) DEFAULT NULL,
  `createDate` date NOT NULL,
  `createrId` int(11) NOT NULL,
  PRIMARY KEY (`classId`),
  KEY `FK_Reference_49` (`schoolId`),
  KEY `FK_Reference_50` (`classroomId`),
  KEY `FK_Reference_56` (`courseId`),
  KEY `FK_Reference_57` (`teacherId`),
  CONSTRAINT `FK_Reference_49` FOREIGN KEY (`schoolId`) REFERENCES `t_school` (`schoolId`),
  CONSTRAINT `FK_Reference_50` FOREIGN KEY (`classroomId`) REFERENCES `t_classroom` (`classroomId`),
  CONSTRAINT `FK_Reference_56` FOREIGN KEY (`courseId`) REFERENCES `t_course` (`courseId`),
  CONSTRAINT `FK_Reference_57` FOREIGN KEY (`teacherId`) REFERENCES `t_user` (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of t_class
-- ----------------------------

-- ----------------------------
-- Table structure for `t_classroom`
-- ----------------------------
DROP TABLE IF EXISTS `t_classroom`;
CREATE TABLE `t_classroom` (
  `classroomId` int(11) NOT NULL,
  `classroomName` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `seating` int(11) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  PRIMARY KEY (`classroomId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='状态  0 -- 禁用，1 -- 可用\r\n';

-- ----------------------------
-- Records of t_classroom
-- ----------------------------

-- ----------------------------
-- Table structure for `t_commodity`
-- ----------------------------
DROP TABLE IF EXISTS `t_commodity`;
CREATE TABLE `t_commodity` (
  `commodityId` int(11) NOT NULL AUTO_INCREMENT,
  `commodityTypeId` int(11) DEFAULT NULL,
  `unitId` int(11) DEFAULT NULL,
  `commodityName` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `purchasePrice` double NOT NULL,
  `salePrice` double NOT NULL,
  `size` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `qtyStore` int(11) NOT NULL,
  `status` int(11) NOT NULL,
  `note` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`commodityId`),
  KEY `FK_Reference_12` (`commodityTypeId`),
  KEY `FK_Reference_13` (`unitId`),
  CONSTRAINT `FK_Reference_12` FOREIGN KEY (`commodityTypeId`) REFERENCES `t_commoditytype` (`commodityTypeId`),
  CONSTRAINT `FK_Reference_13` FOREIGN KEY (`unitId`) REFERENCES `t_unit` (`unitId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of t_commodity
-- ----------------------------

-- ----------------------------
-- Table structure for `t_commoditytype`
-- ----------------------------
DROP TABLE IF EXISTS `t_commoditytype`;
CREATE TABLE `t_commoditytype` (
  `commodityTypeId` int(11) NOT NULL AUTO_INCREMENT,
  `commodityTypeName` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `status` int(11) NOT NULL,
  PRIMARY KEY (`commodityTypeId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of t_commoditytype
-- ----------------------------

-- ----------------------------
-- Table structure for `t_course`
-- ----------------------------
DROP TABLE IF EXISTS `t_course`;
CREATE TABLE `t_course` (
  `courseId` int(11) NOT NULL AUTO_INCREMENT,
  `courseTypeId` int(11) DEFAULT NULL,
  `courseName` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `courseUnit` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `unitPrice` double NOT NULL,
  `status` int(11) NOT NULL,
  `note` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`courseId`),
  KEY `FK_Reference_8` (`courseTypeId`),
  CONSTRAINT `FK_Reference_8` FOREIGN KEY (`courseTypeId`) REFERENCES `t_coursetype` (`courseTypeId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of t_course
-- ----------------------------

-- ----------------------------
-- Table structure for `t_coursetype`
-- ----------------------------
DROP TABLE IF EXISTS `t_coursetype`;
CREATE TABLE `t_coursetype` (
  `courseTypeId` int(11) NOT NULL AUTO_INCREMENT,
  `courseTypeName` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`courseTypeId`),
  UNIQUE KEY `courseTypeId` (`courseTypeId`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of t_coursetype
-- ----------------------------

-- ----------------------------
-- Table structure for `t_feeitem`
-- ----------------------------
DROP TABLE IF EXISTS `t_feeitem`;
CREATE TABLE `t_feeitem` (
  `feeItemId` int(11) NOT NULL AUTO_INCREMENT,
  `feeItemName` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `fee` double DEFAULT NULL,
  `status` int(11) NOT NULL,
  PRIMARY KEY (`feeItemId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of t_feeitem
-- ----------------------------

-- ----------------------------
-- Table structure for `t_holiday`
-- ----------------------------
DROP TABLE IF EXISTS `t_holiday`;
CREATE TABLE `t_holiday` (
  `holidayId` int(11) NOT NULL,
  `holidayName` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `startDate` date NOT NULL,
  `endDate` date NOT NULL,
  PRIMARY KEY (`holidayId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of t_holiday
-- ----------------------------

-- ----------------------------
-- Table structure for `t_lessondegree`
-- ----------------------------
DROP TABLE IF EXISTS `t_lessondegree`;
CREATE TABLE `t_lessondegree` (
  `lessonDegreeId` int(11) NOT NULL,
  `subject` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `periodTimeId` int(11) NOT NULL,
  `lessons` int(11) NOT NULL,
  `factCount` int(11) DEFAULT NULL,
  `lateCount` int(11) DEFAULT NULL,
  `advanceCount` int(11) DEFAULT NULL,
  `truancyCount` int(11) DEFAULT NULL,
  `leaveCount` int(11) DEFAULT NULL,
  `lessonType` int(11) DEFAULT NULL,
  `teacherId` int(11) NOT NULL,
  `lessonStatus` int(11) DEFAULT NULL,
  `userId` int(11) DEFAULT NULL,
  `classroomId` int(11) DEFAULT NULL,
  `classId` int(11) DEFAULT NULL,
  `lessonDegreeDate` date NOT NULL,
  `note` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`lessonDegreeId`),
  KEY `FK_Reference_51` (`teacherId`),
  KEY `FK_Reference_52` (`periodTimeId`),
  KEY `FK_Reference_53` (`classroomId`),
  KEY `FK_Reference_54` (`userId`),
  KEY `FK_Reference_55` (`classId`),
  CONSTRAINT `FK_Reference_51` FOREIGN KEY (`teacherId`) REFERENCES `t_user` (`userId`),
  CONSTRAINT `FK_Reference_52` FOREIGN KEY (`periodTimeId`) REFERENCES `t_periodtime` (`periodTimeId`),
  CONSTRAINT `FK_Reference_53` FOREIGN KEY (`classroomId`) REFERENCES `t_classroom` (`classroomId`),
  CONSTRAINT `FK_Reference_54` FOREIGN KEY (`userId`) REFERENCES `t_user` (`userId`),
  CONSTRAINT `FK_Reference_55` FOREIGN KEY (`classId`) REFERENCES `t_class` (`classId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='上课类型  0 -- 补课，1 -- 正常\r\n上课状态  0 -- 未上课 ， 1 -- 已上课';

-- ----------------------------
-- Records of t_lessondegree
-- ----------------------------

-- ----------------------------
-- Table structure for `t_media`
-- ----------------------------
DROP TABLE IF EXISTS `t_media`;
CREATE TABLE `t_media` (
  `mediaId` int(11) NOT NULL AUTO_INCREMENT,
  `mediaName` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `status` int(11) NOT NULL,
  PRIMARY KEY (`mediaId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of t_media
-- ----------------------------

-- ----------------------------
-- Table structure for `t_payment`
-- ----------------------------
DROP TABLE IF EXISTS `t_payment`;
CREATE TABLE `t_payment` (
  `paymentId` int(11) NOT NULL AUTO_INCREMENT,
  `schoolId` int(11) NOT NULL,
  `studentId` int(11) NOT NULL,
  `userId` int(11) NOT NULL,
  `paymentType` int(11) NOT NULL,
  `payway` int(11) NOT NULL,
  `payMoney` double NOT NULL,
  `transactionDate` date DEFAULT NULL,
  `note` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  `creditExpiration` date DEFAULT NULL,
  `insertTime` date DEFAULT NULL,
  PRIMARY KEY (`paymentId`),
  KEY `FK_Reference_45` (`studentId`),
  KEY `FK_Reference_46` (`schoolId`),
  KEY `FK_Reference_47` (`userId`),
  CONSTRAINT `FK_Reference_45` FOREIGN KEY (`studentId`) REFERENCES `t_student` (`studentId`),
  CONSTRAINT `FK_Reference_46` FOREIGN KEY (`schoolId`) REFERENCES `t_school` (`schoolId`),
  CONSTRAINT `FK_Reference_47` FOREIGN KEY (`userId`) REFERENCES `t_user` (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT=' 帐单类型  1--交费  2--退费  3--借款  4--扣除借款  5--业务扣费  6--业务退费\r\n';

-- ----------------------------
-- Records of t_payment
-- ----------------------------

-- ----------------------------
-- Table structure for `t_periodtime`
-- ----------------------------
DROP TABLE IF EXISTS `t_periodtime`;
CREATE TABLE `t_periodtime` (
  `periodTimeId` int(11) NOT NULL,
  `startTime` varchar(10) COLLATE utf8_unicode_ci NOT NULL,
  `endTime` varchar(10) COLLATE utf8_unicode_ci NOT NULL,
  `groupType` int(11) NOT NULL,
  PRIMARY KEY (`periodTimeId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='分组  1 -- 春，2 -- 夏，3 -- 秋，4 -- 冬，0 -- 其它\r\n';

-- ----------------------------
-- Records of t_periodtime
-- ----------------------------

-- ----------------------------
-- Table structure for `t_potcourse`
-- ----------------------------
DROP TABLE IF EXISTS `t_potcourse`;
CREATE TABLE `t_potcourse` (
  `potCourseId` int(11) NOT NULL AUTO_INCREMENT,
  `CourseName` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `status` int(11) NOT NULL,
  PRIMARY KEY (`potCourseId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of t_potcourse
-- ----------------------------

-- ----------------------------
-- Table structure for `t_potential`
-- ----------------------------
DROP TABLE IF EXISTS `t_potential`;
CREATE TABLE `t_potential` (
  `potentialId` int(11) NOT NULL AUTO_INCREMENT,
  `potCourseId` int(11) DEFAULT NULL,
  `mediaId` int(11) DEFAULT NULL,
  `userId` int(11) DEFAULT NULL,
  `potentialStuStatusId` int(11) DEFAULT NULL,
  `schoolId` int(11) DEFAULT NULL,
  `lastReplyUserId` int(11) DEFAULT NULL,
  `potentialName` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `potentialDate` date NOT NULL,
  `sex` int(11) DEFAULT NULL,
  `appellation` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `tel` varchar(15) COLLATE utf8_unicode_ci NOT NULL,
  `mobileTel` varchar(15) COLLATE utf8_unicode_ci NOT NULL,
  `tel1` varchar(15) COLLATE utf8_unicode_ci DEFAULT NULL,
  `QQ` varchar(15) COLLATE utf8_unicode_ci DEFAULT NULL,
  `email` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `timeRule` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `lastReplyDate` date DEFAULT NULL,
  `reCount` int(11) NOT NULL,
  `NextReplyDate` date DEFAULT NULL,
  `note` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  `insertDate` date NOT NULL,
  PRIMARY KEY (`potentialId`),
  KEY `FK_Reference_25` (`potCourseId`),
  KEY `FK_Reference_26` (`mediaId`),
  KEY `FK_Reference_27` (`potentialStuStatusId`),
  KEY `FK_Reference_28` (`userId`),
  KEY `FK_Reference_29` (`schoolId`),
  KEY `FK_Reference_30` (`lastReplyUserId`),
  CONSTRAINT `FK_Reference_25` FOREIGN KEY (`potCourseId`) REFERENCES `t_potcourse` (`potCourseId`),
  CONSTRAINT `FK_Reference_26` FOREIGN KEY (`mediaId`) REFERENCES `t_media` (`mediaId`),
  CONSTRAINT `FK_Reference_27` FOREIGN KEY (`potentialStuStatusId`) REFERENCES `t_potentialstustatus` (`potentialStuStatusId`),
  CONSTRAINT `FK_Reference_28` FOREIGN KEY (`userId`) REFERENCES `t_user` (`userId`),
  CONSTRAINT `FK_Reference_29` FOREIGN KEY (`schoolId`) REFERENCES `t_school` (`schoolId`),
  CONSTRAINT `FK_Reference_30` FOREIGN KEY (`lastReplyUserId`) REFERENCES `t_user` (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of t_potential
-- ----------------------------

-- ----------------------------
-- Table structure for `t_potentialstustatus`
-- ----------------------------
DROP TABLE IF EXISTS `t_potentialstustatus`;
CREATE TABLE `t_potentialstustatus` (
  `potentialStuStatusId` int(11) NOT NULL AUTO_INCREMENT,
  `potentialStuStatusName` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `status` int(11) NOT NULL,
  PRIMARY KEY (`potentialStuStatusId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='状态  0--禁用  1--可用\r\n';

-- ----------------------------
-- Records of t_potentialstustatus
-- ----------------------------

-- ----------------------------
-- Table structure for `t_recrej`
-- ----------------------------
DROP TABLE IF EXISTS `t_recrej`;
CREATE TABLE `t_recrej` (
  `recRejId` int(11) NOT NULL AUTO_INCREMENT,
  `recRejCode` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `recRejType` int(11) NOT NULL,
  `recRejDate` date NOT NULL,
  `qtyTotal` int(11) NOT NULL,
  `amountTotal` int(11) NOT NULL,
  `note` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  `userId` int(11) NOT NULL,
  `supplierId` int(11) NOT NULL,
  PRIMARY KEY (`recRejId`),
  KEY `AK_AK` (`recRejCode`),
  KEY `FK_Reference_10` (`userId`),
  KEY `FK_Reference_11` (`supplierId`),
  CONSTRAINT `FK_Reference_10` FOREIGN KEY (`userId`) REFERENCES `t_user` (`userId`),
  CONSTRAINT `FK_Reference_11` FOREIGN KEY (`supplierId`) REFERENCES `t_supplier` (`supplierId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='类型 -- 进货 1  退货 -1';

-- ----------------------------
-- Records of t_recrej
-- ----------------------------

-- ----------------------------
-- Table structure for `t_recrejdetail`
-- ----------------------------
DROP TABLE IF EXISTS `t_recrejdetail`;
CREATE TABLE `t_recrejdetail` (
  `recRejDetailId` int(11) NOT NULL AUTO_INCREMENT,
  `recRejId` int(11) DEFAULT NULL,
  `commodityId` int(11) DEFAULT NULL,
  `warehouseId` int(11) DEFAULT NULL,
  `price` double DEFAULT NULL,
  `qty` int(11) DEFAULT NULL,
  PRIMARY KEY (`recRejDetailId`),
  KEY `FK_Reference_14` (`recRejId`),
  KEY `FK_Reference_15` (`commodityId`),
  KEY `FK_Reference_16` (`warehouseId`),
  CONSTRAINT `FK_Reference_14` FOREIGN KEY (`recRejId`) REFERENCES `t_recrej` (`recRejId`),
  CONSTRAINT `FK_Reference_15` FOREIGN KEY (`commodityId`) REFERENCES `t_commodity` (`commodityId`),
  CONSTRAINT `FK_Reference_16` FOREIGN KEY (`warehouseId`) REFERENCES `t_warehouse` (`warehouseId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of t_recrejdetail
-- ----------------------------

-- ----------------------------
-- Table structure for `t_reply`
-- ----------------------------
DROP TABLE IF EXISTS `t_reply`;
CREATE TABLE `t_reply` (
  `replyId` int(11) NOT NULL AUTO_INCREMENT,
  `potentialId` int(11) NOT NULL,
  `potentialStuStatusId` int(11) NOT NULL,
  `userId` int(11) NOT NULL,
  `replyDate` date NOT NULL,
  `content` varchar(500) COLLATE utf8_unicode_ci NOT NULL,
  `nextReplyDate` date DEFAULT NULL,
  PRIMARY KEY (`replyId`),
  KEY `FK_Reference_31` (`potentialId`),
  KEY `FK_Reference_32` (`potentialStuStatusId`),
  KEY `FK_Reference_33` (`userId`),
  CONSTRAINT `FK_Reference_31` FOREIGN KEY (`potentialId`) REFERENCES `t_potential` (`potentialId`),
  CONSTRAINT `FK_Reference_32` FOREIGN KEY (`potentialStuStatusId`) REFERENCES `t_potentialstustatus` (`potentialStuStatusId`),
  CONSTRAINT `FK_Reference_33` FOREIGN KEY (`userId`) REFERENCES `t_user` (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of t_reply
-- ----------------------------

-- ----------------------------
-- Table structure for `t_right`
-- ----------------------------
DROP TABLE IF EXISTS `t_right`;
CREATE TABLE `t_right` (
  `rightId` int(11) NOT NULL AUTO_INCREMENT,
  `array` int(11) DEFAULT NULL,
  `isLeaf` bit(1) DEFAULT NULL,
  `rightName` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `rightUrl` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `parentRightId` int(11) DEFAULT NULL,
  PRIMARY KEY (`rightId`),
  UNIQUE KEY `rightId` (`rightId`) USING BTREE,
  KEY `FK25CA65180E7CC3E` (`parentRightId`) USING BTREE,
  CONSTRAINT `t_right_ibfk_1` FOREIGN KEY (`parentRightId`) REFERENCES `t_right` (`rightId`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of t_right
-- ----------------------------
INSERT INTO `t_right` VALUES ('1', '0', '', '系统权限', null, null);
INSERT INTO `t_right` VALUES ('2', '2', '', '系统管理', '', '1');
INSERT INTO `t_right` VALUES ('3', '2', '', '权限管理', 'system/right.do', '2');
INSERT INTO `t_right` VALUES ('4', '4', '', '角色管理', 'system/role.do', '2');
INSERT INTO `t_right` VALUES ('5', '3', '', '用户管理', 'system/user.do', '2');
INSERT INTO `t_right` VALUES ('6', '1', '', '个人信息管理', '', '1');
INSERT INTO `t_right` VALUES ('9', '5', '', '校区管理', 'system/school.do', '2');
INSERT INTO `t_right` VALUES ('10', '1', '', '个人信息', 'self/self.do', '6');
INSERT INTO `t_right` VALUES ('11', '2', '', '修改密码', 'self/modifyPwd.do', '6');
INSERT INTO `t_right` VALUES ('12', '3', '', '基础数据', '', '1');
INSERT INTO `t_right` VALUES ('13', '1', '', '课程类型', 'dict/courseType.do', '12');
INSERT INTO `t_right` VALUES ('14', '5', '', '仓库管理', '', '1');
INSERT INTO `t_right` VALUES ('15', '4', '', '人事管理', '', '1');
INSERT INTO `t_right` VALUES ('16', '6', '', '客服管理', '', '1');
INSERT INTO `t_right` VALUES ('17', '7', '', '教务管理', '', '1');
INSERT INTO `t_right` VALUES ('19', '2', '', '入库管理', '', '14');
INSERT INTO `t_right` VALUES ('20', '3', '', '出库管理', '', '14');
INSERT INTO `t_right` VALUES ('21', '4', '', '商品单位', 'dict/unit.do', '12');
INSERT INTO `t_right` VALUES ('22', '5', '', '仓库', 'dict/warehouse.do', '12');
INSERT INTO `t_right` VALUES ('23', '6', '', '供应商', 'dict/supplier.do', '12');
INSERT INTO `t_right` VALUES ('24', '7', '', '商品', 'dict/commodity.do', '12');
INSERT INTO `t_right` VALUES ('25', '4', '', '商品库存', '', '14');
INSERT INTO `t_right` VALUES ('26', '1', '', '员工管理', '', '15');
INSERT INTO `t_right` VALUES ('27', '1', '', '潜在生源管理', '', '16');
INSERT INTO `t_right` VALUES ('28', '2', '', '我的正式学员', '', '16');
INSERT INTO `t_right` VALUES ('29', '3', '', '学员列表', '', '16');
INSERT INTO `t_right` VALUES ('30', '8', '', '媒体', 'dict/media.do', '12');
INSERT INTO `t_right` VALUES ('31', '9', '', '咨询课程', 'dict/potCourse.do', '12');
INSERT INTO `t_right` VALUES ('32', '10', '', '潜在生源状态', 'dict/potentialStuStatus.do', '12');
INSERT INTO `t_right` VALUES ('33', '11', '', '教室', '', '12');
INSERT INTO `t_right` VALUES ('34', '12', '', '假日设置', '', '12');
INSERT INTO `t_right` VALUES ('35', '13', '', '时间段', '', '12');
INSERT INTO `t_right` VALUES ('36', '1', '', '班级管理', '', '17');
INSERT INTO `t_right` VALUES ('37', '2', '', '课程', 'dict/course.do', '12');
INSERT INTO `t_right` VALUES ('38', '3', '', '收费项', 'dict/feeItem.do', '12');
INSERT INTO `t_right` VALUES ('39', '1', '', '系统配置', '', '2');

-- ----------------------------
-- Table structure for `t_role`
-- ----------------------------
DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role` (
  `roleId` int(11) NOT NULL AUTO_INCREMENT,
  `roleName` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`roleId`),
  UNIQUE KEY `roleId` (`roleId`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of t_role
-- ----------------------------
INSERT INTO `t_role` VALUES ('1', '超级管理员');

-- ----------------------------
-- Table structure for `t_roleright`
-- ----------------------------
DROP TABLE IF EXISTS `t_roleright`;
CREATE TABLE `t_roleright` (
  `rightId` int(11) NOT NULL,
  `roleId` int(11) NOT NULL,
  `state` int(11) DEFAULT NULL,
  PRIMARY KEY (`rightId`,`roleId`),
  KEY `FKD08ECFBC7040828` (`rightId`) USING BTREE,
  KEY `FKD08ECFB300CAC12` (`roleId`) USING BTREE,
  CONSTRAINT `t_roleright_ibfk_1` FOREIGN KEY (`roleId`) REFERENCES `t_role` (`roleId`),
  CONSTRAINT `t_roleright_ibfk_2` FOREIGN KEY (`rightId`) REFERENCES `t_right` (`rightId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of t_roleright
-- ----------------------------
INSERT INTO `t_roleright` VALUES ('1', '1', '1');
INSERT INTO `t_roleright` VALUES ('2', '1', '1');
INSERT INTO `t_roleright` VALUES ('3', '1', '1');
INSERT INTO `t_roleright` VALUES ('4', '1', '1');
INSERT INTO `t_roleright` VALUES ('5', '1', '1');
INSERT INTO `t_roleright` VALUES ('6', '1', '1');
INSERT INTO `t_roleright` VALUES ('9', '1', '1');
INSERT INTO `t_roleright` VALUES ('10', '1', '1');
INSERT INTO `t_roleright` VALUES ('11', '1', '1');
INSERT INTO `t_roleright` VALUES ('12', '1', '1');
INSERT INTO `t_roleright` VALUES ('13', '1', '1');
INSERT INTO `t_roleright` VALUES ('14', '1', '1');
INSERT INTO `t_roleright` VALUES ('15', '1', '1');
INSERT INTO `t_roleright` VALUES ('16', '1', '1');
INSERT INTO `t_roleright` VALUES ('17', '1', '1');
INSERT INTO `t_roleright` VALUES ('19', '1', '1');
INSERT INTO `t_roleright` VALUES ('20', '1', '1');
INSERT INTO `t_roleright` VALUES ('21', '1', '1');
INSERT INTO `t_roleright` VALUES ('22', '1', '1');
INSERT INTO `t_roleright` VALUES ('23', '1', '1');
INSERT INTO `t_roleright` VALUES ('24', '1', '1');
INSERT INTO `t_roleright` VALUES ('25', '1', '1');
INSERT INTO `t_roleright` VALUES ('26', '1', '1');
INSERT INTO `t_roleright` VALUES ('27', '1', '1');
INSERT INTO `t_roleright` VALUES ('28', '1', '1');
INSERT INTO `t_roleright` VALUES ('29', '1', '1');
INSERT INTO `t_roleright` VALUES ('30', '1', '1');
INSERT INTO `t_roleright` VALUES ('31', '1', '1');
INSERT INTO `t_roleright` VALUES ('32', '1', '1');
INSERT INTO `t_roleright` VALUES ('33', '1', '1');
INSERT INTO `t_roleright` VALUES ('34', '1', '1');
INSERT INTO `t_roleright` VALUES ('35', '1', '1');
INSERT INTO `t_roleright` VALUES ('36', '1', '1');
INSERT INTO `t_roleright` VALUES ('37', '1', '1');
INSERT INTO `t_roleright` VALUES ('38', '1', '1');
INSERT INTO `t_roleright` VALUES ('39', '1', '1');

-- ----------------------------
-- Table structure for `t_school`
-- ----------------------------
DROP TABLE IF EXISTS `t_school`;
CREATE TABLE `t_school` (
  `schoolId` int(11) NOT NULL AUTO_INCREMENT,
  `address` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `array` int(11) NOT NULL,
  `isLeaf` bit(1) DEFAULT NULL,
  `schoolCode` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `schoolName` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `status` int(11) NOT NULL,
  `tel` varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL,
  `parentSchoolId` int(11) DEFAULT NULL,
  PRIMARY KEY (`schoolId`),
  UNIQUE KEY `schoolId` (`schoolId`) USING BTREE,
  KEY `FK4A98FDDFCA03A278` (`parentSchoolId`) USING BTREE,
  CONSTRAINT `t_school_ibfk_1` FOREIGN KEY (`parentSchoolId`) REFERENCES `t_school` (`schoolId`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of t_school
-- ----------------------------
INSERT INTO `t_school` VALUES ('1', null, '1', '', '', '校区总部', '1', null, null);

-- ----------------------------
-- Table structure for `t_store`
-- ----------------------------
DROP TABLE IF EXISTS `t_store`;
CREATE TABLE `t_store` (
  `storeId` int(11) NOT NULL AUTO_INCREMENT,
  `commodityId` int(11) DEFAULT NULL,
  `warehouseId` int(11) DEFAULT NULL,
  `qtyStore` int(11) DEFAULT NULL,
  PRIMARY KEY (`storeId`),
  KEY `FK_Reference_17` (`commodityId`),
  KEY `FK_Reference_18` (`warehouseId`),
  CONSTRAINT `FK_Reference_17` FOREIGN KEY (`commodityId`) REFERENCES `t_commodity` (`commodityId`),
  CONSTRAINT `FK_Reference_18` FOREIGN KEY (`warehouseId`) REFERENCES `t_warehouse` (`warehouseId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of t_store
-- ----------------------------

-- ----------------------------
-- Table structure for `t_stuclass`
-- ----------------------------
DROP TABLE IF EXISTS `t_stuclass`;
CREATE TABLE `t_stuclass` (
  `stuClassId` int(11) NOT NULL,
  `studentId` int(11) NOT NULL,
  `billDetailId` int(11) NOT NULL,
  `classId` int(11) NOT NULL,
  `lessonSchoolId` int(11) NOT NULL,
  `selectSchoolId` int(11) NOT NULL,
  `userId` int(11) NOT NULL,
  `selectDate` date NOT NULL,
  `lessons` int(11) DEFAULT NULL,
  `courseProgress` int(11) DEFAULT NULL,
  `scStatus` int(11) DEFAULT NULL,
  `continueReg` int(11) NOT NULL,
  `score` double DEFAULT NULL,
  `opinionDate` date DEFAULT NULL,
  `opinion` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  `grade` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL,
  `factCount` int(11) DEFAULT NULL,
  `lateCount` int(11) DEFAULT NULL,
  `advanceCount` int(11) DEFAULT NULL,
  `truancyCount` int(11) DEFAULT NULL,
  `leaveCount` int(11) DEFAULT NULL,
  `fillCount` int(11) DEFAULT NULL,
  `note` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  `insertTime` date DEFAULT NULL,
  PRIMARY KEY (`stuClassId`),
  KEY `FK_Reference_58` (`studentId`),
  KEY `FK_Reference_59` (`billDetailId`),
  KEY `FK_Reference_60` (`classId`),
  KEY `FK_Reference_61` (`lessonSchoolId`),
  KEY `FK_Reference_62` (`selectSchoolId`),
  KEY `FK_Reference_63` (`userId`),
  CONSTRAINT `FK_Reference_58` FOREIGN KEY (`studentId`) REFERENCES `t_student` (`studentId`),
  CONSTRAINT `FK_Reference_59` FOREIGN KEY (`billDetailId`) REFERENCES `t_billdetail` (`billDetailId`),
  CONSTRAINT `FK_Reference_60` FOREIGN KEY (`classId`) REFERENCES `t_class` (`classId`),
  CONSTRAINT `FK_Reference_61` FOREIGN KEY (`lessonSchoolId`) REFERENCES `t_school` (`schoolId`),
  CONSTRAINT `FK_Reference_62` FOREIGN KEY (`selectSchoolId`) REFERENCES `t_school` (`schoolId`),
  CONSTRAINT `FK_Reference_63` FOREIGN KEY (`userId`) REFERENCES `t_user` (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='选班状态  1 -- 正常，2 -- 插班，3 -- 转班，4 -- 休学，5 -- 退学，6 --弃学\r\n\r\n';

-- ----------------------------
-- Records of t_stuclass
-- ----------------------------

-- ----------------------------
-- Table structure for `t_student`
-- ----------------------------
DROP TABLE IF EXISTS `t_student`;
CREATE TABLE `t_student` (
  `studentId` int(11) NOT NULL AUTO_INCREMENT,
  `schoolId` int(11) DEFAULT NULL,
  `mediaId` int(11) DEFAULT NULL,
  `userId` int(11) DEFAULT NULL,
  `studentName` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `sex` int(11) DEFAULT NULL,
  `studentType` int(11) NOT NULL,
  `birthday` date DEFAULT NULL,
  `password` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `enrollDate` date NOT NULL,
  `tel` varchar(15) COLLATE utf8_unicode_ci NOT NULL,
  `mobileTel` varchar(15) COLLATE utf8_unicode_ci NOT NULL,
  `tel1` varchar(15) COLLATE utf8_unicode_ci DEFAULT NULL,
  `QQ` varchar(15) COLLATE utf8_unicode_ci DEFAULT NULL,
  `email` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `address` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `postCode` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `IDcard` varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL,
  `diploma` varchar(0) COLLATE utf8_unicode_ci DEFAULT NULL,
  `NextReplyDate` date DEFAULT NULL,
  `note` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  `consumedMoney` double DEFAULT NULL,
  `insertDate` date NOT NULL,
  `billCount` int(11) DEFAULT NULL,
  `arrearageMoney` double DEFAULT NULL,
  `availableMoney` double DEFAULT NULL,
  `creditExpiration` date DEFAULT NULL,
  `creditRemark` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`studentId`),
  KEY `FK_Reference_34` (`schoolId`),
  KEY `FK_Reference_35` (`mediaId`),
  KEY `FK_Reference_36` (`userId`),
  CONSTRAINT `FK_Reference_34` FOREIGN KEY (`schoolId`) REFERENCES `t_school` (`schoolId`),
  CONSTRAINT `FK_Reference_35` FOREIGN KEY (`mediaId`) REFERENCES `t_media` (`mediaId`),
  CONSTRAINT `FK_Reference_36` FOREIGN KEY (`userId`) REFERENCES `t_user` (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='学员类型 0--学生  1--上班族\r\n';

-- ----------------------------
-- Records of t_student
-- ----------------------------

-- ----------------------------
-- Table structure for `t_supplier`
-- ----------------------------
DROP TABLE IF EXISTS `t_supplier`;
CREATE TABLE `t_supplier` (
  `supplierId` int(11) NOT NULL AUTO_INCREMENT,
  `supplierName` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `linkMan` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `tel` varchar(15) COLLATE utf8_unicode_ci DEFAULT NULL,
  `address` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `status` int(11) NOT NULL,
  PRIMARY KEY (`supplierId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of t_supplier
-- ----------------------------

-- ----------------------------
-- Table structure for `t_systemconfig`
-- ----------------------------
DROP TABLE IF EXISTS `t_systemconfig`;
CREATE TABLE `t_systemconfig` (
  `systemConfigId` int(11) NOT NULL AUTO_INCREMENT,
  `systemName` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `classType` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`systemConfigId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='班级模式：\r\n1 --- 一对多模式（以"学期“为单位）\r\n2 --- 一对多模式（以”课时“为单位';

-- ----------------------------
-- Records of t_systemconfig
-- ----------------------------

-- ----------------------------
-- Table structure for `t_unit`
-- ----------------------------
DROP TABLE IF EXISTS `t_unit`;
CREATE TABLE `t_unit` (
  `unitId` int(11) NOT NULL AUTO_INCREMENT,
  `unitName` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `status` int(11) NOT NULL,
  PRIMARY KEY (`unitId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of t_unit
-- ----------------------------

-- ----------------------------
-- Table structure for `t_usecommodity`
-- ----------------------------
DROP TABLE IF EXISTS `t_usecommodity`;
CREATE TABLE `t_usecommodity` (
  `useCommodityId` int(11) NOT NULL AUTO_INCREMENT,
  `userId` int(11) NOT NULL,
  `useDate` date NOT NULL,
  `qtyTotal` int(11) NOT NULL,
  `note` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  `handlerId` int(11) NOT NULL,
  PRIMARY KEY (`useCommodityId`),
  KEY `FK_Reference_20` (`handlerId`),
  KEY `FK_Reference_21` (`userId`),
  CONSTRAINT `FK_Reference_20` FOREIGN KEY (`handlerId`) REFERENCES `t_user` (`userId`),
  CONSTRAINT `FK_Reference_21` FOREIGN KEY (`userId`) REFERENCES `t_user` (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of t_usecommodity
-- ----------------------------

-- ----------------------------
-- Table structure for `t_usecommoditydetail`
-- ----------------------------
DROP TABLE IF EXISTS `t_usecommoditydetail`;
CREATE TABLE `t_usecommoditydetail` (
  `useCommodityDetailId` int(11) NOT NULL AUTO_INCREMENT,
  `useCommodityId` int(11) NOT NULL,
  `commodityId` int(11) NOT NULL,
  `warehouseId` int(11) NOT NULL,
  `qty` int(11) NOT NULL,
  `isNeedReturn` int(11) NOT NULL,
  `returnDate` date DEFAULT NULL,
  `returnStatus` int(11) DEFAULT NULL,
  PRIMARY KEY (`useCommodityDetailId`),
  KEY `FK_Reference_22` (`useCommodityId`),
  KEY `FK_Reference_23` (`commodityId`),
  KEY `FK_Reference_24` (`warehouseId`),
  CONSTRAINT `FK_Reference_22` FOREIGN KEY (`useCommodityId`) REFERENCES `t_usecommodity` (`useCommodityId`),
  CONSTRAINT `FK_Reference_23` FOREIGN KEY (`commodityId`) REFERENCES `t_commodity` (`commodityId`),
  CONSTRAINT `FK_Reference_24` FOREIGN KEY (`warehouseId`) REFERENCES `t_warehouse` (`warehouseId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='是否需要归还 1 -- 需要归还 0 --- 不需要归还\r\n归还状态 1 -- 未还 0 -- 已还';

-- ----------------------------
-- Records of t_usecommoditydetail
-- ----------------------------

-- ----------------------------
-- Table structure for `t_user`
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `userId` int(11) NOT NULL AUTO_INCREMENT,
  `schoolId` int(11) NOT NULL,
  `userCode` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `userName` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `userPwd` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `sex` int(11) NOT NULL,
  `birthday` date NOT NULL,
  `isPartTime` int(11) NOT NULL,
  `IsTeacher` int(11) NOT NULL,
  `comeDate` date NOT NULL,
  `outDate` date DEFAULT NULL,
  `course` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `finishSchool` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `diploma` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `resume` varchar(1000) COLLATE utf8_unicode_ci DEFAULT NULL,
  `tel` varchar(15) COLLATE utf8_unicode_ci DEFAULT NULL,
  `email` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `address` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `postCode` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL,
  `headship` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `IDcard` varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL,
  `basePay` double DEFAULT NULL,
  `hourFee` double DEFAULT NULL,
  `status` int(11) NOT NULL,
  PRIMARY KEY (`userId`),
  KEY `AK_UQ_USERCODE_T_USER` (`userCode`),
  KEY `FK_Reference_7` (`schoolId`),
  CONSTRAINT `FK_Reference_7` FOREIGN KEY (`schoolId`) REFERENCES `t_school` (`schoolId`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='系统用户表，员工表\r\n性别--- 0女，1男\r\n是否兼职。（0否，1是）\r\n是否授课。（0';

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES ('1', '1', 'admin', '超级管理员', '21232f297a57a5a743894a0e4a801fc3', '1', '2013-02-27', '0', '0', '2013-02-27', null, null, null, null, null, null, null, null, null, null, null, null, null, '1');

-- ----------------------------
-- Table structure for `t_userrole`
-- ----------------------------
DROP TABLE IF EXISTS `t_userrole`;
CREATE TABLE `t_userrole` (
  `userId` int(11) NOT NULL,
  `roleId` int(11) NOT NULL,
  PRIMARY KEY (`userId`,`roleId`),
  KEY `FK_Reference_4` (`roleId`),
  CONSTRAINT `FK_Reference_4` FOREIGN KEY (`roleId`) REFERENCES `t_role` (`roleId`),
  CONSTRAINT `FK_Reference_5` FOREIGN KEY (`userId`) REFERENCES `t_user` (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of t_userrole
-- ----------------------------
INSERT INTO `t_userrole` VALUES ('1', '1');

-- ----------------------------
-- Table structure for `t_warehouse`
-- ----------------------------
DROP TABLE IF EXISTS `t_warehouse`;
CREATE TABLE `t_warehouse` (
  `warehouseId` int(11) NOT NULL AUTO_INCREMENT,
  `warehouseName` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `tel` varchar(15) COLLATE utf8_unicode_ci DEFAULT NULL,
  `address` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `schoolId` int(11) NOT NULL,
  `status` int(11) NOT NULL,
  PRIMARY KEY (`warehouseId`),
  KEY `FK_Reference_9` (`schoolId`),
  CONSTRAINT `FK_Reference_9` FOREIGN KEY (`schoolId`) REFERENCES `t_school` (`schoolId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of t_warehouse
-- ----------------------------
