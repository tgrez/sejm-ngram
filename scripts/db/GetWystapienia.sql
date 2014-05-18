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

