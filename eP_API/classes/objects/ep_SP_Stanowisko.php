<?php

class ep_SP_Stanowisko extends ep_Object{

	public $_aliases = array( 'sp_stanowiska' );

	public $_field_init_lookup = 'nazwa';

	/**
	 * @var ep_Dataset
	 */
	protected $_orzeczenia_sp_osoby_stanowiska = null;


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
	public function orzeczenia_sp_osoby_stanowiska(){
		if( !$this->_orzeczenia_sp_osoby_stanowiska ) {
			$this->_orzeczenia_sp_osoby_stanowiska = new ep_Dataset( 'sp_ludzie_stanowiska' );
			$this->_orzeczenia_sp_osoby_stanowiska->init_where( 'orzeczenie_sp_stanowisko_id', '=', $this->id );
		}
		return $this->_orzeczenia_sp_osoby_stanowiska;
	}


}