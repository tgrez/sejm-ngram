<?php
class ep_Sejm_Druk extends ep_Object{

  public $_aliases = array('sejm_druki', 'sejm_druki_typy');
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