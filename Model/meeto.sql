-- phpMyAdmin SQL Dump
-- version 4.0.10deb1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Oct 16, 2014 at 05:29 PM
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

--
-- Dumping data for table `action`
--

INSERT INTO `action` (`idaction`, `description`, `due_to`, `assigned_user`, `done`, `created_datetime`, `active`) VALUES
(1, 'descricao da acçao', '2015-02-12 00:00:00', 6, 0, '2014-10-16 16:27:55', 1);

--
-- Dumping data for table `comment`
--

INSERT INTO `comment` (`idcomment`, `comment`, `user`, `created_datetime`, `item`, `active`) VALUES
(1, 'comentario item 2', 3, '2014-10-15 00:00:00', 2, 1);

--
-- Dumping data for table `group_def`
--

INSERT INTO `group_def` (`idgroup`, `name`, `created_datetime`, `active`) VALUES
(1, 'Grupo Totil', '2014-10-16 16:17:16', 1);

--
-- Dumping data for table `group_user`
--

INSERT INTO `group_user` (`group`, `user`) VALUES
(1, 2),
(1, 6);

--
-- Dumping data for table `item`
--

INSERT INTO `item` (`iditem`, `title`, `description`, `user`, `created_datetime`, `meeting`, `active`) VALUES
(1, 'Item 1', 'descrição do item 1', 4, '2014-10-16 16:19:22', 1, 1),
(2, 'Item 2', 'Descrição item 2', 5, '2014-10-16 16:19:22', 1, 1);

--
-- Dumping data for table `keydecision`
--

INSERT INTO `keydecision` (`idkeydecision`, `description`, `item`, `created_datetime`, `active`) VALUES
(1, 'descricao keydecision', 1, '2014-10-16 16:28:20', 1);

--
-- Dumping data for table `meeting`
--

INSERT INTO `meeting` (`idmeeting`, `title`, `description`, `datetime`, `location`, `leader`, `created_datetime`, `active`) VALUES
(1, 'Reunião A', 'descricao da reuniao a', '2014-12-10 00:00:00', 'DEI', 3, '2014-10-14 00:00:00', 1);

--
-- Dumping data for table `meeting_group`
--

INSERT INTO `meeting_group` (`meeting`, `group`) VALUES
(1, 1);

--
-- Dumping data for table `meeting_user`
--

INSERT INTO `meeting_user` (`meeting`, `user`) VALUES
(1, 1),
(1, 3),
(1, 4),
(1, 5);

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

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
