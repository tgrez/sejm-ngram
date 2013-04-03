/*----------------------------------*/
/* filterout ngrams with count == 1 */
/*----------------------------------*/



USE `wystapienia`;


/* -- preselection of ngrams ids with count > 1*/
DROP TABLE  IF EXISTS `ngrams_id_filtered`;
CREATE TABLE `ngrams_id_filtered` (
    `ngram_id`           INT NOT NULL,
    PRIMARY KEY(ngram_id)
);
insert `ngrams_id_filtered` (`ngram_id`)
select `ngram_id` from `ngrams` group by `ngram_id` having count(*)>1;


/* -- filtered out occurrences */
DROP TABLE  IF EXISTS `ngrams_filtered`;
CREATE TABLE `ngrams_filtered` (
    `id`                INT NOT NULL,

	`ngram_id`		    INT NOT NULL,  /*--corresponding id of entry from ngram_dictionary*/
	`posel_id` 		    INT default 0, /*--corresponding id of entry from posel_dictionary*/

	`klub_id` 	        INT default 0, /*--copied from sejm_wystapienia*/
	`wystapienie_id` 	INT default 0, /*--copied from sejm_wystapienia*/
	`data` 			    DATE NOT NULL, /*--copied from sejm_wystapienia*/   

	PRIMARY KEY(id)
);
ALTER TABLE `ngrams_filtered` ADD INDEX (`data`);
ALTER TABLE `ngrams_filtered` ADD INDEX (`klub_id`);
ALTER TABLE `ngrams_filtered` ADD INDEX (`ngram_id`);

/* -- fill-in with selected records*/
insert `ngrams_filtered` (`id`, `ngram_id`, `posel_id`, `klub_id`, `wystapienie_id`, `data`)
select `id`, ngrams.ngram_id, `posel_id`, `klub_id`, `wystapienie_id`, `data` 
from `ngrams` inner join `ngrams_id_filtered` 
where ngrams.ngram_id = ngrams_id_filtered.ngram_id


/* -- TODO filtered out ngram_dictionary */


