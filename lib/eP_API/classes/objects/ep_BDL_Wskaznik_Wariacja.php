<?php
class ep_BDL_Wskaznik_Wariacja extends ep_Object{
  
  public $_aliases = array('bdl_wskazniki_wariacje', 'bdl_wskazniki');
  
   
   public $_podgrupa = null;
   public $_wojewodztwa = null;
   public $_powiaty	 = null;
  
	/**
	 * @return int
	 */
	public function get_id(){
		return (int)$this->data['id'];
	}
	
	
	
	/**
	 * @return int
	 */
	public function get_podgrupa_id(){
		return (int)$this->data['podgrupa_id'];
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
	
	/**
	 * @return ep_BDL_Podgrupa
	 */
	public function podgrupa(){
		if( !$this->_podgrupa ) {
			$this->_podgrupa = new ep_BDL_Podgrupa( $this->get_podgrupa_id() );
		}
		return $this->_podgrupa;
	}

	public function wojewodztwa(){
		if( !$this->_wojewodztwa ) {
			$this->_wojewodztwa = new ep_Dataset( 'wojewodztwa' );
			$this->_wojewodztwa->init_layer('bdl_wsk_opcje_wojewodztwa_ostatnie')->init_where('bdl_wsk_opcje_wojewodztwa_ostatnie.opcja_id', '=', $this->get_id())->set_limit(10000);
		}
		return $this->_wojewodztwa;
	}
	
	public function powiaty(){
		if( !$this->_powiaty ) {
			$this->_powiaty = new ep_Dataset( 'powiaty' );
			$this->_powiaty->init_layer('bdl_wsk_opcje_powiaty_ostatnie')->init_where('bdl_wsk_opcje_powiaty_ostatnie.opcja_id', '=', $this->get_id())->set_limit(10);
		}
		return $this->_powiaty;
	}
	
	public function gminy(){
		if( !$this->_gminy ) {
			$this->_gminy = new ep_Dataset( 'gminy' );
			$this->_gminy->init_layer('bdl_wsk_opcje_gminy_ostatnie')->init_where('bdl_wsk_opcje_gminy_ostatnie.opcja_id', '=', $this->get_id())->set_limit(10);
		}
		return $this->_gminy;
	}
	
	
}