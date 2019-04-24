<?php
    date_default_timezone_set("Asia/Hong_Kong");
    http_response_code(200);
    $requestObject = json_decode(file_get_contents("php://input"), true);
    $accountName = $requestObject['data']['accountNum'];
    if ($accountName == "9101" || $accountName == "9102" || $accountName == "9103"){
        $respone = array(
            "errorCode"=> "",
            "errorMsg"=> "",
            "accountNum"=>$accountName,
            "clubState"=> 40,
        );
    }else{
        $respone = array(
            "errorCode"=> "10",
            "errorMsg"=> "Type is not valid",
        );
    }
    echo json_encode($respone);
    return;
    break;
?>