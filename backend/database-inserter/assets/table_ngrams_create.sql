USE sejmngram;

DROP TABLE ngrams;

CREATE TABLE ngrams (
	id INT,
	datefrom DATETIME,
	dateto DATETIME,
	ngram VARCHAR(20),
	content TEXT
);