<?php

class ep_SP_Orzeczenie_Czesc extends ep_Object{

	public $_aliases = array( 'sp_orzeczenia_czesci' );

	/**
	 * @var ep_SP_Orzeczenie
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
	public function get_orzeczenie_sp_id(){
		return (int) $this->data['orzeczenie_sp_id'];
	}

	/**
	 * @return string
	 */
	public function get_tytul(){
		return (string) $this->data['tytul'];
	}

	/**
	 * @return string
	 */
	public function get_wartosc(){
		if( file_exists( ROOT . '/../epanstwo/_resources/sp/bloki/' . $this->get_id() . '.html') ) {
			return file_get_contents(ROOT . '/../epanstwo/_resources/sp/bloki/' . $this->get_id() . '.html') ;
		} else {
			return (string) $this->data['wartosc'];
		}
	}
		
	

	/**
	 * @return string
	 */
	public function __toString(){
		return (string) $this->get_tytul();
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