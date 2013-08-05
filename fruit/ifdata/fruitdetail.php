<?php
	header('Content-Type: text/html; charset=utf-8');
	require('../class/ConnectionMySQL.php');
	require('../util/Util.php');
	require('../config.php');
//	require_once ('BaeMemcache.class.php');
//	$mem = new Memcache; 
//  $mem = new BaeMemcache();
//	$mem->connect('127.0.0.1',11211) or die("连接失败"); 
	$db = new ConnectionMySQL();
	
	$cache = new Cache(CACHE_TIMEOUT, realpath(CACHE_PATH). '/');
	
	$fruitid = $_GET[id];
	
	$updatecache = $_GET[updatecache];
	
//	$updatecache = true;
	
	if (empty($fruitid)) {
		
		$fruitid = 0;
	}
	
	$cachekey = 'fruitdetail_'.$fruitid;
	
//	$jsondata = $mem->get($cachekey);
	
	if (!empty($updatecache) || empty($jsondata)) {
		
		$baseattrresult = $db->query("select fruitid as id,name,remark,price,spdw,kc,psfw,cd,pp,concat('".BASE_URL."', imgpath) as img from t_fruit where fruitid = ".$fruitid);
		$baserow = mysql_fetch_array($baseattrresult, MYSQL_ASSOC);
		
		$imgsattrresult = $db->query("select concat('".BASE_URL."', objurl) as img from t_resources where rstatus = 0 and objtype = 1 and type = 1 and objid = ".$fruitid);
		while ($imgrow = mysql_fetch_array($imgsattrresult, MYSQL_ASSOC)) {
			$imgrows[] = $imgrow;
		}
		
		if (!empty($baserow)) {
			
			$spdw = $baserow[spdw];
	        $spdwvalue = "500g";
	        if ($spdw == 2) {
	        	$spdwvalue = "箱";
	        } else if ($spdw == 3) {
	        	$spdwvalue = "盒";
	        }
	        $baserow[spdw] = $spdwvalue;
	        
	        $baserow[spxx] = BASE_URL. PHP_URL . HTML5_PATH . '/' . $baserow[id] . '.html';
			
			foreach($baserow as $k => $v) {
				$jsonobj->$k = $v;
			}
			
			$jsonobj->imglist = $imgrows;
			
			$jsondata = json_encode($jsonobj);
			
//			$mem->set($cachekey, $jsondata, 0, 24*60*60);
		}
	}
	
	$util = new Util();
	
	$jsondata = $util->ob_gzip($jsondata);
	
	echo $jsondata;
	
	$db->close();
?>