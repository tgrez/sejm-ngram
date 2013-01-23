<?php
class ep_Senat_Wystapienie extends ep_Object{
  
  public $_aliases = array('senat_wystapienia');
  private $_mowca = false;
  private $_stanowisko = false;
  private $_posiedzenie = false;
  private $_debata = false;
  private $_punkt = false;
  
  
  public function __construct( $data, $complex = true) {
	  	  
	  parent::__construct( $data, $complex );
	  $this->data['tytul'] = strip_tags( 'Wystąpienie '.$this->mowca()->data['nazwa'] );
	  
  }
  
  
  /*
  public function posiedzenie(){
	  if( !$this->_posiedzenie ) {
		  $this->_posiedzenie = new ep_Sejm_Posiedzenie( $this->data['posiedzenie_id'] );
	  }
	  return $this->_posiedzenie;
  }
  
  public function debata(){
	  if( !$this->_debata ) {
		  $this->_debata = new ep_Sejm_Posiedzenie_Debata( $this->data['debata_id'] );
	  }
	  return $this->_debata;
  }
  
  public function punkt(){
	  if( !$this->_punkt ) {
		  $this->_punkt = new ep_Sejm_Posiedzenie_Punkt( $this->data['punkt_id'] );
	  }
	  return $this->_punkt;
  }
  */
  
  
  
  
  
  
  public function set_ep_mowcy($data){
	  
	  $this->_mowca = new ep_Czlowiek($data);
	  
  }
  
  public function mowca(){
	  return $this->_mowca;
  }
  
  
  
  public function set_ep_stanowiska($data){
	  
	  $this->_stanowisko = new ep_Stanowisko($data);
	  
  }
  
  public function stanowisko(){
	  return $this->_stanowisko;
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