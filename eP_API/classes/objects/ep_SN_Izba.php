<?php

class ep_SN_Izba extends ep_Object{

	public $_aliases = array( 'sn_izby' );

	public $_field_init_lookup = 'nazwa';

	/**
	 * @var ep_Dataset
	 */
	protected $_orzeczenia = null;


	/**
	 * @return int
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

	/**
	 * @return string
	 */
	public function __toString(){
		return (string) $this->get_nazwa();
	}

	/**
	 * @return ep_Dataset
	 */
	public function orzeczenia(){
		if( !$this->_orzeczenia ) {
			$this->_orzeczenia = new ep_Dataset( 'sn_izby-orzeczenia' );
			$this->_orzeczenia->init_where( 'orzeczenie_sn_izba_id', '=', $this->id );
		}
		return $this->_orzeczenia;
	}


}