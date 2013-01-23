<?php
class ep_Posel extends ep_Object{
  
  public $_aliases = array('poslowie','ludzie_poslowie');
  public $_field_init_lookup = 'nazwa';
  
  private $_wystapienia = false;
  private $_glosy = false;
  private $_mowca = false;
  private $_klub = false;
  private $_wspolpracownicy = false;
  private $_oswiadczenia_majatkowe = false;
  private $_rejestr_korzysci = false;
  private $_projekty_ustaw = false;
  private $_projekty_uchwal = false;
  private $_aktywnosci = false;
  private $_twitty = false;  
  
  
  public function set_ep_ludzie($data){
	  
	  $this->_mowca = new ep_Czlowiek($data);
	  
  }
  
  public function set_ep_sejm_kluby($data){
	  
	  $this->_klub = new ep_Sejm_Klub($data);
	  
  }
  
  public function mowca(){
	  
	  return $this->_mowca;
	  
  }
  
  public function klub(){
	  
	  return $this->_klub;
	  
  }
  
  public function aktywnosci(){
	  
	  if( !$this->_aktywnosci ) {
	    	  
	    $this->_aktywnosci = new ep_Dataset('poslowie_aktywnosci');
	    $this->_aktywnosci->init_where('posel_id', '=', $this->data['id']);
    
    }
    return $this->_aktywnosci;
	  
  }
  
  public function wystapienia(){
	  
	  if( !$this->_wystapienia ) {
	    	  
	    $this->_wystapienia = new ep_Dataset('sejm_wystapienia');
	    $this->_wystapienia->init_where('czlowiek_id', '=', $this->data['mowca_id']);
    
    }
    return $this->_wystapienia;
	  
  }
  
  
  public function glosy(){
	  
	  if( !$this->_glosy ) {
	    	  
	    $this->_glosy = new ep_Dataset('poslowie_glosy');
	    $this->_glosy->init_where('posel_id', '=', $this->id);
    
    }
    return $this->_glosy;
	  
  }

  public function twitty(){
	  
	  if( !$this->_twitty ) {
	    	  
	    $this->_twitty = new ep_Dataset('twitter');
	    $this->_twitty->init_where('posel_id', '=', $this->id);
    
    }
    return $this->_twitty;
	  
  }
    
  public function projekty_ustaw(){
	  
	  if( !$this->_projekty_ustaw ) {
	    	  
	    $this->_projekty_ustaw = new ep_Dataset('legislacja_projekty_ustaw');
      $this->_projekty_ustaw->init_where('poslowie.id', '=', $this->id);
    
    }
    return $this->_projekty_ustaw;
	  
  }
  
  public function projekty_uchwal(){
	  
	  if( !$this->projekty_uchwal ) {
	    	  
	    $this->projekty_uchwal = new ep_Dataset('legislacja_projekty_uchwal');
      $this->projekty_uchwal->init_where('poslowie.id', '=', $this->id);
    
    }
    return $this->projekty_uchwal;
	  
  }
  
  public function komisje_stanowiska(){
	  
	  if( !$this->_komisje_stanowiska ) {
	    	  
	    $this->_komisje_stanowiska = new ep_Dataset('poslowie_komisje_stanowiska');
      $this->_komisje_stanowiska->init_where('poslowie.id', '=', $this->id);
    
    }
    return $this->_komisje_stanowiska;
	  
  }
  
  public function wspolpracownicy(){
	  
	  if( !$this->_wspolpracownicy ) {
	    	  
	    $this->_wspolpracownicy = new ep_Dataset('poslowie_wspolpracownicy');
      $this->_wspolpracownicy->init_where('poslowie.id', '=', $this->id);
    
    }
    return $this->_wspolpracownicy;
	  
  }
  
  public function oswiadczenia_majatkowe(){
	  
	  if( !$this->_oswiadczenia_majatkowe ) {
	    	  
	    $this->_oswiadczenia_majatkowe = new ep_Dataset('poslowie_oswiadczenia_majatkowe');
      $this->_oswiadczenia_majatkowe->init_where('poslowie.id', '=', $this->id);
    
    }
    return $this->_oswiadczenia_majatkowe;
	  
  }
  
  public function rejestr_korzysci(){
	  
	  if( !$this->_rejestr_korzysci ) {
	    	  
	    $this->_rejestr_korzysci = new ep_Dataset('poslowie_rejestr_korzysci');
      $this->_rejestr_korzysci->init_where('poslowie.id', '=', $this->id);
    
    }
    return $this->_rejestr_korzysci;
	  
  }
  
  
  
  
  
  
  
  
  
  
  
  
  
	/**
	 * @return int
	 */
	public function get_id(){
		return (int)$this->data['id'];
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
	public function get_imie(){
		return (string)$this->data['imie'];
	}

	/**
	 * @return int 
	 */
	public function get_nazwisko(){
		return (string)$this->data['nazwisko'];
	}

	/**
	 * @return int 
	 */
	public function get_zawod(){
		return (string)$this->data['zawod'];
	}

	/**
	 * @return int 
	 */
	public function get_plec(){
		return (string)$this->data['plec'];
	}

	/**
	 * @return int 
	 */
	public function get_data_urodzenia(){
		return (string)$this->data['data_urodzenia'];
	}

	/**
	 * @return int 
	 */
	public function get_miejsce_urodzenia(){
		return (string)$this->data['miejsce_urodzenia'];
	}
	/**
	 * @return int 
	 */
	public function get_nr_okregu(){
		return (int)$this->data['nr_okregu'];
	}
	
	/**
	 * @return string 
	 */
	public function __toString(){
		return $this->get_nazwa();
	}
}