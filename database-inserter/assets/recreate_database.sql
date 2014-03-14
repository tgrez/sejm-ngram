DROP DATABASE IF EXISTS sejmngram;

CREATE DATABASE sejmngram;

GRANT USAGE ON *.* TO 'db-inserter'@'localhost' IDENTIFIED BY 'sejmngram';
GRANT USAGE on *.* TO 'db-fetcher'@'localhost' IDENTIFIED BY 'sejmngram2';

GRANT ALL PRIVILEGES ON sejmngram.* TO 'db-inserter'@'localhost';
GRANT SELECT ON sejmngram.* TO 'db-fetcher'@'localhost';
