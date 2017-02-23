-- phpMyAdmin SQL Dump
-- version 4.5.2
-- http://www.phpmyadmin.net
--
-- Client :  127.0.0.1
-- Généré le :  Jeu 23 Février 2017 à 13:21
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
-- Structure de la table `achivedobjectives`
--

DROP TABLE IF EXISTS `achivedobjectives`;
CREATE TABLE IF NOT EXISTS `achivedobjectives` (
  `idUser` int(11) NOT NULL,
  `idObjective` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `clue`
--

DROP TABLE IF EXISTS `clue`;
CREATE TABLE IF NOT EXISTS `clue` (
  `idClue` int(11) NOT NULL AUTO_INCREMENT,
  `description` text NOT NULL,
  `image` blob NOT NULL,
  PRIMARY KEY (`idClue`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `objective`
--

DROP TABLE IF EXISTS `objective`;
CREATE TABLE IF NOT EXISTS `objective` (
  `idObjective` int(11) NOT NULL AUTO_INCREMENT,
  `idClue` int(11) NOT NULL,
  `longitude` float NOT NULL,
  `latitude` float NOT NULL,
  `radius` float NOT NULL,
  `textAfterDiscovery` text NOT NULL,
  `reward` int(11) NOT NULL,
  PRIMARY KEY (`idObjective`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

--
-- Contenu de la table `objective`
--

INSERT INTO `objective` (`idObjective`, `idClue`, `longitude`, `latitude`, `radius`, `textAfterDiscovery`, `reward`) VALUES
(1, 1, 0.691029, 47.3945, 100, '', 2);

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
  PRIMARY KEY (`idUser`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
