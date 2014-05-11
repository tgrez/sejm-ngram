USE `sejmngram`;
DROP procedure IF EXISTS `GetWystapienia`;

DELIMITER $$
USE `sejmngram`$$
CREATE DEFINER=`lisu`@`%` PROCEDURE `GetWystapienia`(term TEXT)
BEGIN
	SELECT date, term_count(text, term) AS COUNT FROM wystapienia WHERE MATCH (text) AGAINST ( contact('"', term, '"') IN BOOLEAN MODE);
END$$

DELIMITER ;

