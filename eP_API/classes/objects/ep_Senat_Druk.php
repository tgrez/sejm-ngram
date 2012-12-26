<?php
class ep_Senat_Druk extends ep_Object{

  public $_aliases = array('senat_druki');
  public $_field_init_lookup = 'numer';

	/**
	* @return int
	*/
	public function get_id(){
		return (int)$this->data['id'];
	}
	

	/**
	 * @return string 
	 */
	public function __toString(){
		return $this->get_nazwa();
	}
}