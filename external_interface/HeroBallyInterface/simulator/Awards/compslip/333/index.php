<?php
date_default_timezone_set('Asia/Hong_Kong');
//header("content-type: application/json");

	$requestObject = json_decode(file_get_contents("php://input"), true);
	
	http_response_code(400);
	$returnResponse = array(
	"ErrorMessages" => ["Slip number #333 was settled"],
	"Result" => array(
			"Message" => "This request is invalid",
			"IsSuccess" => false
			)
	);
	
	echo json_encode($returnResponse);
	return;
	

?>