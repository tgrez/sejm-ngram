<?php
class ep_Sejm_Druk_Typ extends ep_Object{

  protected $_aliases = array('sejm_druki_typy');
  protected $_field_init_lookup = 'nazwa';
  private $_druki = false;
  
  
  
  
  public function druki(){
	  
	  if( !$this->_druki ) {
	    	  
	    $this->_druki = new ep_Dataset('sejm_druki');
	    $this->_druki->init_where('sejm_druki.typ_id', '=', $this->id);
    
    }
    return $this->_druki;
	  
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