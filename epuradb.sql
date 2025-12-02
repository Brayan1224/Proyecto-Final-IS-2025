CREATE DATABASE  IF NOT EXISTS `epura` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `epura`;
-- MySQL dump 10.13  Distrib 8.0.38, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: epura
-- ------------------------------------------------------
-- Server version	8.0.40

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
-- Table structure for table `camiones`
--

DROP TABLE IF EXISTS `camiones`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `camiones` (
  `ID_camion` int NOT NULL AUTO_INCREMENT,
  `matricula` varchar(20) DEFAULT NULL,
  `capacidad_maxima_garrafones` int DEFAULT NULL,
  `capacidad_maxima_ml` int DEFAULT NULL,
  `capacidad_maxima_gact` int DEFAULT NULL,
  `capacidad_maxima_mlact` int DEFAULT NULL,
  PRIMARY KEY (`ID_camion`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `camiones`
--

LOCK TABLES `camiones` WRITE;
/*!40000 ALTER TABLE `camiones` DISABLE KEYS */;
INSERT INTO `camiones` VALUES (1,'C001',50,100000,50,100000),(2,'C002',40,150000,40,150000),(3,'C003',50,50000,50,50000),(4,'C004',70,60000,70,60000),(5,'C005',50,80000,50,80000),(6,'C006',60,80000,60,80000);
/*!40000 ALTER TABLE `camiones` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `categorias`
--

DROP TABLE IF EXISTS `categorias`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categorias` (
  `ID_categoria` int NOT NULL AUTO_INCREMENT,
  `nombre_categoria` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`ID_categoria`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categorias`
--

LOCK TABLES `categorias` WRITE;
/*!40000 ALTER TABLE `categorias` DISABLE KEYS */;
INSERT INTO `categorias` VALUES (1,'Garrafones'),(2,'Agua ePura'),(3,'Refrescos'),(4,'Leche Alpura'),(5,'Jugos'),(6,'Gatorade');
/*!40000 ALTER TABLE `categorias` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `clientes`
--

DROP TABLE IF EXISTS `clientes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `clientes` (
  `ID_cliente` int NOT NULL AUTO_INCREMENT,
  `nombre_cliente` varchar(40) DEFAULT NULL,
  `ap_pat_cliente` varchar(20) DEFAULT NULL,
  `ap_mat_cliente` varchar(20) DEFAULT NULL,
  `telefono_cliente` varchar(20) DEFAULT NULL,
  `cp` varchar(10) DEFAULT NULL,
  `calles` varchar(50) DEFAULT NULL,
  `R_ruta` int DEFAULT NULL,
  `R_tipo` int DEFAULT NULL,
  `coordx` int DEFAULT NULL,
  `coordy` int DEFAULT NULL,
  `R_usuario` int DEFAULT NULL,
  PRIMARY KEY (`ID_cliente`),
  KEY `R_ruta` (`R_ruta`),
  KEY `R_tipo` (`R_tipo`),
  KEY `clientes_ibfk_3_idx` (`R_usuario`),
  CONSTRAINT `clientes_ibfk_1` FOREIGN KEY (`R_ruta`) REFERENCES `rutas` (`ID_ruta`),
  CONSTRAINT `clientes_ibfk_2` FOREIGN KEY (`R_tipo`) REFERENCES `tipos_cliente` (`ID_tipo`),
  CONSTRAINT `clientes_ibfk_3` FOREIGN KEY (`R_usuario`) REFERENCES `usuarios` (`ID_usuario`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `clientes`
--

LOCK TABLES `clientes` WRITE;
/*!40000 ALTER TABLE `clientes` DISABLE KEYS */;
INSERT INTO `clientes` VALUES (2,'Raul','Juarez','Aguirre','2299121920','91810','entre calle 1 y calle 2',2,1,NULL,NULL,NULL),(4,'Adrian','Sanchez','Vidal','2291174329','91830','entre calle 4 y esquina 2',2,2,NULL,NULL,NULL),(5,'Saul','Dominguez','Nicolas','2291123210','91889','privada 1 y 2',4,4,NULL,NULL,NULL),(6,'Neli','Gonzalez','Palmeros','2292253210','91829','privada 3 y 4',4,3,NULL,NULL,NULL),(8,'Luis Gerard','Leo','Salamanc','2291182764','91855','chichicatxle casa rosada',3,3,NULL,NULL,NULL),(9,'Brayan Emanuel','Vazquez','Peña','2291183620','91809','cornizuelo y esq membrillo\nReserva 3',2,1,NULL,NULL,NULL),(10,'Luis Gerardo','Leon ','Salamanca','2233445566','91823','calle tal casa color rosa',2,1,2,8,14),(11,'Brayan Emanuel','Vazquez','Peña','2299112233','91888','Casa blanca 2 pisos',1,1,6,6,16);
/*!40000 ALTER TABLE `clientes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `detalles_pedido`
--

DROP TABLE IF EXISTS `detalles_pedido`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `detalles_pedido` (
  `R_pedido` int DEFAULT NULL,
  `R_producto` int DEFAULT NULL,
  `cantidad` int DEFAULT NULL,
  KEY `R_pedido` (`R_pedido`),
  KEY `R_producto` (`R_producto`),
  CONSTRAINT `detalles_pedido_ibfk_1` FOREIGN KEY (`R_pedido`) REFERENCES `pedidos` (`ID_pedido`),
  CONSTRAINT `detalles_pedido_ibfk_2` FOREIGN KEY (`R_producto`) REFERENCES `productos` (`ID_producto`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `detalles_pedido`
--

LOCK TABLES `detalles_pedido` WRITE;
/*!40000 ALTER TABLE `detalles_pedido` DISABLE KEYS */;
INSERT INTO `detalles_pedido` VALUES (3,1,2),(3,10,5),(5,2,2),(5,1,2),(5,9,3),(6,15,4),(7,14,10),(8,11,5),(8,25,5),(9,1,5),(9,4,10),(9,8,1),(10,14,5),(10,16,2),(11,2,10),(11,3,5),(11,1,5),(12,1,5),(12,6,10),(13,4,4),(13,6,6),(14,14,12),(15,10,6),(15,17,3),(16,5,5),(16,8,2),(16,10,1),(17,2,1),(17,11,2),(18,1,1),(19,2,2);
/*!40000 ALTER TABLE `detalles_pedido` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pedidos`
--

DROP TABLE IF EXISTS `pedidos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pedidos` (
  `ID_pedido` int NOT NULL AUTO_INCREMENT,
  `fecha_pedido` date DEFAULT NULL,
  `fecha_entrega` date DEFAULT NULL,
  `R_cliente` int DEFAULT NULL,
  `R_status` int DEFAULT NULL,
  PRIMARY KEY (`ID_pedido`),
  KEY `R_cliente` (`R_cliente`),
  KEY `R_pedido` (`R_status`),
  CONSTRAINT `pedidos_ibfk_1` FOREIGN KEY (`R_cliente`) REFERENCES `clientes` (`ID_cliente`),
  CONSTRAINT `pedidos_ibfk_2` FOREIGN KEY (`R_status`) REFERENCES `status_pedidos` (`ID_status`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pedidos`
--

LOCK TABLES `pedidos` WRITE;
/*!40000 ALTER TABLE `pedidos` DISABLE KEYS */;
INSERT INTO `pedidos` VALUES (3,'2024-11-30','2024-12-02',4,3),(5,'2024-11-30','2024-12-02',5,3),(6,'2024-11-30','2024-12-03',6,3),(7,'2024-11-30','2024-12-03',2,3),(8,'2024-11-30','2024-12-04',6,3),(9,'2024-12-02','2024-12-10',2,3),(10,'2024-12-02','2024-12-03',8,3),(11,'2024-12-02','2024-12-05',8,3),(12,'2024-12-02','2024-12-10',9,3),(13,'2024-12-02','2024-12-11',8,3),(14,'2024-12-02','2024-12-03',4,3),(15,'2024-12-02','2024-12-05',2,3),(16,'2024-12-03','2024-12-12',9,3),(17,'2025-11-29','2025-11-30',10,1),(18,'2025-11-29','2025-12-01',10,1),(19,'2025-11-29','2025-12-01',9,1);
/*!40000 ALTER TABLE `pedidos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `productos`
--

DROP TABLE IF EXISTS `productos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `productos` (
  `ID_producto` int NOT NULL AUTO_INCREMENT,
  `descripcion_producto` varchar(60) DEFAULT NULL,
  `piezas_disponibles` int DEFAULT NULL,
  `precio` float DEFAULT NULL,
  `R_categoria` int DEFAULT NULL,
  `capacidad` int DEFAULT NULL,
  PRIMARY KEY (`ID_producto`),
  KEY `R_categoria` (`R_categoria`),
  CONSTRAINT `productos_ibfk_1` FOREIGN KEY (`R_categoria`) REFERENCES `categorias` (`ID_categoria`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `productos`
--

LOCK TABLES `productos` WRITE;
/*!40000 ALTER TABLE `productos` DISABLE KEYS */;
INSERT INTO `productos` VALUES (1,'Epura 20 Lts Policarbonato',44,50,1,20000),(2,'Epura 1 Lts Pet 6',47,65.5,2,6000),(3,'Epura 600 Ml Pet 6',45,51,2,3600),(4,'Epura 330 Ml Pet 24',36,214.5,2,7920),(5,'Pepsi 1.5 Lts Pet 2',44,50,3,3000),(6,'Mzna Sol RA 1.5 Lts Pet 2',33,50,3,3000),(7,'7 Up RA 1.5 Lts Pet 2',50,50,3,3000),(8,'Pepsi Black 1.5 Lts Pet 2',47,36,3,3000),(9,'Mix 1.5 Lts 4Pepsi/MSolRA/7UpRA Pet 6',50,131,3,9000),(10,'Pepsi 2 Lts Pet 1',43,33,3,2000),(11,'Pepsi 3 Lts Pet 1',46,35,3,3000),(12,'Canada Dry C. S. 2 Lts Pet 2',50,40,3,4000),(13,'Jarritos Mandarina 2 Lts Pet 2',50,48,3,4000),(14,'Jarritos Piña 2 Lts Pet 2',33,48,3,4000),(15,'Jarritos Tamarindo 2 Lts Pet 2',50,48,3,4000),(16,'Jarritos Tuti-fruti 2 Lts Pet 2',48,48,3,4000),(17,'Pepsi 12 Oz Lata Six Pack',47,95,3,2130),(18,'Pepsi Light 12 Oz Lata Six Pack',50,95,3,2130),(19,'7 Up RA 12 Oz Lata 6 Pack',50,95,3,2130),(20,'Pepsi 12 Oz Pet 12',50,115.5,3,4260),(21,'Pepsi Black 12 Oz Pet 4',50,31.5,3,1420),(22,'Pepsi Black 12 Oz PET 12',50,94,3,4260),(23,'Alpura Clasica 1 Lts. TP 1',50,28,4,1000),(24,'Alpura Deslactosada 1 Lts. TP 1',50,29,4,1000),(25,'Alpura Clasica 1 Lts TP 6',50,154.5,4,6000),(26,'Alpura Deslactosada 1 Lts TP 6',50,160,4,6000),(27,'Alpura Vaquitas Chocolate 1 Lts. TP 1',50,33,4,1000),(28,'Alpura Vaquitas Chocolate 1 Lts. TP 2',50,56.5,4,2000),(29,'Cosecha Pura Naranja 1 Lts Pet 4',50,80,5,4000),(30,'Cosecha Pura Naranja 1 Lts Pet 2',50,41,5,2000),(31,'Cosecha Pura Durazno 1 Lts Pet 2',50,41,5,2000),(32,'Cosecha Pura Nectar Mango 1 Lts Pet 2',50,160,5,2000),(33,'Cosecha Pura Manzana 1 Lts Pet 2',50,41,5,2000),(34,'Jumex Fresh Sun Light Citrus 2 Lts Pet 2',50,42,5,4000),(35,'Gatorade Fierce Moras 1 Lts SC Pet 2',50,56,6,2000),(36,'Gatorade Ponche 1 Lts SC Pet 2',50,56,6,2000),(37,'Gatorade Naranja 1 Lts SC Pet 2',50,56,6,2000);
/*!40000 ALTER TABLE `productos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rutas`
--

DROP TABLE IF EXISTS `rutas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rutas` (
  `ID_ruta` int NOT NULL AUTO_INCREMENT,
  `nombre_ruta` varchar(40) DEFAULT NULL,
  PRIMARY KEY (`ID_ruta`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rutas`
--

LOCK TABLES `rutas` WRITE;
/*!40000 ALTER TABLE `rutas` DISABLE KEYS */;
INSERT INTO `rutas` VALUES (1,'Ruta 1'),(2,'Ruta 2'),(3,'Ruta 3'),(4,'Ruta 4');
/*!40000 ALTER TABLE `rutas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rutas_asignadas`
--

DROP TABLE IF EXISTS `rutas_asignadas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rutas_asignadas` (
  `R_camion` int DEFAULT NULL,
  `R_pedido` int DEFAULT NULL,
  KEY `R_camion` (`R_camion`),
  KEY `R_pedido` (`R_pedido`),
  CONSTRAINT `rutas_asignadas_ibfk_2` FOREIGN KEY (`R_camion`) REFERENCES `camiones` (`ID_camion`),
  CONSTRAINT `rutas_asignadas_ibfk_3` FOREIGN KEY (`R_pedido`) REFERENCES `pedidos` (`ID_pedido`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rutas_asignadas`
--

LOCK TABLES `rutas_asignadas` WRITE;
/*!40000 ALTER TABLE `rutas_asignadas` DISABLE KEYS */;
/*!40000 ALTER TABLE `rutas_asignadas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `status_pedidos`
--

DROP TABLE IF EXISTS `status_pedidos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `status_pedidos` (
  `ID_status` int NOT NULL AUTO_INCREMENT,
  `status_pedido` varchar(25) DEFAULT NULL,
  PRIMARY KEY (`ID_status`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `status_pedidos`
--

LOCK TABLES `status_pedidos` WRITE;
/*!40000 ALTER TABLE `status_pedidos` DISABLE KEYS */;
INSERT INTO `status_pedidos` VALUES (1,'Sin Asignar'),(2,'Asignado'),(3,'Entregado');
/*!40000 ALTER TABLE `status_pedidos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tipos_cliente`
--

DROP TABLE IF EXISTS `tipos_cliente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tipos_cliente` (
  `ID_tipo` int NOT NULL AUTO_INCREMENT,
  `tipo_cliente` varchar(25) DEFAULT NULL,
  PRIMARY KEY (`ID_tipo`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tipos_cliente`
--

LOCK TABLES `tipos_cliente` WRITE;
/*!40000 ALTER TABLE `tipos_cliente` DISABLE KEYS */;
INSERT INTO `tipos_cliente` VALUES (1,'casa'),(2,'negocio'),(3,'restaurante'),(4,'hotel'),(5,'otro');
/*!40000 ALTER TABLE `tipos_cliente` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuarios` (
  `ID_usuario` int NOT NULL AUTO_INCREMENT,
  `usuario` varchar(30) DEFAULT NULL,
  `contrasenia` varchar(30) DEFAULT NULL,
  `rol` int DEFAULT NULL,
  PRIMARY KEY (`ID_usuario`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarios`
--

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` VALUES (1,'gerardo','gerardo123',NULL),(2,'brayan','brayan123',NULL),(3,'Admin','123',1),(4,'ADMIN2','123456',NULL),(5,'admin1000','1000123',NULL),(6,'gerardo1000','123',NULL),(7,'gerardox2','123',NULL),(8,'gerardox3','123',NULL),(9,'gerardox4','123',NULL),(10,'gerardox5','123',NULL),(11,'gerardox6','123',NULL),(12,'gerardox7','123',NULL),(13,'gerardo5555','5555',3),(14,'ladeaverdis','123',3),(15,'brayan','123',3),(16,'brayanv2','123',3);
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'epura'
--

--
-- Dumping routines for database 'epura'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-12-01 20:55:39
