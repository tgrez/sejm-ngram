<?php
class ep_Ustawa extends ep_Object{

  public $_aliases = array('ustawy','prawo_typy','isap_pliki');
  // public $_field_init_lookup = 'tytul';
  private $_prawo = false;
  private $_projekty_zmian = false;
  
  
	/**
	* @return int
	*/
	public function get_id(){
		return (int)$this->data['id'];
	}
	
	public function set_ep_Prawo($data){
		
		$this->_prawo = new ep_Prawo($data);
		
		
		
		
	}
	
	
	public function projekty_zmian(){
		
		if( !$this->_projekty_zmian ) {
		  $this->_projekty_zmian = new ep_Dataset('legislacja_projekty_ustaw');
		  $this->_projekty_zmian->init_where('ustawy.id', '=', $this->data['id']);
		}
		
		return $this->_projekty_zmian;
		
	}
	
	
	public function prawo(){
		return $this->_prawo;
	}
	
	function parse_data( $data ){
	  
	  parent::parse_data($data);
	  
	  $fields = array('autor_id', 'data_publikacji', 'data_wejscia_w_zycie', 'data_wydania', 'isap_data_uchylenia', 'isap_data_wygasniecia', 'isap_id', 'isap_uwagi_str', 'status_id', 'sygnatura', 'typ_id', 'typ_nazwa', 'tytul', 'tytul_skrocony', 'zrodlo');
    foreach( $fields as $f )
      $this->data[$f] = $this->prawo()->data[$f];

	  
	  
	  
	  
	}

	/**
	 * @return string 
	 */
	public function __toString(){
		return $this->get_nazwa();
	}
}