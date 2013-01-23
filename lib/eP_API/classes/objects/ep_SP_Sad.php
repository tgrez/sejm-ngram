<?php

class ep_SP_Sad extends ep_Object{

	public $_aliases = array( 'sady_sp' );

	public $_field_init_lookup = 'nazwa';

	/**
	 * @var ep_Dataset
	 */
	protected $_orzeczenia_sp = null;


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
	//public function get_url(){
	//	return (string) $this->data['url'];
	//}
	
	/**
	 * @return string
	 */
	public function __toString(){
		return (string) $this->get_nazwa();
	}

	/**
	 * @return ep_Dataset
	 */
	public function orzeczenia_sp(){
		if( !$this->_orzeczenia_sp ) {
			$this->_orzeczenia_sp = new ep_Dataset( 'sp_orzeczenia' );
			$this->_orzeczenia_sp->init_where( 'sad_sp_id', '=', $this->id );
		}
		return $this->_orzeczenia_sp;
	}


}