-- phpMyAdmin SQL Dump
-- version 4.0.10deb1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Oct 25, 2014 at 09:08 PM
-- Server version: 5.5.38-0ubuntu0.14.04.1
-- PHP Version: 5.5.9-1ubuntu4.3

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `meeto`
--

-- --------------------------------------------------------

--
-- Table structure for table `action`
--

CREATE TABLE IF NOT EXISTS `action` (
  `idaction` int(11) NOT NULL AUTO_INCREMENT,
  `description` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
  `due_to` datetime NOT NULL,
  `assigned_user` int(11) NOT NULL,
  `done` tinyint(4) NOT NULL DEFAULT '0',
  `meeting` int(11) NOT NULL,
  `created_datetime` datetime NOT NULL,
  `active` tinyint(4) NOT NULL DEFAULT '1',
  PRIMARY KEY (`idaction`),
  KEY `fk_action_1_idx` (`assigned_user`),
  KEY `fk_action_2_idx` (`meeting`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=2 ;

--
-- Dumping data for table `action`
--

INSERT INTO `action` (`idaction`, `description`, `due_to`, `assigned_user`, `done`, `meeting`, `created_datetime`, `active`) VALUES
(1, 'descricao da acçao', '2015-02-12 00:00:00', 6, 0, 1, '2014-10-16 16:27:55', 0);

-- --------------------------------------------------------

--
-- Table structure for table `comment`
--

CREATE TABLE IF NOT EXISTS `comment` (
  `idcomment` int(11) NOT NULL AUTO_INCREMENT,
  `comment` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `user` int(11) NOT NULL,
  `created_datetime` datetime NOT NULL,
  `item` int(11) NOT NULL,
  `active` tinyint(4) NOT NULL DEFAULT '1',
  PRIMARY KEY (`idcomment`),
  KEY `fk_comment_1_idx` (`user`),
  KEY `fk_comment_2_idx` (`item`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=2 ;

--
-- Dumping data for table `comment`
--

INSERT INTO `comment` (`idcomment`, `comment`, `user`, `created_datetime`, `item`, `active`) VALUES
(1, 'comentario item 2', 3, '2014-10-15 00:00:00', 2, 1);

-- --------------------------------------------------------

--
-- Table structure for table `group_def`
--

CREATE TABLE IF NOT EXISTS `group_def` (
  `idgroup` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
  `created_datetime` datetime NOT NULL,
  `active` tinyint(4) NOT NULL DEFAULT '1',
  PRIMARY KEY (`idgroup`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=2 ;

--
-- Dumping data for table `group_def`
--

INSERT INTO `group_def` (`idgroup`, `name`, `created_datetime`, `active`) VALUES
(1, 'Grupo Totil', '2014-10-16 16:17:16', 1);

-- --------------------------------------------------------

--
-- Table structure for table `group_user`
--

CREATE TABLE IF NOT EXISTS `group_user` (
  `group_def` int(11) NOT NULL,
  `user` int(11) NOT NULL,
  PRIMARY KEY (`group_def`,`user`),
  KEY `fk_group_user_2_idx` (`user`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `group_user`
--

INSERT INTO `group_user` (`group_def`, `user`) VALUES
(1, 2),
(1, 4),
(1, 6);

-- --------------------------------------------------------

--
-- Table structure for table `item`
--

CREATE TABLE IF NOT EXISTS `item` (
  `iditem` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
  `description` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `user` int(11) NOT NULL,
  `created_datetime` datetime NOT NULL,
  `meeting` int(11) NOT NULL,
  `active` tinyint(4) NOT NULL DEFAULT '1',
  PRIMARY KEY (`iditem`),
  KEY `fk_item_1_idx` (`user`),
  KEY `fk_item_2_idx` (`meeting`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=5 ;

--
-- Dumping data for table `item`
--

INSERT INTO `item` (`iditem`, `title`, `description`, `user`, `created_datetime`, `meeting`, `active`) VALUES
(1, 'Item 1', 'descrição do item 1', 4, '2014-10-16 16:19:22', 1, 1),
(2, 'Item 2', 'Descrição item 2', 5, '2014-10-16 16:19:22', 1, 1),
(3, 'titulo item', 'descricaooooo', 1, '2014-10-23 04:24:45', 1, 1),
(4, 'titulo item', 'descricaooooo', 1, '2014-10-23 04:26:28', 1, 1);

-- --------------------------------------------------------

--
-- Table structure for table `keydecision`
--

CREATE TABLE IF NOT EXISTS `keydecision` (
  `idkeydecision` int(11) NOT NULL AUTO_INCREMENT,
  `description` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `item` int(11) NOT NULL,
  `created_datetime` datetime NOT NULL,
  `active` tinyint(4) NOT NULL DEFAULT '1',
  PRIMARY KEY (`idkeydecision`),
  KEY `fk_keydecision_1_idx` (`item`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=3 ;

--
-- Dumping data for table `keydecision`
--

INSERT INTO `keydecision` (`idkeydecision`, `description`, `item`, `created_datetime`, `active`) VALUES
(1, 'descricao keydecision', 1, '2014-10-16 16:28:20', 1),
(2, 'dsjfjdsf', 2, '0000-00-00 00:00:00', 0);

-- --------------------------------------------------------

--
-- Table structure for table `meeting`
--

CREATE TABLE IF NOT EXISTS `meeting` (
  `idmeeting` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
  `description` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `datetime` datetime NOT NULL,
  `location` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
  `leader` int(11) NOT NULL,
  `finished` tinyint(4) NOT NULL DEFAULT '0',
  `created_datetime` datetime NOT NULL,
  `active` tinyint(4) NOT NULL DEFAULT '1',
  PRIMARY KEY (`idmeeting`),
  KEY `fk_meeting_1_idx` (`leader`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=6 ;

--
-- Dumping data for table `meeting`
--

INSERT INTO `meeting` (`idmeeting`, `title`, `description`, `datetime`, `location`, `leader`, `finished`, `created_datetime`, `active`) VALUES
(1, 'Reunião A', 'descricao da reuniao a', '2014-12-10 00:00:00', 'DEI', 3, 0, '2014-10-14 00:00:00', 1),
(2, 'hjjhgh', 'jhgjhghg', '2014-10-16 00:00:00', 'dei', 4, 0, '2014-10-16 17:38:54', 1),
(3, 'asd', 'asdad', '2014-03-03 00:00:00', 'coimbra', 1, 0, '2014-10-20 21:47:00', 1),
(4, 'asd', 'asdad', '2014-03-03 00:00:00', 'coimbra', 1, 0, '2014-10-20 21:54:00', 1),
(5, 'Reuniao y', 'descricao da reuniao y', '2014-12-23 07:03:00', 'DEI', 1, 1, '2014-10-22 01:07:58', 1);

-- --------------------------------------------------------

--
-- Table structure for table `meeting_group`
--

CREATE TABLE IF NOT EXISTS `meeting_group` (
  `meeting` int(11) NOT NULL,
  `group_def` int(11) NOT NULL,
  PRIMARY KEY (`meeting`,`group_def`),
  KEY `fk_meeting_group_2_idx` (`group_def`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `meeting_group`
--

INSERT INTO `meeting_group` (`meeting`, `group_def`) VALUES
(1, 1);

-- --------------------------------------------------------

--
-- Table structure for table `meeting_user`
--

CREATE TABLE IF NOT EXISTS `meeting_user` (
  `meeting` int(11) NOT NULL,
  `user` int(11) NOT NULL,
  PRIMARY KEY (`meeting`,`user`),
  KEY `fk_meeting_user_2_idx` (`user`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `meeting_user`
--

INSERT INTO `meeting_user` (`meeting`, `user`) VALUES
(1, 2),
(1, 3),
(1, 4),
(1, 6);

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE IF NOT EXISTS `user` (
  `iduser` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
  `password` varchar(150) COLLATE utf8_unicode_ci NOT NULL,
  `created_datetime` datetime NOT NULL,
  `active` tinyint(4) NOT NULL DEFAULT '1',
  PRIMARY KEY (`iduser`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=7 ;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`iduser`, `username`, `password`, `created_datetime`, `active`) VALUES
(1, 'ze', 'ze', '2014-10-16 16:10:43', 1),
(2, 'manel', 'manel', '2014-10-16 16:10:43', 1),
(3, 'jaquim', 'jaquim', '2014-10-16 16:11:15', 1),
(4, 'egas', 'egas', '2014-10-16 16:11:15', 1),
(5, 'becas', 'becas', '2014-10-16 16:12:42', 1),
(6, 'leopoldina', 'leopoldina', '2014-10-16 16:12:42', 1);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `action`
--
ALTER TABLE `action`
  ADD CONSTRAINT `fk_action_1` FOREIGN KEY (`assigned_user`) REFERENCES `user` (`iduser`) ON DELETE NO ACTION ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_action_2` FOREIGN KEY (`meeting`) REFERENCES `meeting` (`idmeeting`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `comment`
--
ALTER TABLE `comment`
  ADD CONSTRAINT `fk_comment_1` FOREIGN KEY (`user`) REFERENCES `user` (`iduser`) ON DELETE NO ACTION ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_comment_2` FOREIGN KEY (`item`) REFERENCES `item` (`iditem`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `group_user`
--
ALTER TABLE `group_user`
  ADD CONSTRAINT `fk_group_user_1` FOREIGN KEY (`group_def`) REFERENCES `group_def` (`idgroup`) ON DELETE NO ACTION ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_group_user_2` FOREIGN KEY (`user`) REFERENCES `user` (`iduser`) ON DELETE NO ACTION ON UPDATE CASCADE;

--
-- Constraints for table `item`
--
ALTER TABLE `item`
  ADD CONSTRAINT `fk_item_1` FOREIGN KEY (`user`) REFERENCES `user` (`iduser`) ON DELETE NO ACTION ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_item_2` FOREIGN KEY (`meeting`) REFERENCES `meeting` (`idmeeting`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `keydecision`
--
ALTER TABLE `keydecision`
  ADD CONSTRAINT `fk_keydecision_1` FOREIGN KEY (`item`) REFERENCES `item` (`iditem`) ON DELETE NO ACTION ON UPDATE CASCADE;

--
-- Constraints for table `meeting`
--
ALTER TABLE `meeting`
  ADD CONSTRAINT `fk_meeting_1` FOREIGN KEY (`leader`) REFERENCES `user` (`iduser`) ON DELETE NO ACTION ON UPDATE CASCADE;

--
-- Constraints for table `meeting_group`
--
ALTER TABLE `meeting_group`
  ADD CONSTRAINT `fk_meeting_group_2` FOREIGN KEY (`group_def`) REFERENCES `group_def` (`idgroup`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_meeting_group_1` FOREIGN KEY (`meeting`) REFERENCES `meeting` (`idmeeting`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `meeting_user`
--
ALTER TABLE `meeting_user`
  ADD CONSTRAINT `fk_meeting_user_1` FOREIGN KEY (`meeting`) REFERENCES `meeting` (`idmeeting`) ON DELETE NO ACTION ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_meeting_user_2` FOREIGN KEY (`user`) REFERENCES `user` (`iduser`) ON DELETE NO ACTION ON UPDATE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
