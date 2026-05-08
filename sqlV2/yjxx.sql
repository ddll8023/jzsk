/*
 Navicat Premium Dump SQL

 Source Server         : db
 Source Server Type    : MySQL
 Source Server Version : 80042 (8.0.42)
 Source Host           : localhost:3306
 Source Schema         : yjxx

 Target Server Type    : MySQL
 Target Server Version : 80042 (8.0.42)
 File Encoding         : 65001

 Date: 15/01/2026 11:47:59
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------

-- ----------------------------
-- Table structure for warning_indicator_setting
-- ----------------------------
DROP TABLE IF EXISTS `warning_indicator_setting`;
CREATE TABLE `warning_indicator_setting`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `position` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '监测点',
  `type` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '监测项',
  `up_up_limit` double NOT NULL COMMENT '上上限',
  `up_limit` double NOT NULL COMMENT '上限',
  `low_limit` double NOT NULL COMMENT '下限',
  `lower_limit` double NOT NULL COMMENT '下下限',
  `unit` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '单位',
  `longitude` decimal(10, 7) NULL DEFAULT NULL COMMENT '经度',
  `latitude` decimal(10, 7) NULL DEFAULT NULL COMMENT '纬度',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 45 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of warning_indicator_setting
-- ----------------------------
INSERT INTO `warning_indicator_setting` VALUES (3, '两河口水库', '水位', 100, 70, 0.1, 0, 'm', 113.4975840, 31.8838420, '2024-07-01 15:36:45', '2024-12-21 12:02:06');
INSERT INTO `warning_indicator_setting` VALUES (32, '流量站1', '流量', 100, 50, -50, -100, 'm³/s', 113.4920780, 31.8824470, '2024-06-06 11:45:03', '2024-12-15 10:02:34');
INSERT INTO `warning_indicator_setting` VALUES (33, '水厂', '水温', 40, 30, 20, 10, '°', 113.4957220, 31.8843810, '2024-07-02 16:30:29', '2024-12-15 10:02:46');
INSERT INTO `warning_indicator_setting` VALUES (34, '水厂', '浊度', 30, 20, 10, 7.5, 'NTU', 113.4957220, 31.8843810, '2024-07-02 16:31:17', '2024-12-15 10:02:49');
INSERT INTO `warning_indicator_setting` VALUES (35, '水厂', 'PH', 10, 8, 6, 4, '无', 113.4957220, 31.8843810, '2024-07-02 17:13:59', '2024-12-15 10:03:04');
INSERT INTO `warning_indicator_setting` VALUES (36, '水厂', '电导率', 30, 20, 10, 7.5, 'uS/cm', 113.4957220, 31.8843810, '2024-07-02 17:15:50', '2024-12-15 10:03:08');
INSERT INTO `warning_indicator_setting` VALUES (37, '水厂', '溶解氧', 30, 20, 10, 7.5, 'mg/L', 113.4957220, 31.8843810, '2024-07-02 17:16:35', '2024-12-15 10:03:12');
INSERT INTO `warning_indicator_setting` VALUES (38, '水厂', '氨氮', 30, 20, 10, 7.5, 'mg/L', 113.4957220, 31.8843810, '2024-07-02 17:17:18', '2024-12-15 10:03:15');
INSERT INTO `warning_indicator_setting` VALUES (39, '水厂', '化学需氧量', 30, 20, 10, 7.5, 'mg/L', 113.4957220, 31.8843810, '2024-07-02 17:18:11', '2024-12-15 10:03:19');
INSERT INTO `warning_indicator_setting` VALUES (40, '水厂', '余氯', 100, 10, 0.01, 0, 'mg/L', 113.4957220, 31.8843810, '2024-07-02 17:19:00', '2024-12-15 10:03:22');
INSERT INTO `warning_indicator_setting` VALUES (41, '流量站2', '流量', 0.5, 0.3, -0.3, -0.5, 'm³/s', 113.4952540, 31.8809160, NULL, NULL);
INSERT INTO `warning_indicator_setting` VALUES (42, '流量站3', '流量', 0.5, 0.3, -0.3, -0.5, 'm³/s', 113.4862180, 31.8848640, NULL, NULL);
INSERT INTO `warning_indicator_setting` VALUES (43, '流量站4', '流量', 0.5, 0.3, -0.3, -0.5, 'm³/s', 113.5260400, 31.8567670, NULL, NULL);
INSERT INTO `warning_indicator_setting` VALUES (44, '流量站5', '流量', 100, 50, -50, -100, 'm³/s', 113.4593800, 31.8079120, NULL, NULL);

-- ----------------------------
-- Table structure for warning_information
-- ----------------------------
DROP TABLE IF EXISTS `warning_information`;
CREATE TABLE `warning_information`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '预警信息id',
  `position` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '监测地点',
  `project` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '所属工程',
  `content` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '预警内容',
  `type` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '监测对象',
  `level` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '预警等级',
  `status` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '预警状态',
  `longitude` decimal(10, 7) NULL DEFAULT NULL COMMENT '经度',
  `latitude` decimal(10, 7) NULL DEFAULT NULL COMMENT '纬度',
  `start_time` datetime NULL DEFAULT NULL COMMENT '发生时间',
  `over_time` datetime NULL DEFAULT NULL COMMENT '解除时间',
  `stay_time` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '持续时长',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `point` point NULL COMMENT '地图位置',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 61 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of warning_information
-- ----------------------------
INSERT INTO `warning_information` VALUES (51, '水厂', '鄂北水资源供水工程', '余氯高于上上限20.00mg/L;', '水质', '严重预警', '未解除', 113.4948220, 31.8848810, '2024-12-23 09:42:01', NULL, NULL, '2024-12-20 21:07:22', '2024-12-20 21:07:22', NULL);
INSERT INTO `warning_information` VALUES (52, '水厂', '鄂北水资源供水工程', '余氯高于上上限20.00mg/L;', '水质', '严重预警', '未解除', 113.4948220, 31.8848810, '2024-12-23 09:42:01', NULL, NULL, '2024-12-20 21:08:05', '2024-12-20 21:08:05', NULL);
INSERT INTO `warning_information` VALUES (57, '两河口水库', '鄂北水资源供水工程', '水位高于上上限400.00米', '水位', '严重预警', '未解除', 113.4985840, 31.8853420, '2024-12-22 16:21:18', NULL, NULL, '2024-12-21 12:38:40', '2024-12-21 12:38:40', NULL);
INSERT INTO `warning_information` VALUES (58, '流量站1', '鄂北水资源供水工程', '流量低于下下限15.07米', '流量', '严重预警', '未解除', 113.4939780, 31.8824470, '2024-12-22 21:15:00', NULL, NULL, '2024-12-21 12:38:40', '2024-12-21 12:38:40', NULL);
INSERT INTO `warning_information` VALUES (59, '流量站2', '鄂北水资源供水工程', NULL, '流量', '一般预警', '未解除', 113.4939780, 31.8824470, '2024-12-22 14:52:42', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `warning_information` VALUES (60, '流量站3', '鄂北水资源供水工程', NULL, '流量', '一般预警', '已解除', 113.4939780, 31.8824470, '2024-12-22 14:53:04', '2024-12-23 15:24:18', '1天0小时31分钟', NULL, '2024-12-23 15:24:18', ST_GeomFromText('POINT(113.493978 31.882447)'));

SET FOREIGN_KEY_CHECKS = 1;
