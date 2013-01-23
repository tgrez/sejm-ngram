<?
 class ep_Api {
	
		public $_version = '0.1';
		public $server_address = 'http://ep_api.sejmometr.pl/v1/'; 
		private $_key = eP_API_KEY;
		private $_secret = eP_API_SECRET;
	  
	  
	
		/**
		 * @return ep_Api
		 */
		static public function init(){
			return new ep_Api();
		}
		
		public function call( $service, $params ){
	    $service = trim( $service );
	    
	    if( !$service )
	      return false;	    
	    
      parse_str( http_build_query( $params ), $params );
      $params[ 'sign' ] = $this->generate_sig( $params );
      $params[ 'key' ] = $this->_key;

      $request_url = $this->server_address . $service;
      	
      
      $data = '';
      $ch = curl_init();
      curl_setopt($ch, CURLOPT_URL, $request_url );
      curl_setopt($ch, CURLOPT_RETURNTRANSFER,1);
      curl_setopt($ch, CURLOPT_POST, 1 );
      curl_setopt($ch, CURLOPT_POSTFIELDS, http_build_query( $params ) );
      $data = curl_exec( $ch );
      
      
      $this->http_code = curl_getinfo($ch, CURLINFO_HTTP_CODE);
      curl_close($ch);
      
      switch( $this->http_code ) {
	      
	      case '401': {
		      throw new Exception('Brak kluczy eP_API. Zarejestruj konto na portalu http://sejmometr.pl i pobierz swoje prywatne klucze.');
	      }
	      
	      case '402': {
		      throw new Exception('Przekroczony limit żądań (3000 żądań na dobę).');
	      }
	      
	      case '200': {
		      return json_decode( $data, true );
	      }
	      
      }

      

    }
		
		/**
		 * @param array $params
		 * @return string
		 */
		private function generate_sig( $params ){	
			$str = json_encode( $params );
			$str .= $this->_secret;
		
			return md5( $str );		 
		}
	}
?>