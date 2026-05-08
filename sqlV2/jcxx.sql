/*
 Navicat Premium Dump SQL

 Source Server         : db
 Source Server Type    : MySQL
 Source Server Version : 80042 (8.0.42)
 Source Host           : localhost:3306
 Source Schema         : jcxx

 Target Server Type    : MySQL
 Target Server Version : 80042 (8.0.42)
 File Encoding         : 65001

 Date: 15/01/2026 11:47:48
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------

-- ----------------------------
-- Table structure for authority
-- ----------------------------
DROP TABLE IF EXISTS `authority`;
CREATE TABLE `authority`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '权限id（主键）',
  `name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '权限名称',
  `code` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '权限代码',
  `subsystemID` bigint NOT NULL COMMENT '父资源',
  `path` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '资源地址',
  `type` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '资源类型',
  `orderNum` int NOT NULL COMMENT '资源顺序',
  `status` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '资源状态',
  `note` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '资源备注',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `name`(`name` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 71 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of authority
-- ----------------------------
INSERT INTO `authority` VALUES (1, '一张图', 'yzt', 0, '/onemap', NULL, 2, '启用', NULL, '2022-01-18 17:13:59', '2022-01-18 17:14:02');
INSERT INTO `authority` VALUES (2, '实时监测', 'spjk', 0, '/home/monitor', NULL, 3, '启用', NULL, '2022-01-18 17:31:54', '2022-01-18 17:31:56');
INSERT INTO `authority` VALUES (3, '工程巡检', 'gcxj', 0, NULL, NULL, 4, '启用', NULL, '2022-01-18 17:37:10', '2022-01-18 17:37:12');
INSERT INTO `authority` VALUES (4, '预警管理', 'yjgl', 0, NULL, NULL, 5, '启用', NULL, '2022-01-18 17:41:48', '2022-01-18 17:41:52');
INSERT INTO `authority` VALUES (5, '管理信息服务', 'glxx', 0, NULL, NULL, 6, '启用', NULL, '2022-01-18 17:29:01', '2022-01-18 17:29:05');
INSERT INTO `authority` VALUES (6, '工程信息服务', 'gcxx', 0, NULL, NULL, 7, '启用', NULL, '2022-01-18 17:25:48', '2022-01-18 17:25:50');
INSERT INTO `authority` VALUES (7, '系统管理', 'xtgl', 0, NULL, NULL, 8, '启用', NULL, '2022-01-18 17:46:30', '2022-01-18 17:46:32');
INSERT INTO `authority` VALUES (8, '预警信息处理', 'yjgl_yjxx', 4, '/home/prewarninginformation', NULL, 1, '启用', NULL, '2022-01-18 17:38:23', '2022-01-18 17:38:27');
INSERT INTO `authority` VALUES (9, '预警指标设定', 'yjgl_yjsd', 4, '/home/prewarningindicatorsetting', NULL, 2, '启用', NULL, '2022-01-18 17:38:54', '2022-01-18 17:38:57');
INSERT INTO `authority` VALUES (10, '巡检记录', 'gcxj_xcjl', 3, '/home/pollingrecord', NULL, 1, '启用', NULL, '2022-01-18 17:43:08', '2022-01-18 17:43:11');
INSERT INTO `authority` VALUES (12, '维护记录', 'gcxj_whjl', 3, '/home/maintencerecord', NULL, 2, '启用', NULL, '2022-03-04 17:48:16', '2022-03-04 17:48:18');
INSERT INTO `authority` VALUES (13, '管理机构信息', 'glxx_jgxx', 5, '/home/manageoraganization', NULL, 1, '启用', NULL, '2022-01-19 17:04:20', '2022-01-19 17:04:21');
INSERT INTO `authority` VALUES (14, '管理人员信息', 'glxx_ryxx', 5, '/home/manageperson', NULL, 2, '启用', NULL, '2022-01-18 17:30:49', '2022-01-18 17:30:51');
INSERT INTO `authority` VALUES (15, '枢纽供水工程', 'gcxx_sngc', 6, '/home/watersupplyengineering', NULL, 1, '启用', NULL, '2022-01-18 17:45:13', '2022-01-18 17:45:15');
INSERT INTO `authority` VALUES (16, '监测站点', 'gcxx_jczd', 6, '/home/monitorsite', NULL, 2, '启用', NULL, '2022-01-18 17:29:43', '2022-01-18 17:29:44');
INSERT INTO `authority` VALUES (17, '测项信息', 'gcxx_cxxx', 6, '/home/monitoritem', NULL, 3, '启用', NULL, '2024-06-30 19:14:28', '2024-06-30 19:14:31');
INSERT INTO `authority` VALUES (18, '行政区划', 'xtgl_xzqh', 7, '/home/administrativedivision', NULL, 1, '启用', NULL, '2022-03-04 17:56:36', '2022-03-04 17:56:38');
INSERT INTO `authority` VALUES (19, '角色管理', 'xtgl_jsgl', 7, '/home/charactermanage', NULL, 2, '启用', NULL, '2022-01-18 17:48:49', '2022-01-18 17:48:51');
INSERT INTO `authority` VALUES (20, '用户管理', 'xtgl_yhgl', 7, '/home/usermanage', NULL, 3, '启用', NULL, '2022-01-18 17:53:39', '2022-01-18 17:53:42');
INSERT INTO `authority` VALUES (21, '部门管理', 'xtgl_bmgl', 7, '/home/departmentmanage', NULL, 4, '启用', NULL, '2022-01-19 16:56:42', '2022-01-19 16:56:44');
INSERT INTO `authority` VALUES (22, '图片监测', 'spjk_spck', 2, '/home/videocheck', NULL, 3, '启用', NULL, '2022-03-14 15:54:13', '2022-03-14 15:54:14');
INSERT INTO `authority` VALUES (23, '视频配置', 'spjk_sppz', 2, '/home/videoconfiguration', NULL, 4, '启用', NULL, '2022-03-14 15:54:55', '2022-03-14 15:54:57');
INSERT INTO `authority` VALUES (25, '泵站', 'gcxx_jcxx', 6, '/home/pumpstation', NULL, 1, '启用', NULL, '2022-03-24 17:31:08', '2022-03-24 17:31:11');
INSERT INTO `authority` VALUES (26, '分水口', 'gcxx_jcxx', 6, '/home/waterdistributor', NULL, 2, '启用', NULL, '2022-03-24 17:31:49', '2022-03-24 17:31:51');
INSERT INTO `authority` VALUES (27, '水库', 'gcxx_jcxx', 6, '/home/reservoir', NULL, 3, '启用', NULL, '2022-03-24 17:32:57', '2022-03-24 17:32:59');
INSERT INTO `authority` VALUES (28, '水厂', 'gcxx_jcxx', 6, '/home/waterworks', NULL, 4, '启用', NULL, '2022-03-24 17:33:34', '2022-03-24 17:33:36');
INSERT INTO `authority` VALUES (29, '管道', 'gcxx_jcxx', 6, '/home/lines', NULL, 5, '启用', NULL, '2024-08-11 16:15:36', '2024-08-11 16:15:39');
INSERT INTO `authority` VALUES (30, '消毒药材', 'gcxx_jcxx', 6, '/home/herb', NULL, 6, '启用', NULL, '2024-08-13 16:09:02', '2024-08-13 16:09:05');
INSERT INTO `authority` VALUES (31, '浮船', 'gcxx_jcxx', 6, '/home/floatingboat', NULL, 7, '启用', NULL, '2024-08-15 16:43:43', '2024-08-15 16:43:46');
INSERT INTO `authority` VALUES (32, '字典管理', 'xtgl_zdgl', 7, '/home/dictmanage', NULL, 5, '启用', NULL, '2024-09-07 22:24:26', '2024-09-07 22:24:30');
INSERT INTO `authority` VALUES (34, '水位监测', 'spjk_swjc', 2, '/home/waterlevel', NULL, 5, '启用', NULL, '2024-06-16 17:27:09', '2024-06-16 17:27:16');
INSERT INTO `authority` VALUES (35, '流量监测', 'spjk_lljc', 2, '/home/flow', NULL, 6, '启用', NULL, '2024-06-16 17:28:08', '2024-06-16 17:28:11');
INSERT INTO `authority` VALUES (36, '水质监测', 'spjk_szjc', 2, '/home/waterquality', NULL, 7, '启用', NULL, '2024-06-16 17:29:25', '2024-06-16 17:29:28');
INSERT INTO `authority` VALUES (37, '数据人员用户管理', 'xtgl_yhgl', 7, '/home/usermanagedata', NULL, 5, '启用', NULL, '2024-07-07 20:10:25', '2024-07-07 20:10:28');
INSERT INTO `authority` VALUES (38, '只读用户预警信息处理', 'yjgl_yjxx', 4, '/home/prewarninginformationread', NULL, 3, '启用', NULL, '2024-07-07 22:15:33', '2024-07-07 22:15:37');
INSERT INTO `authority` VALUES (39, '只读用户预警指标指定', 'yjgl_yjsd', 4, '/home/prewarningindicatorsettingread', NULL, 4, '启用', NULL, '2024-07-07 22:16:21', '2024-07-07 22:16:24');
INSERT INTO `authority` VALUES (40, '只读用户巡检记录', 'gcxj_xcjl', 3, '/home/pollingrecordread', NULL, 3, '启用', NULL, '2024-07-07 22:33:27', '2024-07-07 22:33:30');
INSERT INTO `authority` VALUES (42, '只读用户维护记录', 'gcxj_whjl', 3, '/home/maintencerecordread', NULL, 4, '启用', NULL, '2024-07-07 22:35:34', '2024-07-07 22:35:37');
INSERT INTO `authority` VALUES (43, '只读用户管理机构信息', 'glxx_jgxx', 5, '/home/manageoraganizationread', NULL, 3, '启用', NULL, '2024-07-07 22:53:04', '2024-07-07 22:53:06');
INSERT INTO `authority` VALUES (44, '只读用户管理人员信息', 'glxx_ryxx', 5, '/home/managepersonread', NULL, 4, '启用', NULL, '2024-07-07 22:54:17', '2024-07-07 22:54:19');
INSERT INTO `authority` VALUES (45, '只读用户枢纽供水工程', 'gcxx_sngc', 6, '/home/watersupplyengineeringread', NULL, 10, '启用', NULL, '2024-07-08 22:38:09', '2024-07-08 22:38:12');
INSERT INTO `authority` VALUES (46, '只读用户监测站点', 'gcxx_jczd', 6, '/home/monitorsiteread', NULL, 11, '启用', NULL, '2024-07-08 22:38:57', '2024-07-08 22:39:03');
INSERT INTO `authority` VALUES (47, '只读用户测项信息', 'gcxx_cxxx', 6, '/home/monitoritemread', NULL, 12, '启用', NULL, '2024-07-08 22:39:36', '2024-07-08 22:39:38');
INSERT INTO `authority` VALUES (48, '只读用户泵站', 'gcxx_jcxx', 6, '/home/pumpstationread', NULL, 13, '启用', NULL, '2024-07-08 22:40:11', '2024-07-08 22:40:14');
INSERT INTO `authority` VALUES (49, '只读用户分水口', 'gcxx_jcxx', 6, '/home/waterdistributorread', NULL, 14, '启用', NULL, '2024-07-08 22:40:49', '2024-07-08 22:40:51');
INSERT INTO `authority` VALUES (50, '只读用户水库', 'gcxx_jcxx', 6, '/home/reservoirread', NULL, 15, '启用', NULL, '2024-07-08 22:41:51', '2024-07-08 22:41:53');
INSERT INTO `authority` VALUES (51, '只读用户水厂', 'gcxx_jcxx', 6, '/home/waterworksread', NULL, 16, '启用', NULL, '2024-07-08 22:42:20', '2024-07-08 22:42:22');
INSERT INTO `authority` VALUES (52, '只读用户管道', 'gcxx_jcxx', 6, '/home/linesread', NULL, 17, '启用', NULL, '2024-08-11 16:16:39', '2024-08-11 16:16:42');
INSERT INTO `authority` VALUES (53, '只读用户消毒药材', 'gcxx_jcxx', 6, '/home/herbread', NULL, 18, '启用', NULL, '2024-08-13 16:09:59', '2024-08-13 16:10:02');
INSERT INTO `authority` VALUES (54, '只读用户浮船', 'gcxx_jcxx', 6, '/home/floatingboatread', NULL, 19, '启用', NULL, '2024-08-15 16:44:39', '2024-08-15 16:44:41');
INSERT INTO `authority` VALUES (55, '只读用户村庄', 'gcxx_jcxx', 6, '/home/townread', NULL, 20, '启用', NULL, '2024-09-27 11:55:54', '2024-09-27 11:55:57');
INSERT INTO `authority` VALUES (57, '只读用户行政区划', 'xtgl_xzgh', 7, '/home/administrativedivisionread', NULL, 6, '启用', NULL, '2024-07-09 09:44:27', '2024-07-09 09:44:30');
INSERT INTO `authority` VALUES (58, '只读用户部门管理', 'xtgl_bmgl', 7, '/home/departmentmanageread', NULL, 7, '启用', NULL, '2024-07-09 09:46:24', '2024-07-09 09:46:27');
INSERT INTO `authority` VALUES (59, '只读用户视频监测', 'spjk_spck', 2, '/home/videocheckread', NULL, 7, '启用', NULL, '2024-07-09 09:53:26', '2024-07-09 09:53:29');
INSERT INTO `authority` VALUES (60, '只读用户视频配置', 'spjk_sppz', 2, '/home/videoconfigurationread', NULL, 8, '启用', NULL, '2024-07-09 09:54:05', '2024-07-09 09:54:10');
INSERT INTO `authority` VALUES (61, '只读用户水位监测', 'spjk_swjc', 2, '/home/waterlevelread', NULL, 9, '启用', NULL, '2024-07-09 09:54:49', '2024-07-09 09:54:52');
INSERT INTO `authority` VALUES (62, '只读用户流量监测', 'spjk_lljc', 2, '/home/flowread', NULL, 10, '启用', NULL, '2024-07-09 09:55:32', '2024-07-09 09:55:36');
INSERT INTO `authority` VALUES (63, '只读用户水质监测', 'spjk_szjc', 2, '/home/waterqualityread', NULL, 11, '启用', NULL, '2024-07-09 09:56:10', '2024-07-09 09:56:13');
INSERT INTO `authority` VALUES (64, '村庄', 'gcxx_jcxx', 6, '/home/town', NULL, 8, '启用', NULL, '2024-09-27 11:54:10', '2024-09-27 11:54:14');
INSERT INTO `authority` VALUES (65, '资源下载', 'zyxz', 0, NULL, NULL, 9, '启用', NULL, '2024-11-20 15:31:18', '2024-11-20 15:31:20');
INSERT INTO `authority` VALUES (66, '巡检app安装', 'zyxz_app', 65, '/home/appdownload', NULL, 1, '启用', NULL, '2024-11-20 15:38:56', '2024-11-20 15:38:59');
INSERT INTO `authority` VALUES (67, '工程简介', 'gcjj', 0, '/projectintro', NULL, 1, '启用', NULL, '2024-12-20 16:30:38', '2024-12-20 16:30:42');
INSERT INTO `authority` VALUES (68, '首页维护', 'sywh', 7, '/home/shouyeweihu', NULL, 8, '启用', NULL, '2024-12-26 13:27:24', '2024-12-26 13:27:26');
INSERT INTO `authority` VALUES (69, '管理站视频监测', 'spjk_spck', 2, '/home/provideocheck', NULL, 1, '启用', NULL, '2025-01-15 15:50:11', '2025-01-15 15:50:14');
INSERT INTO `authority` VALUES (70, '水厂视频监测', 'spjk_spck', 2, '/home/provideocheckwater', NULL, 2, '启用', NULL, '2025-02-27 14:18:30', '2025-02-27 14:18:32');

-- ----------------------------
-- Table structure for department
-- ----------------------------
DROP TABLE IF EXISTS `department`;
CREATE TABLE `department`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '部门id',
  `department_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '部门名称',
  `department_responsibility` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '部门职责',
  `level` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '部门级别',
  `company` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '所属公司',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of department
-- ----------------------------
INSERT INTO `department` VALUES (1, '水务部', '水务管理', '厅级', '鄂北水资源', '2022-02-27 17:25:46', '2022-02-27 17:25:46');
INSERT INTO `department` VALUES (2, '水厂部', '水厂管理', '厅级', '鄂北水资源', '2022-03-04 16:30:07', '2022-03-04 16:30:07');
INSERT INTO `department` VALUES (4, '水资源部', '水资源管理', '县级', '鄂北水资源', '2022-03-09 17:28:34', '2022-03-09 17:29:56');
INSERT INTO `department` VALUES (6, '供水部', '供水', '市级', '鄂北水资源', '2024-01-24 14:53:51', '2024-01-24 14:55:48');

-- ----------------------------
-- Table structure for organization
-- ----------------------------
DROP TABLE IF EXISTS `organization`;
CREATE TABLE `organization`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '组织机构id',
  `organization_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '组织机构名称',
  `organization_code` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '组织机构代码',
  `administrative_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '行政区划名称',
  `organization_abbr` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '机构简称',
  `legal_representative` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '法人代表',
  `agency_specifications` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '机构规格',
  `subordinate_relations` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '隶属关系',
  `institutional_type` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '机构类型',
  `main_function` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '主要职能',
  `approve_content` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '主要审批内容',
  `website` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '网站',
  `email` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `address` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '地址',
  `postal_code` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '邮政编码',
  `office_telephone` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '办公室电话',
  `fax` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '传真',
  `staff_size` int NULL DEFAULT NULL COMMENT '编制人数',
  `whether_reform` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '是否实行水务改革',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `organization_name`(`organization_name` ASC) USING BTREE,
  UNIQUE INDEX `organization_code`(`organization_code` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of organization
-- ----------------------------
INSERT INTO `organization` VALUES (1, '希望村组织部', '421343', '湖北省随州市曾都区万店镇朝阳村', 'XX机构', '张三', '200', '希望村部门', '组织部', '组织希望村设备检查活动', '设备是否合格', 'http://xxxwww.com', 'xxxwww@qq.com', '湖北省随州市曾都区河西镇希望村1号', '421343', '13648965236', '123456789', 200, '是', '2022-03-04 16:24:34', '2025-01-15 11:42:40');

-- ----------------------------
-- Table structure for person
-- ----------------------------
DROP TABLE IF EXISTS `person`;
CREATE TABLE `person`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '人员信息ID',
  `name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '姓名',
  `age` int NULL DEFAULT NULL COMMENT '年龄',
  `gender` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '性别',
  `phone` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '电话',
  `organization` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '所属机构',
  `position` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '职位',
  `duty` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '职责',
  `create_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of person
-- ----------------------------
INSERT INTO `person` VALUES (2, '张三', 28, '男', '17635123456', '希望村组织部', '组长', '进行巡检', '2022-03-04 16:29:03', '2025-02-22 20:23:06');
INSERT INTO `person` VALUES (3, '李四', 30, '女', '13453100000', '希望村组织部', '职工', '水质设备检测', '2022-03-04 16:29:04', '2025-02-22 20:23:16');
INSERT INTO `person` VALUES (10, '王五', 20, '男', '16282659536', '红星村组织部', '职工', '管道检测', '2024-05-28 19:41:29', '2025-02-22 20:23:22');

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色id（主键）',
  `name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '角色名',
  `code` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '角色代码',
  `note` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '角色备注',
  `status` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '角色状态',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  `type` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '角色类型',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `name`(`name` ASC) USING BTREE,
  UNIQUE INDEX `code`(`code` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES (2, '数据维护人员', 'data_maintain', '对系统监各项据进行日常维护和信息管理', '启用', '2022-01-18 16:53:01', '2024-12-26 23:45:26', '数据维护');
INSERT INTO `role` VALUES (3, '只读用户', 'read_only', '仅对系统监测数据进行浏览和上传工程巡检信息', '启用', '2022-01-18 16:54:06', '2024-12-26 23:44:01', '只读');
INSERT INTO `role` VALUES (5, '系统管理员', 'sys', '具备系统最高管理权限，实现系统配置和日常维护等核心功能', '启用', '2022-03-07 16:20:39', '2024-12-26 23:44:27', '管理员');

-- ----------------------------
-- Table structure for role_authority
-- ----------------------------
DROP TABLE IF EXISTS `role_authority`;
CREATE TABLE `role_authority`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色_权限id',
  `role_id` bigint NOT NULL COMMENT '角色id',
  `authority_id` bigint NOT NULL COMMENT '权限id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 231 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of role_authority
-- ----------------------------
INSERT INTO `role_authority` VALUES (1, 5, 1, '2022-03-07 20:11:35', '2022-03-07 20:11:35');
INSERT INTO `role_authority` VALUES (2, 5, 2, '2022-03-07 20:11:35', '2022-03-07 20:11:35');
INSERT INTO `role_authority` VALUES (3, 5, 3, '2022-03-07 20:11:35', '2022-03-07 20:11:35');
INSERT INTO `role_authority` VALUES (4, 5, 4, '2022-03-07 20:11:35', '2022-03-07 20:11:35');
INSERT INTO `role_authority` VALUES (5, 5, 5, '2022-03-07 20:11:35', '2022-03-07 20:11:35');
INSERT INTO `role_authority` VALUES (6, 5, 6, '2022-03-07 20:11:35', '2022-03-07 20:11:35');
INSERT INTO `role_authority` VALUES (7, 5, 7, '2022-03-07 20:11:35', '2022-03-07 20:11:35');
INSERT INTO `role_authority` VALUES (8, 5, 8, '2022-03-07 20:11:35', '2022-03-07 20:11:35');
INSERT INTO `role_authority` VALUES (9, 5, 9, '2022-03-07 20:11:35', '2022-03-07 20:11:35');
INSERT INTO `role_authority` VALUES (10, 5, 10, '2022-03-07 20:11:35', '2022-03-07 20:11:35');
INSERT INTO `role_authority` VALUES (11, 5, 11, '2022-03-07 20:11:35', '2022-03-07 20:11:35');
INSERT INTO `role_authority` VALUES (12, 5, 12, '2022-03-07 20:11:35', '2022-03-07 20:11:35');
INSERT INTO `role_authority` VALUES (13, 5, 13, '2022-03-07 20:11:35', '2022-03-07 20:11:35');
INSERT INTO `role_authority` VALUES (14, 5, 14, '2022-03-07 20:11:35', '2022-03-07 20:11:35');
INSERT INTO `role_authority` VALUES (15, 5, 15, '2022-03-07 20:11:35', '2022-03-07 20:11:35');
INSERT INTO `role_authority` VALUES (16, 5, 16, '2022-03-07 20:11:35', '2022-03-07 20:11:35');
INSERT INTO `role_authority` VALUES (17, 5, 17, '2022-03-07 20:11:35', '2022-03-07 20:11:35');
INSERT INTO `role_authority` VALUES (19, 5, 19, '2022-03-07 20:11:35', '2022-03-07 20:11:35');
INSERT INTO `role_authority` VALUES (20, 5, 20, '2022-03-07 20:11:35', '2022-03-07 20:11:35');
INSERT INTO `role_authority` VALUES (21, 5, 21, '2022-03-07 20:11:35', '2022-03-07 20:11:35');
INSERT INTO `role_authority` VALUES (22, 5, 22, '2022-03-07 20:11:35', '2022-03-07 20:11:35');
INSERT INTO `role_authority` VALUES (24, 5, 24, '2022-03-14 15:57:48', '2022-03-14 15:57:50');
INSERT INTO `role_authority` VALUES (25, 5, 25, '2022-03-14 15:57:56', '2022-03-14 15:57:57');
INSERT INTO `role_authority` VALUES (26, 5, 26, '2022-03-14 15:58:04', '2022-03-14 15:58:05');
INSERT INTO `role_authority` VALUES (27, 5, 27, '2022-03-24 18:53:03', '2022-03-24 18:53:05');
INSERT INTO `role_authority` VALUES (28, 5, 28, '2022-03-24 18:53:11', '2022-03-24 18:53:13');
INSERT INTO `role_authority` VALUES (34, 5, 34, '2022-03-24 18:54:08', '2022-03-24 18:54:09');
INSERT INTO `role_authority` VALUES (35, 5, 35, '2022-03-24 18:54:16', '2022-03-24 18:54:17');
INSERT INTO `role_authority` VALUES (36, 5, 36, '2024-07-03 14:05:13', '2024-07-03 14:05:16');
INSERT INTO `role_authority` VALUES (37, 2, 1, '2024-01-26 17:20:38', '2024-01-26 17:20:38');
INSERT INTO `role_authority` VALUES (38, 2, 2, '2024-01-26 17:20:38', '2024-01-26 17:20:38');
INSERT INTO `role_authority` VALUES (39, 2, 3, '2024-01-26 17:20:38', '2024-01-26 17:20:38');
INSERT INTO `role_authority` VALUES (40, 2, 4, '2024-01-26 17:20:38', '2024-01-26 17:20:38');
INSERT INTO `role_authority` VALUES (41, 2, 5, '2024-01-26 17:20:38', '2024-01-26 17:20:38');
INSERT INTO `role_authority` VALUES (42, 2, 6, '2024-01-26 17:20:38', '2024-01-26 17:20:38');
INSERT INTO `role_authority` VALUES (43, 2, 7, '2024-01-26 17:20:38', '2024-01-26 17:20:38');
INSERT INTO `role_authority` VALUES (44, 2, 8, '2024-01-26 17:20:38', '2024-01-26 17:20:38');
INSERT INTO `role_authority` VALUES (45, 2, 9, '2024-01-26 17:20:38', '2024-01-26 17:20:38');
INSERT INTO `role_authority` VALUES (46, 2, 10, '2024-06-16 17:56:17', '2024-06-16 17:56:22');
INSERT INTO `role_authority` VALUES (47, 2, 11, '2024-07-03 13:08:30', '2024-07-03 13:08:33');
INSERT INTO `role_authority` VALUES (48, 2, 12, '2024-07-03 13:08:42', '2024-07-03 13:08:45');
INSERT INTO `role_authority` VALUES (49, 2, 13, '2024-07-03 13:08:52', '2024-07-03 13:08:56');
INSERT INTO `role_authority` VALUES (50, 2, 14, '2024-07-03 13:09:05', '2024-07-03 13:09:08');
INSERT INTO `role_authority` VALUES (51, 2, 15, '2024-07-03 13:09:23', '2024-07-03 13:09:31');
INSERT INTO `role_authority` VALUES (52, 2, 16, '2024-07-03 13:10:16', '2024-07-03 13:10:20');
INSERT INTO `role_authority` VALUES (53, 2, 17, '2024-07-03 13:10:31', '2024-07-03 13:10:33');
INSERT INTO `role_authority` VALUES (57, 2, 21, '2024-07-03 13:11:26', '2024-07-03 13:11:29');
INSERT INTO `role_authority` VALUES (58, 2, 22, '2024-07-03 13:11:37', '2024-07-03 13:11:39');
INSERT INTO `role_authority` VALUES (151, 2, 24, '2024-07-07 19:11:39', '2024-07-07 19:11:42');
INSERT INTO `role_authority` VALUES (152, 2, 25, '2024-07-07 19:11:51', '2024-07-07 19:11:54');
INSERT INTO `role_authority` VALUES (153, 2, 26, '2024-07-07 19:12:00', '2024-07-07 19:12:05');
INSERT INTO `role_authority` VALUES (154, 2, 27, '2024-07-07 19:12:15', '2024-07-07 19:12:18');
INSERT INTO `role_authority` VALUES (155, 2, 28, '2024-07-07 19:12:29', '2024-07-07 19:12:31');
INSERT INTO `role_authority` VALUES (156, 2, 29, '2024-07-07 19:12:42', '2024-07-07 19:12:44');
INSERT INTO `role_authority` VALUES (157, 2, 30, '2024-07-07 19:12:53', '2024-07-07 19:12:56');
INSERT INTO `role_authority` VALUES (158, 2, 31, '2024-07-07 19:13:06', '2024-07-07 19:13:08');
INSERT INTO `role_authority` VALUES (160, 2, 33, '2024-07-07 19:13:34', '2024-07-07 19:13:38');
INSERT INTO `role_authority` VALUES (161, 2, 34, '2024-07-07 19:14:02', '2024-07-07 19:14:04');
INSERT INTO `role_authority` VALUES (162, 2, 35, '2024-07-07 19:14:15', '2024-07-07 19:14:18');
INSERT INTO `role_authority` VALUES (163, 2, 36, '2024-07-07 19:14:25', '2024-07-07 19:14:28');
INSERT INTO `role_authority` VALUES (166, 2, 37, '2024-07-04 20:25:25', '2024-07-04 20:25:30');
INSERT INTO `role_authority` VALUES (169, 3, 1, '2024-07-07 22:17:14', '2024-07-07 22:17:16');
INSERT INTO `role_authority` VALUES (170, 3, 2, '2024-07-07 22:17:23', '2024-07-07 22:17:26');
INSERT INTO `role_authority` VALUES (171, 3, 3, '2024-07-07 22:17:34', '2024-07-07 22:17:36');
INSERT INTO `role_authority` VALUES (172, 3, 4, '2024-07-07 22:17:43', '2024-07-07 22:17:45');
INSERT INTO `role_authority` VALUES (173, 3, 5, '2024-07-07 22:17:52', '2024-07-07 22:17:55');
INSERT INTO `role_authority` VALUES (174, 3, 6, '2024-07-07 22:18:02', '2024-07-07 22:18:05');
INSERT INTO `role_authority` VALUES (175, 3, 7, '2024-07-07 22:18:12', '2024-07-07 22:18:20');
INSERT INTO `role_authority` VALUES (176, 3, 38, '2024-07-07 22:18:41', '2024-07-07 22:18:44');
INSERT INTO `role_authority` VALUES (177, 3, 39, '2024-07-07 22:18:53', '2024-07-07 22:18:59');
INSERT INTO `role_authority` VALUES (178, 3, 40, '2024-07-07 22:39:21', '2024-07-07 22:39:24');
INSERT INTO `role_authority` VALUES (179, 3, 41, '2024-07-07 22:39:32', '2024-07-07 22:39:34');
INSERT INTO `role_authority` VALUES (180, 3, 42, '2024-07-07 22:39:40', '2024-07-07 22:39:43');
INSERT INTO `role_authority` VALUES (181, 3, 43, '2024-07-07 22:54:32', '2024-07-07 22:54:34');
INSERT INTO `role_authority` VALUES (182, 3, 44, '2024-07-07 22:54:39', '2024-07-07 22:54:42');
INSERT INTO `role_authority` VALUES (183, 3, 45, '2024-07-08 22:47:49', '2024-07-08 22:47:52');
INSERT INTO `role_authority` VALUES (184, 3, 46, '2024-07-08 22:47:58', '2024-07-08 22:48:01');
INSERT INTO `role_authority` VALUES (185, 3, 47, '2024-07-08 22:48:11', '2024-07-08 22:48:15');
INSERT INTO `role_authority` VALUES (186, 3, 48, '2024-07-08 22:48:22', '2024-07-08 22:48:24');
INSERT INTO `role_authority` VALUES (187, 3, 49, '2024-07-08 22:48:32', '2024-07-08 22:48:35');
INSERT INTO `role_authority` VALUES (188, 3, 50, '2024-07-08 22:48:42', '2024-07-08 22:48:44');
INSERT INTO `role_authority` VALUES (189, 3, 51, '2024-07-08 22:48:51', '2024-07-08 22:48:53');
INSERT INTO `role_authority` VALUES (196, 3, 58, '2024-07-09 09:47:05', '2024-07-09 09:47:08');
INSERT INTO `role_authority` VALUES (197, 3, 59, '2024-07-09 09:56:30', '2024-07-09 09:56:32');
INSERT INTO `role_authority` VALUES (199, 3, 61, '2024-07-09 09:56:54', '2024-07-09 09:56:59');
INSERT INTO `role_authority` VALUES (200, 3, 62, '2024-07-09 09:57:06', '2024-07-09 09:57:09');
INSERT INTO `role_authority` VALUES (201, 3, 63, '2024-07-09 09:57:19', '2024-07-09 09:57:25');
INSERT INTO `role_authority` VALUES (202, 5, 29, '2024-08-11 16:17:51', '2024-08-11 16:17:54');
INSERT INTO `role_authority` VALUES (203, 3, 52, '2024-08-11 16:18:22', '2024-08-11 16:18:25');
INSERT INTO `role_authority` VALUES (204, 5, 30, '2024-08-13 16:10:53', '2024-08-13 16:10:57');
INSERT INTO `role_authority` VALUES (205, 3, 53, '2024-08-13 16:11:15', '2024-08-13 16:11:18');
INSERT INTO `role_authority` VALUES (206, 5, 31, '2024-08-15 16:45:08', '2024-08-15 16:45:11');
INSERT INTO `role_authority` VALUES (207, 3, 54, '2024-08-15 16:45:22', '2024-08-15 16:45:26');
INSERT INTO `role_authority` VALUES (208, 5, 32, '2024-09-08 20:04:51', '2024-09-08 20:04:56');
INSERT INTO `role_authority` VALUES (210, 5, 64, '2024-09-27 11:56:53', '2024-09-27 11:56:56');
INSERT INTO `role_authority` VALUES (211, 3, 55, '2024-09-27 11:57:40', '2024-09-27 11:57:43');
INSERT INTO `role_authority` VALUES (212, 2, 64, '2024-09-27 11:58:04', '2024-09-27 11:58:07');
INSERT INTO `role_authority` VALUES (213, 5, 65, '2024-11-20 15:44:20', '2024-11-20 15:44:23');
INSERT INTO `role_authority` VALUES (214, 5, 66, '2024-11-20 15:44:33', '2024-11-20 15:44:36');
INSERT INTO `role_authority` VALUES (215, 2, 65, '2024-11-20 15:44:57', '2024-11-20 15:44:59');
INSERT INTO `role_authority` VALUES (216, 2, 66, '2024-11-20 15:46:13', '2024-11-20 15:46:15');
INSERT INTO `role_authority` VALUES (217, 3, 65, '2024-11-20 15:46:24', '2024-11-20 15:46:27');
INSERT INTO `role_authority` VALUES (218, 3, 66, '2024-11-20 15:46:36', '2024-11-20 15:46:38');
INSERT INTO `role_authority` VALUES (219, 5, 67, '2024-12-20 16:31:03', '2024-12-20 16:31:06');
INSERT INTO `role_authority` VALUES (220, 3, 67, NULL, NULL);
INSERT INTO `role_authority` VALUES (221, 2, 67, NULL, NULL);
INSERT INTO `role_authority` VALUES (222, 5, 68, NULL, NULL);
INSERT INTO `role_authority` VALUES (223, 2, 68, NULL, NULL);
INSERT INTO `role_authority` VALUES (227, 5, 69, NULL, NULL);
INSERT INTO `role_authority` VALUES (228, 5, 70, NULL, NULL);
INSERT INTO `role_authority` VALUES (229, 2, 69, NULL, NULL);
INSERT INTO `role_authority` VALUES (230, 2, 70, NULL, NULL);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `personID` bigint NULL DEFAULT NULL COMMENT '人员id',
  `name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '姓名',
  `type` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '用户类型',
  `username` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '密码',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  `department` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '所属部门',
  `gender` varchar(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '性别',
  `ID_number` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '身份证号码',
  `position` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '岗位',
  `technical_title` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '技术职称',
  `academic_qualifications` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '学历',
  `expiration_time` date NULL DEFAULT NULL COMMENT '失效时间',
  `phone_number` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '手机号码',
  `working_time` date NULL DEFAULT NULL COMMENT '工作时间',
  `graduation_institution` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '毕业院校',
  `major` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '专业',
  `address` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '家庭住址',
  `birthplace` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '出生地',
  `ethnicity` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '民族',
  `email` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '电子邮件',
  `birthday` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '出生年月',
  `political_appearance` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '政治面貌',
  `note` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '备注',
  `user_order` int NULL DEFAULT NULL COMMENT '顺序',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 1, '张三', '系统管理员', 'admin01', '$2a$10$.cSnXJDtWWa/7FiLp81aKOcR3tNsz8QABj.p.lgbXqcUHRbl4dD1e', '2022-01-18 16:46:01', '2025-11-18 23:40:15', '管理部门', '女', '431423198512127036', '管理部门组长', '略', '博士研究生', '2025-12-28', '18627659538', '2024-05-16', NULL, '水利', 'XX省XX市XX县XX镇XX村XX地方', '湖北', '汉族', '777888999@qq.com', '1985-07', '中共党员', NULL, 1);
INSERT INTO `user` VALUES (2, 2, '李四', '数据维护人员', 'data01', '$2a$10$TU17nXCRbnPAqGMTaKiJlerE15PWC/YAVrjFoQYLIkVdE/UVUhPhm', '2022-01-18 16:47:44', '2025-01-08 20:09:26', '数据部门', '男', '431423198612127037', '数据维护职工', '略', '硕士', '2025-12-28', '13216547985', '2024-05-16', NULL, '水利', 'XX省XX市XX县XX镇XX村XX地方', '湖北', '汉族', '777888555@qq.com', '1986/12/12', '中共预备党员', NULL, 2);
INSERT INTO `user` VALUES (3, 3, '王五', '只读用户', 'ord01', '$2a$10$cznxL0OOeMm5yM88.5tky.o1Y8k.ZxpeMwDIswAuzVCXiMRoVZbDq', '2022-01-18 16:47:08', '2024-12-21 14:29:00', '无', '女', '431423198712127046', NULL, '略', '硕士', '2025-12-28', '13216547986', '2024-05-28', NULL, '水利', 'XX省XX市XX县XX镇XX村XX地方', '湖北', '汉族', '777888444@qq.com', '1987/12/12', '党员', NULL, 3);
INSERT INTO `user` VALUES (4, NULL, '只读功能测试', '只读用户', 'test02', '$2a$10$rY2rtvZI2tYIpJ/EX68cr.M3ua7bSsl1CYs2DaA2A71reOI4qaTs6', '2024-12-20 16:22:48', '2025-01-08 20:03:07', NULL, '女', '420118198804251111', NULL, '略', '', NULL, '13397143212', NULL, '', '', '', NULL, NULL, '123456@163.com', '', '群众', '', 4);
INSERT INTO `user` VALUES (5, NULL, '数据维护功能测试', '数据维护人员', 'test01', '$2a$10$zfJBuSZarluZ.lm4Q.dGS..RzG4IGjVK2EZNYuJcj9wbVT4/j46JG', '2024-12-26 23:41:28', '2025-01-08 20:03:13', NULL, '男', '', NULL, '略', '', NULL, '', NULL, '', '', '', NULL, NULL, '', '', '共青团员', '', 5);

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户_角色id（主键）',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `role_id` bigint NOT NULL COMMENT '角色id',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 24 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user_role
-- ----------------------------
INSERT INTO `user_role` VALUES (12, 1, 5, '2024-07-02 23:19:39', '2024-07-02 23:19:39');
INSERT INTO `user_role` VALUES (13, 2, 2, '2024-07-07 19:35:05', '2024-07-07 19:35:05');
INSERT INTO `user_role` VALUES (15, 3, 3, '2024-07-13 20:27:00', '2024-07-13 20:27:00');
INSERT INTO `user_role` VALUES (21, 4, 3, '2025-01-08 20:03:07', '2025-01-08 20:03:07');
INSERT INTO `user_role` VALUES (23, 5, 2, '2025-01-08 20:03:13', '2025-01-08 20:03:13');

SET FOREIGN_KEY_CHECKS = 1;
