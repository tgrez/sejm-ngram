USE `sejmngram`;
DROP procedure IF EXISTS `GetWystapienia`;

DELIMITER $$
USE `sejmngram`$$
CREATE DEFINER=`lisu`@`%` PROCEDURE `GetWystapienia`(term TEXT)
BEGIN
	SELECT date, SUM(term_count(text, term))
		FROM wystapienia WHERE MATCH (text) AGAINST ( concat('"', term, '"') IN BOOLEAN MODE)
		GROUP BY date;
END$$

DELIMITER ;

