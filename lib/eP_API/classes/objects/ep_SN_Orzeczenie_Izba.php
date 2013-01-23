<?php

class ep_SN_Orzeczenie_Izba extends ep_Object{

	public $_aliases = array( 'sn_izby-orzeczenia' );

	/**
	 * @var ep_SN_Orzeczenie
	 */
	protected $_orzeczenie_sn = null;

	/**
	 * @var ep_SN_Izba
	 */
	protected $_orzeczenie_sn_izba = null;


	/**
	 * @return int
	 */
	public function get_id(){
		return (int) $this->data['id'];
	}

	/**
	 * @return int
	 */
	public function get_orzeczenie_sn_id(){
		return (int) $this->data['orzeczenie_sn_id'];
	}

	/**
	 * @return int
	 */
	public function get_orzeczenie_sn_izba_id(){
		return (int) $this->data['orzeczenie_sn_izba_id'];
	}

	/**
	 * @return ep_SN_Orzeczenie
	 */
	public function orzeczenie_sn(){
		if( !$this->_orzeczenie_sn ) {
			$this->_orzeczenie_sn = new ep_SN_Orzeczenie( $this->get_orzeczenie_sn_id() );
		}
		return $this->_orzeczenie_sn;
	}

	/**
	 * @return ep_SN_Izba
	 */
	public function orzeczenie_sn_izba(){
		if( !$this->_orzeczenie_sn_izba ) {
			$this->_orzeczenie_sn_izba = new ep_SN_Izba( $this->get_orzeczenie_sn_izba_id() );
		}
		return $this->_orzeczenie_sn_izba;
	}


}