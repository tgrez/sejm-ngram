<?
  class ep_Dataset extends ep_Api {
		
		public $name;
		public $mode;
		public $items_class;
		public $items_found_rows = 0;
		public $items = array();
		public $fields = array();
		public $data = array();
		public $limit = 0;
		public $offset = 0;
		public $performance = array();
		public $q;
		public $respect_limit = true;
		
		protected $_init_wheres = array();
		protected $_init_layers = array();
		protected $_runtime_wheres = array();
		protected $_init_orders = array();
		protected $_runtime_orders = array();
		protected $_init_keywords = array();
		protected $_runtime_keywords = array();
		protected $_runtime_layers = array();
		
	  
	  public function __construct( $name ) {
		  
		  $this->name = $name;
		  
	  }
	  
	  public function keyword($keyword){
		  
		  $this->add_runtime_keywords( $keyword );
			return $this;		
	  }
	  
		public function where( $key, $operator, $value ){
			$this->add_runtime_wheres( array( $this->name.'.'. $key, $operator, $value ) );
			return $this;		
		}
		
		public function init_where( $key, $operator, $value ){
			$this->add_init_wheres( array( $this->name.'.'. $key, $operator, $value ) );
			return $this;		
		}
		
		public function init_layer( $layer ) {
			if( $layer )
			  $this->_init_layers[] = $layer;
			return $this;
		}
		
		public function order_by( $field, $direction = 'ASC' ){

		  if( !$field )
		    return $this;
		  
		  $parts = explode('.', $field);
		  $parts_count = count($parts);
		  

		  if( $parts_count===1 )
		    $d_field = $this->name.'.'. $field;
		  else
			  $d_field = $parts[ $parts_count-2 ].'.'.$parts[ $parts_count-1 ];
		  
	  
		  $this->add_runtime_orders( array( $d_field, $direction ) );
		  
		    
			return $this;
		}
		
		
		
		public function get_info(){
			
			$data = $this->call( 'dataset-info', $this->name );			
			$this->data = $data['dataset'];
			$this->fields = $data['fields'];
						
		}
		
		public function set_limit( $l ){
			$this->limit = (int) $l;
			return $this;
		}
		
		
		public function find_all( $limit = null, $offset = null, $return_objects=true ){
			
			
			if( $limit )
			  $this->limit = $limit;
			
			if( $offset )
			  $this->offset = $offset;
						
			$params = $this->get_params();			
			$data = $this->call( 'dataset-search', $params );
			
			
		  $this->performance = $data['performance'];
		  $this->q = $data['query'];
			$this->items_class = $data['items_class'];
			$this->items = $data['items'];
			$this->items_found_rows = $data['items_found_rows'];
			$this->limit = $data['limit'];
			$this->mode = $data['mode'];
			
		
			
			$order = $data['order'];
			
			// var_export( $order );
			

			
			for( $i=0; $i<count($this->fields); $i++ ) {
		    if( $this->fields[$i]['field']==$order[0] ) {
		      $this->fields[$i]['selected'] = true;
		      $this->fields[$i]['direction'] = $order[1];
		    }
			}
			// var_export( $this->items ); die();
			
			$this->clear();
			
			if( $this->mode=='DBF' ) {
			  
			  
			  $class = $this->items_class;
				$result = array();
							
				if( $class && is_array($this->items) && !empty($this->items) ) {
					foreach( $this->items as $item ) {
						
						$data = json_decode( $item, true );
						
						if( $return_objects ) {
							
							$id = $data['id'];
							
							if( $id ) {
								$obj = new $class( $id, false );
								$obj->data = $data;
								$result[] = $obj;
						  }
							
						} else {
						
							$result[] = $data;
					  
					  }
				  
				  }
				
				return $result;
			  
			  }
			  			
			} else {
			
			
			
				if( $return_objects )
				  return $this->return_objects();
				else 
				  return $this->items;
				
			
			}
			
		}
		
		
		public function count(){
			$params = $this->get_params();
			unset( $params['l'] );
			unset( $params['of'] );
			unset( $params['o'] );
			
			$data = $this->call( 'dataset-count', $params );
				
			return $data['count'];
		}
		
		
		public function find_one($return_objects=true){
			$data = $this->find_all( 1, 0, $return_objects );
			return $data[0];
		}
	
		public function find($return_objects=true){
			return $this->find_one( $return_objects );
		}
		
			
		public function get_params(){
		  
		
			return array(
			  'name' => $this->name,
				'l'  => $this->limit,
				'of' => $this->offset,
				'j' => $this->_joins,
				'w' => $this->get_wheres(),
				'ls' => $this->get_layers(),
				'o'  => array_merge( $this->_init_orders, $this->_runtime_orders ),
				'k'  => array_merge( $this->_init_keywords, $this->_runtime_keywords ),
				'rl' => $this->respect_limit,
			);
		}
		
		
		
		protected function get_wheres(){
		  
		  
		  $result = array();
		  
		  for( $i=0; $i<count($this->_init_wheres); $i++ )
		    $result[] = $this->_init_wheres[$i];
		  
		  for( $i=0; $i<count($this->_runtime_wheres); $i++ )
		    $result[] = $this->_runtime_wheres[$i];
		  
		  
			return $result;
		}
		
		protected function get_layers(){
		  
		  
		  $result = array();
		  
		  for( $i=0; $i<count($this->_init_layers); $i++ )
		    $result[] = $this->_init_layers[$i];
		  
		  for( $i=0; $i<count($this->_runtime_layers); $i++ )
		    $result[] = $this->_runtime_layers[$i];
		  
		  
			return $result;
		}
		
		
		protected function return_objects(){
			
			$class = $this->items_class;
			$result = array();
						
			if( $class && is_array($this->items) && !empty($this->items) )
				foreach( $this->items as $item ) {
				  
				  /*
				  if( $item && $this->mode=='DBF' && $_SERVER['REMOTE_ADDR']=='80.72.34.251' )
					  $item = @json_decode( $item, true );
					*/
					
					if( $item )
						$result[] = new $class( $item, false );
			  
			  }
			
			return $result;
		}
		
		
		protected function clear(){
			$this->_runtime_wheres = array();
			$this->_runtime_orders = array();
		}
		
		public function get_runtime_wheres(){
			return $this->_runtime_wheres;
		}
	
		public function set_runtime_wheres( $val ){
			$this->_runtime_wheres = $val;
			return $this;
		}
	
		public function add_runtime_wheres( $val ){
			$this->_runtime_wheres[] = $val;
			return $this;
		}
		
		public function add_runtime_keywords( $keyword ){
			if( $keyword )
			  $this->_runtime_keywords[] = $keyword;
			return $this;
		}
	
		public function get_init_wheres(){
			return $this->_init_wheres;
		}
	
		public function set_init_wheres( $val ){
			$this->_init_wheres = $val;
			return $this;
		}
	
		public function add_init_where( $val ){
			$this->_init_wheres[] = $val;
			return $this;
		}
		
		public function add_init_wheres( $val ){
			$this->_init_wheres[] = $val;
			return $this;
		}
		
		public function get_runtime_orders(){
			return $this->_runtime_orders;
		}
	
		public function set_runtime_orders( $val ){
			$this->_runtime_orders = $val;
			return $this;
		}
	
		public function add_runtime_orders( $val ){
			$this->_runtime_orders[] = $val;
			return $this;
		}
	
		public function get_init_orders(){
			return $this->_init_orders;
		}
	
		public function set_init_orders( $val ){
			$this->_init_orders = $val;
			return $this;
		}
	
		public function add_init_orders( $val ){
			$this->_init_orders[] = $val;
			return $this;
		}
		
		protected $_joins = array();
		public function add_join( $alias ){
		  $this->_joins[] = $alias;
		}
		
		public function set_joins( $joins ){
		  $this->_joins = $joins;
		}
		
	}
	
?>