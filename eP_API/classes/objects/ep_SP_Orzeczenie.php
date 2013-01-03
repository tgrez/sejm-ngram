<?php

class ep_SP_Orzeczenie extends ep_Object{

	public $_aliases = array( 'sp_orzeczenia', 'sady_sp' );

	/**
	 * @var ep_Sad_sp
	 */
	protected $_sad_sp = null;

	/**
	 * @var ep_Dataset
	 */
	protected $_bloki = null;

	/**
	 * @var ep_Dataset
	 */
	protected $_hasla = null;

	/**
	 * @var ep_Dataset
	 */
	protected $_osoby_stanowiska = null;

	/**
	 * @var ep_Dataset
	 */
	protected $_przepisy = null;


	/**
	 * @return int
	 */
	public function get_akcept(){
		return (int) $this->data['akcept'];
	}
	
	/**
	 * @return string
	 */
	public function get_data(){
		return (string) $this->data['data'];
	}

	/**
	 * @return string
	 */
	public function get_hasla_tematyczne(){
		return (string) $this->data['hasla_tematyczne'];
	}
	
	/**
	 * @return int
	 */
	public function get_id(){
		return (int) $this->data['id'];
	}

	/**
	 * @return string
	 */
	public function get_podstawa_prawna(){
		return (string) $this->data['podstawa_prawna'];
	}

	/**
	 * @return int
	 */
	public function get_sad_sp_id(){
		return (int) $this->data['sad_sp_id'];
	}
	
	/**
	 * @return string
	 */
	public function get_sad(){
		return (string) $this->data['sad'];
	}
	
	/**
	 * @return string
	 */
	public function get_str_ident(){
		return (string) $this->data['str_ident'];
	}

	/**
	 * @return string
	 */
	public function get_sygnatura(){
		return (string) $this->data['sygnatura'];
	}

	/**
	 * @return string
	 */
	public function get_teza(){
		return (string) $this->data['teza'];
	}

	/**
	 * @return string
	 */
	public function get_typ(){
		return (string) $this->data['typ'];
	}

	/**
	 * @return int
	 */
	public function get_typ_id(){
		return (int) $this->data['typ_id'];
	}

	/**
	 * @return string
	 */
	public function get_wydzial(){
		return (string) $this->data['wydzial'];
	}

	/**
	 * @return string
	 */
	public function __toString(){
		return (string) $this->get_sygnatura();
	}

	/**
	 * @return ep_Sad_sp
	 */
	public function sad_sp(){
		if( !$this->_sad_sp ) {
			$this->_sad_sp = new ep_SP_Sad( $this->get_sad_sp_id() );
		}
		return $this->_sad_sp;
	}

	/**
	 * @return ep_Dataset
	 */
	public function bloki(){
		if( !$this->_bloki ) {
			$this->_bloki = new ep_Dataset( 'sp_orzeczenia_czesci' );
			$this->_bloki->init_where( 'orzeczenie_sp_id', '=', $this->id );
		}
		return $this->_bloki;
	}

	/**
	 * @return ep_Dataset
	 */
	public function hasla(){
		if( !$this->_hasla ) {
			$this->_hasla = new ep_Dataset( 'sp_orzeczenia_hasla' );
			$this->_hasla->init_where( 'orzeczenie_sp_id', '=', $this->id );
		}
		return $this->_hasla;
	}

	/**
	 * @return ep_Dataset
	 */
	public function osoby_stanowiska(){
		if( !$this->_osoby_stanowiska ) {
			$this->_osoby_stanowiska = new ep_Dataset( 'sp_ludzie' );
			$this->_osoby_stanowiska->init_layer('sp_ludzie_stanowiska')->init_where( 'sp_ludzie_stanowiska.orzeczenie_sp_id', '=', $this->id );
		}
		return $this->_osoby_stanowiska;
	}

	/**
	 * @return ep_Dataset
	 */
	public function przepisy(){
		if( !$this->_przepisy ) {
			$this->_przepisy = new ep_Dataset( 'sp_przepisy' );
			$this->_przepisy->init_where( 'orzeczenie_sp_id', '=', $this->id );
		}
		return $this->_przepisy;
	}


}