-- MySQL dump 10.13  Distrib 8.0.33, for Win64 (x86_64)
--
-- Host: localhost    Database: fintech_app
-- ------------------------------------------------------
-- Server version	5.7.43-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `admin`
--

DROP TABLE IF EXISTS `admin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admin` (
  `id` bigint(20) NOT NULL,
  `account_status` varchar(255) DEFAULT NULL,
  `created_date` datetime(6) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `phone_number` varchar(255) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  `updated_date` datetime(6) DEFAULT NULL,
  `full_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin`
--

LOCK TABLES `admin` WRITE;
/*!40000 ALTER TABLE `admin` DISABLE KEYS */;
INSERT INTO `admin` VALUES (1,'COMPLETED','2025-01-04 06:20:23.979000','zahra@gmail.com','$2a$10$hU9lp9KTVjJwATdd4Db01edowpu8gSpNqxfphydbC1ry8K..KkOIe','07033215343','ADMIN',NULL,'Zahra Atobatele'),(2,'COMPLETED','2025-01-04 06:26:53.258000','zahragmail.com','$2a$10$nrmZq7pRDTw0MI1qRw4GQezIT9KvFjM0txLtix9nKESZjFqjZul7m','07033215343','ADMIN',NULL,'Zahra Atobatele');
/*!40000 ALTER TABLE `admin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `admin_seq`
--

DROP TABLE IF EXISTS `admin_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admin_seq` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin_seq`
--

LOCK TABLES `admin_seq` WRITE;
/*!40000 ALTER TABLE `admin_seq` DISABLE KEYS */;
INSERT INTO `admin_seq` VALUES (3);
/*!40000 ALTER TABLE `admin_seq` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `audit_log`
--

DROP TABLE IF EXISTS `audit_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `audit_log` (
  `action_type` varchar(255) NOT NULL,
  `admin_id` varchar(255) DEFAULT NULL,
  `created_date` datetime(6) DEFAULT NULL,
  `details` varchar(255) DEFAULT NULL,
  `timestamp` datetime(6) DEFAULT NULL,
  `updated_date` datetime(6) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`action_type`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `audit_log`
--

LOCK TABLES `audit_log` WRITE;
/*!40000 ALTER TABLE `audit_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `audit_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `audit_log_seq`
--

DROP TABLE IF EXISTS `audit_log_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `audit_log_seq` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `audit_log_seq`
--

LOCK TABLES `audit_log_seq` WRITE;
/*!40000 ALTER TABLE `audit_log_seq` DISABLE KEYS */;
INSERT INTO `audit_log_seq` VALUES (1);
/*!40000 ALTER TABLE `audit_log_seq` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `loan`
--

DROP TABLE IF EXISTS `loan`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `loan` (
  `id` bigint(20) NOT NULL,
  `created_date` datetime(6) DEFAULT NULL,
  `disburstment_date` datetime(6) DEFAULT NULL,
  `interest_rate` decimal(19,2) DEFAULT NULL,
  `laon_amount` decimal(19,2) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `tenure` int(11) DEFAULT NULL,
  `total_amount` decimal(19,2) DEFAULT NULL,
  `updated_date` datetime(6) DEFAULT NULL,
  `admin_id` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `verified_by` bigint(20) DEFAULT NULL,
  `narration` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK681hgebvdphygxw4voglktpej` (`admin_id`),
  KEY `FK2diwqxg8wh0nwkggooo2d0mcm` (`user_id`),
  KEY `FKjm1bo17qp6e7bafa6esdl1m3l` (`verified_by`),
  CONSTRAINT `FK2diwqxg8wh0nwkggooo2d0mcm` FOREIGN KEY (`user_id`) REFERENCES `users_account` (`id`),
  CONSTRAINT `FK681hgebvdphygxw4voglktpej` FOREIGN KEY (`admin_id`) REFERENCES `admin` (`id`),
  CONSTRAINT `FKjm1bo17qp6e7bafa6esdl1m3l` FOREIGN KEY (`verified_by`) REFERENCES `admin` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `loan`
--

LOCK TABLES `loan` WRITE;
/*!40000 ALTER TABLE `loan` DISABLE KEYS */;
INSERT INTO `loan` VALUES (1,NULL,'2025-01-04 10:01:32.536270',7.00,20000.00,'REPAID',6,21400.00,NULL,NULL,1,1,NULL);
/*!40000 ALTER TABLE `loan` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `loan_seq`
--

DROP TABLE IF EXISTS `loan_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `loan_seq` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `loan_seq`
--

LOCK TABLES `loan_seq` WRITE;
/*!40000 ALTER TABLE `loan_seq` DISABLE KEYS */;
INSERT INTO `loan_seq` VALUES (2);
/*!40000 ALTER TABLE `loan_seq` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transactions`
--

DROP TABLE IF EXISTS `transactions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `transactions` (
  `id` bigint(20) NOT NULL,
  `amount` decimal(19,2) DEFAULT NULL,
  `app_status` varchar(255) DEFAULT NULL,
  `transaction_date` datetime(6) DEFAULT NULL,
  `transaction_type` varchar(255) DEFAULT NULL,
  `loan_id` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `verified_by` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK5xi4iho1g8h7mqkafhua6dsfw` (`loan_id`),
  KEY `FK5e42ccllwprqobkebsd451rco` (`user_id`),
  KEY `FK4rwre6vaokkc7tvc8okuw4no0` (`verified_by`),
  CONSTRAINT `FK4rwre6vaokkc7tvc8okuw4no0` FOREIGN KEY (`verified_by`) REFERENCES `admin` (`id`),
  CONSTRAINT `FK5e42ccllwprqobkebsd451rco` FOREIGN KEY (`user_id`) REFERENCES `users_account` (`id`),
  CONSTRAINT `FK5xi4iho1g8h7mqkafhua6dsfw` FOREIGN KEY (`loan_id`) REFERENCES `loan` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transactions`
--

LOCK TABLES `transactions` WRITE;
/*!40000 ALTER TABLE `transactions` DISABLE KEYS */;
INSERT INTO `transactions` VALUES (1,20000.00,'PENDING','2025-01-04 10:10:41.814375','DEPOSIT',NULL,1,NULL),(2,5000.00,'PENDING','2025-01-04 10:12:12.350461','WITHDRAWAL',NULL,1,NULL),(3,100000.00,'COMPLETED','2025-01-04 10:25:05.989899','DISBURSEMENT',1,NULL,1),(4,5000.00,'COMPLETED','2025-01-04 10:36:07.581945','REPAYMENT',1,NULL,1);
/*!40000 ALTER TABLE `transactions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transactions_seq`
--

DROP TABLE IF EXISTS `transactions_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `transactions_seq` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transactions_seq`
--

LOCK TABLES `transactions_seq` WRITE;
/*!40000 ALTER TABLE `transactions_seq` DISABLE KEYS */;
INSERT INTO `transactions_seq` VALUES (5);
/*!40000 ALTER TABLE `transactions_seq` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users_account`
--

DROP TABLE IF EXISTS `users_account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users_account` (
  `id` bigint(20) NOT NULL,
  `account_balance` decimal(19,2) DEFAULT NULL,
  `account_status` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `bvn` varchar(255) DEFAULT NULL,
  `created_date` datetime(6) DEFAULT NULL,
  `date_of_birth` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `full_name` varchar(255) DEFAULT NULL,
  `gender` varchar(255) DEFAULT NULL,
  `is_verified` bit(1) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `phone_number` varchar(255) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  `updated_date` datetime(6) DEFAULT NULL,
  `verified_by` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKfwpwb38k4qwsu8po2ate9xb5n` (`verified_by`),
  CONSTRAINT `FKfwpwb38k4qwsu8po2ate9xb5n` FOREIGN KEY (`verified_by`) REFERENCES `admin` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users_account`
--

LOCK TABLES `users_account` WRITE;
/*!40000 ALTER TABLE `users_account` DISABLE KEYS */;
INSERT INTO `users_account` VALUES (1,15000.00,'INACTIVE','3, Saka Street Obantoko','12345678911','2025-01-04 06:27:31.157901','01-08-1994','olasunkanmizik@gmail.com','Zikrullah Atobatele','Male',_binary '','$2a$10$RbGcoeIHCWIvbJwzCocWJuZpPCmMX/5tkuj2LipkJ/upKaavgPlFi',NULL,'USER',NULL,1),(2,0.00,'PENDING','3, Saka Street Obantoko','12345678911','2025-01-04 11:04:33.805143','01-08-1994','olasunkanmizikgmail.com','Lawal Dada','Male',_binary '\0','$2a$10$lWXpUYzK.aqsSTqRReXizeWOjZQ9h7swpdt/EvEIEg6NJ5Djivf6q',NULL,'USER',NULL,NULL),(3,0.00,'PENDING','3, Saka Street Obantoko','12345678911','2025-01-04 11:05:40.340539','01-08-1994','olasunkanmizik1gmail.com','Lawals Dada','Male',_binary '\0','$2a$10$jTyLvdp4kt1KxRwD0ONRQutJF8.MPHoOf0wN73O.9nZd2KGn14Yc6',NULL,'USER',NULL,NULL),(4,0.00,'PENDING','3, Saka Street Obantoko','12345678911','2025-01-04 11:06:27.197001','01-08-1994','olasunkanmizik2gmail.com','Lawals Dada','Male',_binary '\0','$2a$10$Q4bq0gGhLJuFuwH6kZsA6uLIfqdKZ/wyFTrXLAqedsh6rFYdtv3k6',NULL,'USER',NULL,NULL),(5,0.00,'PENDING','3, Saka Street Obantoko','12345678911','2025-01-04 11:11:06.321413','01-08-1994','olasunkanmizik90gmail.com','Lawals Dada','Male',_binary '\0','$2a$10$4birhXjv4CWZKLaxuhLYbONHJ.NKQs4PXJ9zDQ15YnuYa/MhCFK8G',NULL,'USER',NULL,NULL),(6,0.00,'PENDING','3, Saka Street Obantoko','12345678911','2025-01-04 11:20:28.016435','01-08-1994','olasunkanmizik9000@gmail.com','Lawals Dada','Male',_binary '\0','$2a$10$0CUiYsneavxX8HOCewVYOehrS8LWJjX9TnmOBI6vOaY8YQ7rwj642',NULL,'USER',NULL,NULL),(7,0.00,'PENDING','3, Saka Street Obantoko','12345678921','2025-01-04 11:32:10.171058','01-08-1994','david@gmail.com','Adeyemi David','Male',_binary '\0','$2a$10$42meBiY6bxSjEF5jpSvsaeV7Y3ezFfoGVTMCaf6RkoOCeXI6s2iWS',NULL,'USER',NULL,NULL);
/*!40000 ALTER TABLE `users_account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users_account_seq`
--

DROP TABLE IF EXISTS `users_account_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users_account_seq` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users_account_seq`
--

LOCK TABLES `users_account_seq` WRITE;
/*!40000 ALTER TABLE `users_account_seq` DISABLE KEYS */;
INSERT INTO `users_account_seq` VALUES (8);
/*!40000 ALTER TABLE `users_account_seq` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-01-04 11:53:48
