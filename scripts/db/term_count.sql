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

