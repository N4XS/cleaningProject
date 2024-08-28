CREATE DATABASE  IF NOT EXISTS `cleaningproject` /*!40100 DEFAULT CHARACTER SET utf8mb3 */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `cleaningproject`;
-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: cleaningproject
-- ------------------------------------------------------
-- Server version	8.0.37

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
-- Table structure for table `absence`
--

DROP TABLE IF EXISTS `absence`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `absence` (
  `absenceID` char(8) NOT NULL,
  `justification` text,
  `startDate` date NOT NULL,
  `endDate` date DEFAULT NULL,
  `isUnderCertificate` tinyint(1) DEFAULT NULL,
  `absentID` char(8) NOT NULL,
  `replacementID` char(8) DEFAULT NULL,
  PRIMARY KEY (`absenceID`),
  CONSTRAINT `chk_dates` CHECK (((`endDate` is null) or (`endDate` >= `startDate`)))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `absence`
--

LOCK TABLES `absence` WRITE;
/*!40000 ALTER TABLE `absence` DISABLE KEYS */;
INSERT INTO `absence` VALUES ('AB000002','Congé annuel','2024-08-12','2024-08-16',0,'23456789',NULL),('AB000003','Accident de travail','2024-08-06',NULL,1,'34567890',NULL),('AB000004','Congé sans solde','2024-08-21','2024-08-26',0,'45678901',NULL),('AB000005','Maladie','2024-08-24','2024-08-25',1,'56789012',NULL);
/*!40000 ALTER TABLE `absence` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `city`
--

DROP TABLE IF EXISTS `city`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `city` (
  `postalCode` int NOT NULL,
  `locality` varchar(255) NOT NULL,
  PRIMARY KEY (`postalCode`,`locality`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `city`
--

LOCK TABLES `city` WRITE;
/*!40000 ALTER TABLE `city` DISABLE KEYS */;
INSERT INTO `city` VALUES (1000,'Bruxelles'),(2000,'Anvers'),(3000,'Gand'),(3000,'Louvain'),(4000,'Liège'),(5000,'Namur'),(6000,'Charleroi'),(7090,'hennuyere');
/*!40000 ALTER TABLE `city` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cleaningservice`
--

DROP TABLE IF EXISTS `cleaningservice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cleaningservice` (
  `cleaningServiceID` char(8) NOT NULL,
  `dateTimeStartPrest` timestamp NOT NULL,
  `duration` int NOT NULL,
  `contractID` char(8) NOT NULL,
  `teamID` char(8) DEFAULT NULL,
  PRIMARY KEY (`cleaningServiceID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cleaningservice`
--

LOCK TABLES `cleaningservice` WRITE;
/*!40000 ALTER TABLE `cleaningservice` DISABLE KEYS */;
INSERT INTO `cleaningservice` VALUES ('CS000003','2024-08-26 22:00:00',148,'CO000001','TE000001');
/*!40000 ALTER TABLE `cleaningservice` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `client`
--

DROP TABLE IF EXISTS `client`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `client` (
  `clientID` char(8) NOT NULL,
  `name` varchar(255) NOT NULL,
  `firstName` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `gsm` varchar(20) NOT NULL,
  `streetName` varchar(255) NOT NULL,
  `streetNumber` int NOT NULL,
  `boxNumber` varchar(10) DEFAULT NULL,
  `postalCode` int NOT NULL,
  `locality` varchar(255) NOT NULL,
  PRIMARY KEY (`clientID`),
  UNIQUE KEY `email_UNIQUE` (`email`),
  UNIQUE KEY `gsm_UNIQUE` (`gsm`),
  KEY `fk_city` (`postalCode`,`locality`),
  CONSTRAINT `fk_city` FOREIGN KEY (`postalCode`, `locality`) REFERENCES `city` (`postalCode`, `locality`),
  CONSTRAINT `chk_email_format` CHECK ((`email` like _utf8mb3'%@%'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `client`
--

LOCK TABLES `client` WRITE;
/*!40000 ALTER TABLE `client` DISABLE KEYS */;
INSERT INTO `client` VALUES ('CL000001','Dupont','Jean','jean.dupont@example.com','0495123456','Rue de la Loi',16,'A',1000,'Bruxelles'),('CL000002','Peeters','Marie','marie.peeters@example.com','0478567890','Avenue Louise',54,'',2000,'Anvers');
/*!40000 ALTER TABLE `client` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `contract`
--

DROP TABLE IF EXISTS `contract`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `contract` (
  `contractID` char(8) NOT NULL,
  `nbHoursPerWeek` int NOT NULL,
  `startDate` date NOT NULL,
  `endDate` date DEFAULT NULL,
  `planningDesc` text NOT NULL,
  `siteID` char(8) NOT NULL,
  `clientID` char(8) NOT NULL,
  `durationType` enum('1 jour','1 semaine','2 semaines','1 mois','2 mois','3 mois','6 mois','9 mois','1 an','indéterminée') NOT NULL,
  PRIMARY KEY (`contractID`),
  KEY `fk_site` (`siteID`),
  KEY `fk_client` (`clientID`),
  CONSTRAINT `fk_client` FOREIGN KEY (`clientID`) REFERENCES `client` (`clientID`),
  CONSTRAINT `fk_site` FOREIGN KEY (`siteID`) REFERENCES `site` (`siteID`),
  CONSTRAINT `chk_dates_contract` CHECK (((`endDate` is null) or (`endDate` >= `startDate`))),
  CONSTRAINT `contract_chk_1` CHECK ((`nbHoursPerWeek` > 0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contract`
--

LOCK TABLES `contract` WRITE;
/*!40000 ALTER TABLE `contract` DISABLE KEYS */;
INSERT INTO `contract` VALUES ('CO000001',38,'2024-08-26','2025-08-26','Maintenance générale','SI000001','CL000001','1 an'),('CO000002',20,'2024-08-26','2024-11-26','Nettoyage mensuel','SI000002','CL000002','3 mois');
/*!40000 ALTER TABLE `contract` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `entityid`
--

DROP TABLE IF EXISTS `entityid`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `entityid` (
  `entity_name` varchar(50) NOT NULL,
  `next_id` int NOT NULL,
  PRIMARY KEY (`entity_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `entityid`
--

LOCK TABLES `entityid` WRITE;
/*!40000 ALTER TABLE `entityid` DISABLE KEYS */;
INSERT INTO `entityid` VALUES ('Absence',5),('City',1),('cleaningservice',1),('Client',28),('contract',2),('machinery',1),('materialsorder',9),('product',5),('replacement',2),('site',11),('staffmember',1),('team',1),('warning',1);
/*!40000 ALTER TABLE `entityid` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `machinery`
--

DROP TABLE IF EXISTS `machinery`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `machinery` (
  `machineryID` char(8) NOT NULL,
  `name` varchar(255) NOT NULL,
  `isAvailable` tinyint(1) NOT NULL,
  `siteID` char(8) NOT NULL,
  PRIMARY KEY (`machineryID`),
  UNIQUE KEY `name_UNIQUE` (`name`),
  KEY `fk_machinery_site` (`siteID`),
  CONSTRAINT `fk_machinery_site` FOREIGN KEY (`siteID`) REFERENCES `site` (`siteID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `machinery`
--

LOCK TABLES `machinery` WRITE;
/*!40000 ALTER TABLE `machinery` DISABLE KEYS */;
INSERT INTO `machinery` VALUES ('MA000001','Aspirateur Pro',0,'SI000001'),('MA000002','Nettoyeur haute pression',0,'SI000002');
/*!40000 ALTER TABLE `machinery` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `materialsorder`
--

DROP TABLE IF EXISTS `materialsorder`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `materialsorder` (
  `materialsOrderID` char(8) NOT NULL,
  `justification` text NOT NULL,
  `dateOrder` date NOT NULL,
  `teamID` char(8) NOT NULL,
  PRIMARY KEY (`materialsOrderID`),
  KEY `fk_team` (`teamID`),
  CONSTRAINT `fk_team` FOREIGN KEY (`teamID`) REFERENCES `team` (`teamID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `materialsorder`
--

LOCK TABLES `materialsorder` WRITE;
/*!40000 ALTER TABLE `materialsorder` DISABLE KEYS */;
INSERT INTO `materialsorder` VALUES ('MO000005','Ravitaillement de produits de nettoyage pour le mois','2024-08-26','TE000006'),('MO000006','Ravitaillement de produits de nettoyage','2024-08-26','TE000006'),('MO000007','Matériel de protection et de nettoyage','2024-08-26','TE000007'),('MO000008','Besoin brasserie francois','2024-08-28','TE000007');
/*!40000 ALTER TABLE `materialsorder` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `materialsorder_product`
--

DROP TABLE IF EXISTS `materialsorder_product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `materialsorder_product` (
  `materialsOrderID` char(8) NOT NULL,
  `productID` char(8) NOT NULL,
  `quantity` int NOT NULL,
  PRIMARY KEY (`materialsOrderID`,`productID`),
  KEY `fk_product` (`productID`),
  CONSTRAINT `fk_materialsOrder` FOREIGN KEY (`materialsOrderID`) REFERENCES `materialsorder` (`materialsOrderID`),
  CONSTRAINT `fk_product` FOREIGN KEY (`productID`) REFERENCES `product` (`productID`),
  CONSTRAINT `materialsorder_product_chk_1` CHECK ((`quantity` > 0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `materialsorder_product`
--

LOCK TABLES `materialsorder_product` WRITE;
/*!40000 ALTER TABLE `materialsorder_product` DISABLE KEYS */;
INSERT INTO `materialsorder_product` VALUES ('MO000005','PR000007',100),('MO000005','PR000008',50),('MO000006','PR000009',30),('MO000006','PR000010',500),('MO000008','PR000008',10);
/*!40000 ALTER TABLE `materialsorder_product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product` (
  `productID` char(8) NOT NULL,
  `productName` varchar(255) NOT NULL,
  `nbAvailable` int DEFAULT NULL,
  `description` text,
  PRIMARY KEY (`productID`),
  UNIQUE KEY `productName_UNIQUE` (`productName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` VALUES ('PR000004','Nettoyant vitres',110,'Nettoyant pour vitres et surfaces brillantes'),('PR000005','Gants de protection',300,'Gants résistants aux produits chimiques'),('PR000006','Sac poubelle',500,'Sacs poubelle grande capacité'),('PR000007','Sacs poubelle 50L',500,'Sacs poubelle de 50 litres pour usage quotidien'),('PR000008','Désinfectant pour sols',290,'Produit désinfectant pour tous types de sols'),('PR000009','Balais',130,'Balais avec manche en bois'),('PR000010','Gants jetables',900,'Gants jetables en latex');
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `replacement`
--

DROP TABLE IF EXISTS `replacement`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `replacement` (
  `replacementID` char(8) NOT NULL,
  `startDate` date NOT NULL,
  `endDate` date DEFAULT NULL,
  `staffReplacingID` char(8) NOT NULL,
  `absenceReplacedID` char(8) NOT NULL,
  PRIMARY KEY (`replacementID`),
  KEY `fk_staffReplacing` (`staffReplacingID`),
  KEY `fk_absenceReplaced` (`absenceReplacedID`),
  CONSTRAINT `fk_absenceReplaced` FOREIGN KEY (`absenceReplacedID`) REFERENCES `absence` (`absenceID`),
  CONSTRAINT `fk_staffReplacing` FOREIGN KEY (`staffReplacingID`) REFERENCES `staffmember` (`numONSS`),
  CONSTRAINT `chk_dates_replacement` CHECK (((`endDate` is null) or (`endDate` >= `startDate`)))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `replacement`
--

LOCK TABLES `replacement` WRITE;
/*!40000 ALTER TABLE `replacement` DISABLE KEYS */;
INSERT INTO `replacement` VALUES ('RP000002','2024-08-12','2024-08-16','22334455','AB000002'),('RP000003','2024-08-21','2024-08-25','33445566','AB000004'),('RP000004','2024-08-24','2024-08-25','44556677','AB000005'),('RP000005','2024-08-06',NULL,'55667788','AB000003');
/*!40000 ALTER TABLE `replacement` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `site`
--

DROP TABLE IF EXISTS `site`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `site` (
  `siteID` char(8) NOT NULL,
  `siteName` varchar(255) NOT NULL,
  `streetName` varchar(255) NOT NULL,
  `streetNumber` int NOT NULL,
  `boxHouse` varchar(10) DEFAULT NULL,
  `description` text,
  `postalCode` int NOT NULL,
  `locality` varchar(255) NOT NULL,
  `clientOwnerID` char(8) NOT NULL,
  PRIMARY KEY (`siteID`),
  UNIQUE KEY `siteName_UNIQUE` (`siteName`),
  KEY `fk_clientOwner` (`clientOwnerID`),
  KEY `fk_site_city` (`postalCode`,`locality`),
  CONSTRAINT `fk_clientOwner` FOREIGN KEY (`clientOwnerID`) REFERENCES `client` (`clientID`),
  CONSTRAINT `fk_site_city` FOREIGN KEY (`postalCode`, `locality`) REFERENCES `city` (`postalCode`, `locality`),
  CONSTRAINT `site_chk_1` CHECK ((`streetNumber` > 0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `site`
--

LOCK TABLES `site` WRITE;
/*!40000 ALTER TABLE `site` DISABLE KEYS */;
INSERT INTO `site` VALUES ('SI000001','Site A','Chaussée de Gand',500,'','Siège principal',1000,'Bruxelles','CL000001'),('SI000002','Site B','Avenue des Arts',12,'C','Bureau secondaire',2000,'Anvers','CL000002');
/*!40000 ALTER TABLE `site` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `staffmember`
--

DROP TABLE IF EXISTS `staffmember`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `staffmember` (
  `numONSS` char(8) NOT NULL,
  `birthday` date NOT NULL,
  `firstName` varchar(255) NOT NULL,
  `lastName` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `streetName` varchar(255) NOT NULL,
  `streetNumber` int NOT NULL,
  `boxNumber` varchar(10) DEFAULT NULL,
  `cellphoneNumber` varchar(10) DEFAULT NULL,
  `startDate` date NOT NULL,
  `isCleaner` tinyint(1) DEFAULT NULL,
  `graduate` varchar(255) DEFAULT NULL,
  `postalCode` int NOT NULL,
  `locality` varchar(255) NOT NULL,
  PRIMARY KEY (`numONSS`),
  UNIQUE KEY `email_UNIQUE` (`email`),
  UNIQUE KEY `cellphoneNumber_UNIQUE` (`cellphoneNumber`),
  KEY `fk_city_staff` (`postalCode`,`locality`),
  CONSTRAINT `fk_city_staff` FOREIGN KEY (`postalCode`, `locality`) REFERENCES `city` (`postalCode`, `locality`),
  CONSTRAINT `chk_phone_format` CHECK (((`cellphoneNumber` is null) or regexp_like(`cellphoneNumber`,_utf8mb3'^[0-9]{10}$'))),
  CONSTRAINT `staffmember_chk_1` CHECK ((`email` like _utf8mb3'%@%')),
  CONSTRAINT `staffmember_chk_2` CHECK ((`streetNumber` > 0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `staffmember`
--

LOCK TABLES `staffmember` WRITE;
/*!40000 ALTER TABLE `staffmember` DISABLE KEYS */;
INSERT INTO `staffmember` VALUES ('11223344','1987-02-25','Alice','Merville','alice.merville@example.com','Rue des Fleurs',18,'B2','0467890123','2024-08-11',1,'Bachelier',1000,'Bruxelles'),('12345678','1990-04-15','Sophie','Lemoine','sophie.lemoine@example.com','Rue Neuve',100,'','0478123456','2020-09-01',1,'Bachelor',1000,'Bruxelles'),('22334455','1992-07-13','Benoit','Leroux','benoit.leroux@example.com','Avenue de la Gare',22,NULL,'0487654321','2024-07-27',0,'Master',2000,'Anvers'),('33445566','1983-11-05','Celine','Dubois','celine.dubois@example.com','Boulevard des Arts',47,'C4','0467891234','2024-07-12',1,'Licence',3000,'Gand'),('44556677','1980-09-09','David','Bernard','david.bernard@example.com','Place Royale',5,NULL,'0476543210','2024-08-16',1,'Bachelier',4000,'Liège'),('55667788','1995-12-30','Eva','Moreau','eva.moreau@example.com','Rue du Centre',13,'A1','0456789012','2024-08-06',0,'Doctorat',5000,'Namur');
/*!40000 ALTER TABLE `staffmember` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `team`
--

DROP TABLE IF EXISTS `team`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `team` (
  `teamID` char(8) NOT NULL,
  `leaderID` char(8) NOT NULL,
  `secondMemberID` varchar(8) NOT NULL,
  `thirdMemberID` varchar(8) DEFAULT NULL,
  PRIMARY KEY (`teamID`),
  KEY `fk_leader` (`leaderID`),
  CONSTRAINT `fk_leader` FOREIGN KEY (`leaderID`) REFERENCES `staffmember` (`numONSS`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `team`
--

LOCK TABLES `team` WRITE;
/*!40000 ALTER TABLE `team` DISABLE KEYS */;
INSERT INTO `team` VALUES ('TE000006','11223344','22334455','33445566'),('TE000007','44556677','55667788','66778899');
/*!40000 ALTER TABLE `team` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `team_members`
--

DROP TABLE IF EXISTS `team_members`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `team_members` (
  `teamID` varchar(8) NOT NULL,
  `staffMemberID` varchar(8) NOT NULL,
  PRIMARY KEY (`teamID`,`staffMemberID`),
  KEY `staffMemberID` (`staffMemberID`),
  CONSTRAINT `team_members_ibfk_1` FOREIGN KEY (`teamID`) REFERENCES `team` (`teamID`) ON DELETE CASCADE,
  CONSTRAINT `team_members_ibfk_2` FOREIGN KEY (`staffMemberID`) REFERENCES `staffmember` (`numONSS`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `team_members`
--

LOCK TABLES `team_members` WRITE;
/*!40000 ALTER TABLE `team_members` DISABLE KEYS */;
/*!40000 ALTER TABLE `team_members` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `warning`
--

DROP TABLE IF EXISTS `warning`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `warning` (
  `warningID` char(8) NOT NULL,
  `description` text NOT NULL,
  `dateFault` date NOT NULL,
  `isSeriousFault` tinyint(1) NOT NULL,
  `staffMemberWarnedID` char(8) NOT NULL,
  PRIMARY KEY (`warningID`),
  KEY `fk_staffMemberWarned` (`staffMemberWarnedID`),
  CONSTRAINT `fk_staffMemberWarned` FOREIGN KEY (`staffMemberWarnedID`) REFERENCES `staffmember` (`numONSS`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `warning`
--

LOCK TABLES `warning` WRITE;
/*!40000 ALTER TABLE `warning` DISABLE KEYS */;
INSERT INTO `warning` VALUES ('WA000005','Manquement aux règles de sécurité','2024-08-16',1,'22334455'),('WA000006','Absence non justifiée','2024-08-19',1,'33445566'),('WA000007','Comportement inapproprié avec les collègues','2024-08-23',0,'44556677'),('WA000008','Détérioration volontaire du matériel','2024-08-11',1,'55667788');
/*!40000 ALTER TABLE `warning` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-08-28 15:59:24
