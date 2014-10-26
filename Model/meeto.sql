-- phpMyAdmin SQL Dump
-- version 4.0.10deb1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Oct 26, 2014 at 07:00 PM
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
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=4 ;

--
-- Dumping data for table `group_def`
--

INSERT INTO `group_def` (`idgroup`, `name`, `created_datetime`, `active`) VALUES
(2, 'Grupo ABC', '2014-10-09 00:00:00', 1),
(3, 'Grupo XYZ', '2014-10-22 00:00:00', 1);

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
(2, 7),
(2, 8),
(3, 9);

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
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=8 ;

--
-- Dumping data for table `meeting`
--

INSERT INTO `meeting` (`idmeeting`, `title`, `description`, `datetime`, `location`, `leader`, `finished`, `created_datetime`, `active`) VALUES
(6, 'Reuniao A', 'descricao A', '2014-10-20 00:00:00', 'Coimbra', 7, 1, '2014-10-17 00:00:00', 1),
(7, 'Reuniao B', 'descricao B', '2014-10-31 00:00:00', 'DEI', 8, 0, '2014-10-25 00:00:00', 1);

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
(6, 2);

-- --------------------------------------------------------

--
-- Table structure for table `meeting_user`
--

CREATE TABLE IF NOT EXISTS `meeting_user` (
  `meeting` int(11) NOT NULL,
  `user` int(11) NOT NULL,
  `group_def` int(11) DEFAULT NULL,
  PRIMARY KEY (`meeting`,`user`),
  KEY `fk_meeting_user_2_idx` (`user`),
  KEY `fk_meeting_user_3_idx` (`group_def`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `meeting_user`
--

INSERT INTO `meeting_user` (`meeting`, `user`, `group_def`) VALUES
(6, 7, NULL),
(6, 8, 2);

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
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=11 ;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`iduser`, `username`, `password`, `created_datetime`, `active`) VALUES
(7, 'ze', 'ze', '2014-09-01 00:00:00', 1),
(8, 'manel', 'manel', '2014-10-01 00:00:00', 1),
(9, 'leopoldina', 'leopoldina', '2014-10-08 00:00:00', 1),
(10, 'popota', 'popota', '2014-10-09 00:00:00', 1);

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
  ADD CONSTRAINT `fk_meeting_group_1` FOREIGN KEY (`meeting`) REFERENCES `meeting` (`idmeeting`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_meeting_group_2` FOREIGN KEY (`group_def`) REFERENCES `group_def` (`idgroup`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `meeting_user`
--
ALTER TABLE `meeting_user`
  ADD CONSTRAINT `fk_meeting_user_3` FOREIGN KEY (`group_def`) REFERENCES `group_def` (`idgroup`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_meeting_user_1` FOREIGN KEY (`meeting`) REFERENCES `meeting` (`idmeeting`) ON DELETE NO ACTION ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_meeting_user_2` FOREIGN KEY (`user`) REFERENCES `user` (`iduser`) ON DELETE NO ACTION ON UPDATE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
