<?php

class ep_Kod_Pocztowy extends ep_Object{

  public $_aliases = array('kody_pocztowe');
	public $_field_init_lookup = 'kod';



  private $_gminy = false;
  
  
  
  public function gminy(){
	  if( !$this->_gminy ) {
		  
		  $this->_gminy = new ep_Dataset('gminy');
		  $this->_gminy->init_where('kody_pocztowe_gminy.kod_id', '=', $this->data['id'])->order_by('typ_id', 'ASC');
		  
	  }
	  return $this->_gminy;
  }




	/**
	 * @var ep_Wojewodztwo
	 */
	protected $_wojewodztwo = null;



	/**
	 * @return string
	 */
	public function get_id(){
		return (string) $this->data['id'];
	}

	/**
	 * @return string
	 */
	public function get_kod(){
		return (string) $this->data['kod'];
	}

	/**
	 * @return int
	 */
	public function get_kod_int(){
		return (int) $this->data['kod_int'];
	}

	/**
	 * @return int
	 */
	public function get_wojewodztwo_id(){
		return (int) $this->data['wojewodztwo_id'];
	}

	/**
	 * @return ep_Wojewodztwo
	 */
	public function wojewodztwo(){
		if( !$this->_wojewodztwo ) {
			$this->_wojewodztwo = new ep_Wojewodztwo( $this->get_wojewodztwo_id() );
		}
		return $this->_wojewodztwo;
	}


}