<?php

class ep_SN_Orzeczenie extends ep_Object{

	public $_aliases = array( 'sn_orzeczenia' );

	public $_field_init_lookup = 'sygnatura';

	/**
	 * @var ep_SN_Orzeczenie_Forma
	 */
	protected $_forma = null;

	/**
	 * @var ep_SN_Jednostka
	 */
	protected $_jednostka = null;

	/**
	 * @var ep_SN_Sklad
	 */
	protected $_sklad = null;

	/**
	 * @var ep_Dataset
	 */
	protected $_autorzy = null;

	/**
	 * @var ep_Dataset
	 */
	protected $_izby_orzeczenia = null;

	/**
	 * @var ep_Dataset
	 */
	protected $_sedziowie = null;

	/**
	 * @var ep_Dataset
	 */
	protected $_sprawozdawcy = null;

	/**
	 * @var ep_Dataset
	 */
	protected $_wspolsprawozdawcy = null;


	/**
	 * @return string
	 */
	public function get_akcept(){
		return (string) $this->data['akcept'];
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
	public function get_dokument_id(){
		return (string) $this->data['dokument_id'];
	}

	/**
	 * @return string
	 */
	public function get_forma(){
		return (string) $this->data['forma'];
	}

	/**
	 * @return int
	 */
	public function get_id(){
		return (int) $this->data['id'];
	}

	/**
	 * @return int
	 */
	public function get_item_id(){
		return (int) $this->data['item_id'];
	}

	/**
	 * @return string
	 */
	public function get_izby_str(){
		return (string) $this->data['izby_str'];
	}

	/**
	 * @return string
	 */
	public function get_jednostka_str(){
		return (string) $this->data['jednostka_str'];
	}

	/**
	 * @return int
	 */
	public function get_orzeczenie_sn_forma_id(){
		return (int) $this->data['orzeczenie_sn_forma_id'];
	}

	/**
	 * @return int
	 */
	public function get_orzeczenie_sn_jednostka_id(){
		return (int) $this->data['orzeczenie_sn_jednostka_id'];
	}

	/**
	 * @return int
	 */
	public function get_orzeczenie_sn_sklad_id(){
		return (int) $this->data['orzeczenie_sn_sklad_id'];
	}

	/**
	 * @return string
	 */
	public function get_przewodniczacy(){
		return (string) $this->data['przewodniczacy'];
	}

	/**
	 * @return int
	 */
	public function get_przewodniczacy_id(){
		return (int) $this->data['przewodniczacy_id'];
	}

	/**
	 * @return string
	 */
	public function get_sprawozdawcy_str(){
		return (string) $this->data['sprawozdawcy_str'];
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
	public function get_wspolsprawozdawcy_str(){
		return (string) $this->data['wspolsprawozdawcy_str'];
	}

	/**
	 * @return string
	 */
	public function __toString(){
		return (string) $this->get_sygnatura();
	}

	/**
	 * @return ep_SN_Orzeczenie_Forma
	 */
	public function forma(){
		if( !$this->_forma ) {
			$this->_forma = new ep_SN_Orzeczenie_Forma( $this->get_orzeczenie_sn_forma_id() );
		}
		return $this->_forma;
	}

	/**
	 * @return ep_SN_Jednostka
	 */
	public function jednostka(){
		if( !$this->_jednostka ) {
			$this->_jednostka = new ep_SN_Jednostka( $this->get_orzeczenie_sn_jednostka_id() );
		}
		return $this->_jednostka;
	}

	/**
	 * @return ep_SN_Sklad
	 */
	public function sklad(){
		if( !$this->_sklad ) {
			$this->_sklad = new ep_SN_Sklad( $this->get_orzeczenie_sn_sklad_id() );
		}
		return $this->_sklad;
	}

	/**
	 * @return ep_Dataset
	 */
	public function autorzy(){
		if( !$this->_autorzy ) {
			$this->_autorzy = new ep_Dataset( 'sn_orzeczenia_ludzie' );
			$this->_autorzy->init_where( 'orzeczenie_sn_id', '=', $this->id );
		}
		return $this->_autorzy;
	}

	/**
	 * @return ep_Dataset
	 */
	public function izby_orzeczenia(){
		if( !$this->_izby_orzeczenia ) {
			$this->_izby_orzeczenia = new ep_Dataset( 'sn_izby-orzeczenia' );
			$this->_izby_orzeczenia->init_where( 'orzeczenie_sn_id', '=', $this->id );
		}
		return $this->_izby_orzeczenia;
	}

	/**
	 * @return ep_Dataset
	 */
	public function sedziowie(){
		if( !$this->_sedziowie ) {
			$this->_sedziowie = new ep_Dataset( 'sn_sedziowie' );
			$this->_sedziowie->init_where( 'orzeczenie_sn_id', '=', $this->id );
		}
		return $this->_sedziowie;
	}

	/**
	 * @return ep_Dataset
	 */
	public function sprawozdawcy(){
		if( !$this->_sprawozdawcy ) {
			$this->_sprawozdawcy = new ep_Dataset( 'sn_sprawozdawcy' );
			$this->_sprawozdawcy->init_where( 'orzeczenie_sn_id', '=', $this->id );
		}
		return $this->_sprawozdawcy;
	}

	/**
	 * @return ep_Dataset
	 */
	public function wspolsprawozdawcy(){
		if( !$this->_wspolsprawozdawcy ) {
			$this->_wspolsprawozdawcy = new ep_Dataset( 'sn_wspolsprawozdawcy' );
			$this->_wspolsprawozdawcy->init_where( 'orzeczenie_sn_id', '=', $this->id );
		}
		return $this->_wspolsprawozdawcy;
	}


}