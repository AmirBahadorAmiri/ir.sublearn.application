<?php

header('Content-Type: application/json; charset=utf-8');

require "../config/SQLHelper.php";

$sqlmanager = new SQLHelper();
$array_result = array();
$array_result["result_code"] = 0;

if ( isset($_POST['user_email']) && isset($_POST["user_password"]) && isset($_POST["user_new_password"]) && isset($_POST['api_authorization_key']) ) {
    $array_result["result_code"] = 1;
    $user_email = $_POST["user_email"];
    $user_password = $_POST["user_password"];
    $user_new_password = $_POST["user_new_password"];
    $api_authorization_key = $_POST["api_authorization_key"];

    $sqlmanager->sendQuery("SELECT * FROM app_users WHERE user_email = '$user_email'");
    if ($sqlmanager->getResult()->num_rows > 0 ) {
        while ( $rows = $sqlmanager->getResult()->fetch_assoc() ) {
            if ( $user_email == $rows["user_email"] ) {
                if ( $user_password == $rows["user_password"]) {
                    if ( $api_authorization_key == $rows["api_authorization_key"] ) {
                        $array_result["result_code"] = 2;
                    } else {
                        $array_result["result_code"] = 3;
                    }
                } else {
                    $array_result["result_code"] = 4;
                }
            }
        }
    }
    if ( $array_result["result_code"] == 2 ) {
        $sqlmanager->sendQuery("UPDATE app_users SET user_password = '$user_new_password' WHERE user_email = '$user_email'");
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
        $array_result["description"] = "api_authorization_key incorrect";
        break;
    case 4:
        $array_result["description"] = "old password incorrect";
        break;
}

echo json_encode($array_result, JSON_PRETTY_PRINT | JSON_UNESCAPED_UNICODE);