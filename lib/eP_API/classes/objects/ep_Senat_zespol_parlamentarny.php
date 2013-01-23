<?php
class ep_Senat_zespol_parlamentarny extends ep_Object{
  
	public $_aliases = array('senat_zespoly_parlamentarne');
	public $_field_init_lookup = 'nazwa';
  
	/**
	 * @return int
	 */
	public function get_id(){
		return (int)$this->data['id'];
	}

	/**
	 * @return string
	 */
	public function get_nazwa(){
		return (string)$this->data['nazwa'];
	}
	
	/**
	 * @return string 
	 */
	public function __toString(){
		return $this->get_nazwa();
	}
}