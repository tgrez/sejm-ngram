<?php

require_once './eP_API/ep_API.php';

$dataset = new ep_Dataset( 'poslowie' );

for ($offset = 0; $offset <= 450; $offset += 20)
{
	$poslowie = $dataset->find_all(20, $offset);

	foreach ($poslowie as $posel)
	{
		print $posel . "\n";
	}
}

?>
