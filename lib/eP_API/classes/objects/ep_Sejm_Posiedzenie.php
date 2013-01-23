<?php
class ep_Sejm_Posiedzenie extends ep_Object{
  
  public $_aliases = array('sejm_posiedzenia');
  public $_field_init_lookup = 'tytul';
  private $_dni = false;
  private $_punkty = false;
  private $_wystapienia = false;
  private $_glosowania = false;
  private $_poslowie = false;
  
  
  
  public function __construct( $data, $complex = true ){
		
		parent::__construct( $data, $complex );
		$this->data['stats'] = json_decode( $this->data['stats_json'], true );
		unset( $this->data['stats_json'] );
		$this->data['numer'] = (int) $this->data['tytul'];
	  $this->data['tytul'] = 'Posiedzenie Sejmu nr '.$this->data['tytul'];
	  
  }
  
  
  public function dni(){
	  
	  if( !$this->_dni ) {
	    $this->_dni = new ep_Dataset('sejm_posiedzenia_dni');
	    $this->_dni->init_where('posiedzenie_id', '=', $this->id)->order_by('sejm_posiedzenia_dni.data', 'ASC');
	  }
	  
	    
	  return $this->_dni;
	  
	  
  }
  
  public function poslowie(){
	  
	  if( !$this->_poslowie ) {
	    $this->_poslowie = new ep_Dataset('poslowie');
	    $this->_poslowie->init_layer('sejm_posiedzenia_poslowie')->init_where('sejm_posiedzenia_poslowie.posiedzenie_id', '=', $this->data['id']);
	  }
	  
	    
	  return $this->_poslowie;
	  
	  
  }
  
  public function punkty(){
	  
	  if( !$this->_punkty ) {
	    $this->_punkty = new ep_Dataset('sejm_posiedzenia_punkty');
	    $this->_punkty->init_where('sejm_posiedzenia_punkty.posiedzenie_id', '=', $this->id)->order_by('sejm_posiedzenia_punkty.kolejnosc', 'ASC')->set_limit( 1000 );
	  }
	  
	    
	  return $this->_punkty;
	  
	  
  }
  
  public function wystapienia(){
	  
	  if( !$this->_wystapienia ) {
	    $this->_wystapienia = new ep_Dataset('sejm_wystapienia');
	    $this->_wystapienia->init_where('sejm_wystapienia.posiedzenie_id', '=', $this->id)->order_by('sejm_wystapienia.kolejnosc', 'ASC');
	  }
	  
	    
	  return $this->_wystapienia;
	  
  }
  
  public function glosowania(){
	  
	  if( !$this->_glosowania ) {
	    $this->_glosowania = new ep_Dataset('sejm_glosowania');
	    $this->_glosowania->init_where('sejm_glosowania.posiedzenie_id', '=', $this->id)->order_by('sejm_glosowania.czas', 'ASC')->set_limit( 1000 );
	  }
	  
	    
	  return $this->_glosowania;
	  
	  
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