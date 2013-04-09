<?php
    // load in mysql server configuration (connection string, user/pw, etc)
    include 'mysqlConfig.php';

    //override PHP memory default value
    ini_set('memory_limit', '-1');

    // connect to the database
    @mysql_select_db($dsn) or die( "Unable to select database");
     
    
    $dateFrom = $_GET["datefrom"];
    $dateTo = $_GET["dateto"];
    $ngram = $_GET["ngram"];



    // some weird fixes for Polish diacritics
    mysql_query("set charset utf8");
    mysql_query("set names utf8");

    //prepare normal query
    $query = 
            "SELECT klub_id, data from ngrams 
            WHERE   data >= '". $dateFrom ."' AND 
                    data <= '". $dateTo."' AND 
                    ngram LIKE '".$ngram."';";

     
    //$query="SELECT `id`, `data`, `posiedzenie_id` FROM `sejm_wystapienia` LIMIT 2;";
    mysql_query($query);
     
    $result = mysql_query($query,$link) or die('Errant query: '.$query);

    $rows = array();
   
    while($r = mysql_fetch_assoc($result)) {
        $rows[] = $r;
    }
    print json_encode($rows);

     /*
    // outputs the db as lines of text.
    header('Content-type: text/plain; charset=us-ascii');
    $i=0;
    $line="";
   // print(mysql_num_rows($result) + "\n");

    if(mysql_num_rows($result)) {
         while($value = mysql_fetch_assoc($result)) {
            var_dump($value);
            //echo $value[]."\n";

             
            $line=$line.$value["klub_id"];
             $i=$i+1;
             if ($i==52) {
                 $i=0;
                 echo $line."\n";
                 $line="";
             }
             else {
                $line=$line.",";}
         }
    }

    */
    mysql_close();
?>