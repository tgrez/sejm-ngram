<?php
class ep_ISAP_Plik extends ep_Object{

  public $_aliases = array('isap_pliki');

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