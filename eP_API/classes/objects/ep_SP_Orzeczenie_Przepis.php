<?php

class ep_SP_Orzeczenie_Przepis extends ep_Object{

	public $_aliases = array( 'sp_przepisy' );

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
	public function get_lp(){
		return (int) $this->data['lp'];
	}

	/**
	 * @return int
	 */
	public function get_orzeczenie_sp_id(){
		return (int) $this->data['orzeczenie_sp_id'];
	}

	/**
	 * @return string
	 */
	public function get_przepis(){
		return (string) $this->data['przepis'];
	}

	/**
	 * @return string
	 */
	public function __toString(){
		return (string) $this->get_przepis();
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