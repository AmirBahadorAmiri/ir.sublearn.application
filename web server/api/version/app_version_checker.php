<?php

header('Content-Type: application/json; charset=utf-8');

require "../config/SQLHelper.php";
require "../signature/AesCrypto.php";

$sqlmanager = new SQLHelper();
$array_result = array();
$array_result["result_code"] = 0;
$pass = "";

if (isset($_POST["app_version_code"]) && isset($_POST["app_version_name"]) && isset($_POST["API_PASSWORD"])) {
    $array_result["result_code"] = 1;
    $app_version_code = $_POST["app_version_code"];
    $app_version_name = $_POST["app_version_name"];
    $pass = $_POST["API_PASSWORD"];
    $sqlmanager->sendQuery("SELECT * FROM app_version WHERE app_version_code = '$app_version_code'");
    if ($sqlmanager->getResult()->num_rows > 0) {
        while ($rows = $sqlmanager->getResult()->fetch_assoc()) {
            if ($app_version_code == $rows["app_version_code"]) {
                if ($app_version_name == $rows["app_version_name"]) {
                    $array_result["result_code"] = 2;
                    $array_result["app_version_code"] = $rows["app_version_code"];
                    $array_result["app_version_name"] = $rows["app_version_name"];
                    $array_result["can_use"] = $rows["can_use"];
                    $array_result["can_update"] = $rows["can_update"];
                }
            }
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
        $array_result["login"] = "/sublearn/api/user/user_login.php";
        $array_result["register"] = "/sublearn/api/user/user_register.php";
        $array_result["authorization_confirm"] = "/sublearn/api/user/user_authorization_confirm.php";
        $array_result["change_password"] = "/sublearn/api/user/user_change_password.php";
        $array_result["forget_passwprd"] = "/sublearn/api/user/forget_password.php";
        $array_result["get_seasons"] = "/sublearn/api/series/get_seasons.php";
        $array_result["get_episode"] = "/sublearn/api/series/get_episode.php";
        $array_result["translate"] = "https://translate.googleapis.com/translate_a/single";
        $array_result["get_all_movies"] = "/sublearn/api/movies/get_all_movies.php";
        $array_result["get_series"] = "/sublearn/api/series/get_series.php";
        $array_result["get_musics_kids"] = "/sublearn/api/musics/get_musics_kids.php";
        $array_result["get_musics_mobtadi"] = "/sublearn/api/musics/get_musics_mobtadi.php";
        $array_result["get_musics_motevaset"] = "/sublearn/api/musics/get_musics_motevaset.php";
        $array_result["get_musics_pishrafte"] = "/sublearn/api/musics/get_musics_pishrafte.php";
        $array_result["get_musics_top_kids"] = "/sublearn/api/musics/get_musics_top_kids.php";
        $array_result["get_musics_top_mobtadi"] = "/sublearn/api/musics/get_musics_top_mobtadi.php";
        $array_result["get_musics_top_motevaset"] = "/sublearn/api/musics/get_musics_top_motevaset.php";
        $array_result["get_musics_top_pishrafte"] = "/sublearn/api/musics/get_musics_top_pishrafte.php";
        $array_result["get_top_movies"] = "/sublearn/api/movies/get_top_movies.php";
        $array_result["get_top_series"] = "/sublearn/api/series/get_top_series.php";
        break;
}

echo AesCrypto::encrypt(json_encode($array_result, JSON_PRETTY_PRINT | JSON_UNESCAPED_UNICODE), $pass);