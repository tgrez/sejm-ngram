<?php

class ep_SN_Orzeczenie_Sedzia extends ep_Object{

	public $_aliases = array( 'sn_sedziowie' );

	/**
	 * @var ep_SN_Orzeczenie
	 */
	protected $_orzeczenie_sn = null;

	/**
	 * @var ep_SN_Osoba
	 */
	protected $_orzeczenie_sn_osoba = null;


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
	public function get_orzeczenie_sn_osoba_id(){
		return (int) $this->data['orzeczenie_sn_osoba_id'];
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
	 * @return ep_SN_Osoba
	 */
	public function orzeczenie_sn_osoba(){
		if( !$this->_orzeczenie_sn_osoba ) {
			$this->_orzeczenie_sn_osoba = new ep_SN_Osoba( $this->get_orzeczenie_sn_osoba_id() );
		}
		return $this->_orzeczenie_sn_osoba;
	}


}