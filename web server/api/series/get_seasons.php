<?php

header('Content-Type: application/json; charset=utf-8');

require "../config/SQLHelper.php";

$sqlmanager = new SQLHelper();

$array_result = array();
$array_result["result_code"] = 0;
$array_result["movies"] = array();

if (isset($_POST['series_id'])) {
    $array_result["result_code"] = 1;
    $series_id = $_POST['series_id'];
    $sqlmanager->sendQuery("SELECT * FROM app_seasons WHERE series_id = '$series_id' AND is_enabled=1");
    if ($sqlmanager->getResult()->num_rows > 0) {
        $array_result["result_code"] = 2;
        while ($row = $sqlmanager->getResult()->fetch_assoc()) {
            array_push($array_result["movies"], $row);
        }
    }
}

switch ($array_result["result_code"]) {
    case 0:
        $array_result["description"] = "insert data";
        break;
    case 1:
        $array_result["description"] = "series not found";
        break;
    case 2:
        $array_result["description"] = "ok";
        break;
}

echo json_encode($array_result, JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT);