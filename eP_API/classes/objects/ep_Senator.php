<?php
class ep_Senator extends ep_Object{
  
	public $_aliases = array('senatorowie');
	public $_field_init_lookup = 'nazwa';
  
    private $_komisje = false;
    private $_zespoly_parlamentarne = false;
    private $_zespoly_senackie = false;
    private $_oswiadczenia_majatkowe = false;
    private $_rejestr_korzysci = false;
    private $_wystapienia = false;
	/**
	 * @return int
	 */
	public function get_id(){
		return (int)$this->data['id'];
	}

	/**
	 * @return int
	 */
	public function get_mowca_id(){
		return (int)$this->data['mowca_id'];
	}

	/**
	 * @return string
	 */
	public function get_nazwa(){
		return (string)$this->data['nazwa'];
	}
	
	/**
	 * @return string 
	 */
	public function __toString(){
		return $this->get_nazwa();
	}
	
	public function komisje(){
	  
		if( !$this->_komisje ) {			  
			$this->_komisje = new ep_Dataset('senat_senatorowie_komisje');
			$this->_komisje->init_where('senator_id', '=', $this->id);
		}
		return $this->_komisje;
	}
	
	public function zespoly_parlamentarne(){
		if( !$this->_zespoly_parlamentarne ) {			  
			$this->_zespoly_parlamentarne = new ep_Dataset('senat_senatorowie_zespoly_parlamentarne');
			$this->_zespoly_parlamentarne->init_where('senator_id', '=', $this->id);
		}
		return $this->_zespoly_parlamentarne;	
	}
	
	public function zespoly_senackie(){
		if( !$this->_zespoly_senackie ) {			  
			$this->_zespoly_senackie = new ep_Dataset('senat_senatorowie_zespoly_senackie');
			$this->_zespoly_senackie->init_where('senator_id', '=', $this->id);
		}
		return $this->_zespoly_senackie;	
	}
	
	public function oswiadczenia_majatkowe(){
		if( !$this->_oswiadczenia_majatkowe ) {
			$this->_oswiadczenia_majatkowe = new ep_Dataset('senatorowie_oswiadczenia_majatkowe');
			$this->_oswiadczenia_majatkowe->init_where('senator_id', '=', $this->id);

		}
		return $this->_oswiadczenia_majatkowe;
	}
	
	public function rejestr_korzysci(){
		if( !$this->_rejestr_korzysci ) {
			$this->_rejestr_korzysci = new ep_Dataset('senat_rejestr_korzysci');
			$this->_rejestr_korzysci->init_where('senator_id', '=', $this->id);

		}
		return $this->_rejestr_korzysci;
	}
	
	public function wystapienia(){
		if( !$this->_wystapienia ){
			$this->_wystapienia = new ep_Dataset('senat_wystapienia');
			$this->_wystapienia->init_where('mowca_id', '=', $this->get_mowca_id());
		}
		return $this->_wystapienia;
	}	
}