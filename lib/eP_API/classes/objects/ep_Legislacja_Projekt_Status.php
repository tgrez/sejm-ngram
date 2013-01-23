<?php
class ep_Legislacja_Projekt_Status extends ep_Object{

  public $_aliases = array('legislacja_projekty_statusy');

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