<?
  class ep_Search extends ep_Api {
	
	  public $q;
	  public $items;
	  public $tabs;
	  public $limit;
	  public $items_found_rows;
	  public $dataset;
	  
	  public function set_q( $q ) {
		  
		  $this->q = trim( $q );
		  return $this;
		  
	  }
	  
	  public function set_dataset( $dataset ) {
		  
		  $this->dataset = trim( $dataset );
		  return $this;
		  
	  }
	  
	  public function find_all($limit, $offset){
		  
		  $this->limit = 20;
		  if( $offset )
			  $this->offset = $offset;
		  
		  $params = array(
		    'q' => $this->q,
		    'l' => $this->limit,
				'of' => $this->offset,
		  );
		  
		  if( $this->dataset )
		    $params['d'] = $this->dataset;
		  
		  $data = $this->call( 'search', $params );
		  $this->items = array();
		  $this->tabs = $data['tabs'];
		  $this->limit = $data['limit'];
		  $this->items_found_rows = $data['items_found_rows'];
		  
		  foreach( $data['items'] as $i ) {
			  
			  $class = $i['class'];
			  $object_id = $i['object_id'];
			  $o = new $class( $object_id, false );
			  $o->data = json_decode( $i['data'], true );
			  
			  $this->items[] = $o;
			  
		  }
		  		  		  
		  return $this->items;
		  
	  }
	
	}
?>