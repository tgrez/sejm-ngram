<?php
    //this script is used to retrieve config data and store it within JSON response;

    // load in mysql server configuration (connection string, user/pw, etc)
    include 'mysqlConfig.php';
    // connect to the database
    @mysql_select_db($dsn) or die( "Unable to select database");
     
    
    // $dateFrom = $_GET["datefrom"];
    // $dateTo = $_GET["dateto"];
    // $ngram = $_GET["ngram"];

   // var_dump($dateFrom);
 //   var_dump($ngram);
    // reads the map db

    $query = 
            "SELECT id, nazwa FROM wystapienia.sejm_kluby;";


     
    //$query="SELECT `id`, `data`, `posiedzenie_id` FROM `sejm_wystapienia` LIMIT 2;";
    mysql_query($query);
     
    $result = mysql_query($query,$link) or die('Errant query: '.$query);

    $rows = array();
    while($r = mysql_fetch_assoc($result)) {
        $rows[] = $r;
    }
    //print json_encode($rowsf;
    print json_encode(array(
        'parties_id' => $rows,
        'other' => NULL
        ));
    //ray('item' => $post_data), JSON_FORCE_OBJECT


    mysql_close();
?>