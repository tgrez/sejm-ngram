<?php

class ep_SP_Haslo_Tematyczne extends ep_Object{

	public $_aliases = array( 'sp_orzeczenia_hasla_tematyczne' );

	public $_field_init_lookup = 'nazwa';

	/**
	 * @var ep_Dataset
	 */
	protected $_orzeczenia_sp_hasla = null;


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
	public function orzeczenia_sp_hasla(){
		if( !$this->_orzeczenia_sp_hasla ) {
			$this->_orzeczenia_sp_hasla = new ep_Dataset( 'sp_orzeczenia_hasla' );
			$this->_orzeczenia_sp_hasla->init_where( 'orzeczenie_sp_haslo_tematyczne_id', '=', $this->id );
		}
		return $this->_orzeczenia_sp_hasla;
	}


}