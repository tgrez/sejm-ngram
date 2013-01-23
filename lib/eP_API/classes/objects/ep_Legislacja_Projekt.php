<?php
class ep_Legislacja_Projekt extends ep_Object{

  public $_aliases = array('legislacja_projekty_ustaw','legislacja_projekty_uchwal','legislacja_projekty','konsultacje');
  public $_field_init_lookup = 'tytul';
  private $_etapy = false;
  private $_zmieniane_ustawy = false;
  private $_podpisy_poslowie = false;
  
  
  public function etapy(){
  
    if( !$this->_etapy ) {
	    $this->_etapy = new ep_Dataset('legislacja_projekty-etapy');
	    $this->_etapy->init_where('projekt_id', '=', $this->data['id']);
    }
      
    
	  return $this->_etapy;
  }
  
  
  public function zmieniane_ustawy(){
	  
	  if( !$this->_zmieniane_ustawy ) {
		  $this->_zmieniane_ustawy = new ep_Dataset('ustawy');
		  $this->_zmieniane_ustawy->init_where('legislacja_projekty-prawo_glowne.projekt_id', '=', $this->data['id']);
		  
	  }
	  return $this->_zmieniane_ustawy;
	  
  }
  
  
  public function podpisy_poslowie(){
	  
	  if( !$this->_podpisy_poslowie ) {
		  
		  $this->_podpisy_poslowie = new ep_Dataset('legislacja_projekty-podpisy');
		  $this->_podpisy_poslowie->init_where('projekt_id', '=', $this->data['id']);
		  
	  }
	  return $this->_podpisy_poslowie;
	  
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