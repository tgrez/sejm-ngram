DROP DATABASE IF EXISTS `wystapienia`;

CREATE DATABASE `wystapienia`;

USE `wystapienia`;

GRANT ALL ON wystapienia.* TO 'testuser'@'localhost';

CREATE TABLE `ngram` (
	`id`			    INT(10) NOT NULL AUTO_INCREMENT,
	`unigram`		    VARCHAR(20),
	`posel` 		    VARCHAR(40),
	`data` 			    DATE,
    `partia`            VARCHAR(10),
	`stanowisko`		VARCHAR(50),
	`wystapienie_id` 	INT(10),
	PRIMARY KEY(id)
	);
