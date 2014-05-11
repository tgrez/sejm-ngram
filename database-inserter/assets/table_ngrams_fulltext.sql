USE sejmngram;

DROP TABLE IF EXISTS wystapienia;

CREATE TABLE wystapienia (
	id INT NOT NULL AUTO_INCREMENT,
	jsonId BIGINT,
	date DATETIME,
	partyId INT,
	deputyId INT,
	text TEXT,
	PRIMARY KEY (id)

)  ENGINE = MYISAM;

ALTER TABLE sejmngram.wystapienia ADD textNormalized TEXT;

/* ALTER  TABLE wystapienia ADD FULLTEXT INDEX idx_text (textNormalized); */
