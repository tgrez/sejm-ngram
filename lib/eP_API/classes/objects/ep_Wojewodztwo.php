<?php
class ep_Wojewodztwo extends ep_Object{

  public $_aliases = array('wojewodztwa');
  public $_field_init_lookup = 'nazwa';



	/**
	 * @var ep_Area
	 */
	private $_obszar = null;
	
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