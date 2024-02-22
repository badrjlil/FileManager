-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Feb 22, 2024 at 08:51 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `fileman`
--

-- --------------------------------------------------------

--
-- Table structure for table `dossier`
--

CREATE TABLE `dossier` (
  `id_folder` int(11) NOT NULL,
  `path_folder` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `dossier`
--

INSERT INTO `dossier` (`id_folder`, `path_folder`) VALUES
(1, 'C:\\Users\\P3dR0\\Documents\\IGA\\_Projets\\Architecture CS\\FileManager\\FileManager_Server\\ServerStorage'),
(2, 'C:\\Users\\P3dR0\\Documents\\IGA\\_Projets\\Architecture CS\\FileManager\\FileManager_Server\\ServerStorage\\Abdeljalil'),
(3, 'C:\\Users\\P3dR0\\Documents\\IGA\\_Projets\\Architecture CS\\FileManager\\FileManager_Server\\ServerStorage\\Abdeljalil\\folder_1'),
(5, 'C:\\Users\\P3dR0\\Documents\\IGA\\_Projets\\Architecture CS\\FileManager\\FileManager_Server\\ServerStorage\\eded\\dede'),
(6, 'C:\\Users\\P3dR0\\Documents\\IGA\\_Projets\\Architecture CS\\FileManager\\FileManager_Server\\ServerStorage\\badr_22'),
(7, 'C:\\Users\\P3dR0\\Documents\\IGA\\_Projets\\Architecture CS\\FileManager\\FileManager_Server\\ServerStorage\\yasir_22'),
(8, 'C:\\Users\\P3dR0\\Documents\\IGA\\_Projets\\Architecture CS\\FileManager\\FileManager_Server\\ServerStorage\\Yassiro'),
(9, 'C:\\Users\\P3dR0\\Documents\\IGA\\_Projets\\Architecture CS\\FileManager\\FileManager_Server\\ServerStorage\\johno'),
(10, 'C:\\Users\\P3dR0\\Documents\\IGA\\_Projets\\Architecture CS\\FileManager\\FileManager_Server\\ServerStorage\\Abdeljalil\\folder_2'),
(11, 'C:\\Users\\P3dR0\\Documents\\IGA\\_Projets\\Architecture CS\\FileManager\\FileManager_Server\\ServerStorage\\soutnance'),
(12, 'C:\\Users\\P3dR0\\Documents\\IGA\\_Projets\\Architecture CS\\FileManager\\FileManager_Server\\ServerStorage\\soutnance\\ahmed '),
(13, 'C:\\Users\\P3dR0\\Documents\\IGA\\_Projets\\Architecture CS\\FileManager\\FileManager_Server\\ServerStorage\\fatiha'),
(14, 'C:\\Users\\P3dR0\\Documents\\IGA\\_Projets\\Architecture CS\\FileManager\\FileManager_Server\\ServerStorage\\hakim'),
(15, 'C:\\Users\\P3dR0\\Documents\\IGA\\_Projets\\Architecture CS\\FileManager\\FileManager_Server\\ServerStorage\\badrbadr'),
(16, 'C:\\Users\\P3dR0\\Documents\\IGA\\_Projets\\Architecture CS\\FileManager\\FileManager\\FileManager_Server\\ServerStorage');

-- --------------------------------------------------------

--
-- Table structure for table `fichier`
--

CREATE TABLE `fichier` (
  `path_file` varchar(255) DEFAULT NULL,
  `id_folder` int(11) NOT NULL,
  `id` int(11) NOT NULL,
  `read` tinyint(1) NOT NULL,
  `write` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `fichier`
--

INSERT INTO `fichier` (`path_file`, `id_folder`, `id`, `read`, `write`) VALUES
('C:\\Users\\P3dR0\\Documents\\IGA\\_Projets\\Architecture CS\\FileManager\\FileManager_Server\\ServerStorage\\Abdeljalil', 1, 1, 1, 1),
('C:\\Users\\P3dR0\\Documents\\IGA\\_Projets\\Architecture CS\\FileManager\\FileManager_Server\\ServerStorage\\fatiha', 1, 1, 1, 1),
('C:\\Users\\P3dR0\\Documents\\IGA\\_Projets\\Architecture CS\\FileManager\\FileManager_Server\\ServerStorage\\files.txt', 1, 1, 1, 1),
('C:\\Users\\P3dR0\\Documents\\IGA\\_Projets\\Architecture CS\\FileManager\\FileManager_Server\\ServerStorage\\hakim', 1, 1, 1, 1),
('C:\\Users\\P3dR0\\Documents\\IGA\\_Projets\\Architecture CS\\FileManager\\FileManager_Server\\ServerStorage\\johno', 1, 1, 1, 1),
('C:\\Users\\P3dR0\\Documents\\IGA\\_Projets\\Architecture CS\\FileManager\\FileManager_Server\\ServerStorage\\Yassiro', 1, 1, 1, 1),
('C:\\Users\\P3dR0\\Documents\\IGA\\_Projets\\Architecture CS\\FileManager\\FileManager_Server\\ServerStorage\\Abdeljalil\\file_1.txt', 2, 1, 1, 1),
('C:\\Users\\P3dR0\\Documents\\IGA\\_Projets\\Architecture CS\\FileManager\\FileManager_Server\\ServerStorage\\Abdeljalil\\file_2.txt', 2, 1, 1, 1),
('C:\\Users\\P3dR0\\Documents\\IGA\\_Projets\\Architecture CS\\FileManager\\FileManager_Server\\ServerStorage\\Abdeljalil\\file_3.txt', 2, 1, 1, 1),
('C:\\Users\\P3dR0\\Documents\\IGA\\_Projets\\Architecture CS\\FileManager\\FileManager_Server\\ServerStorage\\Abdeljalil\\file_5.txt', 2, 1, 1, 1),
('C:\\Users\\P3dR0\\Documents\\IGA\\_Projets\\Architecture CS\\FileManager\\FileManager_Server\\ServerStorage\\Abdeljalil\\file_6.txt', 2, 1, 1, 1),
('C:\\Users\\P3dR0\\Documents\\IGA\\_Projets\\Architecture CS\\FileManager\\FileManager_Server\\ServerStorage\\Abdeljalil\\file_7.txt', 2, 1, 1, 1),
('C:\\Users\\P3dR0\\Documents\\IGA\\_Projets\\Architecture CS\\FileManager\\FileManager_Server\\ServerStorage\\Abdeljalil\\folder_1', 2, 1, 1, 1),
('C:\\Users\\P3dR0\\Documents\\IGA\\_Projets\\Architecture CS\\FileManager\\FileManager_Server\\ServerStorage\\Abdeljalil\\folder_2', 2, 1, 1, 1),
('C:\\Users\\P3dR0\\Documents\\IGA\\_Projets\\Architecture CS\\FileManager\\FileManager_Server\\ServerStorage\\Abdeljalil\\folder_1\\file_1.txt', 3, 1, 1, 1),
('C:\\Users\\P3dR0\\Documents\\IGA\\_Projets\\Architecture CS\\FileManager\\FileManager_Server\\ServerStorage\\Abdeljalil\\folder_1\\folder_1', 3, 1, 1, 1),
('C:\\Users\\P3dR0\\Documents\\IGA\\_Projets\\Architecture CS\\FileManager\\FileManager_Server\\ServerStorage\\samira', 1, 1, 1, 1),
('C:\\Users\\P3dR0\\Documents\\IGA\\_Projets\\Architecture CS\\FileManager\\FileManager_Server\\ServerStorage\\eded\\deeé', 4, 1, 1, 1),
('C:\\Users\\P3dR0\\Documents\\IGA\\_Projets\\Architecture CS\\FileManager\\FileManager_Server\\ServerStorage\\eded\\dede', 4, 1, 1, 1),
('C:\\Users\\P3dR0\\Documents\\IGA\\_Projets\\Architecture CS\\FileManager\\FileManager_Server\\ServerStorage\\eded\\dede\\frf', 5, 1, 1, 1),
('C:\\Users\\P3dR0\\Documents\\IGA\\_Projets\\Architecture CS\\FileManager\\FileManager_Server\\ServerStorage\\yassir_2.txt', 1, 2, 1, 1),
('C:\\Users\\P3dR0\\Documents\\IGA\\_Projets\\Architecture CS\\FileManager\\FileManager_Server\\ServerStorage\\badr_22', 1, 2, 1, 1),
('C:\\Users\\P3dR0\\Documents\\IGA\\_Projets\\Architecture CS\\FileManager\\FileManager_Server\\ServerStorage\\yasir_22', 1, 2, 1, 1),
('C:\\Users\\P3dR0\\Documents\\IGA\\_Projets\\Architecture CS\\FileManager\\FileManager_Server\\ServerStorage\\soutnance', 1, 1, 1, 1),
('C:\\Users\\P3dR0\\Documents\\IGA\\_Projets\\Architecture CS\\FileManager\\FileManager_Server\\ServerStorage\\soutnance\\spus', 11, 1, 1, 1),
('C:\\Users\\P3dR0\\Documents\\IGA\\_Projets\\Architecture CS\\FileManager\\FileManager_Server\\ServerStorage\\soutnance\\ahmed ', 11, 1, 1, 1),
('C:\\Users\\P3dR0\\Documents\\IGA\\_Projets\\Architecture CS\\FileManager\\FileManager_Server\\ServerStorage\\fatiha\\hello.txt', 13, 1, 1, 1),
('C:\\Users\\P3dR0\\Documents\\IGA\\_Projets\\Architecture CS\\FileManager\\FileManager_Server\\ServerStorage\\Abdeljalil', 1, 2, 1, 1),
('C:\\Users\\P3dR0\\Documents\\IGA\\_Projets\\Architecture CS\\FileManager\\FileManager_Server\\ServerStorage\\yasir_22', 1, 1, 1, 1),
('C:\\Users\\P3dR0\\Documents\\IGA\\_Projets\\Architecture CS\\FileManager\\FileManager_Server\\ServerStorage\\files.txt', 1, 2, 1, 1),
('C:\\Users\\P3dR0\\Documents\\IGA\\_Projets\\Architecture CS\\FileManager\\FileManager_Server\\ServerStorage\\Yassiro\\ahmed.txt', 8, 1, 1, 1),
('C:\\Users\\P3dR0\\Documents\\IGA\\_Projets\\Architecture CS\\FileManager\\FileManager_Server\\ServerStorage\\Yassiro\\file_1.txt', 8, 1, 1, 1),
('C:\\Users\\P3dR0\\Documents\\IGA\\_Projets\\Architecture CS\\FileManager\\FileManager_Server\\ServerStorage\\Yassiro\\file_2.txt', 8, 1, 1, 1),
('C:\\Users\\P3dR0\\Documents\\IGA\\_Projets\\Architecture CS\\FileManager\\FileManager_Server\\ServerStorage\\Yassiro\\file_3.txt', 8, 1, 1, 1),
('C:\\Users\\P3dR0\\Documents\\IGA\\_Projets\\Architecture CS\\FileManager\\FileManager_Server\\ServerStorage\\Yassiro\\folder_1', 8, 1, 1, 1),
('C:\\Users\\P3dR0\\Documents\\IGA\\_Projets\\Architecture CS\\FileManager\\FileManager_Server\\ServerStorage\\Yassiro\\folder_2', 8, 1, 1, 1),
('C:\\Users\\P3dR0\\Documents\\IGA\\_Projets\\Architecture CS\\FileManager\\FileManager_Server\\ServerStorage\\Yassiro\\folder_3', 8, 1, 1, 1),
('C:\\Users\\P3dR0\\Documents\\IGA\\_Projets\\Architecture CS\\FileManager\\FileManager_Server\\ServerStorage\\Yassiro', 1, 2, 1, 1),
('C:\\Users\\P3dR0\\Documents\\IGA\\_Projets\\Architecture CS\\FileManager\\FileManager_Server\\ServerStorage\\Abdeljalil', 1, 2, 1, 1),
('C:\\Users\\P3dR0\\Documents\\IGA\\_Projets\\Architecture CS\\FileManager\\FileManager_Server\\ServerStorage\\fatiha', 1, 2, 1, 1),
('C:\\Users\\P3dR0\\Documents\\IGA\\_Projets\\Architecture CS\\FileManager\\FileManager_Server\\ServerStorage\\files.txt', 1, 2, 1, 1),
('C:\\Users\\P3dR0\\Documents\\IGA\\_Projets\\Architecture CS\\FileManager\\FileManager_Server\\ServerStorage\\hakim', 1, 2, 1, 1),
('C:\\Users\\P3dR0\\Documents\\IGA\\_Projets\\Architecture CS\\FileManager\\FileManager_Server\\ServerStorage\\johno', 1, 2, 1, 1),
('C:\\Users\\P3dR0\\Documents\\IGA\\_Projets\\Architecture CS\\FileManager\\FileManager_Server\\ServerStorage\\Yassiro', 1, 2, 1, 1),
('C:\\Users\\P3dR0\\Documents\\IGA\\_Projets\\Architecture CS\\FileManager\\FileManager_Server\\ServerStorage\\samira', 1, 2, 1, 1),
('C:\\Users\\P3dR0\\Documents\\IGA\\_Projets\\Architecture CS\\FileManager\\FileManager_Server\\ServerStorage\\yassir_2.txt', 1, 2, 1, 1),
('C:\\Users\\P3dR0\\Documents\\IGA\\_Projets\\Architecture CS\\FileManager\\FileManager_Server\\ServerStorage\\badr_22', 1, 2, 1, 1),
('C:\\Users\\P3dR0\\Documents\\IGA\\_Projets\\Architecture CS\\FileManager\\FileManager_Server\\ServerStorage\\yasir_22', 1, 2, 1, 1),
('C:\\Users\\P3dR0\\Documents\\IGA\\_Projets\\Architecture CS\\FileManager\\FileManager_Server\\ServerStorage\\soutnance', 1, 2, 1, 1),
('C:\\Users\\P3dR0\\Documents\\IGA\\_Projets\\Architecture CS\\FileManager\\FileManager_Server\\ServerStorage\\Abdeljalil', 1, 2, 1, 1),
('C:\\Users\\P3dR0\\Documents\\IGA\\_Projets\\Architecture CS\\FileManager\\FileManager_Server\\ServerStorage\\yasir_22', 1, 2, 1, 1),
('C:\\Users\\P3dR0\\Documents\\IGA\\_Projets\\Architecture CS\\FileManager\\FileManager_Server\\ServerStorage\\files.txt', 1, 2, 1, 1),
('C:\\Users\\P3dR0\\Documents\\IGA\\_Projets\\Architecture CS\\FileManager\\FileManager_Server\\ServerStorage\\Yassiro', 1, 2, 1, 1),
('C:\\Users\\P3dR0\\Documents\\IGA\\_Projets\\Architecture CS\\FileManager\\FileManager_Server\\ServerStorage\\yassirv5', 1, 2, 1, 1),
('C:\\Users\\P3dR0\\Documents\\IGA\\_Projets\\Architecture CS\\FileManager\\FileManager_Server\\ServerStorage\\yassirv5', 1, 1, 1, 1),
('C:\\Users\\P3dR0\\Documents\\IGA\\_Projets\\Architecture CS\\FileManager\\FileManager_Server\\ServerStorage\\badrbadr', 1, 2, 1, 1),
('C:\\Users\\P3dR0\\Documents\\IGA\\_Projets\\Architecture CS\\FileManager\\FileManager_Server\\ServerStorage\\badrbadr', 1, 1, 1, 1),
('C:\\Users\\P3dR0\\Documents\\IGA\\_Projets\\Architecture CS\\FileManager\\FileManager_Server\\ServerStorage\\badrbadr', 1, 1, 1, 1),
('C:\\Users\\P3dR0\\Documents\\IGA\\_Projets\\Architecture CS\\FileManager\\FileManager\\FileManager_Server\\ServerStorage\\Abdeljalil', 16, 1, 1, 1),
('C:\\Users\\P3dR0\\Documents\\IGA\\_Projets\\Architecture CS\\FileManager\\FileManager\\FileManager_Server\\ServerStorage\\badrbadr', 16, 1, 1, 1),
('C:\\Users\\P3dR0\\Documents\\IGA\\_Projets\\Architecture CS\\FileManager\\FileManager\\FileManager_Server\\ServerStorage\\badr_22', 16, 1, 1, 1),
('C:\\Users\\P3dR0\\Documents\\IGA\\_Projets\\Architecture CS\\FileManager\\FileManager\\FileManager_Server\\ServerStorage\\fatiha', 16, 1, 1, 1),
('C:\\Users\\P3dR0\\Documents\\IGA\\_Projets\\Architecture CS\\FileManager\\FileManager\\FileManager_Server\\ServerStorage\\files.txt', 16, 1, 1, 1),
('C:\\Users\\P3dR0\\Documents\\IGA\\_Projets\\Architecture CS\\FileManager\\FileManager\\FileManager_Server\\ServerStorage\\hakim', 16, 1, 1, 1),
('C:\\Users\\P3dR0\\Documents\\IGA\\_Projets\\Architecture CS\\FileManager\\FileManager\\FileManager_Server\\ServerStorage\\johno', 16, 1, 1, 1),
('C:\\Users\\P3dR0\\Documents\\IGA\\_Projets\\Architecture CS\\FileManager\\FileManager\\FileManager_Server\\ServerStorage\\soutnance', 16, 1, 1, 1),
('C:\\Users\\P3dR0\\Documents\\IGA\\_Projets\\Architecture CS\\FileManager\\FileManager\\FileManager_Server\\ServerStorage\\Yassiro', 16, 1, 1, 1),
('C:\\Users\\P3dR0\\Documents\\IGA\\_Projets\\Architecture CS\\FileManager\\FileManager\\FileManager_Server\\ServerStorage\\yassirv5', 16, 1, 1, 1);

-- --------------------------------------------------------

--
-- Table structure for table `utilisateur`
--

CREATE TABLE `utilisateur` (
  `id` int(11) NOT NULL,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Dumping data for table `utilisateur`
--

INSERT INTO `utilisateur` (`id`, `username`, `password`) VALUES
(1, 'test', 'test'),
(2, 'yassir', 'yassir');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `dossier`
--
ALTER TABLE `dossier`
  ADD PRIMARY KEY (`id_folder`);

--
-- Indexes for table `fichier`
--
ALTER TABLE `fichier`
  ADD KEY `fichier_ibfk_1` (`id`),
  ADD KEY `fichier_ibfk_2` (`id_folder`);

--
-- Indexes for table `utilisateur`
--
ALTER TABLE `utilisateur`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `dossier`
--
ALTER TABLE `dossier`
  MODIFY `id_folder` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- AUTO_INCREMENT for table `utilisateur`
--
ALTER TABLE `utilisateur`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `fichier`
--
ALTER TABLE `fichier`
  ADD CONSTRAINT `fichier_ibfk_1` FOREIGN KEY (`id`) REFERENCES `utilisateur` (`id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
