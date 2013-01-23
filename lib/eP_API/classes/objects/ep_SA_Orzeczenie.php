<?php
class ep_SA_Orzeczenie extends ep_Object{
  
  public $_aliases = array('sa_orzeczenia', 'sa_orzeczenia_typy');
  public $_field_init_lookup = 'sygnatura';
  
  private $_sad;
  private $_organ;
  private $_wynik;
  private $_sedziowie;
  
  
  
  
  public function __construct( $data, $complex = true ){
		
		parent::__construct( $data, $complex );
		$sad = $this->sad()->data;
		$this->data['tytul_skrocony'] = $this->data['nazwa'].' '.$sad['dopelniacz'].'  z dnia '.sm_data_slowna($this->data['data_orzeczenia']);
	  $this->data['tytul'] = $this->data['sygnatura'];
	  
  }
  
  
  
  
  
  
  public function sedziowie() {
	  
	  if( !$this->_sedziowie ) {
	    $this->_sedziowie = new ep_Dataset('sa_sedziowie');
	    $this->_sedziowie->init_where('sa_sedziowie_orzeczenia.orzeczenie_id', '=', $this->id);
	    
	  }
	  
	  return $this->_sedziowie;
	  
  }
  
  public function set_ep_sa_sady($data){
	  $this->_sad = new ep_SA_Sad($data);
  }
  
  public function sad(){
	  return $this->_sad;
  }
  
  public function set_ep_sa_skarzone_organy($data){
	  $this->_organ = new ep_SA_Skarzony_Organ($data);	  
  }
  
  public function skarzony_organ(){
	  return $this->_organ;
  }
  
  public function set_ep_sa_orzeczenia_wyniki($data){
	  $this->_wynik = new ep_SA_Orzeczenie_Wynik($data);	  
  }
  
  public function wynik(){
	  return $this->_wynik;
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