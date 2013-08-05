<?php
	require('../class/ConnectionMySQL.php');
	require_once '../util/Util.php';
	$db = new ConnectionMySQL();
	
	$result_fruit = $db->query("select fruitid,name,imgpath,spdw,price from t_fruit where fruitid = ".$_GET[fruitid]);
	if ($row_fruit = mysql_fetch_array($result_fruit)) {
		$spdw = $row_fruit[spdw];
        $spdwvalue = "500g";
        if ($spdw == 2) {
        	$spdwvalue = "箱";
        } else if ($spdw == 3) {
        	$spdwvalue = "盒";
        }
        $util = new Util();
        echo stripslashes($util->JSON(array('id' => $row_fruit[fruitid], 'name' => $row_fruit[name], 'img' => $row_fruit[imgpath], 'price' => $row_fruit[price], 'num' => $_GET[num])));
	}
	
	$db->close();
?>