CREATE DATABASE  IF NOT EXISTS `domotics` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `domotics`;
-- MySQL dump 10.13  Distrib 5.7.9, for Win64 (x86_64)
--
-- Host: localhost    Database: domotics
-- ------------------------------------------------------
-- Server version	5.7.13-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `heatindex`
--

DROP TABLE IF EXISTS `heatindex`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `heatindex` (
  `idheatindex` bigint(30) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Incremental unique key for this table.',
  `stationId` int(11) DEFAULT NULL COMMENT 'Which station produced this reading/measument',
  `value` double DEFAULT NULL COMMENT 'Heat Index value',
  `timestamp` timestamp NULL DEFAULT NULL COMMENT 'timestamp when reading received.',
  PRIMARY KEY (`idheatindex`),
  UNIQUE KEY `idtemperature_UNIQUE` (`idheatindex`)
) ENGINE=InnoDB AUTO_INCREMENT=2699 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `humidity`
--

DROP TABLE IF EXISTS `humidity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `humidity` (
  `idhumidity` bigint(30) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Incremental unique key for this table.',
  `stationId` int(11) DEFAULT NULL COMMENT 'Which station produced this reading/measument',
  `value` double DEFAULT NULL COMMENT 'humidity value',
  `timestamp` timestamp NULL DEFAULT NULL COMMENT 'timestamp when reading received.',
  PRIMARY KEY (`idhumidity`),
  UNIQUE KEY `idtemperature_UNIQUE` (`idhumidity`)
) ENGINE=InnoDB AUTO_INCREMENT=2699 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sensortype`
--

DROP TABLE IF EXISTS `sensortype`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sensortype` (
  `id` int(5) NOT NULL,
  `name` varchar(45) NOT NULL,
  `description` varchar(2000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `stations`
--

DROP TABLE IF EXISTS `stations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `stations` (
  `id` int(11) NOT NULL,
  `name` varchar(45) NOT NULL,
  `description` varchar(2000) DEFAULT NULL,
  `type` int(5) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `temperature`
--

DROP TABLE IF EXISTS `temperature`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `temperature` (
  `idtemperature` bigint(30) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Incremental unique key for this table.',
  `stationId` int(11) DEFAULT NULL COMMENT 'Which station produced this reading/measument',
  `value` double DEFAULT NULL COMMENT 'temperature value',
  `timestamp` timestamp NULL DEFAULT NULL COMMENT 'timestamp when reading received.',
  PRIMARY KEY (`idtemperature`),
  UNIQUE KEY `idtemperature_UNIQUE` (`idtemperature`)
) ENGINE=InnoDB AUTO_INCREMENT=2699 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-08-07 23:21:22
