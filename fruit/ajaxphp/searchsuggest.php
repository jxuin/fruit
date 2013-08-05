<?php
	require('../class/ConnectionMySQL.php');
	$db = new ConnectionMySQL();
	
	$searchkey = $_POST[searchkey];
	$result_fruit = $db->query("select fruitid,name from t_fruit where name like '%".$searchkey."%' and rstatus = 0 and status = 0 and pfruitid > 0 order by seq desc, fruitid asc limit 0,10 ");

	$returnjson = '{"list":[';
	
	$resultflag = false;
	
	while ($row_fruit = mysql_fetch_array($result_fruit)) {
		$returnjson = $returnjson.'{"id":"'.$row_fruit[fruitid].'","name":"'.$row_fruit[name].'"},';
		if (!$resultflag) {
			$resultflag = true;
		}
	}

	if ($resultflag) {
		$returnjson = substr($returnjson, 0, strlen($returnjson) - 1);
	}
	
	$returnjson = $returnjson . ']}';
	
	echo $returnjson;
	
	$db->close();
	
?>