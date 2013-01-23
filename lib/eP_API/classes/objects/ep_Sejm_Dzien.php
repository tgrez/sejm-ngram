<?php
class ep_Sejm_Dzien extends ep_Object{
  
  public $_aliases = array('sejm_posiedzenia_dni');
  private $_posiedzenie = false;
  private $_debaty = false;
  
  
  
  
  public function __construct( $data, $complex = true ){
		
		parent::__construct( $data, $complex );
	  $this->data['tytul'] = strip_tags( sm_data_slowna( $this->data['data'] ).', '.sm_dzien_slowny( $this->data['data'] ) );
	  
  }
  
  
  
  public function set_ep_sejm_posiedzenia($data){
	  $this->_posiedzenie = new ep_Sejm_Posiedzenie( $data );
  }
  
  public function posiedzenie(){
	  return $this->_posiedzenie;
  }
  
  public function debaty(){
	  
	  if( !$this->_debaty ) {
	    $this->_debaty = new ep_Dataset('sejm_debaty');
	    $this->_debaty->init_where('dzien_id', '=', $this->data['id'])->order_by('kolejnosc', 'ASC')->set_limit(1000);
	  }
	  
	  return $this->_debaty;
	  
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