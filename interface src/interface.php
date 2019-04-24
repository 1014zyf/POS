<?php
	error_reporting(0);
	date_default_timezone_set('Asia/Taipei');
	
	if (get_magic_quotes_gpc()) {	//	Returns 0 if magic_quotes_gpc is off, 1 otherwise. Or always returns FALSE as of PHP 5.4.0.
		$process = array(&$_GET, &$_POST, &$_COOKIE, &$_REQUEST);
		while (list($key, $val) = each($process)) {
			foreach ($val as $k => $v) {
				unset($process[$key][$k]);
				if (is_array($v)) {
					$process[$key][stripslashes($k)] = $v;
					$process[] = &$process[$key][stripslashes($k)];
				} else {
					$process[$key][stripslashes($k)] = stripslashes($v);
				}
			}
		}
		unset($process);
	}
	if (function_exists('set_magic_quotes_runtime'))	//	function removed in PHP7+
		set_magic_quotes_runtime(false);
	
	// Read ini class
	class ini {
		protected $lines;
		public function read($file) {
			$this->lines = array();

			$section = '';

			foreach(file($file) as $line) {
				// comment or whitespace
				if(preg_match('/^\s*(;.*)?$/', $line)) {
					$this->lines[] = array('type' => 'comment', 'data' => $line);
				// section
				} elseif(preg_match('/\[(.*)\]/', $line, $match)) {
					$section = $match[1];
					$this->lines[] = array('type' => 'section', 'data' => $line, 'section' => $section);
				// entry
				} elseif(preg_match('/^\s*(.*?)\s*=\s*(.*?)\s*$/', $line, $match)) {
					$this->lines[] = array('type' => 'entry', 'data' => $line, 'section' => $section, 'key' => $match[1], 'value' => $match[2]);
				}
			}
		}

		public function get($section, $key) {
			foreach($this->lines as $line) {
				if($line['type'] != 'entry') continue;
				if($line['section'] != $section) continue;
				if($line['key'] != $key) continue;
				return $line['value'];
			}

			//throw new Exception('Missing Section or Key');
			return "";
		}
	}
	
	/***** Web Service Function *****/
	// Connect to POS server launcher
	function connect($id, $password, $UDID, $swipe_card_login = "", $display_mode = "", $extra_info = "", $access_token = ""){
		$responseJsonInfo = "";
		//get server IP and port setting
		$ini = new ini();
		$ini->read("cfg/config.ini");
		$ipAddress = $ini->get("connection", "server_ip_address");
		$port = $ini->get("connection", "launcher_port");

		// Allow the script to hang around waiting for connections.
		$success = 0;
		$packetTimeout = 320;
		$sendTime = time();
		set_time_limit($packetTimeout * 2);
			
		// Turn on implicit output flushing so we see what we're getting as it comes in. 
		ob_implicit_flush();
	
		if (($sock = socket_create(AF_INET, SOCK_STREAM, SOL_TCP)) === false) {
			$responseJsonInfo = '{"error": { "code": "00001", "message": "Fail to create socket that connect POS server '.$ipAddress.'"}}';
		
		}else {
			if (!socket_set_block($sock)){
				die('Unable to set blocking mode for socket');
			}
				
			if ($result = socket_connect($sock, $ipAddress, $port) === false) {
				$responseJsonInfo = '{"error": { "code": "00002", "message": "Fail to connect POS server '.$ipAddress.' with port '.$port.'"}}';
				
			}else {
				// Create the packet with client UDID to launcher
				$connectStr = array(
					'type' => 'Connect', 
					'udid' => $UDID, 
					'login' => $id, 
					'password' => $password, 
					'swipe_card_login' => $swipe_card_login, 
					'display_mode' => $display_mode, 
					'extra_info' => $extra_info, 
					'access_token' => $access_token
				);
				$talkback = json_encode($connectStr).chr(0x04);

				if ($result = socket_write($sock, $talkback, strlen($talkback)) === false) {
					$responseJsonInfo = '{"error": { "code": "00004", "message": "Fail to send packet"}}';
				
				}else {
					$responseJsonInfo = $result;
					$success = 0;
					$finalReply = "";
					$buffer = "";
					do {
						$recv = "";
						error_reporting(0);
						socket_set_option($sock, SOL_SOCKET, SO_RCVTIMEO, array('sec' => $packetTimeout,'usec' => 0));
						$recv = socket_read($sock, '100');
						error_reporting(1);
						$buffer .= $recv;
						//echo $buffer."<br>";
						if ($recv == "") {
							$success = 1;
							break;
						}
						 
					} while(true);
					
					$nowTime = time();
					if (($nowTime - $sendTime) > $packetTimeout) {
						$responseJsonInfo = '{"error": { "code": "00005", "message": "Packet timeout"}}';
						
					}else {
						if ($success == 1) {
							$responseJsonInfo = $buffer;
						}else {
							$responseJsonInfo = '{"error": { "code": "00005", "message": "Packet timeout"}}';
							
						}
					}
				}
			}
		}
		socket_set_option($sock, SOL_SOCKET, SO_LINGER, array ('l_linger' => 0, 'l_onoff' => 1));
		socket_close($sock);
		
		return $responseJsonInfo;
	}
	
	// Connect to POS server application
	function callServer($requestJsonInfo, $portNo, $packetSequence){
		
		$responseJsonInfo = "";
		//get server IP and port setting
		$ini = new ini();
		$ini->read("cfg/config.ini");
		$ipAddress = $ini->get("connection", "server_ip_address");

		// Allow the script to hang around waiting for connections.
		$success = 0;
		$packetTimeout = 320;
		$sendTime = time();
		set_time_limit($packetTimeout * 2);
		
		session_start();
		
		// Turn on implicit output flushing so we see what we're getting as it comes in. 
		ob_implicit_flush();

		// Sequence no. checking
		if(is_numeric($packetSequence)){
			// Check if the request is processed before
			if(!empty($_SESSION['stored_request_'.$portNo.'_'.$packetSequence])){
				// Request is processed and the response is stored
				$responseJsonInfo = $_SESSION['stored_request_'.$portNo.'_'.$packetSequence];

				// Send back the stored response
				return "[".$responseJsonInfo."]";
			}
		
			if(!empty($_SESSION['next_sequence_'.$portNo])){
				if($packetSequence != $_SESSION['next_sequence_'.$portNo]){
					// The incoming packet sequence is NOT the next sequence
					if($packetSequence > ($_SESSION['next_sequence_'.$portNo] + 3)){
						// The sequence is loss too much, skip those packet and resume the sequence

					}else{
						// 1. Store the request
						$_SESSION['wait_'.$portNo.'_'.$packetSequence] = $requestJsonInfo;
						
						// 2. Send back the ACK
						return "[]";
					}
				}
			}
		}
		
        $allResponseJsonInfos = array();
		while(true){
			if (($sock = socket_create(AF_INET, SOCK_STREAM, SOL_TCP)) === false) {
				$responseJsonInfo = '{"error": { "code": "00001", "message": "Fail to create socket that connect POS server '.$ipAddress.'"}}';
			
			}else {
				if (!socket_set_block($sock)){
					die('Unable to set blocking mode for socket');
				}
                
				if ($result = socket_connect($sock, $ipAddress, $portNo) === false) {
					$responseJsonInfo = '{"error": { "code": "00002", "message": "Fail to connect POS server '.$ipAddress.' with port '.$portNo.'"}}';
					
				}else {
					$talkback = $requestJsonInfo.chr(0x04);

					if ($result = socket_write($sock, $talkback, strlen($talkback)) === false) {
						$responseJsonInfo = '{"error": { "code": "00004", "message": "Fail to send packet"}}';
					
					}else {					
						$responseJsonInfo = $result;
						$success = 0;
						$finalReply = "";
						$buffer = "";
						do {
							$recv = "";
							error_reporting(0);
							socket_set_option($sock, SOL_SOCKET, SO_RCVTIMEO, array('sec' => $packetTimeout,'usec' => 0));
							$recv = socket_read($sock, '100');
							error_reporting(1);
							$buffer .= $recv;
							if ($recv == "") {
								$success = 1;
								break;
							}
							 
						} while(true);
						
						$nowTime = time();
						if (($nowTime - $sendTime) > $packetTimeout) {
							$responseJsonInfo = '{"error": { "code": "00005", "message": "Packet timeout"}}';
							
						}else {
							if ($success == 1) {
								$responseJsonInfo = $buffer;
							}else {
								$responseJsonInfo = '{"error": { "code": "00005", "message": "Packet timeout"}}';
							}
						}
					}
				}
			}
			socket_set_option($sock, SOL_SOCKET, SO_LINGER, array ('l_linger' => 0, 'l_onoff' => 1));
			socket_close($sock);
			
			// Store the packet for error resume
			//if(is_numeric($packetSequence))
				$_SESSION['stored_request_'.$portNo.'_'.$packetSequence] = $responseJsonInfo;
			
			// Clear the packet with too early sequence no.
			if($packetSequence > 30){
				$oldSequence = $packetSequence - 30;
				unset($_SESSION['stored_request_'.$portNo.'_'.$oldSequence]);
			}

			// Create the final packet
            if (!empty($responseJsonInfo))
                $allResponseJsonInfos[] = $responseJsonInfo;

			if(is_numeric($packetSequence)){
				// Update sequence no.
				$nextSequence = $packetSequence + 1;
				$packetSequence = $nextSequence;
				
				// Save next sequence no.
				$_SESSION['next_sequence_'.$portNo] = $packetSequence;

				// Check if need to handle the stored request
				if(!empty($_SESSION['wait_'.$portNo.'_'.$packetSequence])){
					// Load the request to memory
					$requestJsonInfo = $_SESSION['wait_'.$portNo.'_'.$packetSequence];
					
					// Clear the packet stored in session
					unset($_SESSION['wait_'.$portNo.'_'.$packetSequence]);
					
					// Continue the loop to process the stored request
					continue;
				}
			}
			
			// Break the loop
			break;
		}
		
		//	Remove session to performance issue due to too many session files
		if(!is_numeric($packetSequence))
			session_destroy();
		
		return '['.implode(',', $allResponseJsonInfos).']';
	}
	
	/***** Web Service Function *****/
	// Ask POS server launcher to execute
	function execute($task, $params){
		$responseJsonInfo = "";
		//get server IP and port setting
		$ini = new ini();
		$ini->read("cfg/config.ini");
		$ipAddress = $ini->get("connection", "server_ip_address");
		$port = $ini->get("connection", "launcher_port");

		// Allow the script to hang around waiting for connections.
		$success = 0;
		$packetTimeout = 320;
		$sendTime = time();
		set_time_limit($packetTimeout * 2);
			
		// Turn on implicit output flushing so we see what we're getting as it comes in. 
		ob_implicit_flush();
	
		if (($sock = socket_create(AF_INET, SOCK_STREAM, SOL_TCP)) === false) {
			$responseJsonInfo = '{"error": { "code": "00001", "message": "Fail to create socket that connect POS server '.$ipAddress.'"}';
		
		}else {
			if (!socket_set_block($sock)){
				die('Unable to set blocking mode for socket');
			}
				
			if ($result = socket_connect($sock, $ipAddress, $port) === false) {
				$responseJsonInfo = '{"error": { "code": "00002", "message": "Fail to connect POS server '.$ipAddress.' with port '.$port.'"}';
				
			}else {
				// Create the packet with client UDID to launcher
				$connectStr = array('task' => $task, 'params' => $params);
				$talkback = json_encode($connectStr).chr(0x04);

				if ($result = socket_write($sock, $talkback, strlen($talkback)) === false) {
					$responseJsonInfo = '{"error": { "code": "00004", "message": "Fail to send packet"}';
				
				}else {
					$responseJsonInfo = $result;
					$success = 0;
					$finalReply = "";
					$buffer = "";
					do {
						$recv = "";
						error_reporting(0);
						socket_set_option($sock, SOL_SOCKET, SO_RCVTIMEO, array('sec' => $packetTimeout,'usec' => 0));
						$recv = socket_read($sock, '100');
						error_reporting(1);
						$buffer .= $recv;
						//echo $buffer."<br>";
						if ($recv == "") {
							$success = 1;
							break;
						}
						 
					} while(true);
					
					$nowTime = time();
					if (($nowTime - $sendTime) > $packetTimeout) {
						$responseJsonInfo = '{"error": { "code": "00005", "message": "Packet timeout"}';
						
					}else {
						if ($success == 1) {
							$responseJsonInfo = $buffer;
						}else {
							$responseJsonInfo = '{"error": { "code": "00005", "message": "Packet timeout"}';
							
						}
					}
				}
			}
		}
		socket_set_option($sock, SOL_SOCKET, SO_LINGER, array ('l_linger' => 0, 'l_onoff' => 1));
		socket_close($sock);
		
		return $responseJsonInfo;
	}
    
	if (!empty($_SERVER["HTTP_SOAPACTION"])) {
		//	WSDL interface
		include_once("lib/nusoap.php");
		
		//create the web service
		$server = new soap_server();
		
		// set encoding to UTF-8
		$server->soap_defencoding = 'UTF-8';
		$server->decode_utf8 = false;
		
		//form the wdsl automatically
		$server->configureWSDL('interface', 'urn:interface');
		$server->register('callServer', 
			array('requestInfo' => 'xsd:string','xsd:integer','xsd:integer'), 
			array('responseInfo' => 'xsd:string'),
			'xsd:interface');
		$server->register('connect', 
			array('requestInfo' => 'xsd:string','xsd:string','xsd:string','xsd:string','xsd:string'), 
			array('responseInfo' => 'xsd:string'),
			'xsd:interface');
		$server->register('execute', 
			array('requestInfo' => 'xsd:string','xsd:string'), 
			array('responseInfo' => 'xsd:string'),
			'xsd:interface');

		$HTTP_RAW_POST_DATA = isset($HTTP_RAW_POST_DATA)? $HTTP_RAW_POST_DATA : '';
		$server->service($HTTP_RAW_POST_DATA);
	}
	else {
        //	Http interface
		if (empty($_POST)) {
			$errorKey = 'invalid_format';
		}
        
		$data = '';
        
		switch ($_POST['action']) {
		case 'connect':
			$login = empty($_POST['login']) ? '' : $_POST['login'];
			$password = empty($_POST['password']) ? '' : $_POST['password'];
			$udid = empty($_POST['udid']) ? '' : $_POST['udid'];
			$swipe_card_login = empty($_POST['swipe_card_login']) ? '' : $_POST['swipe_card_login'];
			$display_mode = empty($_POST['display_mode']) ? 'horizontal_desktop' : $_POST['display_mode'];
			$extra_info = empty($_POST['extra_info']) ? '' : $_POST['extra_info'];
			$access_token = empty($_POST['access_token']) ? '' : $_POST['access_token'];
			$data = connect($login, $password, $udid, $swipe_card_login, $display_mode, $extra_info, $access_token);
			break;
		case 'execute':
			$task = empty($_POST['task']) ? '' : $_POST['task'];
			$params = empty($_POST['params']) ? '' : $_POST['params'];
			$data = execute($task, $params);
			break;
		case 'callServer':
		default:
			$params = empty($_POST['params']) ? '' : $_POST['params'];
			$port = empty($_POST['port']) ? '' : $_POST['port'];
			$seq = empty($_POST['seq']) ? '' : $_POST['seq'];
			$data = callServer($params, $port, $seq);
			break;
		}
        
		if (!empty($errorKey))
			$data = json_encode(array('error' => $errorKey));
		
		echo $data;
	}
?>