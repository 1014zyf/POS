<?php
    date_default_timezone_set("Asia/Hong_Kong");
    http_response_code(200);

    $requestObject = json_decode(file_get_contents("php://input"), true);
    $accountName = $requestObject['data']['accountNum'];
    if($accountName == "9101" || $accountName == "9102" ){
        $respone = array(
            "errorCode" => "",
            "errorMsg" => "",
            "player" => array(
                "lastName"=> "Chan",
                "firstName"=> "Gary",
                "middleName"=> "",
                "nickName"=> "陳加里"
            ),
            "bucketList"=> array(
                array(
                    "typeName"=> "Point",
                    "balance"=> 36.12
                ),
                array(
                    "typeName"=> "Point1",
                    "balance"=> 36.12
                ),
                array(
                    "typeName"=> "Point3",
                    "balance"=> 36.12
                ),
            )
        );
    }else{
        $respone = array(
            "errorCode" => "10",
            "errorMsg" => "Point Enquiry Error"
        );
    }
    echo json_encode($respone);
    return;
    break;
?>