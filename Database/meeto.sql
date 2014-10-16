-- phpMyAdmin SQL Dump
-- version 4.0.10deb1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Oct 16, 2014 at 04:57 PM
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
  `created_datetime` datetime NOT NULL,
  `active` tinyint(4) NOT NULL DEFAULT '1',
  PRIMARY KEY (`idaction`),
  KEY `fk_action_1_idx` (`assigned_user`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `comment`
--

CREATE TABLE IF NOT EXISTS `comment` (
  `idcomment` int(11) NOT NULL AUTO_INCREMENT,
  `comment` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `user` int(11) NOT NULL,
  `created_datetime` datetime NOT NULL,
  `active` tinyint(4) NOT NULL DEFAULT '1',
  PRIMARY KEY (`idcomment`),
  KEY `fk_comment_1_idx` (`user`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=1 ;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `group_user`
--

CREATE TABLE IF NOT EXISTS `group_user` (
  `group` int(11) NOT NULL,
  `user` int(11) NOT NULL,
  PRIMARY KEY (`group`,`user`),
  KEY `fk_group_user_2_idx` (`user`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=1 ;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=1 ;

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
  `created_datetime` datetime NOT NULL,
  `active` tinyint(4) NOT NULL DEFAULT '1',
  PRIMARY KEY (`idmeeting`),
  KEY `fk_meeting_1_idx` (`leader`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `meeting_group`
--

CREATE TABLE IF NOT EXISTS `meeting_group` (
  `meeting` int(11) NOT NULL,
  `group` int(11) NOT NULL,
  PRIMARY KEY (`meeting`,`group`),
  KEY `fk_meeting_group_2_idx` (`group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=1 ;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `action`
--
ALTER TABLE `action`
  ADD CONSTRAINT `fk_action_1` FOREIGN KEY (`assigned_user`) REFERENCES `user` (`iduser`) ON DELETE NO ACTION ON UPDATE CASCADE;

--
-- Constraints for table `comment`
--
ALTER TABLE `comment`
  ADD CONSTRAINT `fk_comment_1` FOREIGN KEY (`user`) REFERENCES `user` (`iduser`) ON DELETE NO ACTION ON UPDATE CASCADE;

--
-- Constraints for table `group_user`
--
ALTER TABLE `group_user`
  ADD CONSTRAINT `fk_group_user_1` FOREIGN KEY (`group`) REFERENCES `group_def` (`idgroup`) ON DELETE NO ACTION ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_group_user_2` FOREIGN KEY (`user`) REFERENCES `user` (`iduser`) ON DELETE NO ACTION ON UPDATE CASCADE;

--
-- Constraints for table `item`
--
ALTER TABLE `item`
  ADD CONSTRAINT `fk_item_2` FOREIGN KEY (`meeting`) REFERENCES `meeting` (`idmeeting`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_item_1` FOREIGN KEY (`user`) REFERENCES `user` (`iduser`) ON DELETE NO ACTION ON UPDATE CASCADE;

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
  ADD CONSTRAINT `fk_meeting_group_2` FOREIGN KEY (`group`) REFERENCES `group_def` (`idgroup`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `meeting_user`
--
ALTER TABLE `meeting_user`
  ADD CONSTRAINT `fk_meeting_user_1` FOREIGN KEY (`meeting`) REFERENCES `meeting` (`idmeeting`) ON DELETE NO ACTION ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_meeting_user_2` FOREIGN KEY (`user`) REFERENCES `user` (`iduser`) ON DELETE NO ACTION ON UPDATE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
