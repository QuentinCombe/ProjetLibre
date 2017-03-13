-- phpMyAdmin SQL Dump
-- version 4.5.2
-- http://www.phpmyadmin.net
--
-- Client :  127.0.0.1
-- Généré le :  Lun 13 Mars 2017 à 15:45
-- Version du serveur :  5.7.9
-- Version de PHP :  5.6.16

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données :  `projetlibre`
--

-- --------------------------------------------------------

--
-- Structure de la table `achievedobjectives`
--

DROP TABLE IF EXISTS `achievedobjectives`;
CREATE TABLE IF NOT EXISTS `achievedobjectives` (
  `idUser` int(11) NOT NULL,
  `idObjective` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Contenu de la table `achievedobjectives`
--

INSERT INTO `achievedobjectives` (`idUser`, `idObjective`) VALUES
(3, 1),
(3, 1),
(3, 2),
(3, 3);

-- --------------------------------------------------------

--
-- Structure de la table `clue`
--

DROP TABLE IF EXISTS `clue`;
CREATE TABLE IF NOT EXISTS `clue` (
  `idClue` int(11) NOT NULL AUTO_INCREMENT,
  `description` text NOT NULL,
  `image` tinytext NOT NULL,
  PRIMARY KEY (`idClue`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

--
-- Contenu de la table `clue`
--

INSERT INTO `clue` (`idClue`, `description`, `image`) VALUES
(1, '', '1'),
(2, '', '2'),
(3, '', '3');

-- --------------------------------------------------------

--
-- Structure de la table `objective`
--

DROP TABLE IF EXISTS `objective`;
CREATE TABLE IF NOT EXISTS `objective` (
  `idObjective` int(11) NOT NULL AUTO_INCREMENT,
  `idClue` int(11) NOT NULL,
  `latitude` float NOT NULL,
  `longitude` float NOT NULL,
  `radius` float NOT NULL,
  `textAfterDiscovery` text NOT NULL,
  `reward` int(11) NOT NULL,
  PRIMARY KEY (`idObjective`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

--
-- Contenu de la table `objective`
--

INSERT INTO `objective` (`idObjective`, `idClue`, `latitude`, `longitude`, `radius`, `textAfterDiscovery`, `reward`) VALUES
(1, 1, 47.3945, 0.691029, 300, 'Rue de la scellerie', 2),
(2, 2, 47.3647, 0.684771, 100, 'Polytech', 1),
(3, 3, 47.3911, 0.687374, 120, 'Rue Etienne Palu', 6);

-- --------------------------------------------------------

--
-- Structure de la table `objectivesinquest`
--

DROP TABLE IF EXISTS `objectivesinquest`;
CREATE TABLE IF NOT EXISTS `objectivesinquest` (
  `idQuest` int(11) NOT NULL,
  `idObjective` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `quest`
--

DROP TABLE IF EXISTS `quest`;
CREATE TABLE IF NOT EXISTS `quest` (
  `idQuest` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(256) NOT NULL,
  `description` text NOT NULL,
  `reward` int(11) NOT NULL,
  PRIMARY KEY (`idQuest`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `user`
--

DROP TABLE IF EXISTS `user`;
CREATE TABLE IF NOT EXISTS `user` (
  `idUser` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(32) NOT NULL,
  `mail` varchar(64) NOT NULL,
  `password` varchar(256) NOT NULL,
  `exp` int(11) NOT NULL,
  `level` int(11) NOT NULL,
  `image` text NOT NULL,
  PRIMARY KEY (`idUser`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

--
-- Contenu de la table `user`
--

INSERT INTO `user` (`idUser`, `username`, `mail`, `password`, `exp`, `level`, `image`) VALUES
(1, 'toto', 'toto@toto.com', 'passwordToto123', 0, 1, ''),
(2, 'quentin', 'quentin@toto.com', 'passwordQuentin123', 1500, 3, ''),
(3, 'caca', 'caca@caca.com', 'prout', 1, 1, '');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
