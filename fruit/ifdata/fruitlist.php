<?php
	require('../class/ConnectionMySQL.php');
	require('../util/Cache.class.php');
	require('../config.php');
	require_once ('BaeMemcache.class.php');
	header('Content-Type: text/html; charset=utf-8');
	
//	$mem = new Memcache; 
	$mem = new BaeMemcache();
	
//	$mem->connect('127.0.0.1',11211) or die("连接失败"); 
	
	$db = new ConnectionMySQL();
	
	$fruitid = $_GET[id];
	
	$updatecache = $_GET[updatecache];
	
//	$updatecache = true;
	
	if (empty($fruitid)) {
		
		$fruitid = 0;
	}
	
	$cachekey = 'fruitlist_'.$fruitid;
	
	$jsondata = $mem->get($cachekey);
	
	if (!empty($updatecache) || empty($jsondata)) {
		
		$result = $db->query("select fruitid as id,concat('".BASE_URL."', imgpath) as img,name,remark,price,spdw,kc from t_fruit where pfruitid = ".$fruitid." and rstatus = 0 and status = 0 order by seq desc, fruitid asc");
		
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
			
			$mem->set($cachekey, $jsondata, 0, 24*60*60);
		}
	}
	
	echo $jsondata;
	
	$db->close();
?>