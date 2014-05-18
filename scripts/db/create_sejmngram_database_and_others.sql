-- CREATES DATABASE, TABLE wystapienia and PROCEDURES/FUNCTIONS FOR SEARCHING THROUGH FTS SEARCHS

--  CREATE DATABASE AND USERS
DROP DATABASE IF EXISTS sejmngram;

CREATE DATABASE sejmngram CHARACTER SET utf8 COLLATE utf8_general_ci;

GRANT USAGE ON *.* TO 'db-inserter'@'localhost' IDENTIFIED BY 'sejmngram';
GRANT USAGE on *.* TO 'db-fetcher'@'localhost' IDENTIFIED BY 'sejmngram2';

GRANT ALL PRIVILEGES ON sejmngram.* TO 'db-inserter'@'localhost';
GRANT SELECT ON sejmngram.* TO 'db-fetcher'@'localhost';


-- CREATE TABLE WYSTAPIENIA WITH FTS
DROP TABLE IF EXISTS wystapienia;

CREATE TABLE wystapienia (
	id INT NOT NULL AUTO_INCREMENT,
	jsonId BIGINT,
	date DATETIME,
	partyId INT,
	deputyId INT,
	text TEXT,
	textNormalized TEXT,
	PRIMARY KEY (id)

)  ENGINE = MYISAM;


-- APPLY FULL TEXT SEARCH
ALTER  TABLE wystapienia ADD FULLTEXT INDEX idx_text (textNormalized);




-- CREATE FUNCTION term count
USE `sejmngram`;
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



-- CREATE PROCEDURE GET WYSTAPIENIA
USE `sejmngram`;
DROP procedure IF EXISTS `GetWystapienia`;

DELIMITER $$
USE `sejmngram`$$
CREATE DEFINER=`db-fetcher`@`%` PROCEDURE `GetWystapienia`(term TEXT)
BEGIN
	SELECT date, SUM(term_count(textNormalized, term)) AS count
		FROM wystapienia WHERE MATCH (textNormalized) AGAINST ( concat('"', term, '"') IN BOOLEAN MODE)
		GROUP BY date;
END$$

DELIMITER ;

GRANT EXECUTE ON PROCEDURE `GetWystapienia` TO `db-fetcher`@`localhost`;


