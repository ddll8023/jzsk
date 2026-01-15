CREATE TABLE IF NOT EXISTS `gnss_monitoring_data` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `station_id` varchar(50) NOT NULL COMMENT '站点ID',
  `station_name` varchar(100) NOT NULL COMMENT '站点名称',
  `longitude` double NOT NULL COMMENT '经度',
  `latitude` double NOT NULL COMMENT '纬度',
  `elevation` double NOT NULL COMMENT '高程',
  `displacement_x` double DEFAULT NULL COMMENT 'X方向位移',
  `displacement_y` double DEFAULT NULL COMMENT 'Y方向位移',
  `displacement_z` double DEFAULT NULL COMMENT 'Z方向位移',
  `monitor_time` datetime NOT NULL COMMENT '监测时间',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_station_id` (`station_id`),
  KEY `idx_monitor_time` (`monitor_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='GNSS监测数据表'; 