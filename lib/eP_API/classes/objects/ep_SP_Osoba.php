<?php

class ep_SP_Osoba extends ep_Object{

	public $_aliases = array( 'sp_ludzie' );

	public $_field_init_lookup = 'nazwa';

	/**
	 * @var ep_Dataset
	 */
	protected $_stanowiska = null;


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
	public function stanowiska(){
		if( !$this->_stanowiska ) {
			$this->_stanowiska = new ep_Dataset( 'sp_ludzie_stanowiska' );
			$this->_stanowiska->init_where( 'orzeczenie_sp_osoba_id', '=', $this->id );
		}
		return $this->_stanowiska;
	}


}