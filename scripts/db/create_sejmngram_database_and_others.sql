-- CREATES DATABASE, TABLE wystapienia and PROCEDURES/FUNCTIONS FOR SEARCHING THROUGH FTS SEARCHS

--  CREATE DATABASE AND USERS
DROP DATABASE IF EXISTS sejmngram;

CREATE DATABASE sejmngram CHARACTER SET utf8 COLLATE utf8_general_ci;

USE sejmngram;

GRANT USAGE ON *.* TO 'db-inserter'@'localhost' IDENTIFIED BY 'sejmngram';
GRANT USAGE on *.* TO 'db-fetcher'@'localhost' IDENTIFIED BY 'sejmngram2';

GRANT ALL PRIVILEGES ON  sejmngram.* TO 'db-inserter'@'localhost';
GRANT SELECT ON sejmngram.* TO 'db-fetcher'@'localhost';


-- CREATE TABLE WYSTAPIENIA WITH FTS
DROP TABLE IF EXISTS wystapienia;

CREATE TABLE wystapienia (
	id INT NOT NULL AUTO_INCREMENT,
	jsonId VARCHAR(15),
	date DATETIME,
	partyId VARCHAR(10),
	deputyId VARCHAR(10),
	text TEXT,
	textNormalized TEXT,
	PRIMARY KEY (id)

)  ENGINE = MYISAM;


-- APPLY FULL TEXT SEARCH
ALTER  TABLE wystapienia ADD FULLTEXT INDEX idx_text (textNormalized);




-- CREATE FUNCTION term count
DROP function IF EXISTS `term_count`;

DELIMITER $$
USE `sejmngram`$$
CREATE FUNCTION `term_count` (str TEXT, term TEXT)
RETURNS INTEGER
BEGIN
	RETURN (LENGTH(str) - LENGTH(REPLACE(str, term, '')))/LENGTH(term);
END$$

DELIMITER ;
GRANT EXECUTE ON FUNCTION `term_count` TO `db-fetcher`@`localhost`;

