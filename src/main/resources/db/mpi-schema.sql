-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: mpi
-- ------------------------------------------------------
-- Server version	5.7.21-log

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
-- Table structure for table `appreciation`
--

DROP TABLE IF EXISTS `appreciation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `appreciation` (
  `id_appreciation` int(11) NOT NULL AUTO_INCREMENT,
  `id_user` int(11) NOT NULL,
  `id_idea` int(11) NOT NULL,
  PRIMARY KEY (`id_appreciation`),
  UNIQUE KEY `id_like_UNIQUE` (`id_appreciation`)
) ENGINE=InnoDB AUTO_INCREMENT=221 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `appreciation`
--

LOCK TABLES `appreciation` WRITE;
/*!40000 ALTER TABLE `appreciation` DISABLE KEYS */;
INSERT INTO `appreciation` VALUES (207,1,269),(209,1,272),(210,1,271),(218,1,287),(220,1,286);
/*!40000 ALTER TABLE `appreciation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `category` (
  `id_category` int(11) NOT NULL AUTO_INCREMENT,
  `body` varchar(100) NOT NULL,
  PRIMARY KEY (`id_category`),
  UNIQUE KEY `body_UNIQUE` (`body`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` VALUES (16,'Artificial Intelligence'),(15,'Books'),(9,'Business'),(1,'Education'),(3,'Finance'),(13,'Food and drinks'),(4,'Gaming'),(11,'Lifestyle'),(12,'Multimedia'),(5,'Music'),(14,'Navigation'),(8,'Photo / Video'),(2,'Social Media'),(6,'Sport'),(10,'Travel'),(7,'Vehicles');
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comment`
--

DROP TABLE IF EXISTS `comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `comment` (
  `id_comment` int(11) NOT NULL AUTO_INCREMENT,
  `body` varchar(500) NOT NULL,
  `posted_date` varchar(100) NOT NULL,
  `id_user` int(11) NOT NULL,
  `id_idea` int(11) NOT NULL,
  PRIMARY KEY (`id_comment`)
) ENGINE=InnoDB AUTO_INCREMENT=127 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment`
--

LOCK TABLES `comment` WRITE;
/*!40000 ALTER TABLE `comment` DISABLE KEYS */;
INSERT INTO `comment` VALUES (116,'That\'s very cool!\n','Tue, 26 Jun 2018 19:12:40',1,269),(123,'Nice idea','Wed, 27 Jun 2018 12:05:44',1,278),(124,'Nice!','Wed, 27 Jun 2018 13:38:16',1,287),(126,'Comme','Thu, 28 Jun 2018 10:39:22',1,299);
/*!40000 ALTER TABLE `comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `conversation`
--

DROP TABLE IF EXISTS `conversation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `conversation` (
  `id_conversation` int(11) NOT NULL AUTO_INCREMENT,
  `id_user` int(11) NOT NULL,
  `id_user2` int(11) NOT NULL,
  `created_date` datetime NOT NULL,
  PRIMARY KEY (`id_conversation`),
  UNIQUE KEY `id_conversation_UNIQUE` (`id_conversation`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `conversation`
--

LOCK TABLES `conversation` WRITE;
/*!40000 ALTER TABLE `conversation` DISABLE KEYS */;
INSERT INTO `conversation` VALUES (10,1,67,'2018-06-26 22:47:14');
/*!40000 ALTER TABLE `conversation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `following`
--

DROP TABLE IF EXISTS `following`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `following` (
  `id_following` int(11) NOT NULL AUTO_INCREMENT,
  `id_user` int(11) NOT NULL,
  `id_following_user` int(11) NOT NULL,
  `date_following` varchar(100) NOT NULL,
  PRIMARY KEY (`id_following`),
  UNIQUE KEY `id_following_UNIQUE` (`id_following`)
) ENGINE=InnoDB AUTO_INCREMENT=110 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `following`
--

LOCK TABLES `following` WRITE;
/*!40000 ALTER TABLE `following` DISABLE KEYS */;
INSERT INTO `following` VALUES (109,1,67,'Tue Jun 26 18:01:05 EEST 2018');
/*!40000 ALTER TABLE `following` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `idea`
--

DROP TABLE IF EXISTS `idea`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `idea` (
  `id_idea` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL,
  `body` varchar(3500) NOT NULL,
  `posted_date` varchar(100) NOT NULL,
  `image_path` varchar(100) DEFAULT 'idea.jpg',
  `id_category` int(11) NOT NULL,
  `id_user` int(11) NOT NULL,
  `semantic` varchar(100) DEFAULT '0',
  `sintactic` varchar(100) DEFAULT '0',
  `likenumber` int(11) DEFAULT '0',
  `comnumber` int(11) DEFAULT '0',
  `simnumber` int(11) DEFAULT '0',
  PRIMARY KEY (`id_idea`)
) ENGINE=InnoDB AUTO_INCREMENT=300 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `idea`
--

LOCK TABLES `idea` WRITE;
/*!40000 ALTER TABLE `idea` DISABLE KEYS */;
INSERT INTO `idea` VALUES (269,'Vehicle Tracking Using Driver Mobile Gps','The Global Positioning System (GPS) is a space-based satellite navigation system that provides location information .This system uses GPS to track the location of the vehicle.This system will track location of the vehicle and will send details about the location to the admin. This system helps admin to find out the location of the driver driving the vehicle. Admin will know which driver is in which location. This system can be implemented in call taxi to find out the location of the driver driving the vehicle and will help the admin to allocate taxi to the customer.','Tue Jun 26 11:09:36 EEST 2018','The_2018-06-26_map-525349_1920.png',14,67,NULL,NULL,1,2,2),(271,'Vehicle tracking','Application that track\'s the location of the vehicle','Tue Jun 26 19:18:48 EEST 2018','Appl_2018-06-26_auto-1819164_1920.jpg',14,1,NULL,NULL,1,0,2),(272,'GPS taxi tracking','A system that can be implemented in call taxi. An GPS tracking application.','Tue Jun 26 19:26:13 EEST 2018','Asy_2018-06-26_navigation-1048294_1920.jpg',14,1,NULL,NULL,1,0,2),(273,'Online Bookstore System','An online bookstore software projects that acts as a central database containing various books in stock along with their title, author and cost. This project is a website that acts as a central book store. This web project is developed using asp.net C# as the front end and sql as a back-end.','Wed Jun 27 11:38:50 EEST 2018','Ano_2018-06-27_old-books-436498_1920.jpg',15,1,NULL,NULL,0,0,1),(276,'Online central book store','An website that acts as a central book store. The project is developed using asp.net C# as the front end and sql as a back-end.','Wed Jun 27 11:46:34 EEST 2018','Anw_2018-06-27_learning-online-2845360_1920.png',15,1,NULL,NULL,0,0,1),(277,'Records Mining For Business Intelligence','In this project we analyse how business intelligence on a website could be obtained from user’s access records instead of web logs of “hits”. User’s access records are captured by implementing a data mining algorithm on the website. User mostly browses those products in which he is interested.','Wed Jun 27 11:53:04 EEST 2018','Int_2018-06-27_business-1839876_1920.jpg',9,1,NULL,NULL,0,0,0),(278,'Education Assignment','This helps Professor and Students to digitally move the assignments on one location over the server.\r\nThe key features of our solution are:\r\n\r\nIt helps us to keep track of the assignments assigned & uploaded by the different professors on various subjects.\r\nIt also helps to keep track on number of the assignments submitted by the students.\r\nReports can be generated for various analyses.\r\nMoreover, it can work with files from a wide range of formats.\r\nIt is a total assignment management system designed to help school and college faculties.\r\nThis project has been designed in java struts and hibernate functionality.','Wed Jun 27 11:56:17 EEST 2018','This_2018-06-27_hammer-719062_1920.jpg',1,1,NULL,NULL,0,1,0),(279,'Social Media Recommendation System','People now-a-days read news online which has become very popular as the web provides access to news articles from millions of sources around the world. News Recommendation is to select relevant news by their themes. Identifying news based on the topic is critical in this task. News is proposed solely based on the author’s point of view. In this system we immensely enhance the performance of recommendation.','Wed Jun 27 12:09:27 EEST 2018','Peop_2018-06-27_social-media-862133_1920.jpg',2,1,NULL,NULL,0,0,0),(280,'Battle Royale (PlayerUnknown’s Battlegrounds)','Any gamer that watched or read The Hunger Games immediately saw the potential in it for an amazing video game. A large number of people drop into an area littered with weapons, and the sole survivor wins. The grim narrative convention of the so-called “Battle Royale” (derived from the eponymous 1999 Japanese novel and its subsequent film adaptation) lends itself perfectly to the indiscriminate carnage of video games.','Wed Jun 27 12:14:06 EEST 2018','Any_2018-06-27_gamer-3159227_1920.jpg',4,1,NULL,NULL,0,0,0),(281,'Guided Tours','Guided tours are a great business idea for you if you\'re living in a tourist area. You can lean on a number of sites out there to advertise your services and your cost to get this up and running is virtually nothing aside from setting up some simple legal or business structure.','Wed Jun 27 12:35:22 EEST 2018','Guid_2018-06-27_passengers-1150043_1920.jpg',10,1,NULL,NULL,0,0,0),(282,'A chess engine','Try to write an engine that can play chess against a human opponent using a Universal Chess Interface compatible GUI, such as XBoard. See Stockfish as an example. For a less daunting challenge, you may wish to focus on the behavior of just one piece, e.g., Knights.','Wed Jun 27 12:42:15 EEST 2018','Try_2018-06-27_chess-1215079_1920.jpg',16,1,NULL,NULL,0,0,0),(283,'Selfie Competition','Find out whose selfies are better. You or your friends','Wed Jun 27 12:49:17 EEST 2018','Find_2018-06-27_tim-gouw-212348-unsplash.jpg',8,1,NULL,NULL,0,0,0),(284,'Personal finance','Hi, I\'m working on some concept for my new idea and I\'m looking for some feedback here. I want to make simple overview of my incoming and outgoing payments. I use Excel and task manager for my personal finance but it really suck. So I decided to try a new way and combine task manager, stats, calendar and notifications into one simple app.','Wed Jun 27 12:51:18 EEST 2018','Hi,_2018-06-27_euro-1785517_1920.jpg',3,1,NULL,NULL,0,0,0),(285,'Rent to buy people\'s Cars','Here in Brazil there is a lot of used car stores and there is a lot of car listings (see webmotors.com.br, icarros.com.br).\r\nIf a SHOP owner wants its vehicles to show up in any listing and his company website, he will have to duplicate service. This can be very dangerous if he forgets to update some of the listings.','Wed Jun 27 12:56:52 EEST 2018','Here_2018-06-27_car-368636_1920.jpg',7,1,NULL,NULL,0,0,0),(286,'Machine Learning in Sports','People are looking for different methods of investment, from currency exchanges to buying companies’ shares and trading futures. All these methods share one major flaw – already heavily applied Machine Learning and prediction models damaged these markets so severely, that it is almost impossible for the small and middle-size investor (who have a couple of thousands of dollars) to make a profit.','Wed Jun 27 12:59:21 EEST 2018','Peop_2018-06-27_digital-marketing-1725340_1920.jpg',16,1,NULL,NULL,1,0,0),(287,'Let\'s Play Together!','A Sports based Social Network. What if we connect the players and the sport. Local players in one particular sport can discuss and play together or discuss about their favorite team or connecting with the right coaches and so on.','Wed Jun 27 13:01:22 EEST 2018','ASp_2018-06-27_team-spirit-2447163_1920.jpg',6,1,NULL,NULL,3,1,1),(299,'Social Network','I think of an social network where players in one particular sport \r\ncan discuss and play together or having discussions about their favourite teams.','Thu Jun 28 10:38:43 EEST 2018','Ith_2018-06-28_social-media-3362037_1920.png',6,1,NULL,NULL,0,1,1);
/*!40000 ALTER TABLE `idea` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `matching`
--

DROP TABLE IF EXISTS `matching`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `matching` (
  `id_match` int(11) NOT NULL AUTO_INCREMENT,
  `id_idea` int(11) NOT NULL,
  `id_match_idea` int(11) NOT NULL,
  `semantic` varchar(10) NOT NULL,
  PRIMARY KEY (`id_match`),
  UNIQUE KEY `id_match_UNIQUE` (`id_match`)
) ENGINE=InnoDB AUTO_INCREMENT=296 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `matching`
--

LOCK TABLES `matching` WRITE;
/*!40000 ALTER TABLE `matching` DISABLE KEYS */;
INSERT INTO `matching` VALUES (278,272,271,'50.0'),(279,271,272,'50.0'),(280,269,271,'67.3'),(281,271,269,'67.3'),(282,269,272,'67.2'),(283,272,269,'67.2'),(286,276,273,'89.0'),(287,273,276,'89.0'),(294,299,287,'97.0'),(295,287,299,'97.0');
/*!40000 ALTER TABLE `matching` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `message`
--

DROP TABLE IF EXISTS `message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `message` (
  `id_message` int(11) NOT NULL AUTO_INCREMENT,
  `body` varchar(500) NOT NULL,
  `send_date` varchar(100) NOT NULL,
  `id_sender` int(11) NOT NULL,
  `id_conversation` int(11) NOT NULL,
  `id_receiver` int(11) NOT NULL,
  PRIMARY KEY (`id_message`)
) ENGINE=InnoDB AUTO_INCREMENT=445 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `message`
--

LOCK TABLES `message` WRITE;
/*!40000 ALTER TABLE `message` DISABLE KEYS */;
INSERT INTO `message` VALUES (434,'Your ideas are great!','Tue, 26 Jun 2018 22:47:14',1,10,67),(435,'Thank you! Lets meet to discuss more, what do you say?','Tue, 26 Jun 2018 22:48:23',67,10,1),(443,'Hello','Thu, 28 Jun 2018 10:36:46',1,10,67),(444,'Yes!','Thu, 28 Jun 2018 10:36:59',1,10,67);
/*!40000 ALTER TABLE `message` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role` (
  `id` int(11) NOT NULL,
  `name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `schema_version`
--

DROP TABLE IF EXISTS `schema_version`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `schema_version` (
  `installed_rank` int(11) NOT NULL,
  `version` varchar(50) DEFAULT NULL,
  `description` varchar(200) NOT NULL,
  `type` varchar(20) NOT NULL,
  `script` varchar(1000) NOT NULL,
  `checksum` int(11) DEFAULT NULL,
  `installed_by` varchar(100) NOT NULL,
  `installed_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `execution_time` int(11) NOT NULL,
  `success` tinyint(1) NOT NULL,
  PRIMARY KEY (`installed_rank`),
  KEY `schema_version_s_idx` (`success`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `schema_version`
--

LOCK TABLES `schema_version` WRITE;
/*!40000 ALTER TABLE `schema_version` DISABLE KEYS */;
INSERT INTO `schema_version` VALUES (1,'1','<< Flyway Baseline >>','BASELINE','<< Flyway Baseline >>',NULL,'root','2018-02-17 20:21:01',0,1),(2,'2017.02.07.15.52.00','createTable PM FORECAST SETTINGS','SQL','V2017.02.07.15.52.00__createTable_PM_FORECAST_SETTINGS.sql',1885708031,'root','2018-02-17 20:21:01',2,1),(3,'2017.12.06.00.00','createTableTest','SQL','V2017.12.06.00.00__createTableTest.sql',0,'root','2018-02-17 22:10:17',3,1);
/*!40000 ALTER TABLE `schema_version` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tag`
--

DROP TABLE IF EXISTS `tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tag` (
  `id_tag` int(11) NOT NULL AUTO_INCREMENT,
  `body` varchar(100) NOT NULL,
  `id_idea` int(11) NOT NULL,
  PRIMARY KEY (`id_tag`),
  UNIQUE KEY `id_UNIQUE` (`id_tag`)
) ENGINE=InnoDB AUTO_INCREMENT=374 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tag`
--

LOCK TABLES `tag` WRITE;
/*!40000 ALTER TABLE `tag` DISABLE KEYS */;
INSERT INTO `tag` VALUES (175,'Global Positioning System GPS',269),(176,'spacebased satellite navigation',269),(177,'provides location information',269),(178,'uses',269),(179,'system helps admin',269),(180,'location',269),(181,'driver',269),(182,'vehicle',269),(183,'system',269),(184,'implemented',269),(185,'call taxi',269),(188,'Application',271),(189,'location',271),(190,'vehicle',271),(191,'system',272),(192,'implemented',272),(193,'call taxi',272),(194,'GPS',272),(195,'online bookstore software projects',273),(196,'acts',273),(197,'central database containing various books',273),(198,'stock',273),(199,'title',273),(200,'website',273),(201,'central book',273),(221,'website',276),(222,'acts',276),(223,'central book',276),(224,'business intelligence',277),(225,'website',277),(226,'obtained',277),(227,'access records instead',277),(228,'access records',277),(229,'data',277),(230,'algorithm',277),(231,'mostly browses',277),(232,'products',277),(233,'helps Professor',278),(234,'Students',278),(235,'digitally',278),(236,'assignments',278),(237,'one location',278),(238,'key features',278),(239,'People nowadays read news online',279),(240,'become',279),(241,'popular',279),(242,'web provides access',279),(243,'articles',279),(244,'millions',279),(245,'News Recommendation',279),(246,'relevant',279),(247,'themes',279),(248,'Identifying news',279),(249,'topic',279),(250,'critical',279),(251,'task',279),(252,'News',279),(253,'point',279),(254,'view',279),(255,'system',279),(256,'performance',279),(257,'recommendation',279),(258,'gamer',280),(259,'Hunger Games',280),(260,'potential',280),(261,'amazing video game',280),(262,'large number',280),(263,'people',280),(264,'area',280),(265,'sole survivor wins',280),(266,'grim narrative convention',280),(267,'socalled',280),(268,'eponymous Japanese novel',280),(269,'subsequent film adaptation',280),(270,'tours',281),(271,'great business idea',281),(272,'youre',281),(273,'tourist',281),(274,'lean',281),(275,'number',281),(276,'services',281),(277,'cost',281),(278,'engine',282),(279,'human opponent',282),(280,'Universal Chess Interface compatible GUI',282),(281,'example',282),(282,'less daunting challenge',282),(283,'wish',282),(284,'focus',282),(285,'behavior',282),(286,'one piece',282),(287,'Knights',282),(288,'selfies',283),(289,'better',283),(290,'friends',283),(291,'concept',284),(292,'new idea',284),(293,'feedback',284),(294,'simple overview',284),(295,'payments',284),(296,'manager',284),(297,'personal finance',284),(298,'suck',284),(299,'new way',284),(300,'manager stats',284),(301,'one simple app',284),(302,'Brazil',285),(303,'lot',285),(304,'car stores',285),(305,'listings',285),(306,'forgets',285),(307,'People',286),(308,'different methods',286),(309,'investment',286),(310,'currency exchanges',286),(311,'shares',286),(312,'trading',286),(314,'Machine',286),(315,'prediction models',286),(316,'markets',286),(339,'Social Network',287),(340,'players',287),(341,'sport',287),(342,'Local players',287),(343,'one particular sport',287),(344,'favorite team',287),(370,'social network',299),(371,'players',299),(372,'one particular sport',299),(373,'discussions',299);
/*!40000 ALTER TABLE `tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `id_user` int(11) NOT NULL AUTO_INCREMENT,
  `full_name` varchar(100) NOT NULL,
  `username` varchar(50) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL,
  `phone_number` varchar(15) DEFAULT ' ',
  `role` int(11) NOT NULL DEFAULT '1',
  `reg_date` varchar(100) NOT NULL,
  `occupation` varchar(100) NOT NULL,
  `image_path` varchar(100) DEFAULT 'user1.png',
  `confirmed` int(11) NOT NULL DEFAULT '0',
  `token` varchar(100) DEFAULT NULL,
  `alert` int(11) NOT NULL DEFAULT '0',
  `forgot_token` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id_user`,`username`),
  UNIQUE KEY `username_UNIQUE` (`username`),
  UNIQUE KEY `email_UNIQUE` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=68 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'Alex Nutu10','alenut10','alexg.nutu@gmail.com','$2a$10$Eife2tG/HgJO0CmRceoKgernfW8RK5vQ5FkG7USp3QD0lrWcUl6xO','1232-123-123',0,'2018-02-23 02:30:42','Director','alenut10_2018-06-26_av22.jpg',1,NULL,1,'e7d790c6-b989-411d-924c-e79f9c779c2c'),(67,'Alexandra Radu','alexandra','alexandru-gheorghe.nutu@my.fmi.unibuc.ro','$2a$10$DPqqUi4YCK4LloSNlqoslu7n9tBNY.fxTVE3uU8WZXDSRICYM.dle','0123-123-123',0,'2018-06-26 11:03:28.353','Team Leader','user1.png',1,'9617605c-e323-4af6-8082-9172b3c524d0',0,NULL);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-07-12 10:01:01
