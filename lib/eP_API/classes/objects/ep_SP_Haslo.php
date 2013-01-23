<?php

class ep_SP_Haslo extends ep_Object{

	public $_aliases = array( 'sp_orzeczenia_hasla' );

	/**
	 * @var ep_SP_Haslo_Tematyczne
	 */
	protected $_orzeczenie_sp_haslo_tematyczne = null;

	/**
	 * @var ep_Orzeczenie_sp
	 */
	protected $_orzeczenie_sp = null;


	/**
	 * @return int
	 */
	public function get_id(){
		return (int) $this->data['id'];
	}

	/**
	 * @return int
	 */
	public function get_orzeczenie_sp_haslo_tematyczne_id(){
		return (int) $this->data['orzeczenie_sp_haslo_tematyczne_id'];
	}

	/**
	 * @return int
	 */
	public function get_orzeczenie_sp_id(){
		return (int) $this->data['orzeczenie_sp_id'];
	}

	/**
	 * @return ep_SP_Haslo_Tematyczne
	 */
	public function orzeczenie_sp_haslo_tematyczne(){
		if( !$this->_orzeczenie_sp_haslo_tematyczne ) {
			$this->_orzeczenie_sp_haslo_tematyczne = new ep_SP_Haslo_Tematyczne( $this->get_orzeczenie_sp_haslo_tematyczne_id() );
		}
		return $this->_orzeczenie_sp_haslo_tematyczne;
	}

	/**
	 * @return ep_SP_Orzeczenie
	 */
	public function orzeczenie_sp(){
		if( !$this->_orzeczenie_sp ) {
			$this->_orzeczenie_sp = new ep_SP_Orzeczenie( $this->get_orzeczenie_sp_id() );
		}
		return $this->_orzeczenie_sp;
	}


}