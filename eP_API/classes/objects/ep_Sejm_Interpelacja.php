<?php
class ep_Sejm_Interpelacja extends ep_Object{

  public $_aliases = array('sejm_interpelacje');
  public $_field_init_lookup = 'numer';
  
  private $_tablica = false;
  private $_poslowie = false;
  
  
  public function poslowie(){
	  
	  if( $this->_poslowie===false ) {
	    $_poslowie = new ep_Dataset('poslowie');
	    $_poslowie->init_where('sejm_interpelacje.id', '=', $this->data['id']);
	    $this->_poslowie = $_poslowie->find_all();
	  }
	  return $this->_poslowie;
	  
  }
  
  public function tablica(){
	  
	  if( $this->_tablica===false ) {
	    $this->_tablica = new ep_Dataset('sejm_interpelacje_pisma');
	    $this->_tablica->init_where('interpelacja_id', '=', $this->data['id']);		  
	  }
	  return $this->_tablica;
	  
  }
  
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