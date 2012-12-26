<?php

class ep_SN_Osoba extends ep_Object{

	public $_aliases = array( 'sn_ludzie' );

	public $_field_init_lookup = 'nazwa';

	/**
	 * @var ep_Dataset
	 */
	protected $_orzeczenia_sn_autorzy = null;

	/**
	 * @var ep_Dataset
	 */
	protected $_orzeczenia_sn_sedziowie = null;

	/**
	 * @var ep_Dataset
	 */
	protected $_orzeczenia_sn_sprawozdawcy = null;

	/**
	 * @var ep_Dataset
	 */
	protected $_orzeczenia_sn_wspolsprawozdawcy = null;


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
	public function orzeczenia_sn_autorzy(){
		if( !$this->_orzeczenia_sn_autorzy ) {
			$this->_orzeczenia_sn_autorzy = new ep_Dataset( 'sn_orzeczenia_ludzie' );
			$this->_orzeczenia_sn_autorzy->init_where( 'orzeczenie_sn_osoba_id', '=', $this->id );
		}
		return $this->_orzeczenia_sn_autorzy;
	}

	/**
	 * @return ep_Dataset
	 */
	public function orzeczenia_sn_sedziowie(){
		if( !$this->_orzeczenia_sn_sedziowie ) {
			$this->_orzeczenia_sn_sedziowie = new ep_Dataset( 'sn_sedziowie' );
			$this->_orzeczenia_sn_sedziowie->init_where( 'orzeczenie_sn_osoba_id', '=', $this->id );
		}
		return $this->_orzeczenia_sn_sedziowie;
	}

	/**
	 * @return ep_Dataset
	 */
	public function orzeczenia_sn_sprawozdawcy(){
		if( !$this->_orzeczenia_sn_sprawozdawcy ) {
			$this->_orzeczenia_sn_sprawozdawcy = new ep_Dataset( 'sn_sprawozdawcy' );
			$this->_orzeczenia_sn_sprawozdawcy->init_where( 'orzeczenie_sn_osoba_id', '=', $this->id );
		}
		return $this->_orzeczenia_sn_sprawozdawcy;
	}

	/**
	 * @return ep_Dataset
	 */
	public function orzeczenia_sn_wspolsprawozdawcy(){
		if( !$this->_orzeczenia_sn_wspolsprawozdawcy ) {
			$this->_orzeczenia_sn_wspolsprawozdawcy = new ep_Dataset( 'sn_wspolsprawozdawcy' );
			$this->_orzeczenia_sn_wspolsprawozdawcy->init_where( 'orzeczenie_sn_osoba_id', '=', $this->id );
		}
		return $this->_orzeczenia_sn_wspolsprawozdawcy;
	}


}