USE sejmngram;

DROP TABLE IF EXISTS ngramsTS;


CREATE TABLE ngramsTSBatch (
	id INT NOT NULL AUTO_INCREMENT,
	data DATETIME,
	wystapienieID VARCHAR(30),
  partiaID INT,
  poselID INT,
  tresc TEXT,
  tytul TEXT,
	PRIMARY KEY (id)
);
