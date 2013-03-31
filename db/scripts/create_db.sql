DROP DATABASE IF EXISTS `wystapienia`;

CREATE DATABASE `wystapienia` DEFAULT CHARACTER SET utf8;

USE `wystapienia`;

GRANT ALL ON wystapienia.* TO 'testuser'@'localhost';

######################################################################################################

#Storage for data downloaded from http://sejmometr.pl/sejm_wystapienia/id where id 1..
CREATE TABLE `html_wystapienia` (
    `id` INT NOT NULL,
	`posel` 		    VARCHAR(64),
	`stanowisko`		VARCHAR(128),
    `tekst`             TEXT,
    `status`            INT default 0,
	PRIMARY KEY(id)
);

#Storage for table from http://sejmometr.pl/api/docs/ep_Sejm_Wystapienie (dataset sejm_wystapienia)
CREATE TABLE `sejm_wystapienia` (
 `id` INT NOT NULL,
 `data` DATE,
 `posiedzenie_id` INT,
 `czlowiek_id` INT,
 `debata_id` INT,
 `dzien_sejmowy_id` INT,
 `ilosc_slow` INT,
 `klub_id` INT,
 `kolejnosc` INT(20),
 `punkt_id` INT,
 `skrot` TEXT,
 `stanowisko_id` INT,
 `video` INT,
 `tytul` VARCHAR(128),
 `status` INT default 0,
 PRIMARY KEY(id)
);


######################################################################################################

DROP TABLE `ngrams`;
CREATE TABLE `ngrams` (
    `id`                INT NOT NULL AUTO_INCREMENT,

	`ngram_id`		    INT NOT NULL,  #corresponding id of entry from ngram_dictionary
	`posel_id` 		    INT default 0, #corresponding id of entry from posel_dictionary

	`klub_id` 	        INT default 0, #copied from sejm_wystapienia
	`wystapienie_id` 	INT default 0, #copied from sejm_wystapienia
	`data` 			    DATE NOT NULL, #copied from sejm_wystapienia
    
    ##HERE YOU CAN ADD ANY COLUMNS EITHER FROM sejm_wystapienia OR html_wystapienia 
    ##AND IT WILL BE AUTOMATICALLY COPIED e.g.:
    #`tytul` VARCHAR(128),
    #`debata_id` INT,

	PRIMARY KEY(id)
);
ALTER TABLE `ngrams` ADD INDEX (`data`);
ALTER TABLE `ngrams` ADD INDEX (`klub_id`);


#Dictionary {id: posel-name}
DROP TABLE `posel_dictionary`;
CREATE TABLE `posel_dictionary` (
    `id`             INT NOT NULL, 
    `posel` 		 VARCHAR(64),
	PRIMARY KEY(id)
);
INSERT INTO `posel_dictionary` (`id`,`posel`) VALUES (0, "UNKNOWN");


#Dictionary {id: ngram}
DROP TABLE `ngram_dictionary`;
CREATE TABLE `ngram_dictionary` (
    `id`             INT NOT NULL, 
    `ngram` 		 VARCHAR(256),
	PRIMARY KEY(id)
);
INSERT INTO `ngram_dictionary` (`id`,`ngram`) VALUES (0, "UNKNOWN");
#ALTER TABLE `ngram_dictionary` ADD UNIQUE INDEX (`ngram`); #TODO


#Dictionary {id: klub}
DROP TABLE `klub_dictionary`;
CREATE TABLE `klub_dictionary` (
    `id`             INT NOT NULL, 
    `klub` 		     VARCHAR(128),
	PRIMARY KEY(id)
);
INSERT INTO `klub_dictionary` (`id`,`klub`) VALUES (0, "UNKNOWN");


