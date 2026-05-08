/*
 Navicat Premium Dump SQL

 Source Server         : 1
 Source Server Type    : MySQL
 Source Server Version : 80042 (8.0.42)
 Source Host           : localhost:3306
 Source Schema         : gcdd

 Target Server Type    : MySQL
 Target Server Version : 80042 (8.0.42)
 File Encoding         : 65001

 Date: 18/01/2026 17:53:59
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------

-- ----------------------------
-- Table structure for dict
-- ----------------------------
DROP TABLE IF EXISTS `dict`;
CREATE TABLE `dict`  (
  `dict_id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典名称',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`dict_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 45 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of dict
-- ----------------------------
INSERT INTO `dict` VALUES (6, '摄像头类型', '实时监测_设备配置_摄像头类型', '2024-09-10 19:09:42', '2025-01-15 19:00:28');
INSERT INTO `dict` VALUES (11, '摄像头地点', '实时监测_设备配置_摄像头地点', '2024-09-10 21:41:29', '2025-01-15 19:00:20');
INSERT INTO `dict` VALUES (13, '工程站点', '一张图_工程站点', '2024-09-11 10:10:39', '2024-09-11 19:01:24');
INSERT INTO `dict` VALUES (14, '水位监测点', '实时监测_水位监测_监测站点', '2024-09-11 10:47:29', '2024-12-23 16:09:04');
INSERT INTO `dict` VALUES (15, '雨量监测点', '实时监测_雨量监测_监测站点', '2024-09-11 10:48:20', '2025-06-18 16:18:39');
INSERT INTO `dict` VALUES (17, '巡检站点', '工程巡检_巡检记录_巡检站点', '2024-09-11 11:03:46', '2024-09-11 19:02:25');
INSERT INTO `dict` VALUES (18, '巡检类型', '工程巡检_巡检记录_巡检类型', '2024-09-10 15:28:18', '2024-09-11 19:02:45');
INSERT INTO `dict` VALUES (19, '预警等级', '预警管理_预警信息_预警等级', '2024-09-09 09:51:50', '2024-09-11 19:13:43');
INSERT INTO `dict` VALUES (20, '预警类型', '预警管理_预警信息_预警类型', '2024-09-11 19:04:10', '2024-12-23 15:57:37');
INSERT INTO `dict` VALUES (21, '监测项', '预警管理_预警指标设定_监测项', '2024-09-11 19:06:07', '2024-09-11 19:06:07');
INSERT INTO `dict` VALUES (22, '预警状态', '预警管理_预警信息_预警状态', '2024-09-11 19:20:05', '2024-09-11 19:20:05');
INSERT INTO `dict` VALUES (23, '监测站名称', '工程信息服务_监测站点_监测站名称', '2024-09-11 19:57:54', '2024-09-11 19:58:25');
INSERT INTO `dict` VALUES (24, '测项名称', '工程信息服务_测项信息_测项名称', '2024-09-11 20:37:32', '2024-09-11 20:37:32');
INSERT INTO `dict` VALUES (25, '职位', '管理信息服务_管理人员信息_职位', '2024-09-11 20:55:51', '2024-09-11 20:55:51');
INSERT INTO `dict` VALUES (26, '性别', '管理信息服务_管理人员信息_性别', '2024-09-11 22:32:21', '2024-09-11 22:32:21');
INSERT INTO `dict` VALUES (28, '管道类型', '工程信息服务_管道_管道类型', '2024-09-26 20:55:02', '2024-09-26 20:55:02');
INSERT INTO `dict` VALUES (30, '负责人', '工程巡检_巡检信息_负责人', '2024-11-18 19:12:24', '2024-11-18 19:12:24');
INSERT INTO `dict` VALUES (31, '异常情况', '工程巡检_巡检信息_异常情况', '2024-11-18 20:57:29', '2024-11-18 20:57:29');
INSERT INTO `dict` VALUES (38, '预警地点', '一张图_预警地点', '2024-12-23 14:44:12', '2024-12-23 14:44:12');
INSERT INTO `dict` VALUES (39, '摄像头次地点', '实时监测_设备配置_次地点', '2025-01-15 18:23:43', '2025-01-15 18:23:43');
INSERT INTO `dict` VALUES (40, '视频监测点', '实时监测_视频监测_视频监测点', '2025-01-15 19:01:38', '2025-01-15 19:01:38');
INSERT INTO `dict` VALUES (42, '管理站', '管理站', '2025-01-15 19:05:24', '2025-01-15 19:05:24');
INSERT INTO `dict` VALUES (44, '处理类型', '工程巡检_巡检记录_巡检类型', '2025-02-22 15:27:52', '2025-02-22 15:27:52');

-- ----------------------------
-- Table structure for dict_detail
-- ----------------------------
DROP TABLE IF EXISTS `dict_detail`;
CREATE TABLE `dict_detail`  (
  `detail_id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `dict_id` bigint NULL DEFAULT NULL COMMENT '字典id',
  `label` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典标签',
  `value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典值',
  `dict_sort` int NULL DEFAULT NULL COMMENT '排序',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`detail_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 220 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of dict_detail
-- ----------------------------
INSERT INTO `dict_detail` VALUES (2, 1, '测试字典详情', '测试字典详情', 2, '2024-09-08 22:19:24', '2024-09-09 20:50:47');
INSERT INTO `dict_detail` VALUES (3, 19, '严重预警', '严重预警', 2, '2024-09-09 09:52:21', '2024-09-10 15:28:50');
INSERT INTO `dict_detail` VALUES (6, 19, '一般预警', '一般预警', 1, '2024-09-10 15:11:42', '2024-09-10 15:28:39');
INSERT INTO `dict_detail` VALUES (8, 2, '不严重预警', '不严重预警', 4, '2024-09-10 17:32:23', '2024-09-10 17:32:23');
INSERT INTO `dict_detail` VALUES (9, 6, '室内', '室内', 1, '2024-09-10 19:10:01', '2024-10-24 15:31:28');
INSERT INTO `dict_detail` VALUES (10, 6, '室外', '室外', 2, '2024-09-10 19:10:12', '2024-10-24 15:31:34');
INSERT INTO `dict_detail` VALUES (12, 18, '日常巡检', '日常巡检', 1, '2024-09-10 19:43:27', '2024-09-10 19:43:27');
INSERT INTO `dict_detail` VALUES (13, 18, '定期巡检', '定期巡检', 2, '2024-09-10 19:43:45', '2024-09-10 19:43:45');
INSERT INTO `dict_detail` VALUES (25, 11, '加压泵站1', '加压泵站1', 1, '2024-09-10 21:42:01', '2024-12-20 14:51:21');
INSERT INTO `dict_detail` VALUES (28, 11, '加压泵站2', '加压泵站2', 2, '2024-09-10 22:02:21', '2024-12-20 14:51:31');
INSERT INTO `dict_detail` VALUES (29, 13, '两河口水库', '两河口水库', 1, '2024-09-11 10:12:07', '2024-09-11 10:12:07');
INSERT INTO `dict_detail` VALUES (30, 13, '水厂', '水厂', 2, '2024-09-11 10:12:18', '2024-12-11 15:58:53');
INSERT INTO `dict_detail` VALUES (31, 13, '加压泵站1', '加压泵站1', 8, '2024-09-11 10:12:54', '2024-12-23 15:28:04');
INSERT INTO `dict_detail` VALUES (35, 14, '两河口水库', '两河口水库', 1, '2024-09-11 10:47:43', '2024-09-11 10:47:43');
INSERT INTO `dict_detail` VALUES (37, 15, '流量站1', '00000001', 1, '2024-09-11 10:48:51', '2024-12-20 16:03:38');
INSERT INTO `dict_detail` VALUES (38, 15, '流量站2', '00000002', 2, '2024-09-11 10:49:06', '2024-12-20 16:03:43');
INSERT INTO `dict_detail` VALUES (41, 17, '测站1', '测站1', 1, '2024-09-11 11:05:26', '2024-12-11 21:32:08');
INSERT INTO `dict_detail` VALUES (42, 17, '测站2', '测站2', 2, '2024-09-11 11:05:35', '2024-12-11 21:32:02');
INSERT INTO `dict_detail` VALUES (43, 17, '测站3', '测站3', 3, '2024-09-11 11:05:50', '2024-09-11 11:05:50');
INSERT INTO `dict_detail` VALUES (44, 17, '测站4', '测站4', 4, '2024-09-11 11:06:01', '2024-09-11 11:06:01');
INSERT INTO `dict_detail` VALUES (45, 17, '测站5', '测站5', 5, '2024-09-11 11:06:12', '2024-09-11 11:06:20');
INSERT INTO `dict_detail` VALUES (46, 17, '测站6', '测站6', 6, '2024-09-11 11:06:34', '2024-09-11 11:06:34');
INSERT INTO `dict_detail` VALUES (47, 17, '测站7', '测站7', 7, '2024-09-11 11:06:46', '2024-09-11 11:06:46');
INSERT INTO `dict_detail` VALUES (48, 20, '水位', '水位', 1, '2024-09-11 19:04:21', '2024-09-11 19:04:21');
INSERT INTO `dict_detail` VALUES (49, 20, '流量', '流量', 2, '2024-09-11 19:04:32', '2024-09-11 19:04:32');
INSERT INTO `dict_detail` VALUES (50, 20, '水质', '水质', 3, '2024-09-11 19:04:38', '2024-09-11 19:04:38');
INSERT INTO `dict_detail` VALUES (51, 21, '水位', '水位', 1, '2024-09-11 19:06:41', '2024-09-11 19:06:41');
INSERT INTO `dict_detail` VALUES (52, 21, '流量', '流量', 2, '2024-09-11 19:06:48', '2024-09-11 19:06:48');
INSERT INTO `dict_detail` VALUES (53, 21, '水温', '水温', 3, '2024-09-11 19:06:57', '2024-09-11 19:06:57');
INSERT INTO `dict_detail` VALUES (54, 21, '浊度', '浊度', 4, '2024-09-11 19:07:04', '2024-09-11 19:07:04');
INSERT INTO `dict_detail` VALUES (55, 21, 'PH', 'PH', 5, '2024-09-11 19:07:16', '2024-09-11 19:07:16');
INSERT INTO `dict_detail` VALUES (56, 21, '电导率', '电导率', 6, '2024-09-11 19:07:29', '2024-09-11 19:07:29');
INSERT INTO `dict_detail` VALUES (57, 21, '溶解氧', '溶解氧', 7, '2024-09-11 19:07:39', '2024-09-11 19:07:39');
INSERT INTO `dict_detail` VALUES (58, 21, '氨氮', '氨氮', 8, '2024-09-11 19:07:47', '2024-09-11 19:07:47');
INSERT INTO `dict_detail` VALUES (59, 21, '化学需氧量', '化学需氧量', 9, '2024-09-11 19:08:02', '2024-09-11 19:08:02');
INSERT INTO `dict_detail` VALUES (60, 21, '余氯', '余氯', 10, '2024-09-11 19:08:12', '2024-09-11 19:08:12');
INSERT INTO `dict_detail` VALUES (61, 22, '未解除', '未解除', 1, '2024-09-11 19:20:40', '2024-09-11 19:20:40');
INSERT INTO `dict_detail` VALUES (62, 22, '已解除', '已解除', 2, '2024-09-11 19:20:47', '2024-09-11 19:20:47');
INSERT INTO `dict_detail` VALUES (63, 23, '流量监测点1', '流量监测点1', 1, '2024-09-11 19:58:56', '2024-12-17 10:52:50');
INSERT INTO `dict_detail` VALUES (64, 23, '流量监测点2', '流量监测点2', 2, '2024-09-11 19:59:11', '2024-12-17 10:53:08');
INSERT INTO `dict_detail` VALUES (65, 23, '流量监测点3', '流量监测点3', 3, '2024-09-11 19:59:20', '2024-12-17 10:53:26');
INSERT INTO `dict_detail` VALUES (66, 23, '流量监测点4', '流量监测点4', 4, '2024-09-11 19:59:28', '2024-12-17 10:54:34');
INSERT INTO `dict_detail` VALUES (67, 24, '水位', '水位', 1, '2024-09-11 20:37:55', '2024-09-11 20:37:55');
INSERT INTO `dict_detail` VALUES (68, 24, '流量', '流量', 2, '2024-09-11 20:38:04', '2024-09-11 20:38:22');
INSERT INTO `dict_detail` VALUES (69, 24, '水质', '水质', 3, '2024-09-11 20:38:11', '2024-09-11 20:38:30');
INSERT INTO `dict_detail` VALUES (70, 25, '组长', '组长', 1, '2024-09-11 21:08:33', '2024-09-11 21:08:33');
INSERT INTO `dict_detail` VALUES (71, 25, '职工', '职工', 2, '2024-09-11 21:08:46', '2024-09-11 21:08:46');
INSERT INTO `dict_detail` VALUES (72, 26, '男', '男', 1, '2024-09-11 22:32:34', '2024-09-11 22:32:34');
INSERT INTO `dict_detail` VALUES (73, 26, '女', '女', 2, '2024-09-11 22:32:42', '2024-09-11 22:32:42');
INSERT INTO `dict_detail` VALUES (76, 28, '供水干管', '供水干管', 1, '2024-09-26 20:55:20', '2024-09-26 20:55:20');
INSERT INTO `dict_detail` VALUES (77, 28, '供水支管', '供水支管', 2, '2024-09-26 20:55:29', '2024-09-26 20:55:29');
INSERT INTO `dict_detail` VALUES (91, 30, '张三', '张三', 1, '2024-11-18 19:12:41', '2024-11-18 19:12:41');
INSERT INTO `dict_detail` VALUES (92, 30, '李四', '李四', 2, '2024-11-18 19:12:51', '2024-11-18 19:12:51');
INSERT INTO `dict_detail` VALUES (93, 30, '王五', '王五', 3, '2024-11-18 19:12:58', '2024-11-18 19:12:58');
INSERT INTO `dict_detail` VALUES (94, 30, '赵六', '赵六', 4, '2024-11-18 19:13:07', '2024-11-18 19:13:07');
INSERT INTO `dict_detail` VALUES (95, 31, '有异常', '有异常', 1, '2024-11-18 20:57:54', '2024-11-18 20:57:54');
INSERT INTO `dict_detail` VALUES (96, 31, '无异常', '无异常', 2, '2024-11-18 20:58:03', '2024-11-18 20:58:03');
INSERT INTO `dict_detail` VALUES (97, 13, '加压泵站2', '加压泵站2', 9, '2024-12-11 15:57:02', '2024-12-23 15:28:07');
INSERT INTO `dict_detail` VALUES (98, 13, '加压泵站3', '加压泵站3', 10, '2024-12-11 15:57:30', '2024-12-23 15:28:17');
INSERT INTO `dict_detail` VALUES (99, 13, '加压泵站4', '加压泵站4', 11, '2024-12-11 15:57:57', '2024-12-23 15:28:19');
INSERT INTO `dict_detail` VALUES (101, 15, '流量站3', '00000003', 3, '2024-12-11 20:40:15', '2024-12-20 16:03:48');
INSERT INTO `dict_detail` VALUES (102, 15, '流量站4', '00000004', 4, '2024-12-11 20:40:26', '2024-12-20 16:03:55');
INSERT INTO `dict_detail` VALUES (103, 15, '流量站5', '00000005', 5, '2024-12-11 20:40:50', '2024-12-20 16:04:02');
INSERT INTO `dict_detail` VALUES (137, 23, '流量监测点5', '流量监测点5', 5, '2024-12-17 10:54:22', '2024-12-17 10:54:22');
INSERT INTO `dict_detail` VALUES (138, 23, '加压视频1', '加压视频1', 6, '2024-12-17 10:54:52', '2024-12-17 10:54:52');
INSERT INTO `dict_detail` VALUES (139, 23, '加压视频2', '加压视频2', 7, '2024-12-17 10:55:22', '2024-12-17 10:55:22');
INSERT INTO `dict_detail` VALUES (140, 23, '加压视频3', '加压视频3', 8, '2024-12-17 10:57:00', '2024-12-17 10:57:00');
INSERT INTO `dict_detail` VALUES (141, 23, '加压视频4', '加压视频4', 9, '2024-12-17 10:57:40', '2024-12-17 10:57:40');
INSERT INTO `dict_detail` VALUES (142, 23, '加压视频5', '加压视频5', 10, '2024-12-17 10:59:23', '2024-12-17 10:59:23');
INSERT INTO `dict_detail` VALUES (143, 13, '流量站1', '流量站1', 3, '2024-12-17 12:08:19', '2024-12-23 15:27:42');
INSERT INTO `dict_detail` VALUES (144, 13, '流量站2', '流量站2', 4, '2024-12-17 12:50:21', '2024-12-23 15:27:46');
INSERT INTO `dict_detail` VALUES (145, 13, '流量站3', '流量站3', 5, '2024-12-17 12:50:37', '2024-12-23 15:27:49');
INSERT INTO `dict_detail` VALUES (146, 13, '流量站4', '流量站4', 6, '2024-12-17 12:51:08', '2024-12-23 15:27:52');
INSERT INTO `dict_detail` VALUES (147, 13, '流量站5', '流量站5', 7, '2024-12-17 12:51:40', '2024-12-23 15:27:55');
INSERT INTO `dict_detail` VALUES (148, 11, '加压泵站3', '加压泵站3', 3, '2024-12-20 14:51:41', '2024-12-20 14:51:41');
INSERT INTO `dict_detail` VALUES (149, 11, '加压泵站4', '加压泵站4', 4, '2024-12-20 14:51:48', '2024-12-20 14:51:48');
INSERT INTO `dict_detail` VALUES (150, 11, '加压泵站5', '加压泵站5', 5, '2024-12-20 14:51:56', '2024-12-20 14:51:56');
INSERT INTO `dict_detail` VALUES (151, 11, '流量站1', '流量站1', 6, '2024-12-20 14:52:11', '2024-12-20 14:52:11');
INSERT INTO `dict_detail` VALUES (152, 11, '流量站2', '流量站2', 7, '2024-12-20 14:52:18', '2024-12-20 14:52:18');
INSERT INTO `dict_detail` VALUES (153, 11, '流量站3', '流量站3', 8, '2024-12-20 14:52:31', '2024-12-20 14:52:31');
INSERT INTO `dict_detail` VALUES (154, 11, '流量站4', '流量站4', 9, '2024-12-20 14:52:56', '2024-12-20 14:52:56');
INSERT INTO `dict_detail` VALUES (155, 11, '流量站5', '流量站5', 10, '2024-12-20 14:53:17', '2024-12-20 14:53:17');
INSERT INTO `dict_detail` VALUES (174, 38, '两河口水库', '两河口水库', 1, '2024-12-23 14:44:41', '2024-12-23 14:44:41');
INSERT INTO `dict_detail` VALUES (175, 38, '水厂', '水厂', 2, '2024-12-23 14:44:49', '2024-12-23 14:44:49');
INSERT INTO `dict_detail` VALUES (176, 38, '流量站1', '流量站1', 3, '2024-12-23 14:44:58', '2024-12-23 14:44:58');
INSERT INTO `dict_detail` VALUES (177, 38, '流量站2', '流量站2', 4, '2024-12-23 14:45:13', '2024-12-23 14:45:13');
INSERT INTO `dict_detail` VALUES (178, 38, '流量站3', '流量站3', 5, '2024-12-23 14:45:26', '2024-12-23 14:45:26');
INSERT INTO `dict_detail` VALUES (179, 38, '流量站4', '流量站4', 6, '2024-12-23 14:45:40', '2024-12-23 14:45:40');
INSERT INTO `dict_detail` VALUES (180, 38, '流量站5', '流量站5', 7, '2024-12-23 14:45:51', '2024-12-23 14:45:51');
INSERT INTO `dict_detail` VALUES (181, 11, '水厂', '水厂', 11, '2024-12-23 15:27:03', '2025-01-15 16:51:02');
INSERT INTO `dict_detail` VALUES (183, 11, '管理站', '管理站', 12, '2025-01-15 16:51:14', '2025-01-15 16:51:14');
INSERT INTO `dict_detail` VALUES (184, 11, '竖井泵站', '竖井泵站', 13, '2025-01-15 16:51:25', '2025-01-15 16:51:25');
INSERT INTO `dict_detail` VALUES (185, 39, '竖井泵站主机间', '竖井泵站主机间', 1, '2025-01-15 18:25:13', '2025-01-15 18:25:13');
INSERT INTO `dict_detail` VALUES (186, 39, '竖井泵站电气副厂房', '竖井泵站电气副厂房', 2, '2025-01-15 18:25:31', '2025-01-15 18:25:31');
INSERT INTO `dict_detail` VALUES (187, 39, '竖井泵站闸室', '竖井泵站闸室', 3, '2025-01-15 18:25:43', '2025-01-15 18:25:43');
INSERT INTO `dict_detail` VALUES (188, 39, '竖井泵站水泵层', '竖井泵站水泵层', 4, '2025-01-15 18:26:01', '2025-01-15 18:26:01');
INSERT INTO `dict_detail` VALUES (189, 39, '竖井泵站室外', '竖井泵站室外', 5, '2025-01-15 18:26:13', '2025-01-15 18:26:13');
INSERT INTO `dict_detail` VALUES (190, 39, '管道末端出水口', '管道末端出水口', 6, '2025-01-15 18:26:28', '2025-01-15 18:26:28');
INSERT INTO `dict_detail` VALUES (191, 39, '泵房', '泵房', 7, '2025-01-15 18:26:40', '2025-01-15 18:26:40');
INSERT INTO `dict_detail` VALUES (192, 39, '电气副厂房', '电气副厂房', 8, '2025-01-15 18:26:54', '2025-01-15 18:26:54');
INSERT INTO `dict_detail` VALUES (193, 39, '水厂室内重要部位', '水厂室内重要部位', 9, '2025-01-15 18:27:05', '2025-01-15 18:29:49');
INSERT INTO `dict_detail` VALUES (194, 39, '取水口', '取水口', 10, '2025-01-15 18:27:14', '2025-01-15 18:27:14');
INSERT INTO `dict_detail` VALUES (195, 39, '厂区', '厂区', 11, '2025-01-15 18:27:23', '2025-01-15 18:27:23');
INSERT INTO `dict_detail` VALUES (196, 39, '机房', '机房', 12, '2025-01-15 18:27:46', '2025-01-15 18:27:46');
INSERT INTO `dict_detail` VALUES (197, 39, '中控室', '中控室', 13, '2025-01-15 18:27:59', '2025-01-15 18:28:04');
INSERT INTO `dict_detail` VALUES (198, 39, '管理站室内重要部位', '管理站室内重要部位', 14, '2025-01-15 18:30:03', '2025-01-15 18:30:03');
INSERT INTO `dict_detail` VALUES (199, 39, '室外', '室外', 15, '2025-01-15 18:30:13', '2025-01-15 18:30:13');
INSERT INTO `dict_detail` VALUES (200, 40, '水厂', '水厂', 1, '2025-01-15 19:02:38', '2025-01-15 19:02:38');
INSERT INTO `dict_detail` VALUES (201, 40, '管理站', '管理站', 2, '2025-01-15 19:02:48', '2025-01-15 19:02:48');
INSERT INTO `dict_detail` VALUES (202, 40, '竖井泵站', '竖井泵站', 3, '2025-01-15 19:03:04', '2025-01-15 19:03:04');
INSERT INTO `dict_detail` VALUES (208, 42, '机房', '机房', 1, '2025-01-15 19:05:46', '2025-01-15 19:05:46');
INSERT INTO `dict_detail` VALUES (209, 42, '中控室', '中控室', 2, '2025-01-15 19:05:57', '2025-01-15 19:05:57');
INSERT INTO `dict_detail` VALUES (210, 42, '管理站室内重要部位', '管理站室内重要部位', 3, '2025-01-15 19:06:19', '2025-01-15 19:06:19');
INSERT INTO `dict_detail` VALUES (211, 42, '室外', '室外', 4, '2025-01-15 19:06:39', '2025-01-15 19:06:39');
INSERT INTO `dict_detail` VALUES (218, 44, '未处理', '未处理', 1, '2025-02-22 15:28:01', '2025-02-22 15:28:01');
INSERT INTO `dict_detail` VALUES (219, 44, '已处理', '已处理', 2, '2025-02-22 15:28:07', '2025-02-22 15:28:07');

-- ----------------------------
-- Table structure for duty_log
-- ----------------------------
DROP TABLE IF EXISTS `duty_log`;
CREATE TABLE `duty_log`  (
  `值班日志ID` int NOT NULL AUTO_INCREMENT COMMENT '值班日志的唯一标识',
  `值班日期` date NOT NULL COMMENT '值班的具体日期',
  `天气` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '当天的天气情况',
  `雨量` decimal(5, 2) NULL DEFAULT NULL COMMENT '当天的雨量，单位毫米',
  `带班领导` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '带班领导的姓名或标识',
  `白班值班人员` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '白班值班人员的姓名或标识',
  `晚班值班人员` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '晚班值班人员的姓名或标识',
  `日志内容` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '值班日志的具体内容',
  `日志填写时间` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '日志填写的时间',
  `日志状态` enum('已填写','未填写') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '未填写' COMMENT '日志的填写状态',
  PRIMARY KEY (`值班日志ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用于记录值班日志的表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of duty_log
-- ----------------------------
INSERT INTO `duty_log` VALUES (1, '2025-06-01', '晴', 0.00, '李四', '张三', '王五', '一切正常，无异常情况。', '2025-05-25 15:55:39', '已填写');
INSERT INTO `duty_log` VALUES (2, '2025-06-02', '多云', 2.50, '周七', '李四', '赵六', '下午3点有短暂降雨，雨量2.5mm，其他正常。', '2025-05-25 15:55:39', '已填写');
INSERT INTO `duty_log` VALUES (3, '2025-06-03', '小雨', 5.00, '吴八', '孙九', '郑十', '全天小雨，雨量5.0mm，水库水位正常。', '2025-05-25 15:55:39', '已填写');
INSERT INTO `duty_log` VALUES (4, '2025-06-04', '阴', 0.00, '钱七', '周八', '吴九', '天气阴沉，无降雨，设备运行正常。', '2025-05-25 15:55:39', '已填写');

-- ----------------------------
-- Table structure for duty_schedule
-- ----------------------------
DROP TABLE IF EXISTS `duty_schedule`;
CREATE TABLE `duty_schedule`  (
  `值班安排ID` int NOT NULL AUTO_INCREMENT COMMENT '值班安排的唯一标识',
  `值班人员` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '值班人员的姓名或标识',
  `带班领导` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '带班领导的姓名或标识',
  `值班时间` datetime NOT NULL COMMENT '值班的具体时间',
  `值班岗位` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '值班人员所在的岗位',
  `创建时间` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '值班安排的创建时间',
  PRIMARY KEY (`值班安排ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用于记录值班安排的表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of duty_schedule
-- ----------------------------
INSERT INTO `duty_schedule` VALUES (1, '张三', '李四', '2025-06-07 08:12:15', '监控室', '2025-05-25 15:55:20');
INSERT INTO `duty_schedule` VALUES (2, '王五', '赵六', '2025-06-01 20:00:00', '大坝巡检', '2025-05-25 15:55:20');
INSERT INTO `duty_schedule` VALUES (3, '李四', '周七', '2025-06-02 08:00:00', '水情室', '2025-05-25 15:55:20');
INSERT INTO `duty_schedule` VALUES (4, '赵六', '吴八', '2025-06-14 11:32:48', '闸门室', '2025-05-25 15:55:20');

-- ----------------------------
-- Table structure for inspection_records
-- ----------------------------
DROP TABLE IF EXISTS `inspection_records`;
CREATE TABLE `inspection_records`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '巡检记录id',
  `project` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '巡检站点',
  `longitude` decimal(10, 7) NOT NULL COMMENT '经度',
  `latitude` decimal(10, 7) NOT NULL COMMENT '纬度',
  `type` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '巡检类型',
  `abnormal` varchar(10) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '异常情况',
  `situation` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '巡检情况',
  `solve` varchar(10) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '处理情况',
  `image` varchar(9999) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '图片',
  `person` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '负责人',
  `date` datetime NOT NULL COMMENT '日期',
  `create_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  `point` point NULL COMMENT '地图位置',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 223 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of inspection_records
-- ----------------------------
INSERT INTO `inspection_records` VALUES (179, '测站3', 114.6186324, 30.4627869, '定期巡检', '有异常', '1111111111111111111', '已处理', NULL, '张三', '2025-06-13 01:38:50', '2025-06-13 01:38:50', '2025-06-17 21:55:40', ST_GeomFromText('POINT(114.6186324 30.4627869)'));
INSERT INTO `inspection_records` VALUES (180, '测站1', 114.6186650, 30.4602310, '日常巡检', '无异常', '一切正常', '已处理', NULL, '张三', '2025-06-15 01:33:02', '2025-06-15 01:33:02', '2025-06-15 01:33:02', ST_GeomFromText('POINT(114.618665 30.460231)'));
INSERT INTO `inspection_records` VALUES (181, '测站3', 114.6186990, 30.4599120, '日常巡检', '无异常', '一切正常', '已处理', NULL, '张三', '2025-06-15 01:49:25', '2025-06-15 01:49:25', '2025-06-15 01:49:25', ST_GeomFromText('POINT(114.618699 30.459912)'));
INSERT INTO `inspection_records` VALUES (182, '测站1', 114.5018990, 30.4921910, '日常巡检', '无异常', '一切正常', '已处理', NULL, '张三', '2025-06-19 16:16:54', '2025-06-19 16:16:54', '2025-06-19 16:16:54', ST_GeomFromText('POINT(114.501899 30.492191)'));
INSERT INTO `inspection_records` VALUES (183, '测站1', 0.0000000, 0.0000000, '日常巡检', '无异常', '一切正常', '已处理', NULL, '张三', '2025-06-20 18:21:49', '2025-06-20 18:21:49', '2025-06-20 18:21:49', ST_GeomFromText('POINT(0 0)'));
INSERT INTO `inspection_records` VALUES (184, '测站2', 0.0000000, 0.0000000, '日常巡检', '有异常', '太阳能电池被树叶遮挡', '已处理', NULL, '张三', '2025-06-21 21:55:56', '2025-06-21 21:55:56', '2025-06-21 22:04:58', ST_GeomFromText('POINT(0 0)'));
INSERT INTO `inspection_records` VALUES (185, '测站4', 0.0000000, 0.0000000, '定期巡检', '无异常', '一切正常', '已处理', NULL, '测试2', '2025-06-21 21:57:47', '2025-06-21 21:57:47', '2025-06-21 21:57:47', ST_GeomFromText('POINT(0 0)'));
INSERT INTO `inspection_records` VALUES (188, '测站1', 114.6189820, 30.4621290, '日常巡检', '无异常', '一切正常', '已处理', NULL, '张三', '2025-06-22 01:20:56', '2025-06-22 01:20:56', '2025-06-22 01:20:56', ST_GeomFromText('POINT(114.618982 30.462129)'));
INSERT INTO `inspection_records` VALUES (189, '测站7', 0.0000000, 0.0000000, '日常巡检', '无异常', '一切正常', '已处理', NULL, '测试2', '2025-06-23 19:54:01', '2025-06-23 19:54:01', '2025-06-23 19:54:01', ST_GeomFromText('POINT(0 0)'));
INSERT INTO `inspection_records` VALUES (190, '测站3', 0.0000000, 0.0000000, '定期巡检', '有异常', '设备掉电', '已处理', NULL, '测试1', '2025-06-23 20:02:36', '2025-06-23 20:02:36', '2025-06-23 20:03:28', ST_GeomFromText('POINT(0 0)'));
INSERT INTO `inspection_records` VALUES (191, '测站6', 113.4965410, 31.8839410, '日常巡检', '有异常', '通信电缆断路', '已处理', NULL, '测试1', '2025-06-23 20:06:48', '2025-06-23 20:06:48', '2025-06-24 10:45:51', ST_GeomFromText('POINT(113.496541 31.883941)'));
INSERT INTO `inspection_records` VALUES (192, '测站1', 0.0000000, 0.0000000, '日常巡检', '无异常', '一切正常', '已处理', NULL, '测试2', '2025-06-24 15:38:01', '2025-06-24 15:38:01', '2025-06-24 15:38:01', ST_GeomFromText('POINT(0 0)'));
INSERT INTO `inspection_records` VALUES (194, '测站2', 0.0000000, 0.0000000, '日常巡检', '有异常', '设备损坏', '未处理', NULL, '测试1', '2025-06-25 19:45:49', '2025-06-25 19:45:49', '2025-06-25 19:45:49', ST_GeomFromText('POINT(0 0)'));
INSERT INTO `inspection_records` VALUES (195, '测站1', 114.6187260, 30.4600620, '日常巡检', '无异常', '一切正常', '已处理', NULL, '张三', '2025-06-25 20:44:36', '2025-06-25 20:44:36', '2025-06-25 20:44:36', ST_GeomFromText('POINT(114.618726 30.460062)'));
INSERT INTO `inspection_records` VALUES (196, '测站1', 116.4062428, 39.9014035, '日常巡检', '无异常', '一切正常', '已处理', NULL, '张三', '2025-06-25 21:17:15', '2025-06-25 21:17:15', '2025-06-25 21:17:15', ST_GeomFromText('POINT(116.4062428 39.9014035)'));
INSERT INTO `inspection_records` VALUES (197, '测站1', 116.4062428, 39.9014035, '日常巡检', '无异常', '一切正常', '已处理', NULL, '张三', '2025-06-25 21:18:46', '2025-06-25 21:18:46', '2025-06-25 21:18:46', ST_GeomFromText('POINT(116.4062428 39.9014035)'));
INSERT INTO `inspection_records` VALUES (200, '测站1', 114.6187260, 30.4600620, '日常巡检', '无异常', '一切正常', '已处理', NULL, '张三', '2025-06-25 22:06:01', '2025-06-25 22:06:01', '2025-06-25 22:06:01', ST_GeomFromText('POINT(114.618726 30.460062)'));
INSERT INTO `inspection_records` VALUES (201, '测站1', 114.6187120, 30.4599500, '日常巡检', '无异常', '一切正常', '已处理', NULL, '张三', '2025-06-25 22:06:49', '2025-06-25 22:06:49', '2025-06-25 22:06:49', ST_GeomFromText('POINT(114.618712 30.45995)'));
INSERT INTO `inspection_records` VALUES (202, '测站1', 114.6187260, 30.4600620, '日常巡检', '无异常', '一切正常', '已处理', NULL, '张三', '2025-06-25 22:10:30', '2025-06-25 22:10:30', '2025-06-25 22:10:30', ST_GeomFromText('POINT(114.618726 30.460062)'));
INSERT INTO `inspection_records` VALUES (204, '测站1', 116.4062428, 39.9014035, '日常巡检', '无异常', '一切正常', '已处理', NULL, '张三', '2025-06-25 22:19:28', '2025-06-25 22:19:28', '2025-06-25 22:19:28', ST_GeomFromText('POINT(116.4062428 39.9014035)'));
INSERT INTO `inspection_records` VALUES (205, '测站1', 116.4062428, 39.9014035, '日常巡检', '无异常', '一切正常', '已处理', NULL, '张三', '2025-06-25 22:35:38', '2025-06-25 22:35:38', '2025-06-25 22:35:38', ST_GeomFromText('POINT(116.4062428 39.9014035)'));
INSERT INTO `inspection_records` VALUES (206, '测站1', 116.4062428, 39.9014035, '日常巡检', '无异常', '一切正常', '已处理', NULL, '张三', '2025-06-25 22:39:40', '2025-06-25 22:39:40', '2025-06-25 22:39:40', ST_GeomFromText('POINT(116.4062428 39.9014035)'));
INSERT INTO `inspection_records` VALUES (207, '测站1', 116.4062428, 39.9014035, '日常巡检', '无异常', '一切正常', '已处理', NULL, '张三', '2025-06-25 23:01:53', '2025-06-25 23:01:53', '2025-06-25 23:01:53', ST_GeomFromText('POINT(116.4062428 39.9014035)'));
INSERT INTO `inspection_records` VALUES (208, '测站1', 116.4062428, 39.9014035, '日常巡检', '无异常', '一切正常', '已处理', NULL, '张三', '2025-06-25 23:23:59', '2025-06-25 23:23:59', '2025-06-25 23:23:59', ST_GeomFromText('POINT(116.4062428 39.9014035)'));
INSERT INTO `inspection_records` VALUES (209, '测站1', 116.4062428, 39.9014035, '日常巡检', '无异常', '一切正常', '已处理', NULL, '张三', '2025-06-25 23:25:01', '2025-06-25 23:25:01', '2025-06-25 23:25:01', ST_GeomFromText('POINT(116.4062428 39.9014035)'));
INSERT INTO `inspection_records` VALUES (210, '测站1', 116.4062428, 39.9014035, '日常巡检', '无异常', '一切正常', '已处理', NULL, '张三', '2025-06-25 23:27:04', '2025-06-25 23:27:04', '2025-06-25 23:27:04', ST_GeomFromText('POINT(116.4062428 39.9014035)'));
INSERT INTO `inspection_records` VALUES (211, '测站1', 116.4062428, 39.9014035, '日常巡检', '无异常', '一切正常', '已处理', NULL, '张三', '2025-06-26 00:14:25', '2025-06-26 00:14:25', '2025-06-26 00:14:25', ST_GeomFromText('POINT(116.4062428 39.9014035)'));
INSERT INTO `inspection_records` VALUES (213, '测站3', 0.0000000, 0.0000000, '定期巡检', '有异常', '网络问题', '已处理', NULL, '张三', '2025-06-26 06:48:41', '2025-06-26 06:48:41', '2025-06-26 15:08:32', ST_GeomFromText('POINT(0 0)'));
INSERT INTO `inspection_records` VALUES (214, '测站2', 0.0000000, 0.0000000, '日常巡检', '无异常', '一切正常', '已处理', NULL, '张三', '2025-06-26 06:54:21', '2025-06-26 06:54:21', '2025-06-26 06:54:21', ST_GeomFromText('POINT(0 0)'));
INSERT INTO `inspection_records` VALUES (216, '测站1', 114.5019040, 30.4922130, '日常巡检', '无异常', '一切正常', '已处理', '0c02d0c7d6d44a2ca4c6eb9fd85a859c.jpg', '张三', '2025-06-26 09:41:41', '2025-06-26 09:41:41', '2025-06-26 09:41:41', ST_GeomFromText('POINT(114.501904 30.492213)'));
INSERT INTO `inspection_records` VALUES (217, '测站7', 114.5018650, 30.4922080, '日常巡检', '无异常', '一切正常', '已处理', '6865fe60fad3401ea2faf28bf0c26213.jpg', '张三', '2025-06-26 10:10:26', '2025-06-26 10:10:26', '2025-06-26 10:10:26', ST_GeomFromText('POINT(114.501865 30.492208)'));
INSERT INTO `inspection_records` VALUES (218, '测站1', 114.5018540, 30.4922030, '日常巡检', '无异常', '一切正常', '已处理', '52ed4a676d4347e6a49124bd86bd2352.jpg', '张三', '2025-06-26 11:29:54', '2025-06-26 11:29:54', '2025-06-26 11:29:54', ST_GeomFromText('POINT(114.501854 30.492203)'));
INSERT INTO `inspection_records` VALUES (219, '测站1', 114.6186870, 30.4599130, '日常巡检', '无异常', '一切正常', '已处理', '516ab07a5483479091e7afce004d529d.jpg', '张三', '2025-06-26 16:28:11', '2025-06-26 16:28:11', '2025-06-26 16:28:11', ST_GeomFromText('POINT(114.618687 30.459913)'));
INSERT INTO `inspection_records` VALUES (220, '测站4', 114.5019020, 30.4922060, '日常巡检', '无异常', '一切正常', '已处理', 'f482e328425b41b1b6876452cd3094df.jpg', '张三', '2025-06-27 11:09:48', '2025-06-27 11:09:48', '2025-06-27 11:09:48', ST_GeomFromText('POINT(114.501902 30.492206)'));
INSERT INTO `inspection_records` VALUES (221, '测站1', 115.6991400, 30.1289630, '日常巡检', '有异常', '111', '已处理', 'b2776f09d1e2401eaad9581737f28134.jpg', '张三', '2025-07-03 10:50:28', '2025-07-03 10:50:28', '2025-07-03 10:50:51', ST_GeomFromText('POINT(115.69914 30.128963)'));
INSERT INTO `inspection_records` VALUES (222, '测站5', 114.5020160, 30.4921340, '日常巡检', '有异常', '11111', '已处理', '579c9d69a57f4ca5983b994e92dbcfad.jpg', '张三', '2025-09-25 12:17:40', '2025-09-25 12:17:40', '2025-09-25 14:34:07', ST_GeomFromText('POINT(114.502016 30.492134)'));

-- ----------------------------
-- Table structure for maintence_record
-- ----------------------------
DROP TABLE IF EXISTS `maintence_record`;
CREATE TABLE `maintence_record`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '工程记录id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '工程名称',
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '工程代码',
  `note` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `responsible_person` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '负责人',
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '负责人电话',
  `start_time` datetime NOT NULL COMMENT '开始维护时间',
  `over_time` datetime NULL DEFAULT NULL COMMENT '结束维护时间',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of maintence_record
-- ----------------------------
INSERT INTO `maintence_record` VALUES (1, '荆竹水库水位监测子系统', '123545689', '对水位监测传感器进行维护', '李四', '13545687895', '2025-06-05 05:07:59', '2025-06-06 00:00:00', '2024-06-20 21:17:52', '2025-06-23 15:12:11');
INSERT INTO `maintence_record` VALUES (2, '荆竹水库雨量监测子系统', '123545680', '对采集设备复位和校准', '李四', '13901234567', '2025-06-07 08:00:00', '2025-06-08 00:00:00', '2025-06-23 15:11:52', '2025-06-23 15:13:43');

-- ----------------------------
-- Table structure for measuring_item
-- ----------------------------
DROP TABLE IF EXISTS `measuring_item`;
CREATE TABLE `measuring_item`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '测项主键id',
  `number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '测项编号',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '测项名称',
  `unit` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '测项单位',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of measuring_item
-- ----------------------------
INSERT INTO `measuring_item` VALUES (2, '4', '水位', 'm', '2024-06-30 20:22:47', '2024-07-02 23:12:11');
INSERT INTO `measuring_item` VALUES (6, '3', '雨量', 'mm', '2024-12-14 11:31:40', '2025-06-18 16:00:02');
INSERT INTO `measuring_item` VALUES (7, '5', '渗流量', '', '2024-12-14 11:31:50', '2025-06-18 16:00:32');
INSERT INTO `measuring_item` VALUES (8, '6', '变形位移', 'mm', '2025-05-20 16:31:59', '2025-06-18 16:00:54');
INSERT INTO `measuring_item` VALUES (9, '7', '视频监测', '', '2025-06-18 16:27:07', '2025-06-18 16:27:07');

-- ----------------------------
-- Table structure for measuring_station
-- ----------------------------
DROP TABLE IF EXISTS `measuring_station`;
CREATE TABLE `measuring_station`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '监测站点id',
  `code` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '站码',
  `name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '站名',
  `water_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '水系名称',
  `river_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '河流名称',
  `monitor_code` varchar(3) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '施测项目码',
  `address_code` varchar(10) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '行政区划码',
  `establish_date` date NULL DEFAULT NULL COMMENT '设站年月',
  `longitude` decimal(10, 7) NOT NULL COMMENT '经度',
  `latitude` decimal(10, 7) NOT NULL COMMENT '纬度',
  `note` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  `point` point NULL COMMENT '地图位置',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 42 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of measuring_station
-- ----------------------------
INSERT INTO `measuring_station` VALUES (20, '4211820043', '坝前水位雨量站(新站)', '', '', 'Q', '', '2025-06-01', 113.4920780, 31.8824470, '', '2024-12-14 11:35:08', '2025-09-25 12:54:35', ST_GeomFromText('POINT(113.492078 31.882447)'));
INSERT INTO `measuring_station` VALUES (26, '24043419', '大坝视频球机1', '', '', 'V', '', '2025-05-01', 113.4863060, 31.8836310, '', '2024-12-16 14:18:05', '2025-06-18 16:22:49', ST_GeomFromText('POINT(113.486306 31.883631)'));
INSERT INTO `measuring_station` VALUES (27, '24043424', '大坝视频球机2', '', '', 'V', '', '2025-05-01', 113.5399610, 31.9083340, '', '2024-12-17 08:55:27', '2025-06-18 16:23:19', ST_GeomFromText('POINT(113.539961 31.908334)'));
INSERT INTO `measuring_station` VALUES (28, '24043418', '大坝视频球机3', NULL, NULL, 'V', NULL, '2025-05-01', 113.5278910, 31.8543280, NULL, '2024-12-17 10:21:08', '2025-06-18 16:23:45', ST_GeomFromText('POINT(113.527891 31.854328)'));
INSERT INTO `measuring_station` VALUES (32, '33210', 'LJ1-1', '', '', 'L', '', '2025-06-01', 113.4920780, 31.8824470, '', '2025-06-27 09:43:45', '2025-06-27 09:45:36', ST_GeomFromText('POINT(113.492078 31.882447)'));
INSERT INTO `measuring_station` VALUES (33, '33214', 'LJ1-2', '', '', 'L', '', '2025-06-01', 113.4952540, 31.8809160, '', '2025-06-27 09:44:08', '2025-06-27 09:45:39', ST_GeomFromText('POINT(113.495254 31.880916)'));
INSERT INTO `measuring_station` VALUES (34, '33216', 'LJ1-3', '', '', 'L', '', '2025-06-01', 113.4862180, 31.8848640, '', '2025-06-27 09:44:30', '2025-06-27 09:45:41', ST_GeomFromText('POINT(113.486218 31.884864)'));
INSERT INTO `measuring_station` VALUES (35, '33212', 'LJ1-4', '', '', 'L', '', '2025-06-01', 113.5260400, 31.8567670, '', '2025-06-27 09:44:53', '2025-06-27 09:45:45', ST_GeomFromText('POINT(113.52604 31.856767)'));
INSERT INTO `measuring_station` VALUES (36, '33215', 'LT2-1', '', '', 'L', '', '2025-06-01', 113.4593800, 31.8079120, '', '2025-06-27 09:45:21', '2025-06-27 09:45:50', ST_GeomFromText('POINT(113.45938 31.807912)'));
INSERT INTO `measuring_station` VALUES (37, '33211', 'LT2-2', '', '', 'L', '', '2025-06-01', 113.4863060, 31.8836310, '', '2025-06-27 09:46:15', '2025-06-27 09:46:15', ST_GeomFromText('POINT(113.486306 31.883631)'));
INSERT INTO `measuring_station` VALUES (38, '33217', 'LT2-3', '', '', 'L', '', '2025-06-01', 113.5399610, 31.9083340, '', '2025-06-27 09:46:38', '2025-06-27 09:46:38', ST_GeomFromText('POINT(113.539961 31.908334)'));
INSERT INTO `measuring_station` VALUES (39, '33213', 'LT2-4', '', '', 'L', '', NULL, 113.5278910, 31.8543280, '', '2025-06-27 09:47:03', '2025-06-27 09:47:03', ST_GeomFromText('POINT(113.527891 31.854328)'));
INSERT INTO `measuring_station` VALUES (40, '33201', '管理处内基准点', '', '', 'L', '', '2025-06-01', 113.5127860, 31.8607960, '', '2025-06-27 09:47:27', '2025-06-27 09:47:27', ST_GeomFromText('POINT(113.512786 31.860796)'));
INSERT INTO `measuring_station` VALUES (41, '33200', '管理处外基准点', '', '', 'L', '', '2025-06-01', 113.5225550, 31.8540360, '', '2025-06-27 09:47:53', '2025-06-27 09:47:53', ST_GeomFromText('POINT(113.522555 31.854036)'));

-- ----------------------------
-- Table structure for seepage_data
-- ----------------------------
DROP TABLE IF EXISTS `seepage_data`;
CREATE TABLE `seepage_data`  (
  `record_id` int NOT NULL AUTO_INCREMENT,
  `station_id` int NOT NULL,
  `record_time` datetime NOT NULL,
  `seepage_flow` float NOT NULL,
  `remarks` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  PRIMARY KEY (`record_id`) USING BTREE,
  INDEX `idx_station_time`(`station_id` ASC, `record_time` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of seepage_data
-- ----------------------------
INSERT INTO `seepage_data` VALUES (1, 1, '2025-05-18 10:03:29', 10, '无特殊情况');
INSERT INTO `seepage_data` VALUES (2, 1, '2025-05-18 11:05:42', 12, '无特殊情况');
INSERT INTO `seepage_data` VALUES (3, 1, '2025-05-18 12:05:53', 8, '无特殊情况');
INSERT INTO `seepage_data` VALUES (4, 2, '2025-05-18 11:06:05', 11, '无特殊情况');
INSERT INTO `seepage_data` VALUES (5, 2, '2025-05-19 12:06:13', 22, '无特殊情况');
INSERT INTO `seepage_data` VALUES (6, 3, '2025-05-23 15:07:18', 17, '无特殊情况');
INSERT INTO `seepage_data` VALUES (7, 3, '2025-05-23 16:07:36', 14, '无特殊情况');
INSERT INTO `seepage_data` VALUES (8, 3, '2025-05-23 17:07:53', 12, '无特殊情况');

-- ----------------------------
-- Table structure for warning_facilities
-- ----------------------------
DROP TABLE IF EXISTS `warning_facilities`;
CREATE TABLE `warning_facilities`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `facility_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `manager` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `last_update` timestamp NULL DEFAULT NULL,
  `record_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of warning_facilities
-- ----------------------------
INSERT INTO `warning_facilities` VALUES (1, '雨量计', '系统报警', '大坝北侧', '正常', '张三', '2025-06-19 14:54:13', '2025-06-19 14:54:13');
INSERT INTO `warning_facilities` VALUES (2, '广播', '应急广播', '闸门北侧', '正常', '李四', '2025-07-02 18:41:03', '2025-06-19 14:54:13');
INSERT INTO `warning_facilities` VALUES (3, '监控摄像头1', '视频监控', '溢洪道', '故障', '王五', '2025-07-02 18:41:05', '2025-06-19 14:54:13');
INSERT INTO `warning_facilities` VALUES (4, '水位计', '系统报警', '大坝北侧', '正常', '张三', '2025-07-02 18:41:07', '2025-07-01 16:20:31');
INSERT INTO `warning_facilities` VALUES (5, '变形监测仪仪', '系统报警', '大坝南北坡', '正常', '张三', '2025-07-02 18:41:08', '2025-07-01 16:21:21');
INSERT INTO `warning_facilities` VALUES (6, '渗流监测计', '系统报警', '大坝断面', '正常', '张三', '2025-07-02 18:41:10', '2025-07-01 16:22:18');
INSERT INTO `warning_facilities` VALUES (7, '闸门1', '系统报警', '输水口', '正常', '张三', '2025-07-02 18:41:11', '2025-07-01 16:23:36');

SET FOREIGN_KEY_CHECKS = 1;

-- ----------------------------
-- Table structure for device_fault_record
-- ----------------------------
DROP TABLE IF EXISTS `device_fault_record`;
CREATE TABLE `device_fault_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '故障记录ID',
  `device_type` varchar(32) NOT NULL COMMENT '设备类型：gnss/rain/seepage',
  `device_code` varchar(100) NOT NULL COMMENT '设备唯一编码：stationId、pointId或业务编码',
  `device_name` varchar(100) NOT NULL COMMENT '设备名称',
  `active_key` varchar(160) NULL COMMENT '活跃故障唯一键，active时为device_type:device_code，resolved后置空',
  `first_fault_status` varchar(32) NOT NULL COMMENT '首次故障状态：offline/abnormal',
  `current_fault_status` varchar(32) NOT NULL COMMENT '当前故障状态：offline/abnormal',
  `fault_type` varchar(64) NOT NULL COMMENT '故障类型：interface_error/no_data/collect_timeout/db_error',
  `fault_detail` text NULL COMMENT '故障详情或采集值快照',
  `last_collect_time` datetime NULL COMMENT '最后采集时间',
  `start_time` datetime NOT NULL COMMENT '故障开始时间',
  `end_time` datetime NULL COMMENT '故障解除时间',
  `duration_minutes` int NULL COMMENT '故障持续分钟数',
  `process_status` varchar(32) NOT NULL DEFAULT 'active' COMMENT '处理状态：active/resolved',
  `create_time` datetime NULL COMMENT '创建时间',
  `update_time` datetime NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_device_fault_active` (`active_key`),
  KEY `idx_device_fault` (`device_type`, `device_code`, `start_time`),
  KEY `idx_fault_query` (`device_type`, `current_fault_status`, `process_status`, `start_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='设备故障记录表';

-- ----------------------------
-- Table structure for device_fault_event_log
-- ----------------------------
DROP TABLE IF EXISTS `device_fault_event_log`;
CREATE TABLE `device_fault_event_log` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '故障事件ID',
  `fault_record_id` bigint NOT NULL COMMENT '故障主记录ID',
  `device_type` varchar(32) NOT NULL COMMENT '设备类型：gnss/rain/seepage',
  `device_code` varchar(100) NOT NULL COMMENT '设备唯一编码',
  `event_status` varchar(32) NOT NULL COMMENT '事件状态：abnormal/offline/online',
  `event_type` varchar(32) NOT NULL COMMENT '事件类型：fault_start/status_change/fault_recover',
  `event_detail` text NULL COMMENT '事件详情',
  `last_collect_time` datetime NULL COMMENT '最后采集时间',
  `event_time` datetime NOT NULL COMMENT '事件发生时间',
  `create_time` datetime NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_fault_record` (`fault_record_id`, `event_time`),
  KEY `idx_device_event` (`device_type`, `device_code`, `event_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='设备故障事件明细表';
