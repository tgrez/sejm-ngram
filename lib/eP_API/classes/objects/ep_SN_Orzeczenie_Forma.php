<?php

class ep_SN_Orzeczenie_Forma extends ep_Object{

	public $_aliases = array( 'sn_orzeczenia_typy' );

	public $_field_init_lookup = 'nazwa';

	/**
	 * @var ep_Dataset
	 */
	protected $_orzeczenia_sn = null;


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
	public function orzeczenia_sn(){
		if( !$this->_orzeczenia_sn ) {
			$this->_orzeczenia_sn = new ep_Dataset( 'sn_orzeczenia' );
			$this->_orzeczenia_sn->init_where( 'orzeczenie_sn_forma_id', '=', $this->id );
		}
		return $this->_orzeczenia_sn;
	}


}