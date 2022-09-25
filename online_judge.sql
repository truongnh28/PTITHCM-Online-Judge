-- MySQL dump 10.13  Distrib 8.0.30, for Win64 (x86_64)
--
-- Host: localhost    Database: online_judge
-- ------------------------------------------------------
-- Server version	8.0.30
DROP DATABASE IF EXISTS online_judge;
CREATE SCHEMA online_judge;
USE online_judge;
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
-- Table structure for table `contest_has_problem`
--

DROP TABLE IF EXISTS `contest_has_problem`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `contest_has_problem` (
                                       `contest_id` varchar(255) NOT NULL,
                                       `problem_id` varchar(255) NOT NULL,
                                       PRIMARY KEY (`contest_id`,`problem_id`),
                                       KEY `FK8ny9fijhd0pyv2gbaorb3sh9t` (`problem_id`),
                                       CONSTRAINT `FK8ny9fijhd0pyv2gbaorb3sh9t` FOREIGN KEY (`problem_id`) REFERENCES `problems` (`problem_id`),
                                       CONSTRAINT `FKrq4fhxf06yft0hr06ujalcx7c` FOREIGN KEY (`contest_id`) REFERENCES `contests` (`contest_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contest_has_problem`
--

LOCK TABLES `contest_has_problem` WRITE;
/*!40000 ALTER TABLE `contest_has_problem` DISABLE KEYS */;
/*!40000 ALTER TABLE `contest_has_problem` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `contests`
--

DROP TABLE IF EXISTS `contests`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `contests` (
                            `contest_id` varchar(100) NOT NULL,
                            `contest_end` datetime NOT NULL,
                            `contest_name` varchar(100) NOT NULL,
                            `contest_start` datetime NOT NULL,
                            `hide` tinyint DEFAULT NULL,
                            `teacher_id` varchar(10) NOT NULL,
                            PRIMARY KEY (`contest_id`),
                            KEY `FKmgx9ac1f3n40pqevrkv7etwch` (`teacher_id`),
                            CONSTRAINT `FKmgx9ac1f3n40pqevrkv7etwch` FOREIGN KEY (`teacher_id`) REFERENCES `teachers` (`teacher_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contests`
--

LOCK TABLES `contests` WRITE;
/*!40000 ALTER TABLE `contests` DISABLE KEYS */;
/*!40000 ALTER TABLE `contests` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `group_has_contest`
--

DROP TABLE IF EXISTS `group_has_contest`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `group_has_contest` (
                                     `contest_id` varchar(255) NOT NULL,
                                     `subject_class_group_id` varchar(255) NOT NULL,
                                     PRIMARY KEY (`contest_id`,`subject_class_group_id`),
                                     KEY `FKafxidmce8clqmgxr6fg1bp5tn` (`subject_class_group_id`),
                                     CONSTRAINT `FK1la0r5fym36gjxutb1b7slxl6` FOREIGN KEY (`contest_id`) REFERENCES `contests` (`contest_id`),
                                     CONSTRAINT `FKafxidmce8clqmgxr6fg1bp5tn` FOREIGN KEY (`subject_class_group_id`) REFERENCES `subject_class_groups` (`subject_class_group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `group_has_contest`
--

LOCK TABLES `group_has_contest` WRITE;
/*!40000 ALTER TABLE `group_has_contest` DISABLE KEYS */;
/*!40000 ALTER TABLE `group_has_contest` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `level`
--

DROP TABLE IF EXISTS `level`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `level` (
                         `level_id` tinyint NOT NULL,
                         `level_name` varchar(20) DEFAULT NULL,
                         PRIMARY KEY (`level_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `level`
--

LOCK TABLES `level` WRITE;
/*!40000 ALTER TABLE `level` DISABLE KEYS */;
INSERT INTO `level` VALUES (1,'Dễ'),(2,'Trung bình'),(3,'Khó');
/*!40000 ALTER TABLE `level` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `problem_has_type`
--

DROP TABLE IF EXISTS `problem_has_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `problem_has_type` (
                                    `problem_id` varchar(255) NOT NULL,
                                    `problem_type_id` varchar(255) NOT NULL,
                                    PRIMARY KEY (`problem_id`,`problem_type_id`),
                                    KEY `FK4erjdrn95082n2v0d9y3aamlf` (`problem_type_id`),
                                    CONSTRAINT `FK4erjdrn95082n2v0d9y3aamlf` FOREIGN KEY (`problem_type_id`) REFERENCES `problem_type` (`problem_type_id`),
                                    CONSTRAINT `FK85oikvlwn1igtxq5d7b4nf2yv` FOREIGN KEY (`problem_id`) REFERENCES `problems` (`problem_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `problem_has_type`
--

LOCK TABLES `problem_has_type` WRITE;
/*!40000 ALTER TABLE `problem_has_type` DISABLE KEYS */;
/*!40000 ALTER TABLE `problem_has_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `problem_type`
--

DROP TABLE IF EXISTS `problem_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `problem_type` (
                                `problem_type_id` varchar(100) NOT NULL,
                                `problem_type_name` varchar(100) NOT NULL,
                                PRIMARY KEY (`problem_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `problem_type`
--

LOCK TABLES `problem_type` WRITE;
/*!40000 ALTER TABLE `problem_type` DISABLE KEYS */;
INSERT INTO `problem_type` VALUES ('array','Array'),('basic','Basic'),('binarysearch','Binary Search'),('greedy','Greedy'),('math','Math'),('matrix','Matrix'),('sorting','Sorting');
/*!40000 ALTER TABLE `problem_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `problems`
--

DROP TABLE IF EXISTS `problems`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `problems` (
                            `problem_id` varchar(100) NOT NULL,
                            `problem_cloudinary_id` varchar(100) DEFAULT NULL,
                            `problem_name` varchar(100) DEFAULT NULL,
                            `problem_score` int DEFAULT NULL,
                            `problem_time_limit` int DEFAULT NULL,
                            `problem_memory_limit` int DEFAULT NULL,
                            `problem_url` longtext,
                            `hide` tinyint DEFAULT NULL,
                            `teacher_id` varchar(10) NOT NULL,
                            `level_id` tinyint NOT NULL,
                            PRIMARY KEY (`problem_id`),
                            KEY `FKbogtrdpnh3ei9selovxy1l3qb` (`teacher_id`),
                            KEY `FK_problems_level_idx` (`level_id`),
                            CONSTRAINT `FK_problems_level` FOREIGN KEY (`level_id`) REFERENCES `level` (`level_id`),
                            CONSTRAINT `FKbogtrdpnh3ei9selovxy1l3qb` FOREIGN KEY (`teacher_id`) REFERENCES `teachers` (`teacher_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `problems`
--

LOCK TABLES `problems` WRITE;
/*!40000 ALTER TABLE `problems` DISABLE KEYS */;
/*!40000 ALTER TABLE `problems` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `role` (
                        `role_id` tinyint NOT NULL,
                        `role_name` varchar(20) DEFAULT NULL,
                        PRIMARY KEY (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `student_of_group`
--

DROP TABLE IF EXISTS `student_of_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `student_of_group` (
                                    `student_id` varchar(255) NOT NULL,
                                    `subject_class_group_id` varchar(255) NOT NULL,
                                    PRIMARY KEY (`student_id`,`subject_class_group_id`),
                                    KEY `FK3y3ef138uc2nfwd7s2mwdnd1j` (`subject_class_group_id`),
                                    CONSTRAINT `FK3y3ef138uc2nfwd7s2mwdnd1j` FOREIGN KEY (`subject_class_group_id`) REFERENCES `subject_class_groups` (`subject_class_group_id`),
                                    CONSTRAINT `FK6fsgrfeatuyel0bvdtsrfxli6` FOREIGN KEY (`student_id`) REFERENCES `students` (`student_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `student_of_group`
--

LOCK TABLES `student_of_group` WRITE;
/*!40000 ALTER TABLE `student_of_group` DISABLE KEYS */;
INSERT INTO `student_of_group` VALUES ('N19DCCN132','INT1316211'),('N19DCCN221','INT1316211');
/*!40000 ALTER TABLE `student_of_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `students`
--

DROP TABLE IF EXISTS `students`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `students` (
                            `student_id` varchar(10) NOT NULL,
                            `active` tinyint DEFAULT NULL,
                            `password` varchar(100) NOT NULL,
                            `student_first_name` varchar(100) NOT NULL,
                            `student_last_name` varchar(100) NOT NULL,
                            PRIMARY KEY (`student_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `students`
--

LOCK TABLES `students` WRITE;
/*!40000 ALTER TABLE `students` DISABLE KEYS */;
INSERT INTO `students` VALUES ('N19DCCN031',1,'02012001','Hân','Trần Thái'),('N19DCCN132',1,'01012001','Nhơn','Trần Quốc'),('N19DCCN190',1,'26102001','Thanh','Nguyễn Nhật'),('N19DCCN221',1,'28022001','Trưởng','Nguyễn Hữu');
/*!40000 ALTER TABLE `students` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `subject_class_groups`
--

DROP TABLE IF EXISTS `subject_class_groups`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `subject_class_groups` (
                                        `subject_class_group_id` varchar(10) NOT NULL,
                                        `subject_class_group_name` varchar(100) DEFAULT NULL,
                                        `subject_class_id` varchar(10) NOT NULL,
                                        PRIMARY KEY (`subject_class_group_id`),
                                        KEY `FK2a0mnnsisml66hi9yui89e1uo` (`subject_class_id`),
                                        CONSTRAINT `FK2a0mnnsisml66hi9yui89e1uo` FOREIGN KEY (`subject_class_id`) REFERENCES `subject_classes` (`subject_class_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subject_class_groups`
--

LOCK TABLES `subject_class_groups` WRITE;
/*!40000 ALTER TABLE `subject_class_groups` DISABLE KEYS */;
INSERT INTO `subject_class_groups` VALUES ('INT1316211','Python D19CN1 1','INT131621'),('INT1316212','Python D19CN1 2','INT131621'),('INT1316221','Python D19CN2 1','INT131622'),('INT1316222','Python D19CN2 2','INT131622'),('INT1316223','Python D19CN2 3','INT131622'),('INT133211','OOP D19CN1 1','INT13321'),('INT133212','OOP D19CN1 2','INT13321'),('INT133221','OOP D19CN2 1','INT13322'),('INT133222','OOP D19CN2 2','INT13322');
/*!40000 ALTER TABLE `subject_class_groups` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `subject_classes`
--

DROP TABLE IF EXISTS `subject_classes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `subject_classes` (
                                   `subject_class_id` varchar(10) NOT NULL,
                                   `subject_class_name` varchar(100) DEFAULT NULL,
                                   `subject_id` varchar(10) NOT NULL,
                                   PRIMARY KEY (`subject_class_id`),
                                   KEY `FKitnnog6jm0qvg0fyivvfe64of` (`subject_id`),
                                   CONSTRAINT `FKitnnog6jm0qvg0fyivvfe64of` FOREIGN KEY (`subject_id`) REFERENCES `subjects` (`subject_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subject_classes`
--

LOCK TABLES `subject_classes` WRITE;
/*!40000 ALTER TABLE `subject_classes` DISABLE KEYS */;
INSERT INTO `subject_classes` VALUES ('INT131621','Python D19CN1','INT13162'),('INT131622','Python D19CN2','INT13162'),('INT13321','OOP D19CN1','INT1332'),('INT13322','OOP D19CN2','INT1332');
/*!40000 ALTER TABLE `subject_classes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `subjects`
--

DROP TABLE IF EXISTS `subjects`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `subjects` (
                            `subject_id` varchar(10) NOT NULL,
                            `subject_name` varchar(100) DEFAULT NULL,
                            PRIMARY KEY (`subject_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subjects`
--

LOCK TABLES `subjects` WRITE;
/*!40000 ALTER TABLE `subjects` DISABLE KEYS */;
INSERT INTO `subjects` VALUES ('INT13162','Lập trình với python'),('INT1332','Lập trình hướng đối tượng');
/*!40000 ALTER TABLE `subjects` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `submissions`
--

DROP TABLE IF EXISTS `submissions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `submissions` (
                               `submission_id` bigint NOT NULL,
                               `submission_score` int DEFAULT NULL,
                               `submission_time` datetime DEFAULT NULL,
                               `verdict` tinyint DEFAULT NULL,
                               `problem_id` varchar(100) NOT NULL,
                               `student_id` varchar(10) NOT NULL,
                               PRIMARY KEY (`submission_id`),
                               KEY `FKj5kbdqokftgx992cx24x3s583` (`problem_id`),
                               KEY `FKhwebuw14r6lb2ja85w9mwa8vf` (`student_id`),
                               CONSTRAINT `FKhwebuw14r6lb2ja85w9mwa8vf` FOREIGN KEY (`student_id`) REFERENCES `students` (`student_id`),
                               CONSTRAINT `FKj5kbdqokftgx992cx24x3s583` FOREIGN KEY (`problem_id`) REFERENCES `problems` (`problem_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `submissions`
--

LOCK TABLES `submissions` WRITE;
/*!40000 ALTER TABLE `submissions` DISABLE KEYS */;
/*!40000 ALTER TABLE `submissions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `teachers`
--

DROP TABLE IF EXISTS `teachers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `teachers` (
                            `teacher_id` varchar(10) NOT NULL,
                            `active` tinyint DEFAULT NULL,
                            `password` varchar(100) NOT NULL,
                            `teacher_first_name` varchar(100) NOT NULL,
                            `teacher_last_name` varchar(100) NOT NULL,
                            PRIMARY KEY (`teacher_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `teachers`
--

LOCK TABLES `teachers` WRITE;
/*!40000 ALTER TABLE `teachers` DISABLE KEYS */;
INSERT INTO `teachers` VALUES ('admin',1,'8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918','Kỳ Thư','Lưu Nguyễn');
/*!40000 ALTER TABLE `teachers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `test_case`
--

DROP TABLE IF EXISTS `test_case`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `test_case` (
                             `test_case_id` varchar(100) NOT NULL,
                             `test_case_score` int DEFAULT NULL,
                             `problem_id` varchar(100) NOT NULL,
                             `test_case_in` longtext NOT NULL,
                             `test_case_out` longtext NOT NULL,
                             PRIMARY KEY (`test_case_id`),
                             KEY `FKmxvd9qtqfvanwpwxvxns4nqpq` (`problem_id`),
                             CONSTRAINT `FKmxvd9qtqfvanwpwxvxns4nqpq` FOREIGN KEY (`problem_id`) REFERENCES `problems` (`problem_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `test_case`
--

LOCK TABLES `test_case` WRITE;
/*!40000 ALTER TABLE `test_case` DISABLE KEYS */;
/*!40000 ALTER TABLE `test_case` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-09-18  0:16:10