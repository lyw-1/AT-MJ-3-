-- MySQL dump 10.13  Distrib 8.0.43, for Win64 (x86_64)
--
-- Host: localhost    Database: mold_digitalization
-- ------------------------------------------------------
-- Server version	8.0.43

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `accessory`
--

DROP TABLE IF EXISTS `accessory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `accessory` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '配件ID',
  `accessory_code` varchar(100) NOT NULL COMMENT '配件编号',
  `accessory_name` varchar(200) NOT NULL COMMENT '配件名称',
  `accessory_type_id` bigint NOT NULL COMMENT '配件类型ID',
  `specification` varchar(200) DEFAULT NULL COMMENT '规格',
  `material` varchar(100) DEFAULT NULL COMMENT '材料',
  `unit` varchar(50) DEFAULT NULL COMMENT '单位',
  `stock_quantity` int DEFAULT '0' COMMENT '库存数量',
  `minimum_stock` int DEFAULT '0' COMMENT '最小库存量',
  `storage_location` varchar(200) DEFAULT NULL COMMENT '存放位置',
  `supplier` varchar(200) DEFAULT NULL COMMENT '供应商',
  `unit_price` decimal(10,2) DEFAULT '0.00' COMMENT '单价',
  `status` tinyint DEFAULT '1' COMMENT '状态：1正常，0停用',
  `creator_id` bigint DEFAULT NULL COMMENT '创建人ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_accessory_code` (`accessory_code`),
  KEY `idx_accessory_type_id` (`accessory_type_id`),
  KEY `idx_storage_location` (`storage_location`),
  KEY `idx_status` (`status`),
  KEY `fk_accessory_creator` (`creator_id`),
  CONSTRAINT `fk_accessory_creator` FOREIGN KEY (`creator_id`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_accessory_type` FOREIGN KEY (`accessory_type_id`) REFERENCES `accessory_type` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='配件主表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `accessory`
--

LOCK TABLES `accessory` WRITE;
/*!40000 ALTER TABLE `accessory` DISABLE KEYS */;
/*!40000 ALTER TABLE `accessory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `accessory_inspection_record`
--

DROP TABLE IF EXISTS `accessory_inspection_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `accessory_inspection_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `accessory_id` bigint NOT NULL COMMENT '配件ID',
  `inspection_date` datetime NOT NULL COMMENT '检验日期',
  `inspector_id` bigint NOT NULL COMMENT '检验员ID',
  `inspection_quantity` int NOT NULL COMMENT '检验数量',
  `qualified_quantity` int NOT NULL COMMENT '合格数量',
  `unqualified_quantity` int NOT NULL COMMENT '不合格数量',
  `inspection_result` varchar(50) NOT NULL COMMENT '检验结果：合格、不合格、待处理',
  `defect_description` varchar(500) DEFAULT NULL COMMENT '缺陷描述',
  `processing_suggestion` varchar(500) DEFAULT NULL COMMENT '处理建议',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_accessory_id` (`accessory_id`),
  KEY `idx_inspector_id` (`inspector_id`),
  KEY `idx_inspection_date` (`inspection_date`),
  CONSTRAINT `fk_inspection_accessory` FOREIGN KEY (`accessory_id`) REFERENCES `accessory` (`id`),
  CONSTRAINT `fk_inspector_user` FOREIGN KEY (`inspector_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='配件检验记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `accessory_inspection_record`
--

LOCK TABLES `accessory_inspection_record` WRITE;
/*!40000 ALTER TABLE `accessory_inspection_record` DISABLE KEYS */;
/*!40000 ALTER TABLE `accessory_inspection_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `accessory_type`
--

DROP TABLE IF EXISTS `accessory_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `accessory_type` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '类型ID',
  `type_name` varchar(100) NOT NULL COMMENT '类型名称',
  `description` varchar(200) DEFAULT NULL COMMENT '类型描述',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_accessory_type_name` (`type_name`)
) ENGINE=InnoDB AUTO_INCREMENT=67 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='配件类型表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `accessory_type`
--

LOCK TABLES `accessory_type` WRITE;
/*!40000 ALTER TABLE `accessory_type` DISABLE KEYS */;
INSERT INTO `accessory_type` VALUES (1,'标准件','标准规格的通用配件'),(2,'易损件','容易磨损需要经常更换的配件'),(3,'特殊件','特定模具专用的特殊配件'),(4,'工具','用于模具维护和操作的工具'),(5,'耗材','模具生产过程中消耗的材料'),(47,'鏍囧噯浠�','鏍囧噯瑙勬牸鐨勯�氱敤閰嶄欢'),(48,'鏄撴崯浠�','瀹规槗纾ㄦ崯闇�瑕佺粡甯告洿鎹㈢殑閰嶄欢'),(49,'鐗规畩浠�','鐗瑰畾妯″叿涓撶敤鐨勭壒娈婇厤浠�'),(50,'宸ュ叿','鐢ㄤ簬妯″叿缁存姢鍜屾搷浣滅殑宸ュ叿'),(51,'鑰楁潗','妯″叿鐢熶骇杩囩▼涓秷鑰楃殑鏉愭枡');
/*!40000 ALTER TABLE `accessory_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `consumable_item`
--

DROP TABLE IF EXISTS `consumable_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `consumable_item` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `item_name` varchar(200) NOT NULL,
  `item_category` varchar(100) DEFAULT NULL,
  `item_spec` varchar(200) DEFAULT NULL,
  `current_stock` int DEFAULT NULL,
  `unit` varchar(50) DEFAULT NULL,
  `safety_stock` int DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `material_code` varchar(200) DEFAULT NULL,
  `specification` varchar(200) DEFAULT NULL,
  `min_stock` int DEFAULT NULL,
  `max_stock` int DEFAULT NULL,
  `stock_status` varchar(50) DEFAULT NULL,
  `unit_price` double DEFAULT NULL,
  `total_value` double DEFAULT NULL,
  `supplier` varchar(200) DEFAULT NULL,
  `location` varchar(200) DEFAULT NULL,
  `remarks` text,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_material_code` (`material_code`),
  KEY `idx_item_name` (`item_name`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `consumable_item`
--

LOCK TABLES `consumable_item` WRITE;
/*!40000 ALTER TABLE `consumable_item` DISABLE KEYS */;
INSERT INTO `consumable_item` VALUES (6,'Stavax ESR 钢材','材料类',NULL,4,'块',NULL,'2025-12-17 13:38:50','2025-12-17 13:38:50','MJG-42','180*180*22 HRC36-38',2,10,'充足',0,0,'','',''),(10,'深孔钻钻头','消耗类',NULL,11,'支',NULL,'2025-12-23 15:08:15','2025-12-23 15:08:15','SKZT-4','D1.1*125',4,15,'充足',0,0,'','',''),(11,'深孔钻钻头','消耗类',NULL,7,'支',NULL,'2025-12-23 15:16:39','2025-12-23 15:16:39','SKZT-14','D1.6*125',2,10,'充足',0,0,'','',''),(12,'深孔钻钻头','消耗类',NULL,5,'支',NULL,'2025-12-23 15:37:05','2025-12-23 15:37:05','SKZT-16','D1.8*125',2,15,'充足',0,0,'','','');
/*!40000 ALTER TABLE `consumable_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `deep_hole_process`
--

DROP TABLE IF EXISTS `deep_hole_process`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `deep_hole_process` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `mold_id` bigint DEFAULT NULL,
  `mold_code` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `equipment_id` bigint DEFAULT NULL,
  `equipment_name` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `craft_type` int DEFAULT NULL COMMENT '1-分层，2-预钻',
  `status` int DEFAULT NULL COMMENT '状态：0-未开始，1-进行中，2-已完成',
  `planned_layer_count` int DEFAULT NULL,
  `expected_finish_time` datetime DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `deep_hole_process`
--

LOCK TABLES `deep_hole_process` WRITE;
/*!40000 ALTER TABLE `deep_hole_process` DISABLE KEYS */;
INSERT INTO `deep_hole_process` VALUES (1,NULL,'TEST001',NULL,'深孔钻机1',1,0,5,'2025-11-15 18:00:00','2025-11-13 04:32:57','2025-11-13 04:32:57');
/*!40000 ALTER TABLE `deep_hole_process` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `department`
--

DROP TABLE IF EXISTS `department`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `department` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `status` tinyint DEFAULT '1',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `parent_id` bigint DEFAULT NULL,
  `sort_order` int DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_name` (`name`),
  KEY `idx_status` (`status`),
  KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `department`
--

LOCK TABLES `department` WRITE;
/*!40000 ALTER TABLE `department` DISABLE KEYS */;
INSERT INTO `department` VALUES (1,'技术部',1,'2025-12-19 05:15:00','2025-12-20 03:16:30',NULL,0),(2,'生产部',1,'2025-12-19 05:15:00','2025-12-19 05:15:00',NULL,0),(3,'品质部',1,'2025-12-19 05:15:00','2025-12-20 03:16:43',NULL,0),(4,'设备部',1,'2025-12-19 05:15:00','2025-12-19 05:15:00',NULL,0),(6,'模具车间',1,'2025-12-19 05:15:00','2025-12-19 05:15:00',NULL,0),(8,'成型车间',1,'2025-12-19 12:25:33','2025-12-20 03:14:30',2,0),(9,'模具加工组',1,'2025-12-20 03:15:21','2025-12-20 03:15:21',6,1),(10,'模具调试组',1,'2025-12-20 03:15:39','2025-12-20 03:15:39',6,0),(12,'销售部',1,'2025-12-20 03:17:18','2025-12-20 03:17:18',NULL,0),(13,'财务部',1,'2025-12-20 03:17:28','2025-12-20 03:17:28',NULL,0),(14,'采购部',1,'2025-12-20 09:07:29','2025-12-20 09:07:29',NULL,0);
/*!40000 ALTER TABLE `department` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `equipment`
--

DROP TABLE IF EXISTS `equipment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `equipment` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `equipment_code` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `equipment_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `equipment_type_id` bigint NOT NULL,
  `status` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT '正常',
  `location` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `manufacture_date` date DEFAULT NULL,
  `manufacturer` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `last_maintain_date` date DEFAULT NULL,
  `next_maintain_date` date DEFAULT NULL,
  `description` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `equipment_code` (`equipment_code`),
  KEY `fk_equipment_type` (`equipment_type_id`),
  CONSTRAINT `fk_equipment_type` FOREIGN KEY (`equipment_type_id`) REFERENCES `equipment_type` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=67 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `equipment`
--

LOCK TABLES `equipment` WRITE;
/*!40000 ALTER TABLE `equipment` DISABLE KEYS */;
INSERT INTO `equipment` VALUES (1,'EQ001','注塑机1号',1,'正常','生产车间A区','2020-01-15','海天注塑机','2023-12-01','2024-03-01','1200吨注塑机','2026-01-06 08:57:39','2026-01-06 08:57:39'),(2,'EQ002','注塑机2号',1,'正常','生产车间A区','2020-03-20','海天注塑机','2023-12-15','2024-03-15','800吨注塑机','2026-01-06 08:57:39','2026-01-06 08:57:39'),(3,'EQ003','冲压机1号',2,'正常','生产车间B区','2019-11-10','徐锻压机','2023-11-20','2024-02-20','200吨冲床','2026-01-06 08:57:39','2026-01-06 08:57:39'),(4,'EQ004','数控铣床1号',4,'维修','模具车间','2021-05-08','牧野机床','2023-10-10','2024-01-10','高精度数控铣床','2026-01-06 08:57:39','2026-01-06 08:57:39'),(5,'EQ005','电火花机床1号',5,'正常','模具车间','2021-08-15','沙迪克','2023-12-05','2024-03-05','精密电火花加工机床','2026-01-06 08:57:39','2026-01-06 08:57:39');
/*!40000 ALTER TABLE `equipment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `equipment_accessory_replacement_record`
--

DROP TABLE IF EXISTS `equipment_accessory_replacement_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `equipment_accessory_replacement_record` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `equipment_id` bigint NOT NULL,
  `accessory_id` bigint DEFAULT NULL,
  `accessory_name` varchar(255) DEFAULT NULL,
  `accessory_code` varchar(255) DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `replace_time` datetime DEFAULT NULL,
  `reason` varchar(500) DEFAULT NULL,
  `operator_user_id` bigint DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_earr_equipment_id` (`equipment_id`),
  KEY `idx_earr_replace_time` (`replace_time`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `equipment_accessory_replacement_record`
--

LOCK TABLES `equipment_accessory_replacement_record` WRITE;
/*!40000 ALTER TABLE `equipment_accessory_replacement_record` DISABLE KEYS */;
INSERT INTO `equipment_accessory_replacement_record` VALUES (1,1001,NULL,'??','F-001',1,'2025-11-15 22:25:03','????',NULL,NULL);
/*!40000 ALTER TABLE `equipment_accessory_replacement_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `equipment_maintenance_cycle`
--

DROP TABLE IF EXISTS `equipment_maintenance_cycle`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `equipment_maintenance_cycle` (
  `equipment_id` bigint NOT NULL,
  `cycle_days` int NOT NULL,
  PRIMARY KEY (`equipment_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `equipment_maintenance_cycle`
--

LOCK TABLES `equipment_maintenance_cycle` WRITE;
/*!40000 ALTER TABLE `equipment_maintenance_cycle` DISABLE KEYS */;
INSERT INTO `equipment_maintenance_cycle` VALUES (101,30),(1001,60);
/*!40000 ALTER TABLE `equipment_maintenance_cycle` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `equipment_maintenance_record`
--

DROP TABLE IF EXISTS `equipment_maintenance_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `equipment_maintenance_record` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `equipment_id` bigint NOT NULL,
  `maintenance_time` datetime DEFAULT NULL,
  `content` varchar(1000) DEFAULT NULL,
  `operator_user_id` bigint DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_emr_equipment_id` (`equipment_id`),
  KEY `idx_emr_maintenance_time` (`maintenance_time`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `equipment_maintenance_record`
--

LOCK TABLES `equipment_maintenance_record` WRITE;
/*!40000 ALTER TABLE `equipment_maintenance_record` DISABLE KEYS */;
INSERT INTO `equipment_maintenance_record` VALUES (1,1001,'2025-11-15 22:24:24','????',2,NULL);
/*!40000 ALTER TABLE `equipment_maintenance_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `equipment_type`
--

DROP TABLE IF EXISTS `equipment_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `equipment_type` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `type_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `type_code` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `type_code` (`type_code`)
) ENGINE=InnoDB AUTO_INCREMENT=67 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `equipment_type`
--

LOCK TABLES `equipment_type` WRITE;
/*!40000 ALTER TABLE `equipment_type` DISABLE KEYS */;
INSERT INTO `equipment_type` VALUES (1,'注塑机','INJECTION_MACHINE','用于塑料注塑成型的设备','2026-01-06 08:57:39','2026-01-06 08:57:39'),(2,'冲压机','PRESS_MACHINE','用于金属冲压的设备','2026-01-06 08:57:39','2026-01-06 08:57:39'),(3,'压铸机','DIE_CASTING_MACHINE','用于金属压铸的设备','2026-01-06 08:57:39','2026-01-06 08:57:39'),(4,'数控铣床','CNC_MILLING','用于模具加工的数控机床','2026-01-06 08:57:39','2026-01-06 08:57:39'),(5,'电火花机床','EDM_MACHINE','用于模具精密加工的电火花设备','2026-01-06 08:57:39','2026-01-06 08:57:39');
/*!40000 ALTER TABLE `equipment_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `flyway_schema_history`
--

DROP TABLE IF EXISTS `flyway_schema_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `flyway_schema_history` (
  `installed_rank` int NOT NULL,
  `version` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `description` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `type` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `script` varchar(1000) COLLATE utf8mb4_unicode_ci NOT NULL,
  `checksum` int DEFAULT NULL,
  `installed_by` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `installed_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `execution_time` int NOT NULL,
  `success` tinyint(1) NOT NULL,
  PRIMARY KEY (`installed_rank`),
  KEY `flyway_schema_history_s_idx` (`success`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `flyway_schema_history`
--

LOCK TABLES `flyway_schema_history` WRITE;
/*!40000 ALTER TABLE `flyway_schema_history` DISABLE KEYS */;
INSERT INTO `flyway_schema_history` VALUES (1,'1','Create business tables','SQL','V1__init_schema.sql',1354151747,'at_mj_1_user','2025-11-06 13:24:09',123,1),(2,'2','Create business tables by requirements','SQL','V2__seed_admin.sql',286024955,'at_mj_1_user','2025-11-06 13:24:09',14,1),(3,'3','Create mold process tables','SQL','V3__align_existing_schema.sql',-2131175256,'at_mj_1_user','2025-11-06 13:24:09',3,1),(4,'1','Create business tables','DELETE','V1__init_schema.sql',1354151747,'at_mj_1_user','2025-11-08 23:41:16',0,1),(5,'2','Create business tables by requirements','DELETE','V2__seed_admin.sql',286024955,'at_mj_1_user','2025-11-08 23:41:16',0,1),(6,'3','Create mold process tables','DELETE','V3__align_existing_schema.sql',-1574197741,'at_mj_1_user','2025-11-08 23:41:16',0,1),(7,'4','Create operation log table','SQL','V4__Create_operation_log_table.sql',655864758,'at_mj_1_user','2025-11-08 23:41:17',74,1),(8,'5','Alter operation log add ip','SQL','V5__Alter_operation_log_add_ip.sql',2114034131,'at_mj_1_user','2025-11-09 00:39:53',82,1),(9,'6','Alter tables add missing columns','SQL','V6__Alter_tables_add_missing_columns.sql',-2018640261,'at_mj_1_user','2025-11-11 01:50:26',57,1),(10,'7','Create process exception table','SQL','V7__Create_process_exception_table.sql',-431341717,'at_mj_1_user','2025-11-11 03:02:31',438,1),(11,'3','Create mold process tables','SQL','V3__Create_mold_process_tables.sql',-2131175256,'at_mj_1_user','2025-11-11 05:31:17',26,1),(12,'10','Seed sample mold process','SQL','V10__Seed_sample_mold_process.sql',-564611344,'at_mj_1_user','2025-11-11 05:32:37',8,1),(13,'11','Align mold process schema','SQL','V11__Align_mold_process_schema.sql',4244218,'at_mj_1_user','2025-11-11 05:55:58',451,1),(14,'12','Add missing columns for mold process','SQL','V12__Add_missing_columns_for_mold_process.sql',-825307987,'at_mj_1_user','2025-11-11 06:05:58',75,1),(15,'13','Align mold process schema to entity','SQL','V13__Align_mold_process_schema_to_entity.sql',817302465,'at_mj_1_user','2025-11-11 07:00:11',36,1),(16,'14','Add priority column to mold process','SQL','V14__Add_priority_column_to_mold_process.sql',114477459,'at_mj_1_user','2025-11-11 07:18:14',40,1),(17,'15','Align process status history schema','SQL','V15__Align_process_status_history_schema.sql',30343856,'at_mj_1_user','2025-11-11 08:55:26',196,1),(18,'16','Insert dev test mold process','SQL','V16__Insert_dev_test_mold_process.sql',1356774985,'at_mj_1_user','2025-11-11 08:58:07',6,1);
/*!40000 ALTER TABLE `flyway_schema_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inspection_result`
--

DROP TABLE IF EXISTS `inspection_result`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `inspection_result` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `process_id` bigint NOT NULL COMMENT '流程ID',
  `inspector_id` bigint NOT NULL COMMENT '检验员ID',
  `inspection_time` datetime NOT NULL COMMENT '检验时间',
  `inspection_result` varchar(20) NOT NULL COMMENT '检验结果：PASS/FAIL',
  `quality_score` decimal(5,2) NOT NULL COMMENT '质量评分',
  `defect_count` int NOT NULL DEFAULT '0' COMMENT '缺陷数量',
  `major_defect_count` int NOT NULL DEFAULT '0' COMMENT '主要缺陷数量',
  `minor_defect_count` int NOT NULL DEFAULT '0' COMMENT '次要缺陷数量',
  `inspection_standard` varchar(100) DEFAULT NULL COMMENT '检验标准',
  `inspection_method` varchar(100) DEFAULT NULL COMMENT '检验方法',
  `inspection_details` text COMMENT '检验详情',
  `improvement_suggestions` text COMMENT '改进建议',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人',
  `update_by` bigint DEFAULT NULL COMMENT '更新人',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='质量检验结果表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inspection_result`
--

LOCK TABLES `inspection_result` WRITE;
/*!40000 ALTER TABLE `inspection_result` DISABLE KEYS */;
/*!40000 ALTER TABLE `inspection_result` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `log`
--

DROP TABLE IF EXISTS `log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `log` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint DEFAULT NULL,
  `username` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `operation_type` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `operation_content` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `operation_ip` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `operation_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `success_flag` tinyint DEFAULT '1',
  `error_message` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_log_user` (`user_id`),
  CONSTRAINT `fk_log_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `log`
--

LOCK TABLES `log` WRITE;
/*!40000 ALTER TABLE `log` DISABLE KEYS */;
/*!40000 ALTER TABLE `log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mold`
--

DROP TABLE IF EXISTS `mold`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mold` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `mold_code` varchar(50) NOT NULL,
  `mold_name` varchar(100) NOT NULL,
  `mold_type_id` bigint NOT NULL,
  `mold_status_id` bigint NOT NULL,
  `responsible_user_id` bigint NOT NULL,
  `design_life` int DEFAULT NULL,
  `current_usage_count` int DEFAULT NULL,
  `storage_time` datetime DEFAULT NULL,
  `estimated_scrap_time` datetime DEFAULT NULL,
  `remark` varchar(500) DEFAULT NULL,
  `material` varchar(50) DEFAULT NULL,
  `length` double DEFAULT NULL,
  `width` double DEFAULT NULL,
  `height` double DEFAULT NULL,
  `weight` double DEFAULT NULL,
  `location` varchar(100) DEFAULT NULL,
  `version` varchar(50) DEFAULT NULL,
  `design_department` varchar(100) DEFAULT NULL,
  `manufacturer` varchar(100) DEFAULT NULL,
  `purchase_date` datetime DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_mold_code` (`mold_code`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mold`
--

LOCK TABLES `mold` WRITE;
/*!40000 ALTER TABLE `mold` DISABLE KEYS */;
INSERT INTO `mold` VALUES (1,'B1000','110-400',1,1,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2025-11-18 19:14:12','2025-11-18 19:14:12'),(3,'B111','110-400',1,1,18,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2025-11-19 16:36:29','2025-11-19 16:36:29'),(4,'M001','流程',1,1,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2025-11-21 19:33:54','2025-11-21 19:33:54');
/*!40000 ALTER TABLE `mold` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mold_borrow_return_record`
--

DROP TABLE IF EXISTS `mold_borrow_return_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mold_borrow_return_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `mold_id` bigint NOT NULL COMMENT '模具ID',
  `borrow_user_id` bigint NOT NULL COMMENT '借用人ID',
  `borrow_date` datetime NOT NULL COMMENT '借用日期',
  `expected_return_date` datetime DEFAULT NULL COMMENT '预计归还日期',
  `actual_return_date` datetime DEFAULT NULL COMMENT '实际归还日期',
  `return_operator_id` bigint DEFAULT NULL COMMENT '归还操作人ID',
  `borrow_purpose` varchar(500) DEFAULT NULL COMMENT '借用用途',
  `borrow_quantity` int DEFAULT '1' COMMENT '借用数量',
  `return_quantity` int DEFAULT '0' COMMENT '归还数量',
  `status` varchar(50) DEFAULT 'BORROWED' COMMENT '状态：BORROWED已借用，RETURNED已归还',
  `borrow_department` varchar(100) DEFAULT NULL COMMENT '借用部门',
  `inspection_result` varchar(50) DEFAULT NULL COMMENT '归还检验结果',
  `inspection_note` varchar(500) DEFAULT NULL COMMENT '归还检验备注',
  `is_overdue` tinyint DEFAULT '0' COMMENT '是否逾期',
  `overdue_days` int DEFAULT '0' COMMENT '逾期天数',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_mold_id` (`mold_id`),
  KEY `idx_borrow_user_id` (`borrow_user_id`),
  KEY `idx_return_operator_id` (`return_operator_id`),
  KEY `idx_borrow_date` (`borrow_date`),
  KEY `idx_actual_return_date` (`actual_return_date`),
  KEY `idx_status` (`status`),
  CONSTRAINT `fk_borrow_return_mold` FOREIGN KEY (`mold_id`) REFERENCES `mold` (`id`),
  CONSTRAINT `fk_borrow_user` FOREIGN KEY (`borrow_user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_return_operator` FOREIGN KEY (`return_operator_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='模具借用归还记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mold_borrow_return_record`
--

LOCK TABLES `mold_borrow_return_record` WRITE;
/*!40000 ALTER TABLE `mold_borrow_return_record` DISABLE KEYS */;
/*!40000 ALTER TABLE `mold_borrow_return_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mold_initial_parameter`
--

DROP TABLE IF EXISTS `mold_initial_parameter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mold_initial_parameter` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `application_no` varchar(100) DEFAULT NULL,
  `category` varchar(100) DEFAULT NULL,
  `mold_code` varchar(100) DEFAULT NULL,
  `product_spec` varchar(255) DEFAULT NULL,
  `material` varchar(100) DEFAULT NULL,
  `hrc` varchar(50) DEFAULT NULL,
  `structure` varchar(255) DEFAULT NULL,
  `total_shrinkage` double DEFAULT NULL,
  `core_size` varchar(100) DEFAULT NULL,
  `outline` varchar(255) DEFAULT NULL,
  `thickness` double DEFAULT NULL,
  `location_hole_pitch` double DEFAULT NULL,
  `inlet_diameter` double DEFAULT NULL,
  `hole_count` int DEFAULT NULL,
  `hole_depth` double DEFAULT NULL,
  `porosity_type` varchar(100) DEFAULT NULL,
  `slot_width` double DEFAULT NULL,
  `slot_depth` double DEFAULT NULL,
  `cut_in_amount` double DEFAULT NULL,
  `center_distance` double DEFAULT NULL,
  `feed_ratio` double DEFAULT NULL,
  `core_step_height` double DEFAULT NULL,
  `owner_name` varchar(100) DEFAULT NULL,
  `remark` varchar(500) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_mip_mold_code` (`mold_code`),
  KEY `idx_mip_application_no` (`application_no`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mold_initial_parameter`
--

LOCK TABLES `mold_initial_parameter` WRITE;
/*!40000 ALTER TABLE `mold_initial_parameter` DISABLE KEYS */;
INSERT INTO `mold_initial_parameter` VALUES (17,'Y251009A','TWC国五','B0458s','130-400','Stavax ESR 钢材','38±2°','斜边模',5,'φ136.84','φ169',NULL,160,1.4,4850,12.6,'间孔',0.17,3.2,0.3,1.337,1.81,2.4,'罗京','',NULL,NULL);
/*!40000 ALTER TABLE `mold_initial_parameter` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mold_process`
--

DROP TABLE IF EXISTS `mold_process`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mold_process` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `process_code` varchar(50) NOT NULL COMMENT '流程编号',
  `process_name` varchar(100) NOT NULL COMMENT '流程名称',
  `mold_id` bigint NOT NULL COMMENT '模具ID',
  `equipment_id` bigint NOT NULL COMMENT '设备ID',
  `operator_id` bigint NOT NULL COMMENT '操作员ID',
  `previous_status` varchar(50) DEFAULT NULL COMMENT '上一个状态',
  `planned_start_time` datetime DEFAULT NULL COMMENT '计划开始时间',
  `planned_end_time` datetime DEFAULT NULL COMMENT '计划结束时间',
  `planned_quantity` int NOT NULL DEFAULT '0' COMMENT '计划生产数量',
  `actual_start_time` datetime DEFAULT NULL COMMENT '实际开始时间',
  `actual_end_time` datetime DEFAULT NULL COMMENT '实际结束时间',
  `actual_quantity` int NOT NULL DEFAULT '0' COMMENT '实际生产数量',
  `qualified_quantity` int NOT NULL DEFAULT '0' COMMENT '合格数量',
  `defective_quantity` int NOT NULL DEFAULT '0' COMMENT '不良数量',
  `estimated_remaining_time` int DEFAULT NULL COMMENT '预计剩余时间（分钟）',
  `quality_score` decimal(5,2) DEFAULT NULL COMMENT '质量评分',
  `inspection_result` varchar(20) DEFAULT NULL COMMENT '检验结果：PASS/FAIL/PENDING',
  `inspection_remark` varchar(500) DEFAULT NULL COMMENT '检验备注',
  `has_exception` tinyint NOT NULL DEFAULT '0' COMMENT '是否有异常：0-无，1-有',
  `exception_count` int NOT NULL DEFAULT '0' COMMENT '异常次数',
  `description` text,
  `remark` text,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人',
  `update_by` bigint DEFAULT NULL COMMENT '更新人',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除：0-未删除，1-已删除',
  `current_progress` int NOT NULL DEFAULT '0' COMMENT '当前进度（百分比，0-100）',
  `last_pause_time` datetime DEFAULT NULL COMMENT '最后一次暂停时间',
  `current_status` int NOT NULL DEFAULT '0' COMMENT '当前状态（0:CREATED/PENDING,1:PLANNED/PREPARING,2:IN_PROGRESS/PROCESSING,3:PAUSED,4:COMPLETED,5:CANCELLED,6:FAILED/EXCEPTION)',
  `production_task_id` bigint DEFAULT NULL COMMENT '生产任务ID',
  `responsible_user_id` bigint DEFAULT NULL COMMENT '责任人ID',
  `estimated_duration` int DEFAULT NULL COMMENT '预估工时（分钟）',
  `actual_duration` int DEFAULT NULL COMMENT '实际工时（分钟）',
  `priority` int DEFAULT NULL COMMENT '优先级：1-低，2-中，3-高',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_process_code` (`process_code`),
  KEY `idx_mold_process_mold` (`mold_id`),
  KEY `idx_mold_process_equipment` (`equipment_id`),
  KEY `idx_mold_process_operator` (`operator_id`),
  KEY `idx_mold_process_planned_time` (`planned_start_time`,`planned_end_time`),
  KEY `idx_mold_process_actual_time` (`actual_start_time`,`actual_end_time`),
  KEY `idx_mold_process_exception` (`has_exception`),
  KEY `idx_mold_process_create_time` (`create_time`),
  CONSTRAINT `fk_process_operator` FOREIGN KEY (`operator_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='模具加工流程主表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mold_process`
--

LOCK TABLES `mold_process` WRITE;
/*!40000 ALTER TABLE `mold_process` DISABLE KEYS */;
INSERT INTO `mold_process` VALUES (2,'DEV-TEST-001','开发联调测试流程',0,0,1,NULL,'2025-11-11 16:58:07',NULL,0,'2025-11-14 20:49:18','2025-11-14 21:00:27',0,0,0,NULL,NULL,NULL,NULL,0,0,NULL,NULL,'2025-11-11 16:58:07','2025-11-15 14:56:08',NULL,NULL,0,100,NULL,4,0,NULL,NULL,11,2),(7,'B9999x',' 流程演示',0,0,4,NULL,NULL,NULL,0,'2025-11-15 08:33:32','2025-11-15 08:33:48',0,0,0,NULL,NULL,NULL,NULL,0,0,'类别:TWC5 材料:S-STAR 孔密度:100','demo','2025-11-15 08:29:54','2025-11-15 08:37:24',NULL,NULL,0,100,NULL,4,NULL,NULL,NULL,0,NULL),(9,'B8291x',' 演示流程',0,0,5,NULL,NULL,NULL,0,'2025-11-15 10:45:14','2025-11-15 10:45:35',0,0,0,NULL,NULL,NULL,NULL,0,0,'类别:TWC5 材料:S-STAR 孔密度:100','demo','2025-11-15 10:37:42','2025-11-15 20:44:32',NULL,NULL,0,100,NULL,4,NULL,NULL,NULL,0,NULL),(10,'B4200x','演示流程',0,0,6,NULL,NULL,NULL,0,NULL,NULL,0,0,0,NULL,NULL,NULL,NULL,0,0,'类别:TWC5 材料:S-STAR 孔密度:100','demo','2025-11-15 10:57:41','2025-11-17 12:33:54',NULL,NULL,0,100,NULL,0,NULL,NULL,NULL,NULL,NULL),(11,'B8813x','演示流程',0,0,7,NULL,NULL,NULL,0,'2025-11-15 11:25:05','2025-11-15 11:27:01',0,0,0,NULL,NULL,NULL,NULL,0,0,'类别:TWC5 材料:S-STAR 孔密度:100','demo','2025-11-15 11:04:47','2025-11-15 11:50:28',NULL,NULL,0,100,NULL,4,NULL,NULL,NULL,1,NULL),(12,'B0428s','蜂窝陶瓷-B0428s-test2',0,0,6,NULL,NULL,NULL,0,'2025-11-15 13:30:33','2025-11-15 13:31:09',0,0,0,NULL,NULL,NULL,NULL,0,0,'类别:TWC国五 材料:堇青石 孔密度:400','流程路线测试','2025-11-15 13:24:09','2025-11-15 13:31:09',NULL,NULL,0,100,NULL,4,NULL,NULL,NULL,0,NULL),(15,'B0428s-6362','蜂窝陶瓷-B0428s-6362',0,0,6,NULL,NULL,NULL,0,NULL,'2025-11-15 20:46:35',0,0,0,NULL,NULL,NULL,NULL,0,0,'类别:TWC国五 材料:堇青石 孔密度:400','流程路线：深孔钻(分层) → 精雕机 → 电火花成型','2025-11-15 13:40:01','2025-11-15 20:46:51',NULL,NULL,0,100,NULL,5,NULL,NULL,NULL,NULL,NULL),(16,'C90382','蜂窝陶瓷-C90382',0,0,6,NULL,NULL,NULL,0,NULL,NULL,0,0,0,NULL,NULL,NULL,NULL,0,0,'Category:TWC国五 Material:堇青石 Cavity:400','流程路线：深孔钻(分层) → 精雕机 → 电火花成型','2025-11-15 13:41:22','2025-11-15 13:41:22',NULL,NULL,0,0,NULL,0,NULL,NULL,NULL,NULL,NULL),(23,'FLOW-DEV-89749','??????',0,0,8,NULL,NULL,NULL,0,'2025-11-15 21:17:38','2025-11-15 21:19:06',0,0,0,NULL,NULL,NULL,NULL,0,0,'类别:TWC 材料:S-STAR 孔密度:100','demo','2025-11-15 21:11:48','2025-11-15 21:19:06',NULL,NULL,0,100,NULL,4,NULL,NULL,NULL,1,NULL),(34,'V0450u','??????',0,0,1,NULL,NULL,NULL,0,NULL,NULL,0,0,0,NULL,NULL,NULL,NULL,0,0,'类别:VOC 材料:M303 孔密度:38','??:????','2025-11-17 11:17:28','2025-11-17 11:17:28',NULL,NULL,0,0,NULL,0,NULL,NULL,NULL,NULL,NULL),(35,'V0450','100*100-300',0,0,1,NULL,NULL,NULL,0,NULL,NULL,0,0,0,NULL,NULL,NULL,NULL,0,0,'类别:VOC 材料:M303 钢材 150*150*22 孔密度:2455','来源：初始参数(S251101A)','2025-11-17 11:24:43','2025-11-17 11:25:26',NULL,NULL,0,100,NULL,0,NULL,NULL,NULL,NULL,NULL),(37,'B111','110-400',3,0,18,NULL,NULL,NULL,0,NULL,NULL,0,0,0,NULL,NULL,NULL,NULL,0,0,NULL,NULL,'2025-11-19 16:36:29','2025-11-19 16:36:29',NULL,NULL,0,0,NULL,0,NULL,NULL,NULL,NULL,NULL),(38,'M001','流程',4,0,1,NULL,NULL,NULL,0,NULL,NULL,0,0,0,NULL,NULL,NULL,NULL,0,0,NULL,NULL,'2025-11-21 19:33:54','2025-11-21 19:33:54',NULL,NULL,0,0,NULL,0,NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `mold_process` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mold_status`
--

DROP TABLE IF EXISTS `mold_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mold_status` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `status_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `status_code` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `status_code` (`status_code`)
) ENGINE=InnoDB AUTO_INCREMENT=137 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mold_status`
--

LOCK TABLES `mold_status` WRITE;
/*!40000 ALTER TABLE `mold_status` DISABLE KEYS */;
INSERT INTO `mold_status` VALUES (1,'在库','IN_STORAGE',NULL,'2026-01-06 08:57:39','2026-01-06 08:57:39'),(2,'使用中','IN_USE',NULL,'2026-01-06 08:57:39','2026-01-06 08:57:39'),(3,'维护中','MAINTENANCE',NULL,'2026-01-06 08:57:39','2026-01-06 08:57:39'),(4,'报废','SCRAPPED',NULL,'2026-01-06 08:57:39','2026-01-06 08:57:39'),(5,'维修中','REPAIRING',NULL,'2026-01-06 08:57:39','2026-01-06 08:57:39');
/*!40000 ALTER TABLE `mold_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mold_storage_record`
--

DROP TABLE IF EXISTS `mold_storage_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mold_storage_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `mold_id` bigint NOT NULL COMMENT '模具ID',
  `storage_date` datetime NOT NULL COMMENT '入库日期',
  `storage_operator_id` bigint NOT NULL COMMENT '入库操作人ID',
  `storage_location` varchar(200) NOT NULL COMMENT '入库位置',
  `inspection_result` varchar(50) DEFAULT NULL COMMENT '检验结果',
  `inspection_note` varchar(500) DEFAULT NULL COMMENT '检验备注',
  `quantity` int DEFAULT '1' COMMENT '数量',
  `is_approved` tinyint DEFAULT '0' COMMENT '是否已审核',
  `approval_user_id` bigint DEFAULT NULL COMMENT '审核人ID',
  `approval_time` datetime DEFAULT NULL COMMENT '审核时间',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_mold_id` (`mold_id`),
  KEY `idx_storage_operator_id` (`storage_operator_id`),
  KEY `idx_approval_user_id` (`approval_user_id`),
  KEY `idx_storage_date` (`storage_date`),
  CONSTRAINT `fk_storage_approval_user` FOREIGN KEY (`approval_user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_storage_mold` FOREIGN KEY (`mold_id`) REFERENCES `mold` (`id`),
  CONSTRAINT `fk_storage_operator` FOREIGN KEY (`storage_operator_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='模具入库记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mold_storage_record`
--

LOCK TABLES `mold_storage_record` WRITE;
/*!40000 ALTER TABLE `mold_storage_record` DISABLE KEYS */;
/*!40000 ALTER TABLE `mold_storage_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mold_type`
--

DROP TABLE IF EXISTS `mold_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mold_type` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `type_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `type_code` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `type_code` (`type_code`)
) ENGINE=InnoDB AUTO_INCREMENT=67 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mold_type`
--

LOCK TABLES `mold_type` WRITE;
/*!40000 ALTER TABLE `mold_type` DISABLE KEYS */;
INSERT INTO `mold_type` VALUES (1,'注塑模具','INJECTION_MOLD','用于注塑成型的模具','2026-01-06 08:57:39','2026-01-06 08:57:39'),(2,'冲压模具','STAMPING_MOLD','用于金属冲压的模具','2026-01-06 08:57:39','2026-01-06 08:57:39'),(3,'压铸模具','DIE_CASTING_MOLD','用于金属压铸的模具','2026-01-06 08:57:39','2026-01-06 08:57:39'),(4,'锻造模具','FORGING_MOLD','用于金属锻造的模具','2026-01-06 08:57:39','2026-01-06 08:57:39'),(5,'吹塑模具','BLOW_MOLDING_MOLD','用于塑料吹塑的模具','2026-01-06 08:57:39','2026-01-06 08:57:39');
/*!40000 ALTER TABLE `mold_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `operation_log`
--

DROP TABLE IF EXISTS `operation_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `operation_log` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'Primary key ID',
  `username` varchar(50) DEFAULT NULL COMMENT 'Operator username',
  `user_id` bigint DEFAULT NULL COMMENT 'Operator user ID',
  `ip` varchar(45) DEFAULT NULL COMMENT 'Client IP address',
  `operation_type` varchar(50) DEFAULT NULL COMMENT 'Operation type',
  `operation_desc` varchar(255) DEFAULT NULL COMMENT 'Operation description',
  `operation_content` text COMMENT 'Operation content (JSON)',
  `result` varchar(20) DEFAULT 'SUCCESS' COMMENT 'Operation result: SUCCESS/FAIL',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT 'Create time',
  `is_sensitive` tinyint(1) DEFAULT '0' COMMENT '是否敏感操作',
  `sensitive_level` varchar(20) DEFAULT 'normal' COMMENT '敏感级别(normal/high/critical)',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_operation_type` (`operation_type`)
) ENGINE=InnoDB AUTO_INCREMENT=535 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Operation Log table';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `operation_log`
--

LOCK TABLES `operation_log` WRITE;
/*!40000 ALTER TABLE `operation_log` DISABLE KEYS */;
INSERT INTO `operation_log` VALUES (1,'admin',1,'0:0:0:0:0:0:0:1','resetUserPassword','Reset user password','{\"error\": \"BadSqlGrammarException: \r\n### Error querying database.  Cause: java.sql.SQLSyntaxErrorException: Unknown column \'department\' in \'field list\'\r\n### The error may exist in com/mold/digitalization/mapper/UserMapper.java (best guess)\r\n### The error may involve defaultParameterMap\r\n### The error occurred while setting parameters\r\n### SQL: SELECT id,username,password,department,role,phone,status,create_time,update_time FROM `user` WHERE id=?\r\n### Cause: java.sql.SQLSyntaxErrorException: Unknown column \'department\' in \'field list\'\n; bad SQL grammar []; nested exception is java.sql.SQLSyntaxErrorException: Unknown column \'department\' in \'field list\'\"}','SUCCESS','2025-11-09 09:40:04',0,'normal'),(2,'admin',1,'0:0:0:0:0:0:0:1','resetUserPassword','Reset user password','{\"error\": \"BadSqlGrammarException: \r\n### Error querying database.  Cause: java.sql.SQLSyntaxErrorException: Unknown column \'department\' in \'field list\'\r\n### The error may exist in com/mold/digitalization/mapper/UserMapper.java (best guess)\r\n### The error may involve defaultParameterMap\r\n### The error occurred while setting parameters\r\n### SQL: SELECT id,username,password,department,role,phone,status,create_time,update_time FROM `user` WHERE id=?\r\n### Cause: java.sql.SQLSyntaxErrorException: Unknown column \'department\' in \'field list\'\n; bad SQL grammar []; nested exception is java.sql.SQLSyntaxErrorException: Unknown column \'department\' in \'field list\'\"}','SUCCESS','2025-11-09 09:40:16',0,'normal'),(3,'admin',1,'0:0:0:0:0:0:0:1','resetUserPassword','Reset user password','{\"error\": \"RedisConnectionFailureException: Unable to connect to Redis; nested exception is io.lettuce.core.RedisConnectionException: Unable to connect to localhost/<unresolved>:6379\"}','SUCCESS','2025-11-09 11:00:37',0,'normal'),(4,'admin',1,'0:0:0:0:0:0:0:1','resetUserPassword','Reset user password','{\"error\": \"RedisConnectionFailureException: Unable to connect to Redis; nested exception is io.lettuce.core.RedisConnectionException: Unable to connect to localhost/<unresolved>:6379\"}','SUCCESS','2025-11-09 11:00:49',0,'normal'),(5,'admin',1,'0:0:0:0:0:0:0:1','resetUserPassword','Reset user password','{\"error\": \"RedisConnectionFailureException: Unable to connect to Redis; nested exception is io.lettuce.core.RedisConnectionException: Unable to connect to localhost/<unresolved>:6379\"}','SUCCESS','2025-11-09 11:01:15',0,'normal'),(6,'admin',1,'0:0:0:0:0:0:0:1','resetUserPassword','Reset user password','{\"error\": \"RedisConnectionFailureException: Unable to connect to Redis; nested exception is io.lettuce.core.RedisConnectionException: Unable to connect to localhost/<unresolved>:6379\"}','SUCCESS','2025-11-09 11:01:56',0,'normal'),(7,'admin',1,'0:0:0:0:0:0:0:1','resetUserPassword','Reset user password','{\"error\": \"RedisConnectionFailureException: Unable to connect to Redis; nested exception is io.lettuce.core.RedisConnectionException: Unable to connect to localhost/<unresolved>:6379\"}','SUCCESS','2025-11-09 16:26:43',0,'normal'),(8,'admin',1,'0:0:0:0:0:0:0:1','resetUserPassword','Reset user password','{\"error\": \"RedisConnectionFailureException: Unable to connect to Redis; nested exception is io.lettuce.core.RedisConnectionException: Unable to connect to localhost/<unresolved>:6379\"}','SUCCESS','2025-11-09 16:26:55',0,'normal'),(9,'admin',1,'0:0:0:0:0:0:0:1','resetUserPassword','Reset user password','{\"error\": \"RedisConnectionFailureException: Unable to connect to Redis; nested exception is io.lettuce.core.RedisConnectionException: Unable to connect to localhost/<unresolved>:6379\"}','SUCCESS','2025-11-09 16:26:59',0,'normal'),(10,'admin',1,'0:0:0:0:0:0:0:1','resetUserPassword','Reset user password','{\"error\": \"RedisConnectionFailureException: Unable to connect to Redis; nested exception is io.lettuce.core.RedisConnectionException: Unable to connect to localhost/<unresolved>:6379\"}','SUCCESS','2025-11-09 16:31:33',0,'normal'),(11,'admin',1,'0:0:0:0:0:0:0:1','resetUserPassword','Reset user password','{\"error\": \"RedisConnectionFailureException: Unable to connect to Redis; nested exception is io.lettuce.core.RedisConnectionException: Unable to connect to localhost/<unresolved>:6379\"}','SUCCESS','2025-11-09 16:31:50',0,'normal'),(12,'admin',1,'0:0:0:0:0:0:0:1','resetUserPassword','Reset user password','{\"error\": \"RedisConnectionFailureException: Unable to connect to Redis; nested exception is io.lettuce.core.RedisConnectionException: Unable to connect to localhost/<unresolved>:6379\"}','SUCCESS','2025-11-09 16:31:53',0,'normal'),(13,'admin',1,'0:0:0:0:0:0:0:1','resetUserPassword','Reset user password','{\"error\": \"RedisConnectionFailureException: Unable to connect to Redis; nested exception is io.lettuce.core.RedisConnectionException: Unable to connect to localhost/<unresolved>:6379\"}','SUCCESS','2025-11-09 16:32:30',0,'normal'),(14,NULL,NULL,NULL,'閲嶇疆密码','閲嶇疆用户密码: admin',NULL,'SUCCESS','2025-11-09 16:37:41',0,'normal'),(15,'admin',1,'0:0:0:0:0:0:0:1','resetUserPassword','Reset user password','<200 OK OK,ResetPasswordResult(userId=1, resetTime=2025-11-09T16:37:41.401, resetTimeDate=Sun Nov 09 16:37:41 CST 2025, defaultPassword=true),[]>','SUCCESS','2025-11-09 16:37:41',0,'normal'),(16,NULL,NULL,NULL,'閲嶇疆密码','閲嶇疆用户密码: admin',NULL,'SUCCESS','2025-11-09 16:37:54',0,'normal'),(17,'admin',1,'0:0:0:0:0:0:0:1','resetUserPassword','Reset user password','<200 OK OK,ResetPasswordResult(userId=1, resetTime=2025-11-09T16:37:54.015, resetTimeDate=Sun Nov 09 16:37:54 CST 2025, defaultPassword=true),[]>','SUCCESS','2025-11-09 16:37:54',0,'normal'),(18,NULL,NULL,NULL,'閲嶇疆密码','閲嶇疆用户密码: user',NULL,'SUCCESS','2025-11-09 16:38:00',0,'normal'),(19,'admin',1,'0:0:0:0:0:0:0:1','resetUserPassword','Reset user password','<200 OK OK,ResetPasswordResult(userId=2, resetTime=2025-11-09T16:38:00.382, resetTimeDate=Sun Nov 09 16:38:00 CST 2025, defaultPassword=true),[]>','SUCCESS','2025-11-09 16:38:00',0,'normal'),(20,NULL,NULL,NULL,'閲嶇疆密码','閲嶇疆用户密码: admin',NULL,'SUCCESS','2025-11-09 16:38:18',0,'normal'),(21,'admin',1,'0:0:0:0:0:0:0:1','resetUserPassword','Reset user password','<200 OK OK,ResetPasswordResult(userId=1, resetTime=2025-11-09T16:38:17.543, resetTimeDate=Sun Nov 09 16:38:17 CST 2025, defaultPassword=true),[]>','SUCCESS','2025-11-09 16:38:18',0,'normal'),(22,NULL,NULL,NULL,'閲嶇疆密码','閲嶇疆用户密码: admin',NULL,'SUCCESS','2025-11-09 16:40:38',0,'normal'),(23,'admin',1,'0:0:0:0:0:0:0:1','resetUserPassword','Reset user password','<200 OK OK,ResetPasswordResult(userId=1, resetTime=2025-11-09T16:40:38.007, resetTimeDate=Sun Nov 09 16:40:38 CST 2025, defaultPassword=true),[]>','SUCCESS','2025-11-09 16:40:38',0,'normal'),(24,NULL,NULL,NULL,'閲嶇疆密码','閲嶇疆用户密码: user',NULL,'SUCCESS','2025-11-09 16:42:27',0,'normal'),(25,'admin',1,'0:0:0:0:0:0:0:1','resetUserPassword','Reset user password','<200 OK OK,ResetPasswordResult(userId=2, resetTime=2025-11-09T16:42:27.039, resetTimeDate=Sun Nov 09 16:42:27 CST 2025, defaultPassword=true),[]>','SUCCESS','2025-11-09 16:42:27',0,'normal'),(26,NULL,NULL,NULL,'閲嶇疆密码','閲嶇疆用户密码: user',NULL,'SUCCESS','2025-11-09 18:30:10',0,'normal'),(27,'admin',1,'0:0:0:0:0:0:0:1','resetUserPassword','Reset user password','<200 OK OK,ResetPasswordResult(userId=2, resetTime=2025-11-09T18:30:10.356, resetTimeDate=Sun Nov 09 18:30:10 CST 2025, defaultPassword=true),[]>','SUCCESS','2025-11-09 18:30:10',0,'normal'),(28,NULL,NULL,NULL,'閲嶇疆密码','閲嶇疆用户密码: user',NULL,'SUCCESS','2025-11-09 18:31:55',0,'normal'),(29,'admin',1,'0:0:0:0:0:0:0:1','resetUserPassword','Reset user password','<200 OK OK,ResetPasswordResult(userId=2, resetTime=2025-11-09T18:31:54.553, resetTimeDate=Sun Nov 09 18:31:54 CST 2025, defaultPassword=true),[]>','SUCCESS','2025-11-09 18:31:55',0,'normal'),(30,NULL,NULL,NULL,'閲嶇疆密码','閲嶇疆用户密码: user',NULL,'SUCCESS','2025-11-09 18:35:02',0,'normal'),(31,'admin',1,'0:0:0:0:0:0:0:1','resetUserPassword','Reset user password','<200 OK OK,ResetPasswordResult(userId=2, resetTime=2025-11-09T18:35:02.427, resetTimeDate=Sun Nov 09 18:35:02 CST 2025, defaultPassword=true),[]>','SUCCESS','2025-11-09 18:35:02',0,'normal'),(32,NULL,NULL,NULL,'閲嶇疆密码','閲嶇疆用户密码: user',NULL,'SUCCESS','2025-11-09 18:35:26',0,'normal'),(33,'admin',1,'0:0:0:0:0:0:0:1','resetUserPassword','Reset user password','<200 OK OK,ResetPasswordResult(userId=2, resetTime=2025-11-09T18:35:26.177, resetTimeDate=Sun Nov 09 18:35:26 CST 2025, defaultPassword=true),[]>','SUCCESS','2025-11-09 18:35:26',0,'normal'),(34,NULL,NULL,NULL,'閲嶇疆密码','閲嶇疆用户密码: user',NULL,'SUCCESS','2025-11-09 21:24:31',0,'normal'),(35,'admin',1,'0:0:0:0:0:0:0:1','resetUserPassword','Reset user password','<200 OK OK,ResetPasswordResult(userId=2, resetTime=2025-11-09T21:24:31.051, resetTimeDate=Sun Nov 09 21:24:31 CST 2025, defaultPassword=true),[]>','SUCCESS','2025-11-09 21:24:31',0,'normal'),(36,NULL,NULL,NULL,'閲嶇疆密码','閲嶇疆用户密码: user',NULL,'SUCCESS','2025-11-09 21:29:09',0,'normal'),(37,'admin',1,'0:0:0:0:0:0:0:1','resetUserPassword','Reset user password','<200 OK OK,ResetPasswordResult(userId=2, resetTime=2025-11-09T21:29:08.729, resetTimeDate=Sun Nov 09 21:29:08 CST 2025, defaultPassword=true),[]>','SUCCESS','2025-11-09 21:29:09',0,'normal'),(38,NULL,NULL,NULL,'閲嶇疆密码','閲嶇疆用户密码: user',NULL,'SUCCESS','2025-11-09 21:29:21',0,'normal'),(39,'admin',1,'0:0:0:0:0:0:0:1','resetUserPassword','Reset user password','<200 OK OK,ResetPasswordResult(userId=2, resetTime=2025-11-09T21:29:20.961, resetTimeDate=Sun Nov 09 21:29:20 CST 2025, defaultPassword=true),[]>','SUCCESS','2025-11-09 21:29:21',0,'normal'),(40,NULL,NULL,NULL,'閲嶇疆密码','閲嶇疆用户密码: user',NULL,'SUCCESS','2025-11-09 21:29:35',0,'normal'),(41,'admin',1,'0:0:0:0:0:0:0:1','resetUserPassword','Reset user password','<200 OK OK,ResetPasswordResult(userId=2, resetTime=2025-11-09T21:29:35.395, resetTimeDate=Sun Nov 09 21:29:35 CST 2025, defaultPassword=true),[]>','SUCCESS','2025-11-09 21:29:35',0,'normal'),(42,NULL,NULL,NULL,'閲嶇疆密码','閲嶇疆用户密码: user',NULL,'SUCCESS','2025-11-09 21:30:57',0,'normal'),(43,'admin',1,'0:0:0:0:0:0:0:1','resetUserPassword','Reset user password','<200 OK OK,ResetPasswordResult(userId=2, resetTime=2025-11-09T21:30:57.432, resetTimeDate=Sun Nov 09 21:30:57 CST 2025, defaultPassword=true),[]>','SUCCESS','2025-11-09 21:30:57',0,'normal'),(44,NULL,NULL,NULL,'閲嶇疆密码','閲嶇疆用户密码: user',NULL,'SUCCESS','2025-11-09 21:37:56',0,'normal'),(45,'admin',1,'0:0:0:0:0:0:0:1','resetUserPassword','Reset user password','<200 OK OK,ResetPasswordResult(userId=2, resetTime=2025-11-09T21:37:55.868, resetTimeDate=Sun Nov 09 21:37:55 CST 2025, defaultPassword=true),[]>','SUCCESS','2025-11-09 21:37:56',0,'normal'),(46,NULL,NULL,NULL,'閲嶇疆密码','閲嶇疆用户密码: user',NULL,'SUCCESS','2025-11-09 21:41:54',0,'normal'),(47,'admin',1,'0:0:0:0:0:0:0:1','resetUserPassword','Reset user password','<200 OK OK,ResetPasswordResult(userId=2, resetTime=2025-11-09T21:41:54.478, resetTimeDate=Sun Nov 09 21:41:54 CST 2025, defaultPassword=true),[]>','SUCCESS','2025-11-09 21:41:54',0,'normal'),(48,NULL,NULL,NULL,'閲嶇疆密码','閲嶇疆用户密码: user',NULL,'SUCCESS','2025-11-09 21:45:30',0,'normal'),(49,'admin',1,'0:0:0:0:0:0:0:1','resetUserPassword','Reset user password','<200 OK OK,ResetPasswordResult(userId=2, resetTime=2025-11-09T21:45:29.890, resetTimeDate=Sun Nov 09 21:45:29 CST 2025, defaultPassword=true),[]>','SUCCESS','2025-11-09 21:45:30',0,'normal'),(50,NULL,NULL,NULL,'閲嶇疆密码','閲嶇疆用户密码: user',NULL,'SUCCESS','2025-11-09 21:47:57',0,'normal'),(51,'admin',1,'0:0:0:0:0:0:0:1','resetUserPassword','Reset user password','<200 OK OK,ResetPasswordResult(userId=2, resetTime=2025-11-09T21:47:57.294, resetTimeDate=Sun Nov 09 21:47:57 CST 2025, defaultPassword=true),[]>','SUCCESS','2025-11-09 21:47:57',0,'normal'),(52,NULL,NULL,NULL,'閲嶇疆密码','閲嶇疆用户密码: user',NULL,'SUCCESS','2025-11-09 21:48:02',0,'normal'),(53,'admin',1,'0:0:0:0:0:0:0:1','resetUserPassword','Reset user password','<200 OK OK,ResetPasswordResult(userId=2, resetTime=2025-11-09T21:48:02.495, resetTimeDate=Sun Nov 09 21:48:02 CST 2025, defaultPassword=true),[]>','SUCCESS','2025-11-09 21:48:02',0,'normal'),(54,NULL,NULL,NULL,'閲嶇疆密码','閲嶇疆用户密码: user',NULL,'SUCCESS','2025-11-09 21:51:50',0,'normal'),(55,'admin',1,'0:0:0:0:0:0:0:1','resetUserPassword','Reset user password','<200 OK OK,ResetPasswordResult(userId=2, resetTime=2025-11-09T21:51:49.666, resetTimeDate=Sun Nov 09 21:51:49 CST 2025, defaultPassword=true),[]>','SUCCESS','2025-11-09 21:51:50',0,'normal'),(56,NULL,NULL,NULL,'閲嶇疆密码','閲嶇疆用户密码: user',NULL,'SUCCESS','2025-11-09 21:52:34',0,'normal'),(57,'admin',1,'0:0:0:0:0:0:0:1','resetUserPassword','Reset user password','<200 OK OK,ResetPasswordResult(userId=2, resetTime=2025-11-09T21:52:33.887, resetTimeDate=Sun Nov 09 21:52:33 CST 2025, defaultPassword=true),[]>','SUCCESS','2025-11-09 21:52:34',0,'normal'),(58,NULL,NULL,NULL,'閲嶇疆密码','閲嶇疆用户密码: admin',NULL,'SUCCESS','2025-11-10 11:09:41',0,'normal'),(59,'admin',1,'0:0:0:0:0:0:0:1','resetUserPassword','Reset user password','<200 OK OK,ResetPasswordResult(userId=1, resetTime=2025-11-10T11:09:41.249, resetTimeDate=Mon Nov 10 11:09:41 CST 2025, defaultPassword=true, defaultPasswordValue=xq9C028WKj),[]>','SUCCESS','2025-11-10 11:09:41',0,'normal'),(60,NULL,NULL,NULL,'閲嶇疆密码','閲嶇疆用户密码: admin',NULL,'SUCCESS','2025-11-10 11:09:42',0,'normal'),(61,'admin',1,'0:0:0:0:0:0:0:1','resetUserPassword','Reset user password','<200 OK OK,ResetPasswordResult(userId=1, resetTime=2025-11-10T11:09:41.550, resetTimeDate=Mon Nov 10 11:09:41 CST 2025, defaultPassword=false, defaultPasswordValue=null),[]>','SUCCESS','2025-11-10 11:09:42',0,'normal'),(62,'admin',1,'0:0:0:0:0:0:0:1','resetUserPassword','Reset user password','{\"error\": \"BusinessException: User not found\"}','SUCCESS','2025-11-10 11:09:42',0,'normal'),(63,NULL,NULL,NULL,'閲嶇疆密码','閲嶇疆用户密码: admin',NULL,'SUCCESS','2025-11-10 11:10:23',0,'normal'),(64,'admin',1,'0:0:0:0:0:0:0:1','resetUserPassword','Reset user password','<200 OK OK,ResetPasswordResult(userId=1, resetTime=2025-11-10T11:10:22.931, resetTimeDate=Mon Nov 10 11:10:22 CST 2025, defaultPassword=true, defaultPasswordValue=RUPwp0jUwm),[]>','SUCCESS','2025-11-10 11:10:23',0,'normal'),(65,NULL,NULL,NULL,'閲嶇疆密码','閲嶇疆用户密码: admin',NULL,'SUCCESS','2025-11-10 11:10:36',0,'normal'),(66,'admin',1,'0:0:0:0:0:0:0:1','resetUserPassword','Reset user password','<200 OK OK,ResetPasswordResult(userId=1, resetTime=2025-11-10T11:10:36.507, resetTimeDate=Mon Nov 10 11:10:36 CST 2025, defaultPassword=false, defaultPasswordValue=null),[]>','SUCCESS','2025-11-10 11:10:37',0,'normal'),(67,'admin',1,'0:0:0:0:0:0:0:1','resetUserPassword','Reset user password','{\"error\": \"BusinessException: User not found\"}','SUCCESS','2025-11-10 11:10:54',0,'normal'),(68,'admin',1,'0:0:0:0:0:0:0:1','resetUserPassword','Reset user password','{\"error\": \"BusinessException: User not found\"}','SUCCESS','2025-11-10 11:15:50',0,'normal'),(69,'admin',1,'0:0:0:0:0:0:0:1','resetUserPassword','Reset user password','{\"error\": \"BusinessException: User not found\"}','SUCCESS','2025-11-10 12:32:43',0,'normal'),(70,'admin',1,'0:0:0:0:0:0:0:1','resetUserPassword','Reset user password','{\"error\": \"BusinessException: User not found\"}','SUCCESS','2025-11-10 12:32:43',0,'normal'),(71,NULL,NULL,NULL,'閲嶇疆密码','閲嶇疆用户密码: user',NULL,'SUCCESS','2025-11-10 12:35:14',0,'normal'),(72,'admin',1,'0:0:0:0:0:0:0:1','resetUserPassword','Reset user password','<200 OK OK,ResetPasswordResult(userId=2, resetTime=2025-11-10T12:35:13.740, resetTimeDate=Mon Nov 10 12:35:13 CST 2025, defaultPassword=true, defaultPasswordValue=tt8oaKnxOx),[]>','SUCCESS','2025-11-10 12:35:14',0,'normal'),(73,'admin',1,'0:0:0:0:0:0:0:1','unlockUser','Admin action: unlock user account','<404 NOT_FOUND Not Found,用户不存在,[]>','SUCCESS','2025-11-10 12:42:22',0,'normal'),(74,'admin',1,'0:0:0:0:0:0:0:1','unlockUserJson','Admin action: unlock user account (JSON)','{\"error\": \"BusinessException: User not found\"}','SUCCESS','2025-11-10 12:42:22',0,'normal'),(75,'admin',1,'0:0:0:0:0:0:0:1','lockUser','Admin action: lock/unlock user account','<404 NOT_FOUND Not Found,用户不存在,[]>','SUCCESS','2025-11-10 12:42:43',0,'normal'),(76,'admin',1,'0:0:0:0:0:0:0:1','resetPassword','Admin action: reset user password','<400 BAD_REQUEST Bad Request,新密码与确认密码不匹配,[]>','SUCCESS','2025-11-10 12:43:02',0,'normal'),(77,'admin',1,'0:0:0:0:0:0:0:1','lockUser','Admin action: lock/unlock user account','<404 NOT_FOUND Not Found,用户不存在,[]>','SUCCESS','2025-11-10 12:47:06',0,'normal'),(78,'admin',1,'0:0:0:0:0:0:0:1','unlockUser','Admin action: unlock user account','<404 NOT_FOUND Not Found,用户不存在,[]>','SUCCESS','2025-11-10 12:47:06',0,'normal'),(79,'admin',1,'0:0:0:0:0:0:0:1','unlockUserJson','Admin action: unlock user account (JSON)','{\"error\": \"BusinessException: User not found\"}','SUCCESS','2025-11-10 12:47:06',0,'normal'),(80,'admin',1,'0:0:0:0:0:0:0:1','unlockUserJson','Admin action: unlock user account (JSON)','{\"error\": \"BusinessException: User not found\"}','SUCCESS','2025-11-10 12:49:07',0,'normal'),(81,'admin',1,'0:0:0:0:0:0:0:1','resetPassword','Admin action: reset user password','<404 NOT_FOUND Not Found,用户不存在,[]>','SUCCESS','2025-11-10 14:23:24',0,'normal'),(82,'admin',1,'0:0:0:0:0:0:0:1','lockUser','Admin action: lock/unlock user account','<404 NOT_FOUND Not Found,用户不存在,[]>','SUCCESS','2025-11-10 14:23:24',0,'normal'),(83,'admin',1,'0:0:0:0:0:0:0:1','unlockUser','Admin action: unlock user account','<404 NOT_FOUND Not Found,用户不存在,[]>','SUCCESS','2025-11-10 14:23:24',0,'normal'),(84,'admin',1,'0:0:0:0:0:0:0:1','unlockUserJson','Admin action: unlock user account (JSON)','{\"error\": \"BusinessException: User not found\"}','SUCCESS','2025-11-10 14:23:25',0,'normal'),(85,'admin',1,'0:0:0:0:0:0:0:1','resetPassword','Admin action: reset user password','<404 NOT_FOUND Not Found,用户不存在,[]>','SUCCESS','2025-11-10 14:24:18',0,'normal'),(86,'admin',1,'0:0:0:0:0:0:0:1','lockUser','Admin action: lock/unlock user account','<404 NOT_FOUND Not Found,用户不存在,[]>','SUCCESS','2025-11-10 14:24:18',0,'normal'),(87,'admin',1,'0:0:0:0:0:0:0:1','unlockUser','Admin action: unlock user account','<404 NOT_FOUND Not Found,用户不存在,[]>','SUCCESS','2025-11-10 14:24:18',0,'normal'),(88,'admin',1,'0:0:0:0:0:0:0:1','unlockUserJson','Admin action: unlock user account (JSON)','{\"error\": \"BusinessException: User not found\"}','SUCCESS','2025-11-10 14:24:18',0,'normal'),(89,'admin',1,'0:0:0:0:0:0:0:1','resetPassword','Admin action: reset user password','<404 NOT_FOUND Not Found,用户不存在,[]>','SUCCESS','2025-11-10 14:24:51',0,'normal'),(90,'admin',1,'0:0:0:0:0:0:0:1','lockUser','Admin action: lock/unlock user account','<404 NOT_FOUND Not Found,用户不存在,[]>','SUCCESS','2025-11-10 14:24:52',0,'normal'),(91,'admin',1,'0:0:0:0:0:0:0:1','unlockUser','Admin action: unlock user account','<404 NOT_FOUND Not Found,用户不存在,[]>','SUCCESS','2025-11-10 14:24:52',0,'normal'),(92,'admin',1,'0:0:0:0:0:0:0:1','unlockUserJson','Admin action: unlock user account (JSON)','{\"error\": \"BusinessException: User not found\"}','SUCCESS','2025-11-10 14:24:52',0,'normal'),(93,'admin',1,'0:0:0:0:0:0:0:1','resetPassword','Admin action: reset user password','<404 NOT_FOUND Not Found,用户不存在,[]>','SUCCESS','2025-11-10 14:28:50',0,'normal'),(94,'admin',1,'0:0:0:0:0:0:0:1','lockUser','Admin action: lock/unlock user account','<404 NOT_FOUND Not Found,用户不存在,[]>','SUCCESS','2025-11-10 14:28:50',0,'normal'),(95,'admin',1,'0:0:0:0:0:0:0:1','unlockUser','Admin action: unlock user account','<404 NOT_FOUND Not Found,用户不存在,[]>','SUCCESS','2025-11-10 14:28:50',0,'normal'),(96,'admin',1,'0:0:0:0:0:0:0:1','unlockUserJson','Admin action: unlock user account (JSON)','{\"error\": \"BusinessException: User not found\"}','SUCCESS','2025-11-10 14:28:51',0,'normal'),(97,'admin',1,'0:0:0:0:0:0:0:1','resetPassword','Admin action: reset user password','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-10 14:42:58',0,'normal'),(98,'admin',1,'0:0:0:0:0:0:0:1','lockUser','Admin action: lock/unlock user account','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-10 14:42:58',0,'normal'),(99,'admin',1,'0:0:0:0:0:0:0:1','unlockUser','Admin action: unlock user account','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-10 14:42:58',0,'normal'),(100,'admin',1,'0:0:0:0:0:0:0:1','unlockUserJson','Admin action: unlock user account (JSON)','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-10 14:42:58',0,'normal'),(101,'admin',1,'0:0:0:0:0:0:0:1','resetPassword','Admin action: reset user password','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-10 17:13:29',0,'normal'),(102,'admin',1,'0:0:0:0:0:0:0:1','lockUser','Admin action: lock/unlock user account','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-10 17:13:29',0,'normal'),(103,'admin',1,'0:0:0:0:0:0:0:1','unlockUser','Admin action: unlock user account','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-10 17:13:29',0,'normal'),(104,'admin',1,'0:0:0:0:0:0:0:1','unlockUserJson','Admin action: unlock user account (JSON)','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-10 17:13:30',0,'normal'),(105,'admin',1,'0:0:0:0:0:0:0:1','resetPassword','Admin action: reset user password','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-10 17:15:11',0,'normal'),(106,'admin',1,'0:0:0:0:0:0:0:1','lockUser','Admin action: lock/unlock user account','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-10 17:15:11',0,'normal'),(107,'admin',1,'0:0:0:0:0:0:0:1','unlockUser','Admin action: unlock user account','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-10 17:15:11',0,'normal'),(108,'admin',1,'0:0:0:0:0:0:0:1','unlockUserJson','Admin action: unlock user account (JSON)','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-10 17:15:11',0,'normal'),(109,'admin',1,'0:0:0:0:0:0:0:1','resetPassword','Admin action: reset user password','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-10 17:16:03',0,'normal'),(110,'admin',1,'0:0:0:0:0:0:0:1','lockUser','Admin action: lock/unlock user account','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-10 17:16:03',0,'normal'),(111,'admin',1,'0:0:0:0:0:0:0:1','unlockUser','Admin action: unlock user account','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-10 17:16:03',0,'normal'),(112,'admin',1,'0:0:0:0:0:0:0:1','unlockUserJson','Admin action: unlock user account (JSON)','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-10 17:16:04',0,'normal'),(113,'admin',1,'0:0:0:0:0:0:0:1','resetPassword','Admin action: reset user password','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-10 17:32:52',0,'normal'),(114,'admin',1,'0:0:0:0:0:0:0:1','lockUser','Admin action: lock/unlock user account','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-10 17:32:53',0,'normal'),(115,'admin',1,'0:0:0:0:0:0:0:1','unlockUser','Admin action: unlock user account','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-10 17:32:53',0,'normal'),(116,'admin',1,'0:0:0:0:0:0:0:1','unlockUserJson','Admin action: unlock user account (JSON)','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-10 17:32:53',0,'normal'),(117,'admin',1,'0:0:0:0:0:0:0:1','resetPassword','Admin action: reset user password','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-10 19:16:15',0,'normal'),(118,'admin',1,'0:0:0:0:0:0:0:1','lockUser','Admin action: lock/unlock user account','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-10 19:16:15',0,'normal'),(119,'admin',1,'0:0:0:0:0:0:0:1','unlockUser','Admin action: unlock user account','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-10 19:16:15',0,'normal'),(120,'admin',1,'0:0:0:0:0:0:0:1','unlockUserJson','Admin action: unlock user account (JSON)','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-10 19:16:16',0,'normal'),(121,'admin',1,'0:0:0:0:0:0:0:1','resetPassword','Admin action: reset user password','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-10 19:17:07',0,'normal'),(122,'admin',1,'0:0:0:0:0:0:0:1','lockUser','Admin action: lock/unlock user account','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-10 19:17:08',0,'normal'),(123,'admin',1,'0:0:0:0:0:0:0:1','unlockUser','Admin action: unlock user account','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-10 19:17:08',0,'normal'),(124,'admin',1,'0:0:0:0:0:0:0:1','unlockUserJson','Admin action: unlock user account (JSON)','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-10 19:17:08',0,'normal'),(125,'admin',1,'0:0:0:0:0:0:0:1','resetPassword','Admin action: reset user password','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-10 19:17:35',0,'normal'),(126,'admin',1,'0:0:0:0:0:0:0:1','lockUser','Admin action: lock/unlock user account','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-10 19:17:35',0,'normal'),(127,'admin',1,'0:0:0:0:0:0:0:1','unlockUser','Admin action: unlock user account','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-10 19:17:35',0,'normal'),(128,'admin',1,'0:0:0:0:0:0:0:1','unlockUserJson','Admin action: unlock user account (JSON)','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-10 19:17:36',0,'normal'),(129,'admin',1,'0:0:0:0:0:0:0:1','resetPassword','Admin action: reset user password','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-10 19:18:14',0,'normal'),(130,'admin',1,'0:0:0:0:0:0:0:1','lockUser','Admin action: lock/unlock user account','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-10 19:18:14',0,'normal'),(131,'admin',1,'0:0:0:0:0:0:0:1','unlockUser','Admin action: unlock user account','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-10 19:18:14',0,'normal'),(132,'admin',1,'0:0:0:0:0:0:0:1','unlockUserJson','Admin action: unlock user account (JSON)','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-10 19:18:14',0,'normal'),(133,'admin',1,'0:0:0:0:0:0:0:1','resetPassword','Admin action: reset user password','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-10 19:18:46',0,'normal'),(134,'admin',1,'0:0:0:0:0:0:0:1','lockUser','Admin action: lock/unlock user account','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-10 19:18:46',0,'normal'),(135,'admin',1,'0:0:0:0:0:0:0:1','unlockUser','Admin action: unlock user account','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-10 19:18:46',0,'normal'),(136,'admin',1,'0:0:0:0:0:0:0:1','unlockUserJson','Admin action: unlock user account (JSON)','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-10 19:18:46',0,'normal'),(137,'admin',1,'0:0:0:0:0:0:0:1','resetPassword','Admin action: reset user password','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-10 19:36:10',0,'normal'),(138,'admin',1,'0:0:0:0:0:0:0:1','lockUser','Admin action: lock/unlock user account','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-10 19:36:10',0,'normal'),(139,'admin',1,'0:0:0:0:0:0:0:1','unlockUser','Admin action: unlock user account','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-10 19:36:10',0,'normal'),(140,'admin',1,'0:0:0:0:0:0:0:1','unlockUserJson','Admin action: unlock user account (JSON)','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-10 19:36:11',0,'normal'),(141,'admin',1,'0:0:0:0:0:0:0:1','assignRoleToUser','Assign role to user - high sensitivity','{\"error\": \"BadSqlGrammarException: \r\n### Error querying database.  Cause: java.sql.SQLSyntaxErrorException: Unknown column \'status\' in \'field list\'\r\n### The error may exist in com/mold/digitalization/mapper/RoleMapper.java (best guess)\r\n### The error may involve defaultParameterMap\r\n### The error occurred while setting parameters\r\n### SQL: SELECT id,role_name,role_code,description,status,create_time,update_time FROM role WHERE id=?\r\n### Cause: java.sql.SQLSyntaxErrorException: Unknown column \'status\' in \'field list\'\n; bad SQL grammar []; nested exception is java.sql.SQLSyntaxErrorException: Unknown column \'status\' in \'field list\'\"}','SUCCESS','2025-11-10 20:55:35',0,'normal'),(142,'admin',1,'0:0:0:0:0:0:0:1','assignRoleToUser','Assign role to user - high sensitivity','{\"error\": \"BadSqlGrammarException: \r\n### Error querying database.  Cause: java.sql.SQLSyntaxErrorException: Unknown column \'status\' in \'field list\'\r\n### The error may exist in com/mold/digitalization/mapper/RoleMapper.java (best guess)\r\n### The error may involve defaultParameterMap\r\n### The error occurred while setting parameters\r\n### SQL: SELECT id,role_name,role_code,description,status,create_time,update_time FROM role WHERE id=?\r\n### Cause: java.sql.SQLSyntaxErrorException: Unknown column \'status\' in \'field list\'\n; bad SQL grammar []; nested exception is java.sql.SQLSyntaxErrorException: Unknown column \'status\' in \'field list\'\"}','SUCCESS','2025-11-11 03:43:04',0,'normal'),(143,'admin',1,'0:0:0:0:0:0:0:1','assignRoleToUser','Assign role to user - high sensitivity','{\"error\": \"BadSqlGrammarException: \r\n### Error querying database.  Cause: java.sql.SQLSyntaxErrorException: Unknown column \'status\' in \'field list\'\r\n### The error may exist in com/mold/digitalization/mapper/RoleMapper.java (best guess)\r\n### The error may involve defaultParameterMap\r\n### The error occurred while setting parameters\r\n### SQL: SELECT id,role_name,role_code,description,status,create_time,update_time FROM role WHERE id=?\r\n### Cause: java.sql.SQLSyntaxErrorException: Unknown column \'status\' in \'field list\'\n; bad SQL grammar []; nested exception is java.sql.SQLSyntaxErrorException: Unknown column \'status\' in \'field list\'\"}','SUCCESS','2025-11-11 03:43:37',0,'normal'),(144,'admin',1,'0:0:0:0:0:0:0:1','resetPassword','Admin action: reset user password','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 03:46:04',0,'normal'),(145,'admin',1,'0:0:0:0:0:0:0:1','lockUser','Admin action: lock/unlock user account','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 03:46:05',0,'normal'),(146,'admin',1,'0:0:0:0:0:0:0:1','unlockUser','Admin action: unlock user account','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 03:46:05',0,'normal'),(147,'admin',1,'0:0:0:0:0:0:0:1','unlockUserJson','Admin action: unlock user account (JSON)','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 03:46:05',0,'normal'),(148,'admin',1,'0:0:0:0:0:0:0:1','resetPassword','Admin action: reset user password','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 03:46:33',0,'normal'),(149,'admin',1,'0:0:0:0:0:0:0:1','lockUser','Admin action: lock/unlock user account','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 03:46:33',0,'normal'),(150,'admin',1,'0:0:0:0:0:0:0:1','unlockUser','Admin action: unlock user account','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 03:46:33',0,'normal'),(151,'admin',1,'0:0:0:0:0:0:0:1','unlockUserJson','Admin action: unlock user account (JSON)','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 03:46:33',0,'normal'),(152,'admin',1,'0:0:0:0:0:0:0:1','resetPassword','Admin action: reset user password','<200 OK OK,ResponseDTO(code=200, message=密码重置成功, data=null),[]>','SUCCESS','2025-11-11 08:31:05',0,'normal'),(153,'admin',1,'0:0:0:0:0:0:0:1','resetPassword','Admin action: reset user password','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 08:32:48',0,'normal'),(154,'admin',1,'0:0:0:0:0:0:0:1','lockUser','Admin action: lock/unlock user account','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 08:32:48',0,'normal'),(155,'admin',1,'0:0:0:0:0:0:0:1','unlockUser','Admin action: unlock user account','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 08:32:48',0,'normal'),(156,'admin',1,'0:0:0:0:0:0:0:1','unlockUserJson','Admin action: unlock user account (JSON)','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 08:32:49',0,'normal'),(157,'admin',1,'0:0:0:0:0:0:0:1','resetPassword','Admin action: reset user password','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 08:38:03',0,'normal'),(158,'admin',1,'0:0:0:0:0:0:0:1','lockUser','Admin action: lock/unlock user account','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 08:38:03',0,'normal'),(159,'admin',1,'0:0:0:0:0:0:0:1','unlockUser','Admin action: unlock user account','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 08:38:03',0,'normal'),(160,'admin',1,'0:0:0:0:0:0:0:1','unlockUserJson','Admin action: unlock user account (JSON)','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 08:38:03',0,'normal'),(161,'admin',1,'127.0.0.1','resetPassword','Admin action: reset user password','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 08:46:03',0,'normal'),(162,'admin',1,'127.0.0.1','lockUser','Admin action: lock/unlock user account','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 08:46:03',0,'normal'),(163,'admin',1,'127.0.0.1','unlockUser','Admin action: unlock user account','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 08:46:03',0,'normal'),(164,'admin',1,'127.0.0.1','unlockUserJson','Admin action: unlock user account (JSON)','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 08:46:03',0,'normal'),(165,'admin',1,'127.0.0.1','resetPassword','Admin action: reset user password','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 08:55:11',0,'normal'),(166,'admin',1,'127.0.0.1','lockUser','Admin action: lock/unlock user account','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 08:55:11',0,'normal'),(167,'admin',1,'127.0.0.1','unlockUser','Admin action: unlock user account','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 08:55:11',0,'normal'),(168,'admin',1,'127.0.0.1','unlockUserJson','Admin action: unlock user account (JSON)','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 08:55:12',0,'normal'),(169,'admin',1,'127.0.0.1','resetPassword','Admin action: reset user password','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 09:01:12',0,'normal'),(170,'admin',1,'127.0.0.1','lockUser','Admin action: lock/unlock user account','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 09:01:12',0,'normal'),(171,'admin',1,'127.0.0.1','unlockUser','Admin action: unlock user account','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 09:01:12',0,'normal'),(172,'admin',1,'127.0.0.1','unlockUserJson','Admin action: unlock user account (JSON)','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 09:01:12',0,'normal'),(173,'admin',1,'127.0.0.1','resetPassword','Admin action: reset user password','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 09:31:18',0,'normal'),(174,'admin',1,'127.0.0.1','lockUser','Admin action: lock/unlock user account','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 09:31:18',0,'normal'),(175,'admin',1,'127.0.0.1','unlockUser','Admin action: unlock user account','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 09:31:18',0,'normal'),(176,'admin',1,'127.0.0.1','unlockUserJson','Admin action: unlock user account (JSON)','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 09:31:18',0,'normal'),(177,'admin',1,'127.0.0.1','resetPassword','Admin action: reset user password','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 09:37:42',0,'normal'),(178,'admin',1,'127.0.0.1','lockUser','Admin action: lock/unlock user account','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 09:37:42',0,'normal'),(179,'admin',1,'127.0.0.1','unlockUser','Admin action: unlock user account','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 09:37:42',0,'normal'),(180,'admin',1,'127.0.0.1','unlockUserJson','Admin action: unlock user account (JSON)','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 09:37:42',0,'normal'),(181,'admin',1,'127.0.0.1','resetPassword','Admin action: reset user password','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 09:38:36',0,'normal'),(182,'admin',1,'127.0.0.1','lockUser','Admin action: lock/unlock user account','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 09:38:36',0,'normal'),(183,'admin',1,'127.0.0.1','unlockUser','Admin action: unlock user account','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 09:38:36',0,'normal'),(184,'admin',1,'127.0.0.1','unlockUserJson','Admin action: unlock user account (JSON)','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 09:38:36',0,'normal'),(185,'admin',1,'127.0.0.1','resetPassword','Admin action: reset user password','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 09:53:01',1,'critical'),(186,'admin',1,'127.0.0.1','lockUser','Admin action: lock/unlock user account','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 09:53:01',1,'critical'),(187,'admin',1,'127.0.0.1','unlockUser','Admin action: unlock user account','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 09:53:02',1,'critical'),(188,'admin',1,'127.0.0.1','unlockUserJson','Admin action: unlock user account (JSON)','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 09:53:02',1,'critical'),(189,'admin',1,'127.0.0.1','resetPassword','Admin action: reset user password','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 10:04:09',1,'critical'),(190,'admin',1,'127.0.0.1','lockUser','Admin action: lock/unlock user account','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 10:04:09',1,'critical'),(191,'admin',1,'127.0.0.1','unlockUser','Admin action: unlock user account','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 10:04:09',1,'critical'),(192,'admin',1,'127.0.0.1','unlockUserJson','Admin action: unlock user account (JSON)','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 10:04:09',1,'critical'),(193,'admin',1,'127.0.0.1','resetPassword','Admin action: reset user password','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 10:05:34',1,'critical'),(194,'admin',1,'127.0.0.1','lockUser','Admin action: lock/unlock user account','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 10:05:34',1,'critical'),(195,'admin',1,'127.0.0.1','unlockUser','Admin action: unlock user account','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 10:05:34',1,'critical'),(196,'admin',1,'127.0.0.1','unlockUserJson','Admin action: unlock user account (JSON)','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 10:05:35',1,'critical'),(197,'admin',1,'127.0.0.1','resetPassword','Admin action: reset user password','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 10:11:38',1,'critical'),(198,'admin',1,'127.0.0.1','lockUser','Admin action: lock/unlock user account','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 10:11:38',1,'critical'),(199,'admin',1,'127.0.0.1','unlockUser','Admin action: unlock user account','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 10:11:38',1,'critical'),(200,'admin',1,'127.0.0.1','unlockUserJson','Admin action: unlock user account (JSON)','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 10:11:38',1,'critical'),(201,'admin',1,'127.0.0.1','resetPassword','Admin action: reset user password','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 10:34:53',1,'critical'),(202,'admin',1,'127.0.0.1','lockUser','Admin action: lock/unlock user account','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 10:34:53',1,'critical'),(203,'admin',1,'127.0.0.1','unlockUser','Admin action: unlock user account','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 10:34:53',1,'critical'),(204,'admin',1,'127.0.0.1','unlockUserJson','Admin action: unlock user account (JSON)','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 10:34:53',1,'critical'),(205,'admin',1,'127.0.0.1','resetPassword','Admin action: reset user password','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 11:04:29',1,'critical'),(206,'admin',1,'127.0.0.1','lockUser','Admin action: lock/unlock user account','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 11:04:30',1,'critical'),(207,'admin',1,'127.0.0.1','unlockUser','Admin action: unlock user account','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 11:04:30',1,'critical'),(208,'admin',1,'127.0.0.1','unlockUserJson','Admin action: unlock user account (JSON)','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 11:04:30',1,'critical'),(209,'admin',1,'127.0.0.1','resetPassword','Admin action: reset user password','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 11:12:45',1,'critical'),(210,'admin',1,'127.0.0.1','lockUser','Admin action: lock/unlock user account','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 11:12:45',1,'critical'),(211,'admin',1,'127.0.0.1','unlockUser','Admin action: unlock user account','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 11:12:45',1,'critical'),(212,'admin',1,'127.0.0.1','unlockUserJson','Admin action: unlock user account (JSON)','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 11:12:46',1,'critical'),(213,'admin',1,'127.0.0.1','resetPassword','Admin action: reset user password','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 11:13:26',1,'critical'),(214,'admin',1,'127.0.0.1','lockUser','Admin action: lock/unlock user account','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 11:13:26',1,'critical'),(215,'admin',1,'127.0.0.1','unlockUser','Admin action: unlock user account','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 11:13:26',1,'critical'),(216,'admin',1,'127.0.0.1','unlockUserJson','Admin action: unlock user account (JSON)','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 11:13:26',1,'critical'),(217,'admin',1,'127.0.0.1','resetPassword','Admin action: reset user password','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 11:29:29',1,'critical'),(218,'admin',1,'127.0.0.1','lockUser','Admin action: lock/unlock user account','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 11:29:29',1,'critical'),(219,'admin',1,'127.0.0.1','unlockUser','Admin action: unlock user account','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 11:29:29',1,'critical'),(220,'admin',1,'127.0.0.1','unlockUserJson','Admin action: unlock user account (JSON)','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 11:29:29',1,'critical'),(221,'admin',1,'127.0.0.1','resetPassword','Admin action: reset user password','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 11:50:48',1,'critical'),(222,'admin',1,'127.0.0.1','lockUser','Admin action: lock/unlock user account','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 11:50:48',1,'critical'),(223,'admin',1,'127.0.0.1','unlockUser','Admin action: unlock user account','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 11:50:48',1,'critical'),(224,'admin',1,'127.0.0.1','unlockUserJson','Admin action: unlock user account (JSON)','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 11:50:48',1,'critical'),(225,'admin',1,'127.0.0.1','resetPassword','Admin action: reset user password','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 12:19:28',1,'critical'),(226,'admin',1,'127.0.0.1','lockUser','Admin action: lock/unlock user account','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 12:19:29',1,'critical'),(227,'admin',1,'127.0.0.1','unlockUser','Admin action: unlock user account','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 12:19:29',1,'critical'),(228,'admin',1,'127.0.0.1','unlockUserJson','Admin action: unlock user account (JSON)','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 12:19:29',1,'critical'),(229,'admin',1,'127.0.0.1','resetPassword','Admin action: reset user password','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 12:20:09',1,'critical'),(230,'admin',1,'127.0.0.1','lockUser','Admin action: lock/unlock user account','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 12:20:09',1,'critical'),(231,'admin',1,'127.0.0.1','unlockUser','Admin action: unlock user account','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 12:20:09',1,'critical'),(232,'admin',1,'127.0.0.1','unlockUserJson','Admin action: unlock user account (JSON)','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 12:20:09',1,'critical'),(233,'admin',1,'127.0.0.1','resetPassword','Admin action: reset user password','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 12:20:29',1,'critical'),(234,'admin',1,'127.0.0.1','lockUser','Admin action: lock/unlock user account','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 12:20:29',1,'critical'),(235,'admin',1,'127.0.0.1','unlockUser','Admin action: unlock user account','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 12:20:29',1,'critical'),(236,'admin',1,'127.0.0.1','unlockUserJson','Admin action: unlock user account (JSON)','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 12:20:29',1,'critical'),(237,'admin',1,'127.0.0.1','resetPassword','Admin action: reset user password','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 12:21:52',1,'critical'),(238,'admin',1,'127.0.0.1','lockUser','Admin action: lock/unlock user account','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 12:21:53',1,'critical'),(239,'admin',1,'127.0.0.1','unlockUser','Admin action: unlock user account','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 12:21:53',1,'critical'),(240,'admin',1,'127.0.0.1','unlockUserJson','Admin action: unlock user account (JSON)','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 12:21:53',1,'critical'),(241,'admin',1,'127.0.0.1','resetPassword','Admin action: reset user password','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 12:27:57',1,'critical'),(242,'admin',1,'127.0.0.1','lockUser','Admin action: lock/unlock user account','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 12:27:57',1,'critical'),(243,'admin',1,'127.0.0.1','unlockUser','Admin action: unlock user account','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 12:27:57',1,'critical'),(244,'admin',1,'127.0.0.1','unlockUserJson','Admin action: unlock user account (JSON)','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 12:27:58',1,'critical'),(245,'admin',1,'127.0.0.1','resetPassword','Admin action: reset user password','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 12:33:39',1,'critical'),(246,'admin',1,'127.0.0.1','lockUser','Admin action: lock/unlock user account','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 12:33:40',1,'critical'),(247,'admin',1,'127.0.0.1','unlockUser','Admin action: unlock user account','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 12:33:40',1,'critical'),(248,'admin',1,'127.0.0.1','unlockUserJson','Admin action: unlock user account (JSON)','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-11 12:33:40',1,'critical'),(249,NULL,NULL,NULL,'创建用户','创建用户: devuser-demo',NULL,'SUCCESS','2025-11-15 08:28:54',0,'normal'),(250,NULL,NULL,NULL,'创建用户','创建用户:  devuser-1763174233014',NULL,'SUCCESS','2025-11-15 10:37:13',0,'normal'),(251,NULL,NULL,NULL,'创建用户','创建用户: devuser-1763175389120',NULL,'SUCCESS','2025-11-15 10:56:29',0,'normal'),(252,NULL,NULL,NULL,'创建用户','创建用户: devuser-1763175886673',NULL,'SUCCESS','2025-11-15 11:04:47',0,'normal'),(253,NULL,NULL,NULL,'创建用户','创建用户: op-测试',NULL,'SUCCESS','2025-11-15 14:53:07',0,'normal'),(254,NULL,NULL,NULL,'创建用户','创建用户: test_mold_20251115151939',NULL,'SUCCESS','2025-11-15 15:19:40',0,'normal'),(255,'admin',1,'0:0:0:0:0:0:0:1','createUser','Create user operation','<201 CREATED Created,UserResponse(id=9, username=test_mold_20251115151939, realName=模具加工测试, phone=null, status=1, locked=null),[]>','SUCCESS','2025-11-15 15:19:40',1,'high'),(256,NULL,NULL,NULL,'创建用户','创建用户: test_mold_20251115160925',NULL,'SUCCESS','2025-11-15 16:09:26',0,'normal'),(257,'admin',1,'0:0:0:0:0:0:0:1','createUser','Create user operation','<201 CREATED Created,UserResponse(id=10, username=test_mold_20251115160925, realName=模具加工测试, phone=null, status=1, locked=null),[]>','SUCCESS','2025-11-15 16:09:26',1,'high'),(258,NULL,NULL,NULL,'创建用户','创建用户: test_mold_20251115160934',NULL,'SUCCESS','2025-11-15 16:09:34',0,'normal'),(259,'admin',1,'0:0:0:0:0:0:0:1','createUser','Create user operation','<201 CREATED Created,UserResponse(id=11, username=test_mold_20251115160934, realName=模具加工测试, phone=null, status=1, locked=null),[]>','SUCCESS','2025-11-15 16:09:34',1,'high'),(260,NULL,NULL,NULL,'创建用户','创建用户: test_mold_20251115161203',NULL,'SUCCESS','2025-11-15 16:12:04',0,'normal'),(261,'admin',1,'0:0:0:0:0:0:0:1','createUser','Create user operation','<201 CREATED Created,UserResponse(id=12, username=test_mold_20251115161203, realName=模具加工测试, phone=null, status=1, locked=null),[]>','SUCCESS','2025-11-15 16:12:04',1,'high'),(262,NULL,NULL,NULL,'创建用户','创建用户: test_mold_20251115161452',NULL,'SUCCESS','2025-11-15 16:14:53',0,'normal'),(263,'admin',1,'0:0:0:0:0:0:0:1','createUser','Create user operation','<201 CREATED Created,UserResponse(id=13, username=test_mold_20251115161452, realName=模具加工测试, phone=null, status=1, locked=null),[]>','SUCCESS','2025-11-15 16:14:53',1,'high'),(264,NULL,NULL,NULL,'创建用户','创建用户: test_mold_20251115162116',NULL,'SUCCESS','2025-11-15 16:21:16',0,'normal'),(265,'admin',1,'0:0:0:0:0:0:0:1','createUser','Create user operation','<201 CREATED Created,UserResponse(id=14, username=test_mold_20251115162116, realName=模具加工测试, phone=null, status=1, locked=null),[]>','SUCCESS','2025-11-15 16:21:16',1,'high'),(266,NULL,NULL,NULL,'创建用户','创建用户: test_mold_20251115162121',NULL,'SUCCESS','2025-11-15 16:21:21',0,'normal'),(267,'admin',1,'0:0:0:0:0:0:0:1','createUser','Create user operation','<201 CREATED Created,UserResponse(id=15, username=test_mold_20251115162121, realName=模具加工测试, phone=null, status=1, locked=null),[]>','SUCCESS','2025-11-15 16:21:21',1,'high'),(268,NULL,NULL,NULL,'创建用户','创建用户: test_mold_20251115162153',NULL,'SUCCESS','2025-11-15 16:21:53',0,'normal'),(269,'admin',1,'0:0:0:0:0:0:0:1','createUser','Create user operation','<201 CREATED Created,UserResponse(id=16, username=test_mold_20251115162153, realName=模具加工测试, phone=null, status=1, locked=null),[]>','SUCCESS','2025-11-15 16:21:54',1,'high'),(270,NULL,NULL,NULL,'创建用户','创建用户: test_mold_20251115163503',NULL,'SUCCESS','2025-11-15 16:35:03',0,'normal'),(271,'admin',1,'0:0:0:0:0:0:0:1','createUser','Create user operation','<201 CREATED Created,UserResponse(id=17, username=test_mold_20251115163503, realName=模具加工测试, phone=null, status=1, locked=null),[]>','SUCCESS','2025-11-15 16:35:03',1,'high'),(272,'admin',1,'0:0:0:0:0:0:0:1','assignRoleToUser','Assign role to user - high sensitivity','<500 INTERNAL_SERVER_ERROR Internal Server Error,Role assignment failed due to server error,[]>','SUCCESS','2025-11-15 16:35:03',1,'high'),(273,NULL,NULL,NULL,'创建用户','创建用户: test_mold_20251115163509',NULL,'SUCCESS','2025-11-15 16:35:10',0,'normal'),(274,'admin',1,'0:0:0:0:0:0:0:1','createUser','Create user operation','<201 CREATED Created,UserResponse(id=18, username=test_mold_20251115163509, realName=模具加工测试, phone=null, status=1, locked=null),[]>','SUCCESS','2025-11-15 16:35:10',1,'high'),(275,'admin',1,'0:0:0:0:0:0:0:1','assignRoleToUser','Assign role to user - high sensitivity','<500 INTERNAL_SERVER_ERROR Internal Server Error,Role assignment failed due to server error,[]>','SUCCESS','2025-11-15 16:35:10',1,'high'),(276,NULL,NULL,NULL,'创建用户','创建用户: test_mold_20251115163527',NULL,'SUCCESS','2025-11-15 16:35:28',0,'normal'),(277,'admin',1,'0:0:0:0:0:0:0:1','createUser','Create user operation','<201 CREATED Created,UserResponse(id=19, username=test_mold_20251115163527, realName=模具加工测试, phone=null, status=1, locked=null),[]>','SUCCESS','2025-11-15 16:35:28',1,'high'),(278,'admin',1,'0:0:0:0:0:0:0:1','assignRoleToUser','Assign role to user - high sensitivity','<500 INTERNAL_SERVER_ERROR Internal Server Error,Role assignment failed due to server error,[]>','SUCCESS','2025-11-15 16:35:28',1,'high'),(279,'admin',1,'0:0:0:0:0:0:0:1','assignRoleToUser','Assign role to user - high sensitivity','<200 OK OK,Role assignment successful,[]>','SUCCESS','2025-11-15 16:39:08',1,'high'),(280,NULL,NULL,NULL,'创建用户','创建用户: test_mold_20251115165936',NULL,'SUCCESS','2025-11-15 16:59:36',0,'normal'),(281,'admin',1,'0:0:0:0:0:0:0:1','createUser','Create user operation','<201 CREATED Created,UserResponse(id=20, username=test_mold_20251115165936, realName=模具加工测试, phone=null, status=1, locked=null),[]>','SUCCESS','2025-11-15 16:59:36',1,'high'),(282,'admin',1,'0:0:0:0:0:0:0:1','assignRoleToUser','Assign role to user - high sensitivity','<200 OK OK,Role assignment successful,[]>','SUCCESS','2025-11-15 16:59:36',1,'high'),(283,NULL,NULL,NULL,'创建用户','创建用户: test_mold_20251115171445',NULL,'SUCCESS','2025-11-15 17:14:45',0,'normal'),(284,'admin',1,'0:0:0:0:0:0:0:1','createUser','Create user operation','<201 CREATED Created,UserResponse(id=21, username=test_mold_20251115171445, realName=模具加工测试, phone=null, status=1, locked=null),[]>','SUCCESS','2025-11-15 17:14:45',1,'high'),(285,'admin',1,'0:0:0:0:0:0:0:1','assignRoleToUser','Assign role to user - high sensitivity','<200 OK OK,Role assignment successful,[]>','SUCCESS','2025-11-15 17:14:46',1,'high'),(286,NULL,NULL,NULL,'创建用户','创建用户: del_test_user',NULL,'SUCCESS','2025-11-15 17:17:27',0,'normal'),(287,NULL,NULL,NULL,'删除用户','删除用户: del_test_user',NULL,'SUCCESS','2025-11-15 17:18:13',0,'normal'),(288,'admin',1,'0:0:0:0:0:0:0:1','deleteUser','删除用户操作','<200 OK OK,[]>','SUCCESS','2025-11-15 17:18:13',1,'critical'),(289,'admin',1,'0:0:0:0:0:0:0:1','assignRoleToUser','Assign role to user - high sensitivity','<200 OK OK,Role assignment successful,[]>','SUCCESS','2025-11-15 17:27:41',1,'high'),(290,NULL,NULL,NULL,'创建用户','创建用户: test_mold_20251117122147',NULL,'SUCCESS','2025-11-17 12:21:48',0,'normal'),(291,'admin',1,'0:0:0:0:0:0:0:1','createUser','Create user operation','<201 CREATED Created,UserResponse(id=23, username=test_mold_20251117122147, realName=模具加工测试, phone=null, status=1, locked=null, roles=null),[]>','SUCCESS','2025-11-17 12:21:48',1,'high'),(292,'admin',1,'0:0:0:0:0:0:0:1','assignRoleToUser','Assign role to user - high sensitivity','<200 OK OK,Role assignment successful,[]>','SUCCESS','2025-11-17 12:21:48',1,'high'),(293,NULL,NULL,NULL,'更新用户','更新用户淇℃伅: null',NULL,'SUCCESS','2025-11-17 12:22:10',0,'normal'),(294,'admin',1,'0:0:0:0:0:0:0:1','updateUser','更新用户淇℃伅操作','<200 OK OK,[]>','SUCCESS','2025-11-17 12:22:10',1,'high'),(295,NULL,NULL,NULL,'更新用户','更新用户淇℃伅: null',NULL,'SUCCESS','2025-11-17 12:22:28',0,'normal'),(296,'admin',1,'0:0:0:0:0:0:0:1','updateUser','更新用户淇℃伅操作','<200 OK OK,[]>','SUCCESS','2025-11-17 12:22:28',1,'high'),(297,NULL,NULL,NULL,'删除用户','删除用户: test_mold_20251117122147',NULL,'SUCCESS','2025-11-17 12:22:42',0,'normal'),(298,'admin',1,'0:0:0:0:0:0:0:1','deleteUser','删除用户操作','<200 OK OK,[]>','SUCCESS','2025-11-17 12:22:42',1,'critical'),(299,NULL,NULL,NULL,'删除用户','删除用户: test_mold_20251115171445',NULL,'SUCCESS','2025-11-17 12:22:45',0,'normal'),(300,'admin',1,'0:0:0:0:0:0:0:1','deleteUser','删除用户操作','<200 OK OK,[]>','SUCCESS','2025-11-17 12:22:45',1,'critical'),(301,NULL,NULL,NULL,'更新用户','更新用户淇℃伅: null',NULL,'SUCCESS','2025-11-17 12:22:55',0,'normal'),(302,'admin',1,'0:0:0:0:0:0:0:1','updateUser','更新用户淇℃伅操作','<200 OK OK,[]>','SUCCESS','2025-11-17 12:22:55',1,'high'),(303,NULL,NULL,NULL,'更新用户','更新用户淇℃伅: null',NULL,'SUCCESS','2025-11-17 12:26:16',0,'normal'),(304,'admin',1,'0:0:0:0:0:0:0:1','updateUser','更新用户淇℃伅操作','<200 OK OK,[]>','SUCCESS','2025-11-17 12:26:16',1,'high'),(305,NULL,NULL,NULL,'更新用户','更新用户淇℃伅: null',NULL,'SUCCESS','2025-11-17 13:25:54',0,'normal'),(306,'admin',1,'0:0:0:0:0:0:0:1','updateUser','更新用户淇℃伅操作','<200 OK OK,[]>','SUCCESS','2025-11-17 13:25:54',1,'high'),(307,'admin',1,'0:0:0:0:0:0:0:1','removeRoleFromUser','Remove role from user - high sensitivity','<200 OK OK,Role removal successful,[]>','SUCCESS','2025-11-17 13:26:07',1,'high'),(308,NULL,NULL,NULL,'更新用户','更新用户淇℃伅: null',NULL,'SUCCESS','2025-11-17 13:27:05',0,'normal'),(309,'admin',1,'0:0:0:0:0:0:0:1','updateUser','更新用户淇℃伅操作','<200 OK OK,[]>','SUCCESS','2025-11-17 13:27:05',1,'high'),(310,'admin',1,'0:0:0:0:0:0:0:1','updateUser','更新用户淇℃伅操作','{\"error\": \"BadSqlGrammarException: \r\n### Error querying database.  Cause: java.sql.SQLSyntaxErrorException: Unknown column \'real_name\' in \'field list\'\r\n### The error may exist in com/mold/digitalization/mapper/UserMapper.java (best guess)\r\n### The error may involve defaultParameterMap\r\n### The error occurred while setting parameters\r\n### SQL: SELECT id,username,password,real_name,department_name AS department,status FROM `user` WHERE id=?\r\n### Cause: java.sql.SQLSyntaxErrorException: Unknown column \'real_name\' in \'field list\'\n; bad SQL grammar []; nested exception is java.sql.SQLSyntaxErrorException: Unknown column \'real_name\' in \'field list\'\"}','SUCCESS','2025-11-17 13:32:27',1,'high'),(311,'admin',1,'0:0:0:0:0:0:0:1','updateUser','更新用户淇℃伅操作','{\"error\": \"BadSqlGrammarException: \r\n### Error querying database.  Cause: java.sql.SQLSyntaxErrorException: Unknown column \'real_name\' in \'field list\'\r\n### The error may exist in com/mold/digitalization/mapper/UserMapper.java (best guess)\r\n### The error may involve defaultParameterMap\r\n### The error occurred while setting parameters\r\n### SQL: SELECT id,username,password,real_name,department_name AS department,status FROM `user` WHERE id=?\r\n### Cause: java.sql.SQLSyntaxErrorException: Unknown column \'real_name\' in \'field list\'\n; bad SQL grammar []; nested exception is java.sql.SQLSyntaxErrorException: Unknown column \'real_name\' in \'field list\'\"}','SUCCESS','2025-11-17 13:32:29',1,'high'),(312,'admin',1,'0:0:0:0:0:0:0:1','updateUser','更新用户淇℃伅操作','{\"error\": \"BadSqlGrammarException: \r\n### Error querying database.  Cause: java.sql.SQLSyntaxErrorException: Unknown column \'real_name\' in \'field list\'\r\n### The error may exist in com/mold/digitalization/mapper/UserMapper.java (best guess)\r\n### The error may involve defaultParameterMap\r\n### The error occurred while setting parameters\r\n### SQL: SELECT id,username,password,real_name,department_name AS department,status FROM `user` WHERE id=?\r\n### Cause: java.sql.SQLSyntaxErrorException: Unknown column \'real_name\' in \'field list\'\n; bad SQL grammar []; nested exception is java.sql.SQLSyntaxErrorException: Unknown column \'real_name\' in \'field list\'\"}','SUCCESS','2025-11-17 13:36:40',1,'high'),(313,'admin',1,'0:0:0:0:0:0:0:1','updateUser','更新用户淇℃伅操作','{\"error\": \"BadSqlGrammarException: \r\n### Error querying database.  Cause: java.sql.SQLSyntaxErrorException: Unknown column \'real_name\' in \'field list\'\r\n### The error may exist in com/mold/digitalization/mapper/UserMapper.java (best guess)\r\n### The error may involve defaultParameterMap\r\n### The error occurred while setting parameters\r\n### SQL: SELECT id,username,password,real_name,department_name AS department,status FROM `user` WHERE id=?\r\n### Cause: java.sql.SQLSyntaxErrorException: Unknown column \'real_name\' in \'field list\'\n; bad SQL grammar []; nested exception is java.sql.SQLSyntaxErrorException: Unknown column \'real_name\' in \'field list\'\"}','SUCCESS','2025-11-17 13:36:47',1,'high'),(314,'admin',1,'0:0:0:0:0:0:0:1','updateUser','更新用户淇℃伅操作','{\"error\": \"BadSqlGrammarException: \r\n### Error querying database.  Cause: java.sql.SQLSyntaxErrorException: Unknown column \'real_name\' in \'field list\'\r\n### The error may exist in com/mold/digitalization/mapper/UserMapper.java (best guess)\r\n### The error may involve defaultParameterMap\r\n### The error occurred while setting parameters\r\n### SQL: SELECT id,username,password,real_name,department_name AS department,status FROM `user` WHERE id=?\r\n### Cause: java.sql.SQLSyntaxErrorException: Unknown column \'real_name\' in \'field list\'\n; bad SQL grammar []; nested exception is java.sql.SQLSyntaxErrorException: Unknown column \'real_name\' in \'field list\'\"}','SUCCESS','2025-11-17 13:37:08',1,'high'),(315,'admin',1,'0:0:0:0:0:0:0:1','updateUser','更新用户淇℃伅操作','{\"error\": \"BadSqlGrammarException: \r\n### Error updating database.  Cause: java.sql.SQLSyntaxErrorException: Unknown column \'real_name\' in \'field list\'\r\n### The error may exist in com/mold/digitalization/mapper/UserMapper.java (best guess)\r\n### The error may involve com.mold.digitalization.mapper.UserMapper.updateById-Inline\r\n### The error occurred while setting parameters\r\n### SQL: UPDATE `user`  SET username=?, password=?, real_name=?, department_name=?,   status=?  WHERE id=?\r\n### Cause: java.sql.SQLSyntaxErrorException: Unknown column \'real_name\' in \'field list\'\n; bad SQL grammar []; nested exception is java.sql.SQLSyntaxErrorException: Unknown column \'real_name\' in \'field list\'\"}','SUCCESS','2025-11-17 16:56:55',1,'high'),(316,NULL,NULL,NULL,'更新用户','更新用户淇℃伅: test_mold_20251115165936',NULL,'SUCCESS','2025-11-17 17:34:33',0,'normal'),(317,'admin',1,'0:0:0:0:0:0:0:1','updateUser','更新用户淇℃伅操作','<200 OK OK,[]>','SUCCESS','2025-11-17 17:34:33',1,'high'),(318,NULL,NULL,NULL,'更新用户','更新用户淇℃伅: test_mold_20251115165936',NULL,'SUCCESS','2025-11-17 17:34:45',0,'normal'),(319,'admin',1,'0:0:0:0:0:0:0:1','updateUser','更新用户淇℃伅操作','<200 OK OK,[]>','SUCCESS','2025-11-17 17:34:45',1,'high'),(320,NULL,NULL,NULL,'更新用户','更新用户淇℃伅: test_mold_20251115165936',NULL,'SUCCESS','2025-11-17 17:35:01',0,'normal'),(321,'admin',1,'0:0:0:0:0:0:0:1','updateUser','更新用户淇℃伅操作','<200 OK OK,[]>','SUCCESS','2025-11-17 17:35:01',1,'high'),(322,NULL,NULL,NULL,'更新用户','更新用户淇℃伅: test_mold_20251115165936',NULL,'SUCCESS','2025-11-17 17:35:15',0,'normal'),(323,'admin',1,'0:0:0:0:0:0:0:1','updateUser','更新用户淇℃伅操作','<200 OK OK,[]>','SUCCESS','2025-11-17 17:35:15',1,'high'),(324,'admin',1,'0:0:0:0:0:0:0:1','removeRoleFromUser','Remove role from user - high sensitivity','<200 OK OK,Role removal successful,[]>','SUCCESS','2025-11-17 17:38:43',1,'high'),(325,NULL,NULL,NULL,'更新用户','更新用户淇℃伅: test_mold_20251115165936',NULL,'SUCCESS','2025-11-17 18:58:21',0,'normal'),(326,'admin',1,'0:0:0:0:0:0:0:1','updateUser','更新用户淇℃伅操作','<200 OK OK,[]>','SUCCESS','2025-11-17 18:58:21',1,'high'),(327,NULL,NULL,NULL,'更新用户','更新用户淇℃伅: test_mold_20251115165936',NULL,'SUCCESS','2025-11-17 18:58:31',0,'normal'),(328,'admin',1,'0:0:0:0:0:0:0:1','updateUser','更新用户淇℃伅操作','<200 OK OK,[]>','SUCCESS','2025-11-17 18:58:31',1,'high'),(329,NULL,NULL,NULL,'更新用户','更新用户淇℃伅: test_mold_20251115165936',NULL,'SUCCESS','2025-11-17 19:33:07',0,'normal'),(330,'admin',1,'0:0:0:0:0:0:0:1','updateUser','更新用户淇℃伅操作','<200 OK OK,[]>','SUCCESS','2025-11-17 19:33:08',1,'high'),(331,NULL,NULL,NULL,'更新用户','更新用户淇℃伅: test_mold_20251115165936',NULL,'SUCCESS','2025-11-17 19:37:57',0,'normal'),(332,'admin',1,'0:0:0:0:0:0:0:1','updateUser','更新用户淇℃伅操作','<200 OK OK,[]>','SUCCESS','2025-11-17 19:37:57',1,'high'),(333,NULL,NULL,NULL,'更新用户','更新用户淇℃伅: test_mold_20251115165936',NULL,'SUCCESS','2025-11-17 19:49:29',0,'normal'),(334,'admin',1,'0:0:0:0:0:0:0:1','updateUser','更新用户淇℃伅操作','<200 OK OK,[]>','SUCCESS','2025-11-17 19:49:29',1,'high'),(335,NULL,NULL,NULL,'更新用户','更新用户淇℃伅: test_mold_20251115165936',NULL,'SUCCESS','2025-11-17 21:15:40',0,'normal'),(336,'admin',1,'0:0:0:0:0:0:0:1','updateUser','更新用户淇℃伅操作','<200 OK OK,[]>','SUCCESS','2025-11-17 21:15:40',1,'high'),(337,NULL,NULL,NULL,'更新用户','更新用户淇℃伅: test_mold_20251115165936',NULL,'SUCCESS','2025-11-17 21:16:08',0,'normal'),(338,'admin',1,'0:0:0:0:0:0:0:1','updateUser','更新用户淇℃伅操作','<200 OK OK,[]>','SUCCESS','2025-11-17 21:16:08',1,'high'),(339,NULL,NULL,NULL,'更新用户','更新用户淇℃伅: admin',NULL,'SUCCESS','2025-11-17 21:20:08',0,'normal'),(340,'admin',1,'0:0:0:0:0:0:0:1','updateUser','更新用户淇℃伅操作','<200 OK OK,[]>','SUCCESS','2025-11-17 21:20:08',1,'high'),(341,NULL,NULL,NULL,'更新用户','更新用户淇℃伅: admin',NULL,'SUCCESS','2025-11-17 21:22:44',0,'normal'),(342,'admin',1,'0:0:0:0:0:0:0:1','updateUser','更新用户淇℃伅操作','<200 OK OK,[]>','SUCCESS','2025-11-17 21:22:45',1,'high'),(343,NULL,NULL,NULL,'更新用户','更新用户淇℃伅: test_mold_20251115165936',NULL,'SUCCESS','2025-11-17 21:29:05',0,'normal'),(344,'admin',1,'0:0:0:0:0:0:0:1','updateUser','更新用户淇℃伅操作','<200 OK OK,[]>','SUCCESS','2025-11-17 21:29:05',1,'high'),(345,'admin',1,'0:0:0:0:0:0:0:1','batchAssignRolesToUser','Batch assign roles to user - high sensitivity','<200 OK OK,Batch role assignment successful,[]>','SUCCESS','2025-11-17 21:33:19',1,'high'),(346,NULL,NULL,NULL,'更新用户','更新用户淇℃伅: test_mold_20251115163509',NULL,'SUCCESS','2025-11-18 09:56:57',0,'normal'),(347,'admin',1,'0:0:0:0:0:0:0:1','updateUser','更新用户淇℃伅操作','<200 OK OK,[]>','SUCCESS','2025-11-18 09:56:57',1,'high'),(348,'admin',1,'0:0:0:0:0:0:0:1','batchAssignRolesToUser','Batch assign roles to user - high sensitivity','<200 OK OK,Batch role assignment successful,[]>','SUCCESS','2025-11-19 19:42:44',1,'high'),(349,NULL,NULL,NULL,'更新用户','更新用户淇℃伅: test_mold_20251115163527',NULL,'SUCCESS','2025-11-19 19:48:54',0,'normal'),(350,'admin',1,'0:0:0:0:0:0:0:1','updateUser','更新用户淇℃伅操作','<200 OK OK,[]>','SUCCESS','2025-11-19 19:48:54',1,'high'),(351,'admin',1,'0:0:0:0:0:0:0:1','batchAssignRolesToUser','Batch assign roles to user - high sensitivity','<200 OK OK,Batch role assignment successful,[]>','SUCCESS','2025-11-19 19:49:32',1,'high'),(352,NULL,NULL,NULL,'更新用户','更新用户淇℃伅: test_mold_20251115163527',NULL,'SUCCESS','2025-11-20 13:19:59',0,'normal'),(353,'admin',1,'0:0:0:0:0:0:0:1','updateUser','更新用户淇℃伅操作','<200 OK OK,[]>','SUCCESS','2025-11-20 13:19:59',1,'high'),(354,NULL,NULL,NULL,'更新用户','更新用户淇℃伅: test_mold_20251115163527',NULL,'SUCCESS','2025-11-20 13:40:07',0,'normal'),(355,'admin',1,'0:0:0:0:0:0:0:1','updateUser','更新用户淇℃伅操作','<200 OK OK,[]>','SUCCESS','2025-11-20 13:40:07',1,'high'),(356,NULL,NULL,NULL,'更新用户','更新用户淇℃伅: test_mold_20251115163527',NULL,'SUCCESS','2025-11-20 17:40:44',0,'normal'),(357,'admin',1,'0:0:0:0:0:0:0:1','updateUser','更新用户淇℃伅操作','<200 OK OK,[]>','SUCCESS','2025-11-20 17:40:44',1,'high'),(358,NULL,NULL,NULL,'更新用户','更新用户淇℃伅: test_mold_20251115165936',NULL,'SUCCESS','2025-11-21 12:28:30',0,'normal'),(359,'admin',1,'0:0:0:0:0:0:0:1','updateUser','更新用户淇℃伅操作','<200 OK OK,[]>','SUCCESS','2025-11-21 12:28:30',1,'high'),(360,NULL,NULL,NULL,'更新用户','更新用户淇℃伅: test_mold_20251115163527',NULL,'SUCCESS','2025-11-21 12:28:39',0,'normal'),(361,'admin',1,'0:0:0:0:0:0:0:1','updateUser','更新用户淇℃伅操作','<200 OK OK,[]>','SUCCESS','2025-11-21 12:28:39',1,'high'),(362,NULL,NULL,NULL,'更新用户','更新用户淇℃伅: test_mold_20251115163503',NULL,'SUCCESS','2025-11-21 12:29:05',0,'normal'),(363,'admin',1,'0:0:0:0:0:0:0:1','updateUser','更新用户淇℃伅操作','<200 OK OK,[]>','SUCCESS','2025-11-21 12:29:05',1,'high'),(364,NULL,NULL,NULL,'更新用户','更新用户淇℃伅: PLX',NULL,'SUCCESS','2025-11-21 12:32:48',0,'normal'),(365,'admin',1,'0:0:0:0:0:0:0:1','updateUser','更新用户淇℃伅操作','<200 OK OK,[]>','SUCCESS','2025-11-21 12:32:48',1,'high'),(366,NULL,NULL,NULL,'更新用户','更新用户淇℃伅: CJY',NULL,'SUCCESS','2025-11-21 12:32:59',0,'normal'),(367,'admin',1,'0:0:0:0:0:0:0:1','updateUser','更新用户淇℃伅操作','<200 OK OK,[]>','SUCCESS','2025-11-21 12:32:59',1,'high'),(368,NULL,NULL,NULL,'更新用户','更新用户淇℃伅: LJ1',NULL,'SUCCESS','2025-11-21 12:33:13',0,'normal'),(369,'admin',1,'0:0:0:0:0:0:0:1','updateUser','更新用户淇℃伅操作','<200 OK OK,[]>','SUCCESS','2025-11-21 12:33:13',1,'high'),(370,NULL,NULL,NULL,'更新用户','更新用户淇℃伅: CJT',NULL,'SUCCESS','2025-11-21 12:33:25',0,'normal'),(371,'admin',1,'0:0:0:0:0:0:0:1','updateUser','更新用户淇℃伅操作','<200 OK OK,[]>','SUCCESS','2025-11-21 12:33:25',1,'high'),(372,'admin',1,'127.0.0.1','resetPassword','Admin action: reset user password','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-21 19:36:15',1,'critical'),(373,'admin',1,'127.0.0.1','lockUser','Admin action: lock/unlock user account','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-21 19:36:15',1,'critical'),(374,'admin',1,'127.0.0.1','unlockUser','Admin action: unlock user account','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-21 19:36:15',1,'critical'),(375,'admin',1,'127.0.0.1','unlockUserJson','Admin action: unlock user account (JSON)','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-21 19:36:16',1,'critical'),(376,'admin',1,'127.0.0.1','unlockUser','Admin action: unlock user account','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-21 19:36:16',1,'critical'),(377,'admin',1,'127.0.0.1','unlockUser','Admin action: unlock user account','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-21 19:36:17',1,'critical'),(378,'admin',1,'127.0.0.1','unlockUser','Admin action: unlock user account','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-21 21:35:29',1,'critical'),(379,'admin',1,'127.0.0.1','resetPassword','Admin action: reset user password','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-21 21:36:13',1,'critical'),(380,'admin',1,'127.0.0.1','lockUser','Admin action: lock/unlock user account','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-21 21:36:13',1,'critical'),(381,'admin',1,'127.0.0.1','unlockUser','Admin action: unlock user account','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-21 21:36:14',1,'critical'),(382,'admin',1,'127.0.0.1','unlockUserJson','Admin action: unlock user account (JSON)','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-21 21:36:14',1,'critical'),(383,'admin',1,'127.0.0.1','resetPassword','Admin action: reset user password','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-21 22:11:58',1,'critical'),(384,'admin',1,'127.0.0.1','lockUser','Admin action: lock/unlock user account','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-21 22:11:58',1,'critical'),(385,'admin',1,'127.0.0.1','unlockUser','Admin action: unlock user account','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-21 22:11:59',1,'critical'),(386,'admin',1,'127.0.0.1','unlockUserJson','Admin action: unlock user account (JSON)','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-21 22:11:59',1,'critical'),(387,'admin',1,'127.0.0.1','resetPassword','Admin action: reset user password','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-21 22:12:28',1,'critical'),(388,'admin',1,'127.0.0.1','lockUser','Admin action: lock/unlock user account','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-21 22:12:28',1,'critical'),(389,'admin',1,'127.0.0.1','unlockUser','Admin action: unlock user account','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-21 22:12:29',1,'critical'),(390,'admin',1,'127.0.0.1','unlockUserJson','Admin action: unlock user account (JSON)','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-21 22:12:29',1,'critical'),(391,'admin',1,'127.0.0.1','resetPassword','Admin action: reset user password','<200 OK OK,ResponseDTO(code=200, message=密码重置成功, data=null),[]>','SUCCESS','2025-11-21 22:14:01',1,'critical'),(392,'admin',1,'127.0.0.1','resetPassword','Admin action: reset user password','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-21 22:14:19',1,'critical'),(393,'admin',1,'127.0.0.1','lockUser','Admin action: lock/unlock user account','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-21 22:14:19',1,'critical'),(394,'admin',1,'127.0.0.1','unlockUser','Admin action: unlock user account','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-21 22:14:19',1,'critical'),(395,'admin',1,'127.0.0.1','unlockUserJson','Admin action: unlock user account (JSON)','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-21 22:14:19',1,'critical'),(396,'admin',1,'127.0.0.1','resetPassword','Admin action: reset user password','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-21 22:20:09',1,'critical'),(397,'admin',1,'127.0.0.1','lockUser','Admin action: lock/unlock user account','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-21 22:20:09',1,'critical'),(398,'admin',1,'127.0.0.1','unlockUser','Admin action: unlock user account','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-21 22:20:09',1,'critical'),(399,'admin',1,'127.0.0.1','unlockUserJson','Admin action: unlock user account (JSON)','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-21 22:20:10',1,'critical'),(400,'admin',1,'127.0.0.1','resetPassword','Admin action: reset user password','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-21 22:20:28',1,'critical'),(401,'admin',1,'127.0.0.1','lockUser','Admin action: lock/unlock user account','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-21 22:20:28',1,'critical'),(402,'admin',1,'127.0.0.1','unlockUser','Admin action: unlock user account','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-21 22:20:28',1,'critical'),(403,'admin',1,'127.0.0.1','unlockUserJson','Admin action: unlock user account (JSON)','{\"error\": \"BusinessException: 用户未找到\"}','SUCCESS','2025-11-21 22:20:28',1,'critical'),(404,NULL,NULL,NULL,'更新用户','更新用户淇℃伅: PLX',NULL,'SUCCESS','2025-12-21 08:15:53',0,'normal'),(405,'admin',1,'127.0.0.1','updateUser','更新用户淇℃伅操作','<200 OK OK,[]>','SUCCESS','2025-12-21 08:15:53',1,'high'),(406,NULL,NULL,NULL,'更新用户','更新用户淇℃伅: PLX',NULL,'SUCCESS','2025-12-21 15:18:01',0,'normal'),(407,'admin',1,'127.0.0.1','updateUser','更新用户淇℃伅操作','<200 OK OK,[]>','SUCCESS','2025-12-21 15:18:01',1,'high'),(408,NULL,NULL,NULL,'更新用户','更新用户淇℃伅: PLX',NULL,'SUCCESS','2025-12-21 15:18:38',0,'normal'),(409,'admin',1,'127.0.0.1','updateUser','更新用户淇℃伅操作','<200 OK OK,[]>','SUCCESS','2025-12-21 15:18:38',1,'high'),(410,NULL,NULL,NULL,'更新用户','更新用户淇℃伅: PLX',NULL,'SUCCESS','2025-12-21 15:19:59',0,'normal'),(411,'admin',1,'127.0.0.1','updateUser','更新用户淇℃伅操作','<200 OK OK,[]>','SUCCESS','2025-12-21 15:19:59',1,'high'),(412,NULL,NULL,NULL,'更新用户','更新用户淇℃伅: PLX',NULL,'SUCCESS','2025-12-21 16:00:46',0,'normal'),(413,'admin',1,'127.0.0.1','updateUser','更新用户淇℃伅操作','<200 OK OK,[]>','SUCCESS','2025-12-21 16:00:46',1,'high'),(414,NULL,NULL,NULL,'更新用户','更新用户淇℃伅: PLX',NULL,'SUCCESS','2025-12-21 16:04:50',0,'normal'),(415,'admin',1,'127.0.0.1','updateUser','更新用户淇℃伅操作','<200 OK OK,[]>','SUCCESS','2025-12-21 16:04:50',1,'high'),(416,NULL,NULL,NULL,'更新用户','更新用户淇℃伅: PLX',NULL,'SUCCESS','2025-12-21 17:44:24',0,'normal'),(417,'admin',1,'127.0.0.1','updateUser','更新用户淇℃伅操作','<200 OK OK,[]>','SUCCESS','2025-12-21 17:44:24',1,'high'),(418,NULL,NULL,NULL,'更新用户','更新用户淇℃伅: PLX',NULL,'SUCCESS','2025-12-22 13:38:51',0,'normal'),(419,'admin',1,'127.0.0.1','updateUser','更新用户淇℃伅操作','<200 OK OK,[]>','SUCCESS','2025-12-22 13:38:51',1,'high'),(420,NULL,NULL,NULL,'更新用户','更新用户淇℃伅: PLX',NULL,'SUCCESS','2025-12-22 21:18:53',0,'normal'),(421,'admin',1,'127.0.0.1','updateUser','更新用户淇℃伅操作','<200 OK OK,[]>','SUCCESS','2025-12-22 21:18:53',1,'high'),(422,NULL,NULL,NULL,'更新用户','更新用户淇℃伅: PLX',NULL,'SUCCESS','2025-12-22 21:20:28',0,'normal'),(423,'admin',1,'127.0.0.1','updateUser','更新用户淇℃伅操作','<200 OK OK,[]>','SUCCESS','2025-12-22 21:20:28',1,'high'),(424,NULL,NULL,NULL,'更新用户','更新用户淇℃伅: PLX',NULL,'SUCCESS','2025-12-22 21:22:56',0,'normal'),(425,'admin',1,'127.0.0.1','updateUser','更新用户淇℃伅操作','<200 OK OK,[]>','SUCCESS','2025-12-22 21:22:56',1,'high'),(426,NULL,NULL,NULL,'更新用户','更新用户淇℃伅: PLX',NULL,'SUCCESS','2025-12-22 21:25:53',0,'normal'),(427,'admin',1,'127.0.0.1','updateUser','更新用户淇℃伅操作','<200 OK OK,[]>','SUCCESS','2025-12-22 21:25:53',1,'high'),(428,NULL,NULL,NULL,'更新用户','更新用户淇℃伅: PLX',NULL,'SUCCESS','2025-12-22 21:53:19',0,'normal'),(429,'admin',1,'127.0.0.1','updateUser','更新用户淇℃伅操作','<200 OK OK,[]>','SUCCESS','2025-12-22 21:53:19',1,'high'),(430,NULL,NULL,NULL,'更新用户','更新用户淇℃伅: PLX',NULL,'SUCCESS','2025-12-22 21:53:28',0,'normal'),(431,'admin',1,'127.0.0.1','updateUser','更新用户淇℃伅操作','<200 OK OK,[]>','SUCCESS','2025-12-22 21:53:28',1,'high'),(432,NULL,NULL,NULL,'更新用户','更新用户淇℃伅: PLX',NULL,'SUCCESS','2025-12-22 21:54:48',0,'normal'),(433,'admin',1,'127.0.0.1','updateUser','更新用户淇℃伅操作','<200 OK OK,[]>','SUCCESS','2025-12-22 21:54:48',1,'high'),(434,NULL,NULL,NULL,'更新用户','更新用户淇℃伅: PLX',NULL,'SUCCESS','2025-12-22 22:02:27',0,'normal'),(435,'admin',1,'127.0.0.1','updateUser','更新用户淇℃伅操作','<200 OK OK,[]>','SUCCESS','2025-12-22 22:02:27',1,'high'),(436,NULL,NULL,NULL,'更新用户','更新用户淇℃伅: PLX',NULL,'SUCCESS','2025-12-22 22:02:35',0,'normal'),(437,'admin',1,'127.0.0.1','updateUser','更新用户淇℃伅操作','<200 OK OK,[]>','SUCCESS','2025-12-22 22:02:35',1,'high'),(438,NULL,NULL,NULL,'更新用户','更新用户淇℃伅: CJY',NULL,'SUCCESS','2025-12-22 22:04:00',0,'normal'),(439,'admin',1,'127.0.0.1','updateUser','更新用户淇℃伅操作','<200 OK OK,[]>','SUCCESS','2025-12-22 22:04:00',1,'high'),(440,NULL,NULL,NULL,'更新用户','更新用户淇℃伅: PLX',NULL,'SUCCESS','2025-12-22 22:04:19',0,'normal'),(441,'admin',1,'127.0.0.1','updateUser','更新用户淇℃伅操作','<200 OK OK,[]>','SUCCESS','2025-12-22 22:04:19',1,'high'),(442,NULL,NULL,NULL,'更新用户','更新用户淇℃伅: CJY',NULL,'SUCCESS','2025-12-22 22:04:31',0,'normal'),(443,'admin',1,'127.0.0.1','updateUser','更新用户淇℃伅操作','<200 OK OK,[]>','SUCCESS','2025-12-22 22:04:31',1,'high'),(444,NULL,NULL,NULL,'更新用户','更新用户淇℃伅: PLX',NULL,'SUCCESS','2025-12-23 11:06:57',0,'normal'),(445,'admin',1,'127.0.0.1','updateUser','更新用户淇℃伅操作','<200 OK OK,[]>','SUCCESS','2025-12-23 11:06:57',1,'high'),(446,NULL,NULL,NULL,'更新用户','更新用户淇℃伅: CJY',NULL,'SUCCESS','2025-12-23 11:07:04',0,'normal'),(447,'admin',1,'127.0.0.1','updateUser','更新用户淇℃伅操作','<200 OK OK,[]>','SUCCESS','2025-12-23 11:07:04',1,'high'),(448,NULL,NULL,NULL,'更新用户','更新用户淇℃伅: LJ1',NULL,'SUCCESS','2025-12-23 11:07:10',0,'normal'),(449,'admin',1,'127.0.0.1','updateUser','更新用户淇℃伅操作','<200 OK OK,[]>','SUCCESS','2025-12-23 11:07:10',1,'high'),(450,NULL,NULL,NULL,'更新用户','更新用户淇℃伅: LJ1',NULL,'SUCCESS','2025-12-23 11:07:19',0,'normal'),(451,'admin',1,'127.0.0.1','updateUser','更新用户淇℃伅操作','<200 OK OK,[]>','SUCCESS','2025-12-23 11:07:19',1,'high'),(452,NULL,NULL,NULL,'更新用户','更新用户淇℃伅: CJT',NULL,'SUCCESS','2025-12-23 11:07:46',0,'normal'),(453,'admin',1,'127.0.0.1','updateUser','更新用户淇℃伅操作','<200 OK OK,[]>','SUCCESS','2025-12-23 11:07:46',1,'high'),(454,NULL,NULL,NULL,'更新用户','更新用户淇℃伅: PLX',NULL,'SUCCESS','2025-12-23 11:39:55',0,'normal'),(455,'admin',1,'127.0.0.1','updateUser','更新用户淇℃伅操作','<200 OK OK,[]>','SUCCESS','2025-12-23 11:39:55',1,'high'),(456,NULL,NULL,NULL,'更新用户','更新用户淇℃伅: PLX',NULL,'SUCCESS','2025-12-23 11:59:28',0,'normal'),(457,'admin',1,'127.0.0.1','updateUser','更新用户淇℃伅操作','<200 OK OK,[]>','SUCCESS','2025-12-23 11:59:28',1,'high'),(458,NULL,NULL,NULL,'更新用户','更新用户淇℃伅: PLX',NULL,'SUCCESS','2025-12-23 13:27:30',0,'normal'),(459,'admin',1,'127.0.0.1','updateUser','更新用户淇℃伅操作','<200 OK OK,[]>','SUCCESS','2025-12-23 13:27:30',1,'high'),(460,NULL,NULL,NULL,'更新用户','更新用户淇℃伅: PLX',NULL,'SUCCESS','2025-12-23 13:27:39',0,'normal'),(461,'admin',1,'127.0.0.1','updateUser','更新用户淇℃伅操作','<200 OK OK,[]>','SUCCESS','2025-12-23 13:27:39',1,'high'),(462,NULL,NULL,NULL,'更新用户','更新用户淇℃伅: PLX',NULL,'SUCCESS','2025-12-23 13:28:04',0,'normal'),(463,'admin',1,'127.0.0.1','updateUser','更新用户淇℃伅操作','<200 OK OK,[]>','SUCCESS','2025-12-23 13:28:04',1,'high'),(464,NULL,NULL,NULL,'更新用户','更新用户淇℃伅: PLX',NULL,'SUCCESS','2025-12-23 13:28:14',0,'normal'),(465,'admin',1,'127.0.0.1','updateUser','更新用户淇℃伅操作','<200 OK OK,[]>','SUCCESS','2025-12-23 13:28:14',1,'high'),(466,NULL,NULL,NULL,'更新用户','更新用户淇℃伅: PLX',NULL,'SUCCESS','2025-12-23 13:31:16',0,'normal'),(467,'admin',1,'127.0.0.1','updateUser','更新用户淇℃伅操作','<200 OK OK,[]>','SUCCESS','2025-12-23 13:31:16',1,'high'),(468,NULL,NULL,NULL,'更新用户','更新用户淇℃伅: PLX',NULL,'SUCCESS','2025-12-23 13:31:35',0,'normal'),(469,'admin',1,'127.0.0.1','updateUser','更新用户淇℃伅操作','<200 OK OK,[]>','SUCCESS','2025-12-23 13:31:35',1,'high'),(470,NULL,NULL,NULL,'更新用户','更新用户淇℃伅: PLX',NULL,'SUCCESS','2025-12-23 13:33:07',0,'normal'),(471,'admin',1,'127.0.0.1','updateUser','更新用户淇℃伅操作','<200 OK OK,[]>','SUCCESS','2025-12-23 13:33:07',1,'high'),(472,'admin',1,'127.0.0.1','batchAssignRolesToUser','Batch assign roles to user - high sensitivity','<200 OK OK,Batch role assignment successful,[]>','SUCCESS','2025-12-23 13:33:07',1,'high'),(473,NULL,NULL,NULL,'更新用户','更新用户淇℃伅: PLX',NULL,'SUCCESS','2025-12-23 13:33:15',0,'normal'),(474,'admin',1,'127.0.0.1','updateUser','更新用户淇℃伅操作','<200 OK OK,[]>','SUCCESS','2025-12-23 13:33:15',1,'high'),(475,'admin',1,'127.0.0.1','batchAssignRolesToUser','Batch assign roles to user - high sensitivity','<200 OK OK,Batch role assignment successful,[]>','SUCCESS','2025-12-23 13:33:15',1,'high'),(476,NULL,NULL,NULL,'创建用户','创建用户: ybh',NULL,'SUCCESS','2025-12-23 13:55:24',0,'normal'),(477,'admin',1,'127.0.0.1','createUser','Create user operation','<201 CREATED Created,UserResponse(id=24, username=ybh, realName=易本红, phone=, email=null, departmentName=null, status=1, locked=null, roles=null),[]>','SUCCESS','2025-12-23 13:55:24',1,'high'),(478,NULL,NULL,NULL,'更新用户','更新用户淇℃伅: ybh',NULL,'SUCCESS','2025-12-23 13:55:35',0,'normal'),(479,'admin',1,'127.0.0.1','updateUser','更新用户淇℃伅操作','<200 OK OK,[]>','SUCCESS','2025-12-23 13:55:35',1,'high'),(480,'admin',1,'127.0.0.1','batchAssignRolesToUser','Batch assign roles to user - high sensitivity','<200 OK OK,Batch role assignment successful,[]>','SUCCESS','2025-12-23 13:55:35',1,'high'),(481,NULL,NULL,NULL,'更新用户','更新用户淇℃伅: ybh',NULL,'SUCCESS','2025-12-23 13:55:43',0,'normal'),(482,'admin',1,'127.0.0.1','updateUser','更新用户淇℃伅操作','<200 OK OK,[]>','SUCCESS','2025-12-23 13:55:43',1,'high'),(483,'admin',1,'127.0.0.1','batchAssignRolesToUser','Batch assign roles to user - high sensitivity','<200 OK OK,Batch role assignment successful,[]>','SUCCESS','2025-12-23 13:55:43',1,'high'),(484,NULL,NULL,NULL,'更新用户','更新用户淇℃伅: ybh',NULL,'SUCCESS','2025-12-23 13:55:53',0,'normal'),(485,'admin',1,'127.0.0.1','updateUser','更新用户淇℃伅操作','<200 OK OK,[]>','SUCCESS','2025-12-23 13:55:53',1,'high'),(486,'admin',1,'127.0.0.1','batchAssignRolesToUser','Batch assign roles to user - high sensitivity','<200 OK OK,Batch role assignment successful,[]>','SUCCESS','2025-12-23 13:55:53',1,'high'),(487,NULL,NULL,NULL,'创建用户','创建用户: cxf',NULL,'SUCCESS','2025-12-23 13:59:50',0,'normal'),(488,'admin',1,'127.0.0.1','createUser','Create user operation','<201 CREATED Created,UserResponse(id=25, username=cxf, realName=程晓峰, phone=, email=null, departmentName=null, status=1, locked=null, roles=null),[]>','SUCCESS','2025-12-23 13:59:50',1,'high'),(489,'admin',1,'127.0.0.1','batchAssignRolesToUser','Batch assign roles to user - high sensitivity','<200 OK OK,Batch role assignment successful,[]>','SUCCESS','2025-12-23 13:59:50',1,'high'),(490,NULL,NULL,NULL,'创建用户','创建用户: cjz',NULL,'SUCCESS','2025-12-23 14:06:19',0,'normal'),(491,'admin',1,'127.0.0.1','createUser','Create user operation','<201 CREATED Created,UserResponse(id=26, username=cjz, realName=程爵志, phone=, email=, departmentName=模具调试组, status=1, locked=null, roles=null),[]>','SUCCESS','2025-12-23 14:06:19',1,'high'),(492,'admin',1,'127.0.0.1','batchAssignRolesToUser','Batch assign roles to user - high sensitivity','<200 OK OK,Batch role assignment successful,[]>','SUCCESS','2025-12-23 14:06:19',1,'high'),(493,NULL,NULL,NULL,'更新用户','更新用户淇℃伅: cxf',NULL,'SUCCESS','2025-12-23 14:06:36',0,'normal'),(494,'admin',1,'127.0.0.1','updateUser','更新用户淇℃伅操作','<200 OK OK,[]>','SUCCESS','2025-12-23 14:06:36',1,'high'),(495,'admin',1,'127.0.0.1','batchAssignRolesToUser','Batch assign roles to user - high sensitivity','<200 OK OK,Batch role assignment successful,[]>','SUCCESS','2025-12-23 14:06:36',1,'high'),(496,NULL,NULL,NULL,'删除用户','删除用户: test_mold_20251115162153',NULL,'SUCCESS','2025-12-23 14:06:45',0,'normal'),(497,'admin',1,'127.0.0.1','deleteUser','删除用户操作','<200 OK OK,[]>','SUCCESS','2025-12-23 14:06:45',1,'critical'),(498,NULL,NULL,NULL,'删除用户','删除用户: test_mold_20251115162121',NULL,'SUCCESS','2025-12-23 14:08:18',0,'normal'),(499,'admin',1,'127.0.0.1','deleteUser','删除用户操作','<200 OK OK,[]>','SUCCESS','2025-12-23 14:08:18',1,'critical'),(500,NULL,NULL,NULL,'删除用户','删除用户: test_mold_20251115162116',NULL,'SUCCESS','2025-12-23 14:12:47',0,'normal'),(501,NULL,NULL,NULL,'删除用户','删除用户: test_mold_20251115161452',NULL,'SUCCESS','2025-12-23 14:12:47',0,'normal'),(502,NULL,NULL,NULL,'删除用户','删除用户: test_mold_20251115161203',NULL,'SUCCESS','2025-12-23 14:12:47',0,'normal'),(503,NULL,NULL,NULL,'删除用户','删除用户: test_mold_20251115160934',NULL,'SUCCESS','2025-12-23 14:12:47',0,'normal'),(504,'admin',1,'127.0.0.1','deleteUser','删除用户操作','<200 OK OK,[]>','SUCCESS','2025-12-23 14:12:47',1,'critical'),(505,NULL,NULL,NULL,'删除用户','删除用户: test_mold_20251115151939',NULL,'SUCCESS','2025-12-23 14:12:47',0,'normal'),(506,'admin',1,'127.0.0.1','deleteUser','删除用户操作','<200 OK OK,[]>','SUCCESS','2025-12-23 14:12:47',1,'critical'),(507,'admin',1,'127.0.0.1','deleteUser','删除用户操作','<200 OK OK,[]>','SUCCESS','2025-12-23 14:12:47',1,'critical'),(508,'admin',1,'127.0.0.1','deleteUser','删除用户操作','<200 OK OK,[]>','SUCCESS','2025-12-23 14:12:47',1,'critical'),(509,'admin',1,'127.0.0.1','deleteUser','删除用户操作','<200 OK OK,[]>','SUCCESS','2025-12-23 14:12:47',1,'critical'),(510,NULL,NULL,NULL,'删除用户','删除用户: test_mold_20251115160925',NULL,'SUCCESS','2025-12-23 14:12:47',0,'normal'),(511,'admin',1,'127.0.0.1','deleteUser','删除用户操作','<200 OK OK,[]>','SUCCESS','2025-12-23 14:12:47',1,'critical'),(512,'admin',1,'127.0.0.1','deleteUser','删除用户操作','{\"error\": \"DataIntegrityViolationException: \r\n### Error updating database.  Cause: java.sql.SQLIntegrityConstraintViolationException: Cannot delete or update a parent row: a foreign key constraint fails (`mold_digitalization`.`process_status_history`, CONSTRAINT `fk_history_operator` FOREIGN KEY (`operator_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT)\r\n### The error may exist in com/mold/digitalization/mapper/UserMapper.java (best guess)\r\n### The error may involve defaultParameterMap\r\n### The error occurred while setting parameters\r\n### SQL: DELETE FROM `user` WHERE id=?\r\n### Cause: java.sql.SQLIntegrityConstraintViolationException: Cannot delete or update a parent row: a foreign key constraint fails (`mold_digitalization`.`process_status_history`, CONSTRAINT `fk_history_operator` FOREIGN KEY (`operator_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT)\n; Cannot delete or update a parent row: a foreign key constraint fails (`mold_digitalization`.`process_status_history`, CONSTRAINT `fk_history_operator` FOREIGN KEY (`operator_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT); nested exception is java.sql.SQLIntegrityConstraintViolationException: Cannot delete or update a parent row: a foreign key constraint fails (`mold_digitalization`.`process_status_history`, CONSTRAINT `fk_history_operator` FOREIGN KEY (`operator_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT)\"}','SUCCESS','2025-12-23 14:12:47',1,'critical'),(513,'admin',1,'127.0.0.1','deleteUser','删除用户操作','{\"error\": \"DataIntegrityViolationException: \r\n### Error updating database.  Cause: java.sql.SQLIntegrityConstraintViolationException: Cannot delete or update a parent row: a foreign key constraint fails (`mold_digitalization`.`mold_process`, CONSTRAINT `fk_process_operator` FOREIGN KEY (`operator_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT)\r\n### The error may exist in com/mold/digitalization/mapper/UserMapper.java (best guess)\r\n### The error may involve defaultParameterMap\r\n### The error occurred while setting parameters\r\n### SQL: DELETE FROM `user` WHERE id=?\r\n### Cause: java.sql.SQLIntegrityConstraintViolationException: Cannot delete or update a parent row: a foreign key constraint fails (`mold_digitalization`.`mold_process`, CONSTRAINT `fk_process_operator` FOREIGN KEY (`operator_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT)\n; Cannot delete or update a parent row: a foreign key constraint fails (`mold_digitalization`.`mold_process`, CONSTRAINT `fk_process_operator` FOREIGN KEY (`operator_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT); nested exception is java.sql.SQLIntegrityConstraintViolationException: Cannot delete or update a parent row: a foreign key constraint fails (`mold_digitalization`.`mold_process`, CONSTRAINT `fk_process_operator` FOREIGN KEY (`operator_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT)\"}','SUCCESS','2025-12-23 14:12:47',1,'critical'),(514,'admin',1,'127.0.0.1','deleteUser','删除用户操作','{\"error\": \"DataIntegrityViolationException: \r\n### Error updating database.  Cause: java.sql.SQLIntegrityConstraintViolationException: Cannot delete or update a parent row: a foreign key constraint fails (`mold_digitalization`.`mold_process`, CONSTRAINT `fk_process_operator` FOREIGN KEY (`operator_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT)\r\n### The error may exist in com/mold/digitalization/mapper/UserMapper.java (best guess)\r\n### The error may involve defaultParameterMap\r\n### The error occurred while setting parameters\r\n### SQL: DELETE FROM `user` WHERE id=?\r\n### Cause: java.sql.SQLIntegrityConstraintViolationException: Cannot delete or update a parent row: a foreign key constraint fails (`mold_digitalization`.`mold_process`, CONSTRAINT `fk_process_operator` FOREIGN KEY (`operator_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT)\n; Cannot delete or update a parent row: a foreign key constraint fails (`mold_digitalization`.`mold_process`, CONSTRAINT `fk_process_operator` FOREIGN KEY (`operator_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT); nested exception is java.sql.SQLIntegrityConstraintViolationException: Cannot delete or update a parent row: a foreign key constraint fails (`mold_digitalization`.`mold_process`, CONSTRAINT `fk_process_operator` FOREIGN KEY (`operator_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT)\"}','SUCCESS','2025-12-23 14:12:47',1,'critical'),(515,'admin',1,'127.0.0.1','deleteUser','删除用户操作','{\"error\": \"DataIntegrityViolationException: \r\n### Error updating database.  Cause: java.sql.SQLIntegrityConstraintViolationException: Cannot delete or update a parent row: a foreign key constraint fails (`mold_digitalization`.`process_status_history`, CONSTRAINT `fk_history_operator` FOREIGN KEY (`operator_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT)\r\n### The error may exist in com/mold/digitalization/mapper/UserMapper.java (best guess)\r\n### The error may involve defaultParameterMap\r\n### The error occurred while setting parameters\r\n### SQL: DELETE FROM `user` WHERE id=?\r\n### Cause: java.sql.SQLIntegrityConstraintViolationException: Cannot delete or update a parent row: a foreign key constraint fails (`mold_digitalization`.`process_status_history`, CONSTRAINT `fk_history_operator` FOREIGN KEY (`operator_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT)\n; Cannot delete or update a parent row: a foreign key constraint fails (`mold_digitalization`.`process_status_history`, CONSTRAINT `fk_history_operator` FOREIGN KEY (`operator_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT); nested exception is java.sql.SQLIntegrityConstraintViolationException: Cannot delete or update a parent row: a foreign key constraint fails (`mold_digitalization`.`process_status_history`, CONSTRAINT `fk_history_operator` FOREIGN KEY (`operator_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT)\"}','SUCCESS','2025-12-23 14:12:47',1,'critical'),(516,'admin',1,'127.0.0.1','deleteUser','删除用户操作','{\"error\": \"DataIntegrityViolationException: \r\n### Error updating database.  Cause: java.sql.SQLIntegrityConstraintViolationException: Cannot delete or update a parent row: a foreign key constraint fails (`mold_digitalization`.`process_status_history`, CONSTRAINT `fk_history_operator` FOREIGN KEY (`operator_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT)\r\n### The error may exist in com/mold/digitalization/mapper/UserMapper.java (best guess)\r\n### The error may involve defaultParameterMap\r\n### The error occurred while setting parameters\r\n### SQL: DELETE FROM `user` WHERE id=?\r\n### Cause: java.sql.SQLIntegrityConstraintViolationException: Cannot delete or update a parent row: a foreign key constraint fails (`mold_digitalization`.`process_status_history`, CONSTRAINT `fk_history_operator` FOREIGN KEY (`operator_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT)\n; Cannot delete or update a parent row: a foreign key constraint fails (`mold_digitalization`.`process_status_history`, CONSTRAINT `fk_history_operator` FOREIGN KEY (`operator_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT); nested exception is java.sql.SQLIntegrityConstraintViolationException: Cannot delete or update a parent row: a foreign key constraint fails (`mold_digitalization`.`process_status_history`, CONSTRAINT `fk_history_operator` FOREIGN KEY (`operator_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT)\"}','SUCCESS','2025-12-23 14:12:47',1,'critical'),(517,NULL,NULL,NULL,'删除用户','删除用户: testuser',NULL,'SUCCESS','2025-12-23 14:12:47',0,'normal'),(518,'admin',1,'127.0.0.1','deleteUser','删除用户操作','<200 OK OK,[]>','SUCCESS','2025-12-23 14:12:47',1,'critical'),(519,'admin',1,'127.0.0.1','deleteUser','删除用户操作','{\"error\": \"DataIntegrityViolationException: \r\n### Error updating database.  Cause: java.sql.SQLIntegrityConstraintViolationException: Cannot delete or update a parent row: a foreign key constraint fails (`mold_digitalization`.`process_status_history`, CONSTRAINT `fk_history_operator` FOREIGN KEY (`operator_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT)\r\n### The error may exist in com/mold/digitalization/mapper/UserMapper.java (best guess)\r\n### The error may involve defaultParameterMap\r\n### The error occurred while setting parameters\r\n### SQL: DELETE FROM `user` WHERE id=?\r\n### Cause: java.sql.SQLIntegrityConstraintViolationException: Cannot delete or update a parent row: a foreign key constraint fails (`mold_digitalization`.`process_status_history`, CONSTRAINT `fk_history_operator` FOREIGN KEY (`operator_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT)\n; Cannot delete or update a parent row: a foreign key constraint fails (`mold_digitalization`.`process_status_history`, CONSTRAINT `fk_history_operator` FOREIGN KEY (`operator_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT); nested exception is java.sql.SQLIntegrityConstraintViolationException: Cannot delete or update a parent row: a foreign key constraint fails (`mold_digitalization`.`process_status_history`, CONSTRAINT `fk_history_operator` FOREIGN KEY (`operator_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT)\"}','SUCCESS','2025-12-23 14:14:06',1,'critical'),(520,'admin',1,'127.0.0.1','deleteUser','删除用户操作','{\"error\": \"DataIntegrityViolationException: \r\n### Error updating database.  Cause: java.sql.SQLIntegrityConstraintViolationException: Cannot delete or update a parent row: a foreign key constraint fails (`mold_digitalization`.`process_status_history`, CONSTRAINT `fk_history_operator` FOREIGN KEY (`operator_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT)\r\n### The error may exist in com/mold/digitalization/mapper/UserMapper.java (best guess)\r\n### The error may involve defaultParameterMap\r\n### The error occurred while setting parameters\r\n### SQL: DELETE FROM `user` WHERE id=?\r\n### Cause: java.sql.SQLIntegrityConstraintViolationException: Cannot delete or update a parent row: a foreign key constraint fails (`mold_digitalization`.`process_status_history`, CONSTRAINT `fk_history_operator` FOREIGN KEY (`operator_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT)\n; Cannot delete or update a parent row: a foreign key constraint fails (`mold_digitalization`.`process_status_history`, CONSTRAINT `fk_history_operator` FOREIGN KEY (`operator_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT); nested exception is java.sql.SQLIntegrityConstraintViolationException: Cannot delete or update a parent row: a foreign key constraint fails (`mold_digitalization`.`process_status_history`, CONSTRAINT `fk_history_operator` FOREIGN KEY (`operator_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT)\"}','SUCCESS','2025-12-23 14:14:07',1,'critical'),(521,'admin',1,'127.0.0.1','deleteUser','删除用户操作','{\"error\": \"DataIntegrityViolationException: \r\n### Error updating database.  Cause: java.sql.SQLIntegrityConstraintViolationException: Cannot delete or update a parent row: a foreign key constraint fails (`mold_digitalization`.`process_status_history`, CONSTRAINT `fk_history_operator` FOREIGN KEY (`operator_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT)\r\n### The error may exist in com/mold/digitalization/mapper/UserMapper.java (best guess)\r\n### The error may involve defaultParameterMap\r\n### The error occurred while setting parameters\r\n### SQL: DELETE FROM `user` WHERE id=?\r\n### Cause: java.sql.SQLIntegrityConstraintViolationException: Cannot delete or update a parent row: a foreign key constraint fails (`mold_digitalization`.`process_status_history`, CONSTRAINT `fk_history_operator` FOREIGN KEY (`operator_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT)\n; Cannot delete or update a parent row: a foreign key constraint fails (`mold_digitalization`.`process_status_history`, CONSTRAINT `fk_history_operator` FOREIGN KEY (`operator_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT); nested exception is java.sql.SQLIntegrityConstraintViolationException: Cannot delete or update a parent row: a foreign key constraint fails (`mold_digitalization`.`process_status_history`, CONSTRAINT `fk_history_operator` FOREIGN KEY (`operator_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT)\"}','SUCCESS','2025-12-23 14:14:07',1,'critical'),(522,'admin',1,'127.0.0.1','deleteUser','删除用户操作','{\"error\": \"DataIntegrityViolationException: \r\n### Error updating database.  Cause: java.sql.SQLIntegrityConstraintViolationException: Cannot delete or update a parent row: a foreign key constraint fails (`mold_digitalization`.`mold_process`, CONSTRAINT `fk_process_operator` FOREIGN KEY (`operator_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT)\r\n### The error may exist in com/mold/digitalization/mapper/UserMapper.java (best guess)\r\n### The error may involve defaultParameterMap\r\n### The error occurred while setting parameters\r\n### SQL: DELETE FROM `user` WHERE id=?\r\n### Cause: java.sql.SQLIntegrityConstraintViolationException: Cannot delete or update a parent row: a foreign key constraint fails (`mold_digitalization`.`mold_process`, CONSTRAINT `fk_process_operator` FOREIGN KEY (`operator_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT)\n; Cannot delete or update a parent row: a foreign key constraint fails (`mold_digitalization`.`mold_process`, CONSTRAINT `fk_process_operator` FOREIGN KEY (`operator_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT); nested exception is java.sql.SQLIntegrityConstraintViolationException: Cannot delete or update a parent row: a foreign key constraint fails (`mold_digitalization`.`mold_process`, CONSTRAINT `fk_process_operator` FOREIGN KEY (`operator_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT)\"}','SUCCESS','2025-12-23 14:14:07',1,'critical'),(523,'admin',1,'127.0.0.1','deleteUser','删除用户操作','{\"error\": \"DataIntegrityViolationException: \r\n### Error updating database.  Cause: java.sql.SQLIntegrityConstraintViolationException: Cannot delete or update a parent row: a foreign key constraint fails (`mold_digitalization`.`mold_process`, CONSTRAINT `fk_process_operator` FOREIGN KEY (`operator_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT)\r\n### The error may exist in com/mold/digitalization/mapper/UserMapper.java (best guess)\r\n### The error may involve defaultParameterMap\r\n### The error occurred while setting parameters\r\n### SQL: DELETE FROM `user` WHERE id=?\r\n### Cause: java.sql.SQLIntegrityConstraintViolationException: Cannot delete or update a parent row: a foreign key constraint fails (`mold_digitalization`.`mold_process`, CONSTRAINT `fk_process_operator` FOREIGN KEY (`operator_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT)\n; Cannot delete or update a parent row: a foreign key constraint fails (`mold_digitalization`.`mold_process`, CONSTRAINT `fk_process_operator` FOREIGN KEY (`operator_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT); nested exception is java.sql.SQLIntegrityConstraintViolationException: Cannot delete or update a parent row: a foreign key constraint fails (`mold_digitalization`.`mold_process`, CONSTRAINT `fk_process_operator` FOREIGN KEY (`operator_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT)\"}','SUCCESS','2025-12-23 14:14:07',1,'critical'),(524,NULL,NULL,NULL,'閲嶇疆密码','閲嶇疆用户密码: admin',NULL,'SUCCESS','2026-01-06 08:58:29',0,'normal'),(525,NULL,NULL,NULL,'閲嶇疆密码','閲嶇疆用户密码: admin',NULL,'SUCCESS','2026-01-06 09:20:26',0,'normal'),(526,NULL,NULL,NULL,'閲嶇疆密码','閲嶇疆用户密码: admin',NULL,'SUCCESS','2026-01-06 10:37:16',0,'normal'),(527,NULL,NULL,NULL,'閲嶇疆密码','閲嶇疆用户密码: admin',NULL,'SUCCESS','2026-01-06 12:23:38',0,'normal'),(528,NULL,NULL,NULL,'閲嶇疆密码','閲嶇疆用户密码: admin',NULL,'SUCCESS','2026-01-06 14:22:17',0,'normal'),(529,NULL,NULL,NULL,'閲嶇疆密码','閲嶇疆用户密码: admin',NULL,'SUCCESS','2026-01-06 14:31:22',0,'normal'),(530,NULL,NULL,NULL,'閲嶇疆密码','閲嶇疆用户密码: admin',NULL,'SUCCESS','2026-01-06 14:41:06',0,'normal'),(531,NULL,NULL,NULL,'閲嶇疆密码','閲嶇疆用户密码: admin',NULL,'SUCCESS','2026-01-06 16:01:49',0,'normal'),(532,NULL,NULL,NULL,'閲嶇疆密码','閲嶇疆用户密码: admin',NULL,'SUCCESS','2026-01-06 21:04:46',0,'normal'),(533,NULL,NULL,NULL,'閲嶇疆密码','閲嶇疆用户密码: admin',NULL,'SUCCESS','2026-01-07 09:20:59',0,'normal'),(534,NULL,NULL,NULL,'閲嶇疆密码','閲嶇疆用户密码: admin',NULL,'SUCCESS','2026-01-07 09:40:53',0,'normal');
/*!40000 ALTER TABLE `operation_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `permission`
--

DROP TABLE IF EXISTS `permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `permission` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '权限ID',
  `permission_name` varchar(100) NOT NULL COMMENT '权限名称',
  `permission_code` varchar(100) NOT NULL COMMENT '权限代码',
  `description` varchar(200) DEFAULT NULL COMMENT '权限描述',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_permission_code` (`permission_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='权限表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `permission`
--

LOCK TABLES `permission` WRITE;
/*!40000 ALTER TABLE `permission` DISABLE KEYS */;
/*!40000 ALTER TABLE `permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `process_exception`
--

DROP TABLE IF EXISTS `process_exception`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `process_exception` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `process_id` bigint NOT NULL COMMENT '流程ID',
  `exception_type` int DEFAULT NULL COMMENT '异常类型（枚举/字典）',
  `exception_code` varchar(64) DEFAULT NULL COMMENT '异常编码',
  `exception_description` text COMMENT '异常描述',
  `severity_level` int DEFAULT NULL COMMENT '严重级别：1-轻微，2-一般，3-严重，4-致命',
  `occurrence_time` datetime DEFAULT NULL COMMENT '异常发生时间',
  `handling_status` int DEFAULT NULL COMMENT '处理状态：0-未处理，1-处理中，2-已处理',
  `handler_id` bigint DEFAULT NULL COMMENT '处理人ID',
  `handling_start_time` datetime DEFAULT NULL COMMENT '处理开始时间',
  `handling_end_time` datetime DEFAULT NULL COMMENT '处理结束时间',
  `handling_result` varchar(64) DEFAULT NULL COMMENT '处理结果：PENDING/RESOLVED/ESCALATED 等',
  `handling_measures` varchar(500) DEFAULT NULL COMMENT '处理措施',
  `affects_production` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否影响生产：0-否，1-是',
  `estimated_recovery_time` datetime DEFAULT NULL COMMENT '预计恢复时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `exception_time` datetime DEFAULT NULL COMMENT '异常时间（兼容历史字段）',
  `resolved` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否已解决：0-否，1-是',
  `exception_message` varchar(500) DEFAULT NULL COMMENT '异常消息',
  `operator_id` bigint DEFAULT NULL COMMENT '操作人ID',
  PRIMARY KEY (`id`),
  KEY `idx_exception_process_id` (`process_id`),
  KEY `idx_exception_type` (`exception_type`),
  KEY `idx_exception_severity` (`severity_level`),
  KEY `idx_exception_time` (`exception_time`),
  KEY `idx_exception_status` (`handling_status`),
  KEY `fk_exception_handler` (`handler_id`),
  KEY `fk_exception_operator` (`operator_id`),
  KEY `idx_exception_process` (`process_id`),
  CONSTRAINT `fk_exception_handler` FOREIGN KEY (`handler_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT,
  CONSTRAINT `fk_exception_operator` FOREIGN KEY (`operator_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT,
  CONSTRAINT `fk_exception_process` FOREIGN KEY (`process_id`) REFERENCES `mold_process` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='加工异常记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `process_exception`
--

LOCK TABLES `process_exception` WRITE;
/*!40000 ALTER TABLE `process_exception` DISABLE KEYS */;
/*!40000 ALTER TABLE `process_exception` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `process_status_history`
--

DROP TABLE IF EXISTS `process_status_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `process_status_history` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `process_id` bigint NOT NULL COMMENT '流程ID',
  `status` int DEFAULT NULL COMMENT '当前状态代码（ProcessStatusEnum.code）',
  `status_description` varchar(100) DEFAULT NULL COMMENT '状态描述（冗余便于审计展示）',
  `progress` int DEFAULT NULL COMMENT '进度(0-100)',
  `operation_time` datetime DEFAULT NULL COMMENT '操作时间',
  `operation_remark` varchar(255) DEFAULT NULL COMMENT '操作备注',
  `from_status` varchar(50) NOT NULL COMMENT '原状态',
  `to_status` varchar(50) NOT NULL COMMENT '目标状态',
  `event` varchar(50) DEFAULT NULL COMMENT '触发事件（如 START/PROCESS/PAUSE/RESUME/COMPLETE/CANCEL/EXCEPTION）',
  `description` varchar(255) DEFAULT NULL COMMENT '状态转换描述',
  `transition_time` datetime DEFAULT NULL COMMENT '状态转换时间',
  `change_reason` varchar(200) DEFAULT NULL COMMENT '变更原因',
  `change_remark` varchar(500) DEFAULT NULL COMMENT '变更备注',
  `operator_id` bigint NOT NULL COMMENT '操作员ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_status_history_process` (`process_id`),
  KEY `idx_status_history_time` (`create_time`),
  KEY `idx_status_history_status` (`from_status`,`to_status`),
  KEY `fk_history_operator` (`operator_id`),
  KEY `idx_psh_process_id` (`process_id`),
  KEY `idx_psh_transition_time` (`transition_time`),
  KEY `idx_psh_status` (`status`),
  KEY `idx_psh_from_to` (`from_status`,`to_status`),
  CONSTRAINT `fk_history_operator` FOREIGN KEY (`operator_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT,
  CONSTRAINT `fk_history_process` FOREIGN KEY (`process_id`) REFERENCES `mold_process` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='流程状态历史记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `process_status_history`
--

LOCK TABLES `process_status_history` WRITE;
/*!40000 ALTER TABLE `process_status_history` DISABLE KEYS */;
INSERT INTO `process_status_history` VALUES (17,11,1,'Planned',NULL,'2025-11-15 11:08:10',NULL,'0','1','PLAN','Plan: Created -> Planned','2025-11-15 11:08:10',NULL,NULL,7,'2025-11-15 11:08:10'),(18,11,NULL,NULL,NULL,NULL,NULL,'0','1','PLAN','Plan','2025-11-15 11:08:10',NULL,NULL,7,'2025-11-15 11:08:10'),(19,11,2,'In Progress',NULL,'2025-11-15 11:25:05',NULL,'1','2','START','Start: Planned -> In Progress','2025-11-15 11:25:05',NULL,NULL,6,'2025-11-15 11:25:05'),(20,11,NULL,NULL,NULL,NULL,NULL,'1','2','START','Start','2025-11-15 11:25:05',NULL,NULL,6,'2025-11-15 11:25:05'),(21,11,3,'Paused',NULL,'2025-11-15 11:27:01',NULL,'2','3','PAUSE','Pause: In Progress -> Paused','2025-11-15 11:27:01',NULL,NULL,6,'2025-11-15 11:27:01'),(22,11,NULL,NULL,NULL,NULL,NULL,'2','3','PAUSE','Pause','2025-11-15 11:27:01',NULL,NULL,6,'2025-11-15 11:27:01'),(23,11,2,'In Progress',NULL,'2025-11-15 11:27:01',NULL,'3','2','RESUME','Resume: Paused -> In Progress','2025-11-15 11:27:01',NULL,NULL,6,'2025-11-15 11:27:01'),(24,11,NULL,NULL,NULL,NULL,NULL,'3','2','RESUME','Resume','2025-11-15 11:27:01',NULL,NULL,6,'2025-11-15 11:27:01'),(25,11,4,'Completed',NULL,'2025-11-15 11:27:01','demo','2','4','COMPLETE','Complete: In Progress -> Completed','2025-11-15 11:27:01',NULL,NULL,6,'2025-11-15 11:27:01'),(26,12,1,'Planned',NULL,'2025-11-15 13:30:16','auto','0','1','PLAN','Plan: Created -> Planned','2025-11-15 13:30:16',NULL,NULL,6,'2025-11-15 13:30:15'),(27,12,NULL,NULL,NULL,NULL,NULL,'0','1','PLAN','Plan','2025-11-15 13:30:16',NULL,NULL,6,'2025-11-15 13:30:15'),(28,12,2,'In Progress',NULL,'2025-11-15 13:30:33','auto','1','2','START','Start: Planned -> In Progress','2025-11-15 13:30:33',NULL,NULL,6,'2025-11-15 13:30:33'),(29,12,NULL,NULL,NULL,NULL,NULL,'1','2','START','Start','2025-11-15 13:30:34',NULL,NULL,6,'2025-11-15 13:30:33'),(30,12,3,'Paused',NULL,'2025-11-15 13:30:45','auto','2','3','PAUSE','Pause: In Progress -> Paused','2025-11-15 13:30:45',NULL,NULL,6,'2025-11-15 13:30:44'),(31,12,NULL,NULL,NULL,NULL,NULL,'2','3','PAUSE','Pause','2025-11-15 13:30:45',NULL,NULL,6,'2025-11-15 13:30:44'),(32,12,2,'In Progress',NULL,'2025-11-15 13:30:45','auto','3','2','RESUME','Resume: Paused -> In Progress','2025-11-15 13:30:45',NULL,NULL,6,'2025-11-15 13:30:44'),(33,12,NULL,NULL,NULL,NULL,NULL,'3','2','RESUME','Resume','2025-11-15 13:30:45',NULL,NULL,6,'2025-11-15 13:30:44'),(34,12,4,'Completed',NULL,'2025-11-15 13:31:09','frontend-dev','2','4','COMPLETE','Complete: In Progress -> Completed','2025-11-15 13:31:09',NULL,NULL,6,'2025-11-15 13:31:09'),(35,23,2,'In Progress',NULL,'2025-11-15 21:17:38','start','1','2','START','Start: Planned -> In Progress','2025-11-15 21:17:38',NULL,NULL,8,'2025-11-15 21:17:38'),(36,23,NULL,NULL,NULL,NULL,NULL,'1','2','START','Start','2025-11-15 21:17:38',NULL,NULL,8,'2025-11-15 21:17:38'),(37,23,4,'Completed',NULL,'2025-11-15 21:19:06','force','2','4','COMPLETE','Complete: In Progress -> Completed','2025-11-15 21:19:06',NULL,NULL,8,'2025-11-15 21:19:06');
/*!40000 ALTER TABLE `process_status_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '成品ID',
  `product_category` varchar(255) DEFAULT NULL COMMENT '成品类别',
  `product_spec` varchar(255) DEFAULT NULL COMMENT '成品规格',
  `density_requirement_min` double DEFAULT NULL COMMENT '容重要求最小值',
  `density_requirement_max` double DEFAULT NULL COMMENT '容重要求最大值',
  `wall_thickness_requirement` double DEFAULT NULL COMMENT '壁厚要求',
  `slot_width_requirement_min` double DEFAULT NULL COMMENT '槽宽要求最小值',
  `slot_width_requirement_max` double DEFAULT NULL COMMENT '槽宽要求最大值',
  `other_requirements` varchar(500) DEFAULT NULL COMMENT '其他特殊要求',
  `customer` varchar(255) DEFAULT NULL COMMENT '客户名称',
  `create_time` varchar(50) DEFAULT NULL COMMENT '创建时间',
  `update_time` varchar(50) DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_product_category_spec` (`product_category`,`product_spec`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='成品表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `production_record`
--

DROP TABLE IF EXISTS `production_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `production_record` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `task_id` bigint NOT NULL,
  `mold_id` bigint NOT NULL,
  `equipment_id` bigint NOT NULL,
  `operator_id` bigint DEFAULT NULL,
  `production_date` datetime NOT NULL,
  `production_quantity` int NOT NULL,
  `qualified_quantity` int DEFAULT '0',
  `defective_quantity` int DEFAULT '0',
  `mold_usage_count` int DEFAULT '0',
  `equipment_parameters` text COLLATE utf8mb4_unicode_ci,
  `remark` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_record_task` (`task_id`),
  KEY `fk_record_mold` (`mold_id`),
  KEY `fk_record_equipment` (`equipment_id`),
  KEY `fk_record_operator` (`operator_id`),
  CONSTRAINT `fk_record_equipment` FOREIGN KEY (`equipment_id`) REFERENCES `equipment` (`id`),
  CONSTRAINT `fk_record_mold` FOREIGN KEY (`mold_id`) REFERENCES `mold` (`id`),
  CONSTRAINT `fk_record_operator` FOREIGN KEY (`operator_id`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_record_task` FOREIGN KEY (`task_id`) REFERENCES `production_task` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=80 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `production_record`
--

LOCK TABLES `production_record` WRITE;
/*!40000 ALTER TABLE `production_record` DISABLE KEYS */;
/*!40000 ALTER TABLE `production_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `production_task`
--

DROP TABLE IF EXISTS `production_task`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `production_task` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `task_code` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `task_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `mold_id` bigint NOT NULL,
  `equipment_id` bigint NOT NULL,
  `operator_id` bigint DEFAULT NULL,
  `planned_quantity` int NOT NULL,
  `actual_quantity` int DEFAULT '0',
  `qualified_quantity` int DEFAULT '0',
  `defective_quantity` int DEFAULT '0',
  `status` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT '待开始',
  `start_time` datetime DEFAULT NULL,
  `end_time` datetime DEFAULT NULL,
  `description` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `task_code` (`task_code`),
  KEY `fk_task_mold` (`mold_id`),
  KEY `fk_task_equipment` (`equipment_id`),
  KEY `fk_task_operator` (`operator_id`),
  CONSTRAINT `fk_task_equipment` FOREIGN KEY (`equipment_id`) REFERENCES `equipment` (`id`),
  CONSTRAINT `fk_task_mold` FOREIGN KEY (`mold_id`) REFERENCES `mold` (`id`),
  CONSTRAINT `fk_task_operator` FOREIGN KEY (`operator_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=55 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `production_task`
--

LOCK TABLES `production_task` WRITE;
/*!40000 ALTER TABLE `production_task` DISABLE KEYS */;
/*!40000 ALTER TABLE `production_task` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `role` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `role_code` varchar(50) NOT NULL,
  `role_name` varchar(50) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `status` tinyint(1) DEFAULT '1' COMMENT '状态:0-禁用,1-启用',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `role_code` (`role_code`)
) ENGINE=InnoDB AUTO_INCREMENT=85 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES (1,'ADMIN','管理员','Administrator',1,'2025-11-11 09:50:26','2025-12-20 17:14:39'),(2,'USER','普通用户','Standard user',1,'2025-11-11 09:50:26','2025-11-15 17:46:00'),(4,'MJJG_Worker','模具加工组员工','模具加工技师',1,'2025-11-15 16:35:03','2025-12-23 13:42:11'),(5,'MJTS_Worker','模具调试组员工','调模师',1,'2025-11-15 17:41:39','2025-12-23 13:41:56'),(6,'MJTS_Leader','模具调试组长','模具调试组长',1,'2025-11-15 17:41:39','2025-12-23 13:44:18'),(7,'CXCJ_Worker','成型车间员工','线长',1,'2025-11-15 17:41:39','2025-12-23 13:45:13'),(8,'CXCJ_ZR','成型车间主任','成型车间主任',1,'2025-11-15 17:41:39','2025-12-23 13:45:52'),(9,'CXCJ_FZR','成型车间副主任','成型车间副主任',1,'2025-11-15 17:41:39','2025-12-23 13:46:20'),(16,'MGY','模管员','管理模具及模具配件',1,'2025-12-20 17:13:07','2025-12-20 17:13:07'),(17,'MJZY','模具专用','协助模具主任安排分配模具车间任务',1,'2025-12-20 17:14:15','2025-12-20 17:14:15'),(18,'MJ_Manager','模具主管','',1,'2025-12-23 13:49:20','2025-12-23 13:49:20');
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `system_parameter`
--

DROP TABLE IF EXISTS `system_parameter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `system_parameter` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `param_key` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `param_value` text COLLATE utf8mb4_unicode_ci,
  `param_type` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `description` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `is_visible` tinyint DEFAULT '1',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `param_key` (`param_key`)
) ENGINE=InnoDB AUTO_INCREMENT=93 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `system_parameter`
--

LOCK TABLES `system_parameter` WRITE;
/*!40000 ALTER TABLE `system_parameter` DISABLE KEYS */;
INSERT INTO `system_parameter` VALUES (1,'SYSTEM_NAME','模具数字化管理系统','STRING','系统名称',1,'2026-01-06 08:57:39','2026-01-06 08:57:39'),(2,'SYSTEM_VERSION','V1.0.0','STRING','系统版本号',1,'2026-01-06 08:57:39','2026-01-06 08:57:39'),(3,'MAX_LOGIN_ATTEMPTS','5','NUMBER','最大登录尝试次数',0,'2026-01-06 08:57:39','2026-01-06 08:57:39'),(4,'PASSWORD_EXPIRE_DAYS','90','NUMBER','密码过期天数',0,'2026-01-06 08:57:39','2026-01-06 08:57:39'),(5,'MOLD_WARNING_USAGE_RATE','80','NUMBER','模具使用次数预警比例(%)',1,'2026-01-06 08:57:39','2026-01-06 08:57:39'),(6,'DEFAULT_PAGE_SIZE','10','NUMBER','默认分页大小',1,'2026-01-06 08:57:39','2026-01-06 08:57:39'),(7,'MAX_PAGE_SIZE','100','NUMBER','最大分页大小',1,'2026-01-06 08:57:39','2026-01-06 08:57:39');
/*!40000 ALTER TABLE `system_parameter` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tuning_record`
--

DROP TABLE IF EXISTS `tuning_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tuning_record` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `mold_id` bigint DEFAULT NULL,
  `mold_code` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `image_url` varchar(300) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `stage` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `record_time` datetime DEFAULT NULL,
  `equipment` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `pressure` double DEFAULT NULL,
  `flow_rate` double DEFAULT NULL,
  `mud_material` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `mud_hardness` double DEFAULT NULL,
  `remark` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_tr_mold_code` (`mold_code`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tuning_record`
--

LOCK TABLES `tuning_record` WRITE;
/*!40000 ALTER TABLE `tuning_record` DISABLE KEYS */;
INSERT INTO `tuning_record` VALUES (1,NULL,'B0428s',NULL,'??','2025-11-17 17:48:07','E-01',1.2,3.4,'Cr12',60,'??','2025-11-17 17:48:07','2025-11-17 17:48:07'),(2,NULL,NULL,NULL,'??',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2025-11-17 17:48:55','2025-11-17 17:48:55'),(3,NULL,NULL,NULL,'??',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2025-11-17 17:49:19','2025-11-17 17:49:19');
/*!40000 ALTER TABLE `tuning_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(100) NOT NULL,
  `real_name` varchar(50) DEFAULT NULL COMMENT '真实姓名',
  `department_name` varchar(100) DEFAULT NULL COMMENT '部门名称',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `status` int NOT NULL DEFAULT '1',
  `roles` varchar(128) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'admin','$2a$10$lXoyBVplfPQpmgmEbqk24eV7jFaqumzTMhlwNAepq7E3oZLA0wPl2',NULL,'???',NULL,NULL,1,'ADMIN,USER',NULL,NULL),(2,'user','$2a$10$4fte54xc6p1ZwCqoWSc.tufZAvFPoQLgOfTyLohKhS0o5kyiGy/cu',NULL,NULL,NULL,NULL,1,'USER','2025-11-07 11:18:28','2025-11-07 11:18:28'),(4,'devuser-demo','$2a$10$uU5MEALCNvAOWYvWm0jUOeZanTQdlHU3hYrhNvV0qYom4KYIVDuzW',NULL,NULL,NULL,NULL,1,NULL,NULL,NULL),(5,' devuser-1763174233014','$2a$10$FnGl6dyZVV2gSG4J8BBWwe42DVndDmXPHw.usxuZC19wMoNaoktwC',NULL,NULL,NULL,NULL,1,NULL,NULL,NULL),(6,'devuser-1763175389120','$2a$10$FYwRyIcckcIVg8nUQFKs4uUFzzdL..H7TJJV3dHW9J6nW9h.esYVG',NULL,NULL,NULL,NULL,1,NULL,NULL,NULL),(7,'devuser-1763175886673','$2a$10$7pHMNnrMjPczFOUwKAS7iezRWJBi0Lv.VQX56YB.1nipvaOHNQ2Em',NULL,NULL,NULL,NULL,1,NULL,NULL,NULL),(8,'op-测试','$2a$10$FhImETQBQC.eVjuTpKCDVerUmb84P6R6ZeJvNxWOTjYlUwG2PNUw.',NULL,NULL,NULL,NULL,1,NULL,NULL,NULL),(17,'CJT','$2a$10$hlbQ/FxmW56jRHT9j1LBauepNd3or7dldx1Hp7W0D1UtUNFkhhvv6','曹江停','模具加工组','','',1,NULL,NULL,NULL),(18,'LJ1','$2a$10$m.0DHifTP5BzRdrgp7VGiuQIbxqRE.NfZtTjQjMIwlYElFsZzurcq','罗京','模具加工组',NULL,'',1,NULL,NULL,NULL),(19,'CJY','$2a$10$qxWrzaZwLgWQoluVeugC5OTcyb2Rvh8wXBn2NrLdFj72JwipjSKWq','程爵永','模具加工组','','',1,NULL,NULL,NULL),(20,'PLX','$2a$10$v9m/2N4E6guKc4CrK/k.dezLV/rEghy737CUArPPPyMx0hrZ.ic1y','潘隆兴','模具加工组','','',1,NULL,NULL,NULL),(24,'ybh','$2a$10$.FVxDM/CDJvUDKPc0iL9I.GiuZ0iuHy21eCt.FVgj71IzkjGCtNh6','易本红','模具调试组','','',1,NULL,NULL,NULL),(25,'cxf','$2a$10$3y.Ep0Nt38rlZa6gy5QNM.qHdxM7M3lhBqUWk5tI88AGpgFBKtsVW','程晓峰','模具调试组','','',1,NULL,NULL,NULL),(26,'cjz','$2a$10$pfdgu6vXdkSDeAtd0l/Qpu27k5mIoB6pTfZxqsi4bKuQ5wLAk5tV2','程爵志','模具调试组','','',1,NULL,NULL,NULL);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_department`
--

DROP TABLE IF EXISTS `user_department`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_department` (
  `user_id` bigint NOT NULL,
  `department_id` bigint NOT NULL,
  UNIQUE KEY `uk_user` (`user_id`),
  KEY `idx_department` (`department_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_department`
--

LOCK TABLES `user_department` WRITE;
/*!40000 ALTER TABLE `user_department` DISABLE KEYS */;
INSERT INTO `user_department` VALUES (1,5),(17,9),(18,9),(19,9),(20,9),(24,10),(25,10),(26,10);
/*!40000 ALTER TABLE `user_department` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_role`
--

DROP TABLE IF EXISTS `user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_role` (
  `user_id` bigint NOT NULL,
  `role_id` bigint NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `fk_user_role_role` (`role_id`),
  CONSTRAINT `fk_user_role_role` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_user_role_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_role`
--

LOCK TABLES `user_role` WRITE;
/*!40000 ALTER TABLE `user_role` DISABLE KEYS */;
INSERT INTO `user_role` VALUES (1,1),(1,2),(2,2),(17,4),(18,4),(19,4),(20,4),(25,5),(26,5),(24,6);
/*!40000 ALTER TABLE `user_role` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-01-08 12:02:21
