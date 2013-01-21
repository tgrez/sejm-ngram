DROP DATABASE IF EXISTS `wystapienia_test`;

CREATE DATABASE `wystapienia_test`;

USE `wystapienia_test`;

CREATE TABLE `ngram` (
	`id`			INT(10) NOT NULL AUTO_INCREMENT,
	`unigram`		VARCHAR(20),
	`posel` 		VARCHAR(40),
	`data` 			DATE,
	`partia`		VARCHAR(10),
	`wystapienie_id` 	INT(10),
	PRIMARY KEY(id)
	);
