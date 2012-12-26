<?php

class ep_Miejscowosc extends ep_Object{

	public $_aliases = array( 'miejscowosci' );

	/**
	 * @var ep_Gmina
	 */
	protected $_gmina = null;

	/**
	 * @var ep_Powiat
	 */
	protected $_powiat = null;

	/**
	 * @var ep_Wojewodztwo
	 */
	protected $_wojewodztwo = null;


	/**
	 * @return int
	 */
	public function get_gmina_id(){
		return (int) $this->data['gmina_id'];
	}

	/**
	 * @return string
	 */
	public function get_id(){
		return (string) $this->data['id'];
	}

	/**
	 * @return string
	 */
	public function get_nazwa(){
		return (string) $this->data['nazwa'];
	}

	/**
	 * @return string
	 */
	public function get_nazwa_gminy(){
		return (string) $this->data['nazwa_gminy'];
	}

	/**
	 * @return int
	 */
	public function get_powiat_id(){
		return (int) $this->data['powiat_id'];
	}

	/**
	 * @return int
	 */
	public function get_wojewodztwo_id(){
		return (int) $this->data['wojewodztwo_id'];
	}

	/**
	 * @return string
	 */
	public function __toString(){
		return (string) $this->get_nazwa();
	}

	/**
	 * @return ep_Gmina
	 */
	public function gmina(){
		if( !$this->_gmina ) {
			$this->_gmina = new ep_Gmina( $this->get_gmina_id() );
		}
		return $this->_gmina;
	}

	/**
	 * @return ep_Powiat
	 */
	public function powiat(){
		if( !$this->_powiat ) {
			$this->_powiat = new ep_Powiat( $this->get_powiat_id() );
		}
		return $this->_powiat;
	}

	/**
	 * @return ep_Wojewodztwo
	 */
	public function wojewodztwo(){
		if( !$this->_wojewodztwo ) {
			$this->_wojewodztwo = new ep_Wojewodztwo( $this->get_wojewodztwo_id() );
		}
		return $this->_wojewodztwo;
	}


}