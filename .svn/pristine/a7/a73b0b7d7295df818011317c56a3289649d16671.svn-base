<?php
date_default_timezone_set('Asia/Hong_Kong');
//header("content-type: application/json");

	$requestObject = json_decode(file_get_contents("php://input"), true);
	
//	echo json_encode($requestObject['Acct']);
//	return;
	
//	echo print_r($requestObject, true);
//	return;
	
/*	if(isset($requestObject)){
		http_response_code(400);
		$returnMessage = array(
			"Code" => 1024,
			"Message" => "xcxzzczxc"
		);
		echo json_encode($returnMessage);
		return;
	}else{
		http_response_code(400);
		$returnMessage = array(
			"Code" => 1024,
			"Message" => 'yyyyyyyy'
		);
		echo json_encode($returnMessage);
		return;
	}
	*/

	$acctNo = $requestObject['Acct'];
	
	switch ($acctNo) {
	case "400":
		http_response_code(400);
		$returnMessage = array(
			"IsSuccess" => false,
			"Message" => "Invalid message content, please provide all the required information."
		);
		
		echo json_encode($returnMessage);
		return;
		break;
	case "401":
		http_response_code(401);
		$returnMessage = array(
			"IsSuccess" => false,
			"Message" => "You are unauthorized to access this resource"
			);
		
		echo json_encode($returnMessage);
		return;
		break;
	case "403":
		http_response_code(403);
		$returnMessage = array(
			"IsSuccess" => false,
			"Message" => "SystemId not found or does not have permission to call the target function."
			);
		
		echo json_encode($returnMessage);
		return;
		break;
	case "500":
		http_response_code(500);
		$returnMessage = array(
			"IsSuccess" => false,
			"Message" => "...",
			"exception" => "..."
		);
		
		echo json_encode($returnMessage);
		return;
		break;
	default:
		http_response_code(200);
		break;
	}
	
	switch ($acctNo) {
	case "9101":
	case "9102":
		$returnResponse = array(
			"Acct" => $acctNo,
			"GpcTranId" => "1111",
			"CmpTranId" => "90107515893",
			"Result" => array(
					"Message" => "Transaction Redeem Points is posted Successfully for the Account : " . $acctNo,
					"IsSuccess" => true
					)
		);
		echo json_encode($returnResponse);
		return;
		break;
	default:
		$returnResponse = array(
			"Acct" => $acctNo,
			"GpcTranId" => "1111",
			"CmpTranId" => "0",
			"Result" => array(
					"Message" => "Result failed, please check HUBAPI log to find the details",
					"IsSuccess" => false
					)
		);
		echo json_encode($returnResponse);
		return;
		break;
	}
?>