<?php
date_default_timezone_set('Asia/Hong_Kong');
//header("content-type: application/json");

	$requestObject = json_decode(file_get_contents("php://input"), true);
	
	http_response_code(200);
	$returnResponse = array(
	"Acct" => "9101",
	"Result" => array(
			"Message" => "Slip number#11 is belong to Acct#9101 and ready to use",
			"IsSuccess" => true
			)
	);
	
	echo json_encode($returnResponse);
	return;
	

?>