<?php

header('Content-Type: application/json; charset=utf-8');

require "../config/SQLHelper.php";
require "../signature/RandomString.php";
require "../config/MailSender.php";

$signature = new RandomString();
$sqlmanager = new SQLHelper();
$mailsender = new MailSender();

$array_result = array();
$array_result["result_code"] = 0;
$random_code = random_int(1000, 9999);
$currentTime = round(microtime(true));

if (isset($_POST["user_name"]) && isset($_POST["user_email"]) && isset($_POST["user_password"])) {
    $array_result["result_code"] = 1;
    $user_name = $_POST["user_name"];
    $user_email = $_POST["user_email"];
    $user_password = $_POST["user_password"];
    $api_authorization_key = $signature->generateRandomString(16);

    $sqlmanager->sendQuery("SELECT * FROM app_users WHERE user_email='$user_email'");
    if ( $sqlmanager->getResult()->num_rows > 0 ) {
        while ( $row = $sqlmanager->getResult()->fetch_assoc() ) {
            if ( $user_email == $row["user_email"] ) {
                $array_result["result_code"] = 2;
            }
        }
    }

    if ( $array_result["result_code"] == 1 ) {
        $sqlmanager->sendQuery("INSERT INTO `app_users` (`user_name`,`user_email`,`user_password`,`api_authorization_key`,`api_authorization_code`,`api_authorization_time`) VALUES ('$user_name', '$user_email', '$user_password', '$api_authorization_key','$random_code','$currentTime')");
        $mailsender->setEmailTo($user_email)
            ->setSubject("رمز عبور")
            ->setBody("رمز عبور یکبار مصرف شما : <b>$random_code</b>")
            ->sendEmail();
        $array_result["result_code"] = 3;
    }

}

switch ($array_result["result_code"]) {
    case 0:
        $array_result["description"] = "data inject";
        break;
    case 1:
        $array_result["description"] = "not created";
        break;
    case 2:
        $array_result["description"] = "user already exist";
        break;
    case 3:
        $array_result["description"] = "user already created";
        break;
}

echo json_encode($array_result, JSON_PRETTY_PRINT | JSON_UNESCAPED_UNICODE);
