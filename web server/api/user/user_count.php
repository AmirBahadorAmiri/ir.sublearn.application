<?php

require "../config/SQLHelper.php";

$sqlmanager = new SQLHelper();

$sqlmanager->sendQuery("SELECT COUNT(*) AS total_users FROM app_users");
$row = $sqlmanager->getResult()->fetch_assoc();
$count  = number_format($row["total_users"]);
?>

<style>
    body, html {
        height: 100%;
        margin: 0;
    }

    .special-number {
        font-family: 'Orbitron', sans-serif;
        font-size: 120px;
        color: #ffffff;
        background: #2f65dd;
        display: flex;
        justify-content: center; /* افقی */
        align-items: center;     /* عمودی */
        text-align: center;
        height: 100%;
    }

</style>
<div class="special-number" style="font-weight: bold; text-shadow: 2px 2px 5px #000;">
    <?php
    echo "Followers" . "<br>";
    echo number_format($count);
    ?>
</div>