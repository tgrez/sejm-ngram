<?php
class ep_Senat_senator_komisja extends ep_Object{
  
	public $_aliases = array('senat_senatorowie_komisje');

	private $_komisja = false;
	private $_senator = false; 
  
	/**
	 * @return int
	 */
	public function get_id(){
		return (int)$this->data['id'];
	}


	/**
	 * @return int
	 */
	public function get_komisja_id(){
		return (int)$this->data['komisja_id'];
	}

	/**
	 * @return int
	 */
	public function get_senator_id(){
		return (int)$this->data['senator_id'];
	}

	/**
	 * @return string
	 */
	public function get_stanowisko(){
		$str = trim( ucfirst( mb_strtolower( $this->data['stanowisko'], 'UTF-8' ) ) );
		if( $str == '' ){
			$str = 'Członek';
		}
		return (string)$str;
	}
			
	/**
	 * @return string 
	 */
	public function __toString(){
		return $this->get_stanowisko();
	}
	
	public function komisja(){
		if( !$this->_komisja ){
			$this->_komisja = new ep_Senat_komisja($this->get_komisja_id() );
		}	
		return $this->_komisja;
	}
	
	public function senator(){
		if( !$this->_senator ){
			$this->_senator = new ep_Senator($this->get_senator_id() );
		}	
		return $this->_senator;
	}

}