<?php
class ep_RCL_Projekt extends ep_Object{
  
  public $_aliases = array('rcl_projekty');
  
  private $_tablica;
  private $_autor;
  
  public function tablica() {
	  
	  if( !$this->_tablica ) {
	    $this->_tablica = new ep_Dataset('rcl_projekty_tablice');
	    $this->_tablica->init_where('projekt_id', '=', $this->id);
	  }
	  
	  return $this->_tablica;
	   
  }
  
  
  
  public function set_ep_instytucje($data){
	  
	  $this->_autor = new ep_Instytucja($data);
	  
  }
  
  public function autor(){
	  return $this->_autor;
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
	public function get_nazwa(){
		return (string)$this->data['nazwa'];
	}

	/**
	 * @return string 
	 */
	public function get_imie(){
		return (string)$this->data['imie'];
	}

	/**
	 * @return int 
	 */
	public function get_nazwisko(){
		return (string)$this->data['nazwisko'];
	}

	/**
	 * @return int 
	 */
	public function get_zawod(){
		return (string)$this->data['zawod'];
	}

	/**
	 * @return int 
	 */
	public function get_plec(){
		return (string)$this->data['plec'];
	}

	/**
	 * @return int 
	 */
	public function get_data_urodzenia(){
		return (string)$this->data['data_urodzenia'];
	}

	/**
	 * @return int 
	 */
	public function get_miejsce_urodzenia(){
		return (string)$this->data['miejsce_urodzenia'];
	}
	/**
	 * @return int 
	 */
	public function get_nr_okregu(){
		return (int)$this->data['nr_okregu'];
	}
	
	/**
	 * @return string 
	 */
	public function __toString(){
		return $this->get_nazwa();
	}
}