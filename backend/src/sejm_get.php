<?
	echo "####################################################\n";
	echo "The script downloads and prints (CSV) datasets using ep_API (sejmometr.pl).\n";
	echo "Usage: php sejm_get.php -d dataset_name -o output_file [-h true/false] [-t separator]\n";
	echo " -h true/false - whether header should be attached or not\n";
	echo " -s start id value  -l last id value\n";
	echo "Example: php sejm_get.php -d sejm_wystapienia -o /tmp/tmp.txt -s 100 -l 150\n";

	require_once('../../lib/eP_API/ep_API.php');

	$options = getopt("d:o:h:t:s:l:");
	$dataset_name 				= $options["d"];
	$output_file  				= $options["o"];
	$attach_header				= ($options["h"]=="false")? 0: 1;
	$requested_page_size  		= 100;	
	$csv_separator				= $options["t"];
    $start_id                   = $options["s"];
    $last_id                    = $options["l"];

	if (empty($dataset_name)) {
		echo "[ERROR] dataset_name cannot be empty. Use -d option!\n";
		exit(-1);
	}

	if (empty($output_file)) {
		echo "[ERROR] output_file cannot be empty. Use -o option!\n";
		exit(-1);
	}

	if (empty($csv_separator)) {
		echo "[WARNING] Separator is empty. Using default!\n";
		$csv_separator = ";";
	}

	if (empty($start_id)) {
		echo "[WARNING] Start id is empty. Using default!\n";
		$start_id = "0";
	}


	if (empty($last_id)) {
		echo "[WARNING] Last id is empty. Using default!\n";
		$last_id = "10000000";
	}

    $csv_separator_replacement  =  str_replace_all_non_whs($csv_separator, ',');

    ###############################################################################################################

	function str_replace_all_non_whs($str, $replacement_char) { #replace all non-whitespace characters with replacement character
		for ($i=0; $i<strlen($str); $i++) {
			if ($str[$i]!=' ' && $str[$i]!='\n' && $str[$i]!='\t') {
				$str[$i]=$replacement_char;
			}
		}
		return $str;
	}

	function csv_array_reduce($arr, $keys) { #takes array and reduces into single string (CSV-separated)
		global $csv_separator;
		$str = "";
		for($i=0; $i<count($keys)-1; $i++) {
			$key = $keys[$i];
			$str .= str_replace($csv_separator, $csv_separator_replacement, $arr[$key]);
			$str .= " ".$csv_separator." ";
		}
		$last_key = $keys[count($keys)-1];
		$str 	 .= $arr[$last_key];
		return $str;
	}

	function csv_header_reduce($keys) { #takes array of keys and reduces into single string (CSV-separated)
		global $csv_separator;
		$str = "";
		for($i=0; $i<count($keys)-1; $i++) {
			$key = $keys[$i];
			$str .= str_replace($csv_separator, $csv_separator_replacement, $key);
			$str .= " ".$csv_separator." ";
		}
		$last_key = $keys[count($keys)-1];
		$str 	 .= $last_key;
		return $str;
	}

    ###############################################################################################################

	echo "####################################################\n";
	echo "Running with dataset_name=$dataset_name output_file=$output_file attach_header=$attach_header ";
	echo "requested_page_size=$requested_page_size csv_separator=$csv_separator\n ";
	echo "csv_separator_replacement=$csv_separator_replacement ";
	echo "start_id=$start_id last_id=$last_id\n";
	echo "####################################################\n";

	$fh = fopen($output_file, 'w') or die("[ERROR] Can't open file:".$output_file);
	$dataset = new ep_Dataset($dataset_name);

	$first_page = $dataset->find_all(1, 0);
    #if ( sizeof($first_page) <= 0 ) {
    #    echo "[ERROR] The dataset seems to be empty!\n";
    #    exit(-2);
    #}
	$data_keys	= array_keys($first_page[0]->data);
	if ($attach_header) fwrite($fh, csv_header_reduce($data_keys)."\n");

	$offset = 0;
	while (True) { #iterate over pages
		echo " * next request with offset=$offset -> ";
		$page = $dataset->where( 'id', 'BETWEEN', array($start_id, $last_id) )->order_by('id', 'ASC')->find_all($requested_page_size, $offset);
		$page_size = count($page);
		echo $page_size." records obtained...\n";

		if ($page_size<=0) break; #is there anything more?
		$offset += $page_size;

		for ($element_ix = 0; $element_ix < $page_size; ++$element_ix) { #iterate over page's elements
			$element_data = $page[$element_ix]->data;
			$element_data_string = csv_array_reduce($element_data, $data_keys);			
			fwrite($fh, $element_data_string."\n");
		}
	}
	fclose($fh);

?>
