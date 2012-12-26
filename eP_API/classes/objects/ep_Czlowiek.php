<?php
class ep_Czlowiek extends ep_Object{

  protected $_aliases = array('ludzie');
  protected $_field_init_lookup = 'nazwa';
  private $_stanowiska = false;
  
  
  
  
  public function stanowiska(){
	  
	  if( !$this->_stanowiska ) {
	    	  
	    $this->_stanowiska = new ep_Dataset('stanowiska');
	    $this->_stanowiska->init_where('ludzie.id', '=', $this->id);
    
    }
    return $this->_stanowiska;
	  
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