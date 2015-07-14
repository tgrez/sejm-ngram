# Sejmotrendy
[![Build Status](https://travis-ci.org/tgrez/sejm-ngram.png?branch=master)](https://travis-ci.org/tgrez/sejm-ngram)

Aplikacja webowa pozwalająca na wizualizację trendów używania słów i zwrotów w parlamencie. Dzięki niej, użytkownicy będą mogli sprawdzić, np. posłowie której partii na przestrzeni czasu najczęściej używali sformułowania “tego się nie da zrobić”.

[www.sejmotrendy.pl][0]

### Jak uruchomić projekt

1. Clone repo

  ```git clone https://github.com/tgrez/sejm-ngram.git```

2. Potrzebne będą zainstalowane:
  1. Java 1.7+ <br>
    zainstalowaną Javę weryfikujemy w ten sposób:
    ```java -version```
  2. Maven 3 <br>
  zainstalowanego Mavena weryfikujemy tak:
    ```mvn --version```
  3. Elasticsearch (lub MySQL w przypadku brancha MySQLowego - domyślny jest już teraz Elasticsearch)<br>
  Elasticsearcha trzeba ściągnąć ([download link][1]), rozpakować i uruchomić (```bin/elasticsearch```)

3. Budujemy aplikację

  w katalogu głównym ```{ścieżka do projektu}/sejm-ngram``` wykonujemy:
  ```
  mvn clean install
  ```

4. Uruchamiamy aplikację

  Jeśli Elasticsearch został już uruchomiony, to stawiamy serwer w katalogu ```{ścieżka do projektu}/sejm-ngram/rest-server```:
  ```
  ./run.sh
  ```

W przypadku pytań można pisać na mail widoczny na profilu @tgrez

[0]: http://www.sejmotrendy.pl
[1]: https://www.elastic.co/downloads/elasticsearch

