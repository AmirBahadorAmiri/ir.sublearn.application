<?php

header('Content-Type: application/json; charset=utf-8');

require "../config/SQLHelper.php";
require "../signature/RandomString.php";

$sqlmanager = new SQLHelper();
$signature = new RandomString();
$array_result = array();
$array_result["result_code"] = 0;
$random_code = random_int(1000, 9999);
$currentTime = round(microtime(true));
$rows = array();

if (isset($_POST['user_email']) && isset($_POST['api_authorization_code'])) {
    $array_result["result_code"] = 1;
    $user_email = $_POST['user_email'];
    $api_authorization_code = $_POST['api_authorization_code'];
    $api_authorization_key = $signature->generateRandomString(16);

    $sqlmanager->sendQuery("SELECT * FROM app_users WHERE user_email = '$user_email'");
    if ($sqlmanager->getResult()->num_rows > 0) {
        while ($rows = $sqlmanager->getResult()->fetch_assoc()) {
            if ($user_email == $rows["user_email"]) {
                if ($api_authorization_code == $rows["api_authorization_code"]) {
                    if (($rows["api_authorization_time"] + 300) >= $currentTime) {
                        $array_result["result_code"] = 2;
                        $array_result["user"]["user_id"] = (int) $rows["user_id"];
                        $array_result["user"]["user_name"] = $rows["user_name"];
                        $array_result["user"]["user_email"] = $rows["user_email"];
                        $array_result["user"]["api_authorization_key"] = $api_authorization_key;
                    } else {
                        $array_result["result_code"] = 3;
                    }
                } else {
                    $array_result["result_code"] = 4;
                }
            }
        }
    }
    if ($array_result["result_code"] == 2) {
        $sqlmanager->sendQuery("UPDATE app_users SET api_authorization_key = '$api_authorization_key' , api_authorization_code = '$random_code' WHERE user_email = '$user_email'");
    }
}

switch ($array_result["result_code"]) {
    case 0:
        $array_result["description"] = "data inject";
        break;
    case 1:
        $array_result["description"] = "not found";
        break;
    case 2:
        $array_result["description"] = "ok";
        break;
    case 3:
        $array_result["description"] = "time left";
        break;
    case 4:
        $array_result["description"] = "api_authorization_code incorrect";
        break;
}

echo json_encode($array_result, JSON_PRETTY_PRINT | JSON_UNESCAPED_UNICODE);