<?php

header('Content-Type: application/json; charset=utf-8');

require "../config/SQLHelper.php";
require "../config/MailSender.php";

$sqlmanager = new SQLHelper();
$mailsender = new MailSender();

$array_result = array();
$array_result["result_code"] = 0;
$random_code = random_int(1000, 9999);
$currentTime = round(microtime(true));

if (isset($_POST["user_email"])) {
    $array_result["result_code"] = 1;
    $user_email = $_POST["user_email"];
    $sqlmanager->sendQuery("SELECT * FROM app_users WHERE user_email = '$user_email'");
    if ($sqlmanager->getResult()->num_rows > 0) {
        while ($rows = $sqlmanager->getResult()->fetch_assoc()) {
            if ($user_email == $rows["user_email"]) {
                if (($rows["api_authorization_time"] + 300) < $currentTime) {
                    $array_result["result_code"] = 2;
                } else {
                    $array_result["result_code"] = 3;
                }
            }
        }
        if ($array_result["result_code"] == 2) {
            $sqlmanager->sendQuery("UPDATE app_users SET api_authorization_time = '$currentTime' , api_authorization_code = '$random_code' WHERE user_email = '$user_email'");
            $mailsender->setEmailTo($user_email)
                ->setSubject("رمز عبور")
                ->setBody("رمز عبور یکبار مصرف شما : <b>$random_code</b>")
                ->sendEmail();
        }
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
}

echo json_encode($array_result, JSON_PRETTY_PRINT | JSON_UNESCAPED_UNICODE);