<?php

header('Content-Type: application/json; charset=utf-8');

require "../config/SQLHelper.php";

$sqlmanager = new SQLHelper();
$array_result = array();
$array_result["result_code"] = 0;
$array_result["movies"] = array();

$sqlmanager->sendQuery("SELECT * FROM app_movies where is_enabled=1 limit 10");
if ($sqlmanager->getResult()->num_rows > 0) {
    $array_result["result_code"] = 1;
    while ($row = $sqlmanager->getResult()->fetch_assoc()) {
        array_push($array_result["movies"], $row);
    }
}

switch ($array_result["result_code"]) {
    case 0:
        $array_result["description"] = "insert data";
        break;
    case 1:
        $array_result["description"] = "ok";
        break;
}

echo json_encode($array_result, JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT);