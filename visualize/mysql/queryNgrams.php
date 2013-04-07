<?php
    // load in mysql server configuration (connection string, user/pw, etc)
    include 'mysqlConfig.php';
    // connect to the database
    @mysql_select_db($dsn) or die( "Unable to select database");
     
    
    $dateFrom = $_GET["datefrom"];
    $dateTo = $_GET["dateto"];
    $ngram = $_GET["ngram"];

   // var_dump($dateFrom);
 //   var_dump($ngram);
    // reads the map db

    $query = 
            "SELECT klub_id, data from ngrams AS a 
                JOIN ngram_dictionary AS b ON 
                    b.id = a.ngram_id 
            WHERE   data >= '". $dateFrom ."' AND 
                    data <= '". $dateTo."' AND 
                    b.ngram LIKE '".$ngram."';";

     
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