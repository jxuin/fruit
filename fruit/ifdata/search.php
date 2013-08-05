<?php
	require('../class/ConnectionMySQL.php');
	require('../util/Util.php');
	require('../config.php');
	
	header('Content-Type: text/html; charset=utf-8');
	
	$db = new ConnectionMySQL();
	
	$searchkey = $_POST[key];
	
	$result = $db->query("select fruitid as id,concat('".BASE_URL."', imgpath) as img,name,remark,price,spdw,kc from t_fruit where pfruitid > 0 and rstatus = 0 and status = 0 and name like '%".$searchkey."%' order by seq desc, fruitid asc");
	
	while($row = mysql_fetch_array($result, MYSQL_ASSOC)) {
		
		$spdw = $row[spdw];
        $spdwvalue = "500g";
        if ($spdw == 2) {
        	$spdwvalue = "箱";
        } else if ($spdw == 3) {
        	$spdwvalue = "盒";
        }
        $row[spdw] = $spdwvalue;
		
		$rows[] = $row;
	}
	
	if (!empty($rows) && count($rows) > 0) {
		
		$jsonobj->list = $rows;
		
		$jsondata = json_encode($jsonobj);
		
	}
	$util = new Util();
	
	$jsondata = $util->ob_gzip($jsondata);
	
	echo $jsondata;
	
	$db->close();
?>