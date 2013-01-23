<?php
class ep_Sejm_Posiedzenie_Punkt extends ep_Object{
  
  public $_aliases = array('sejm_posiedzenia_punkty');
  
  private $_debaty = false;
  private $_wystapienia = false;
  private $_glosowania = false;
  private $_druki = false;
  private $_posiedzenie = false;
  
  
  public function posiedzenie(){
	  
	  if( !$this->_posiedzenie ) {
		  $this->_posiedzenie = new ep_Sejm_Posiedzenie( $this->data['posiedzenie_id'] );
	  }
	  
	  return $this->_posiedzenie;
  }
  
  
  public function debaty(){
	  if( !$this->_debaty ) {
		  $this->_debaty = new ep_Dataset('sejm_debaty');
		  $this->_debaty->init_where('punkt_id', '=', $this->data['id'])->order_by('kolejnosc', 'ASC');
	  }
	  return $this->_debaty;
  }
  
  public function wystapienia(){
	  if( !$this->_wystapienia ) {
		  $this->_wystapienia = new ep_Dataset('sejm_wystapienia');
		  $this->_wystapienia->init_where('sejm_wystapienia.punkt_id', '=', $this->data['id'])->order_by('kolejnosc', 'ASC');
	  }
	  return $this->_wystapienia;
  }
  
  public function glosowania(){
	  if( !$this->_glosowania ) {
		  $this->_glosowania = new ep_Dataset('sejm_glosowania');
		  $this->_glosowania->init_where('punkt_id', '=', $this->data['id'])->order_by('czas', 'ASC');
	  }
	  return $this->_glosowania;
  }
  
  public function druki(){
	  if( !$this->_druki ) {
		  $this->_druki = new ep_Dataset('sejm_druki');
		  $this->_druki->init_where('sejm_posiedzenia_punkty.id', '=', $this->data['id']);
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