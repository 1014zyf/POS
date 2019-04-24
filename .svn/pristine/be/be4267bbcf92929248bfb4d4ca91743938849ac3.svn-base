<?php
date_default_timezone_set('Asia/Hong_Kong');
//header("content-type: application/json");

	$requestObject = json_decode(file_get_contents("php://input"), true);
	
	http_response_code(400);
	$returnResponse = array(
	"ErrorMessages" => ["Slip number #222 not found in HUB or data outsync"],
	"Result" => array(
			"Message" => "This request is invalid",
			"IsSuccess" => false
			)
	);
	
	echo json_encode($returnResponse);
	return;
	

?>