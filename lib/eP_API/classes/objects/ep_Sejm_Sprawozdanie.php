<?php
class ep_Sejm_Sprawozdanie extends ep_Object{

  public $_aliases = array('sejm_sprawozdania');
  public $_field_init_lookup = 'tytul';

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