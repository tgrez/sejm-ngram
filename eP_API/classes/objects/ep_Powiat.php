<?php
class ep_Powiat extends ep_Object{

	public $_aliases = array('powiaty');
	public $_field_init_lookup = 'nazwa';


	/**
	 * @var ep_Wojewodztwo
	 */
	private $_wojewodztwo = null;
	
	
	/**
	 * @var ep_Area
	 */
	private $_obszar = null;
	
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
		return (string) $this->data['nazwa'];
	}
	
	public function __toString(){
		return $this->get_nazwa();
	}
	
	/**
	 * @return ep_Wojewodztwo
	 */
	public function wojewodztwo(){
		return $this->_wojewodztwo;	
	}
	
	/**
	 * @param array|ep_Wojewodztwo $data
	 */
	public function set_ep_wojewodztwo( $data ){
		if( $data instanceof ep_Wojewodztwo ){
			$this->_wojewodztwo = $data; 
		} else {
			$this->_wojewodztwo = new ep_Wojewodztwo( $data, false );
		}
		return $this;
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