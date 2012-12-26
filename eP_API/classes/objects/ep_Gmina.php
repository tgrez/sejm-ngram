<?php
class ep_Gmina extends ep_Object{

	public $_aliases = array('gminy');
  public $_field_init_lookup = 'nazwa';
  
  private $_pna = false;
  
  
  public function parse_data($data){
	  
	  parent::parse_data($data);
	  
	  switch( $this->data['typ_id'] ) {
		  case '1': { $this->data['typ_nazwa'] = 'Gmina miejska'; break; }
		  case '2': { $this->data['typ_nazwa'] = 'Gmina wiejska'; break; }
		  case '3': { $this->data['typ_nazwa'] = 'Gmina miejsko-wiejska'; break; }
		  case '4': { $this->data['typ_nazwa'] = 'Miasto stołeczne'; break; }
	  }
	  
  }
  
  
  
  public function pna(){
	  if( !$this->_pna ) {
		  $this->_pna = new ep_Dataset('kody_pocztowe_miejsca');
		  $this->_pna->init_where('gmina_id', '=', $this->data['id']);
	  }
	  return $this->_pna;
  }
  
  
  
  
  

	/**
	 * @var ep_Wojewodztwo
	 */	
	protected $_wojewodztwo = null;
	
	/**
	 * @var ep_Powiat
	 */
	protected $_powiat = null;
	
	/**
	 * @var ep_Area
	 */
	protected $_obszar = null;
	
	/**
	 * @return ep_Wojewodztwo
	 */
	public function wojewodztwo(){
		return $this->_wojewodztwo;
	}
	
	
	
	protected $_poslowie_dataset = null;
	public function poslowie(){
		
		if( !$this->_poslowie_dataset ) {
				  
		  $this->_poslowie_dataset = new ep_Dataset('poslowie');
		  $this->_poslowie_dataset->init_where('sejm_okreg_id', '=', $this->powiat()->data['sejm_okreg_id']);
		}
		
		return $this->_poslowie_dataset;
		
	}
	
	/**
	 * @return ep_Powiat
	 */
	public function powiat(){
		return $this->_powiat;
	}

	/**
	 * @param array|ep_Wojewodztwo $data
	 */
	public function set_ep_wojewodztwa( $data ){
		if( $data instanceof ep_Wojewodztwo ){
			$this->_wojewodztwo = $data;	
		} else {
			$this->_wojewodztwo = new ep_Wojewodztwo( $data, false );
		}
		
		if( $this->powiat() && !$this->powiat()->wojewodztwo() ){
			$this->_powiat->set_ep_wojewodztwo( $this->wojewodztwo() );
		}
		return $this;
	}
	
	/**
	 * @param array|ep_Powiat $data
	 */
	public function set_ep_powiaty( $data ){		
		if( $data instanceof ep_Powiat ){
			$this->_powiat = $data;
		} else {
			$this->_powiat = new ep_Powiat( $data, false );
		}
		
		if( $this->wojewodztwo() && !$this->powiat()->wojewodztwo()  ){
			$this->_powiat->set_ep_wojewodztwo( $this->wojewodztwo() );
		}
		return $this;
	}

public function set_ep_Powiat( $data ){
		if( $data instanceof ep_Powiat ){
			$this->_powiat = $data;
		} else {
			$this->_powiat = new ep_Powiat( $data, false );
		}

		if( $this->wojewodztwo() && !$this->powiat()->wojewodztwo()  ){
			$this->_powiat->set_ep_wojewodztwo( $this->wojewodztwo() );
		}
		return $this;
	}
	/**
	 * @return integer
	 */
	public function get_id(){
		return (int) $this->data['id'];
	}

	/**
	 * @return string
	 */
	public function get_nazwa(){
		return (string)$this->data['nazwa'];
	}

	/**
	 * @return int
	 */
	public function get_typ_id(){
		return (int)$this->data['typ_id'];
	}
		
	/**
	 * @return string
	 */
	public function __toString(){
		return $this->get_nazwa();
	}
	
	/**
	 * @return ep_Area
	 */
	public function obszar(){
		if( $this->_obszar === null ){
			$this->_obszar = ep_Area::init()->set_raw_data( ep_Api::init()->call( get_class($this) . '/obszar', array( 'id' => $this->id ) ) );
		}
		return $this->_obszar;
	}
}