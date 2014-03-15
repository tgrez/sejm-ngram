USE sejmngram;

DROP TABLE IF EXISTS ngrams;

CREATE TABLE ngrams (
	id INT NOT NULL AUTO_INCREMENT,
	datefrom DATETIME,
	dateto DATETIME,
	ngram VARCHAR(256),
	nrOccurences INT,
	content BLOB,
	PRIMARY KEY (id)

);
