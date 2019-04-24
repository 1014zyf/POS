<?php
    date_default_timezone_set("Asia/Hong_Kong");
    http_response_code(200);
    $requestObject = json_decode(file_get_contents("php://input"), true);
    $accountName = $requestObject['data']['cardNum'];
    if ($accountName == "09101"){
        $respone = array(
            "errorCode" => "",
            "errorMsg"=> "",
            "cardNum"=> $accountName,
            "result"=> true,
        );
    }else if ($accountName == "09102"){
        $respone =array(
            "result"=> false,
        );
    }else{
        $respone = array(
            "errorCode" => "10",
            "errorMsg"=> "Card is not valid"
        );
    }

    echo json_encode($respone);
    return;
    break;
?>