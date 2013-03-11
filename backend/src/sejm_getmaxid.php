<?
	echo "####################################################\n";
	echo "The script checks what is max id in specified dataset using ep_API (sejmometr.pl).\n";
	echo "Usage: php sejm_get.php -d dataset_name \n";
	echo "####################################################\n";

	require_once('../../lib/eP_API/ep_API.php');

	$options = getopt("d:");
	$dataset_name 				= $options["d"];

	if (empty($dataset_name)) {
		echo "[ERROR] dataset_name cannot be empty. Use -d option!\n";
		exit(-1);
	}

    echo "dataset_name = $dataset_name\n";
	$dataset = new ep_Dataset($dataset_name);
	$first_page = $dataset->order_by('id', 'DESC')->find_all(1, 0);
    echo "maxid = ".$first_page[0]->data["id"]."\n";
?>

