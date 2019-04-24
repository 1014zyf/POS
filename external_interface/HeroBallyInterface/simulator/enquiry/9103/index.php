<?php
date_default_timezone_set('Asia/Hong_Kong');
	http_response_code(200);
	$returnResponse = array(
		"CardInfo" => array(
			"Acct" => "9103",
			"CardType" => "Gold",
			"ClubState" => "50",
			"Title" => "Miss", 
			"FirstName" => "FN9101",
			"LastName" => "LN9101",
			"ChineseName" => "Teng Teng",
			"DiscountPercentage" => 10.00,
			),
		"Result" => array(
				"Message" => "Successfully found valid card info.",
				"IsSuccess" => true
				)
	);
	echo json_encode($returnResponse);
	return;
	break;

?>