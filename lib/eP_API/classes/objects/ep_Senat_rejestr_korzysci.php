<?php
//
class ep_Senat_rejestr_korzysci extends ep_Object{

	public $_aliases = array('senat_rejestr_korzysci');
	public $_field_init_lookup = 'nazwa';
	
	private $_senator = false;
  
  
	public function set_ep_senatorowie($data){
		$this->_senator = new ep_Senator($data);
	}
	
	public function senator(){
		return $this->_senator;
	} 
	
	
	/**
	 * @return integer
	 */
	public function get_id(){
		return (int) $this->data['id'];
	}


	/**
	 * @return integer
	 */
	public function get_dokument_id(){
		return (int) $this->data['dokument_id'];
	}

	/**
	 * @return integer
	 */
	public function get_posel_id(){
		return (int) $this->data['senator_id'];
	}
	
	/**
	 * @return string
	 */
	public function get_nazwa(){
		return (string) $this->data['nazwa'];
	}
	
	public function __toString(){
		return $this->get_nazwa();
	}
	
}