<?php
session_start();
if (!isset($_SESSION["userid"])) {
	header("location:login.php");
} else {
	require('../../config.php');
	require('../../class/ConnectionMySQL.php');
	$db = new ConnectionMySQL();
	
	$imgpath = $_POST[imgpathinput];
	
	if(empty($imgpath)) {
		$imgpath = $_POST[imgpath];
	}
	
	$db->query("update t_fruit set name = '".$_POST[name]."', remark = '".$_POST[remark]."', price = '".$_POST[price]."', spdw = '".$_POST[spdw]."', kc = '".$_POST[kc]."', psfw = '".$_POST[psfw]."', cd = '".$_POST[cd]."', pp = '".$_POST[pp]."', syxslc = '".$_POST[syxslc]."', imgpath = '".$imgpath."' where fruitid = ".$_POST[fruitid]);
	
	$db->query("delete from t_fruitattr where fruitid = ".$_POST[fruitid]);
	
	$db->query("insert into t_fruitattr(fruitid,attrname,attrkey,attrvalue) values ('".$_POST[fruitid]."','商品介绍','spjs','".$_POST[spjs]."');");
	$db->query("insert into t_fruitattr(fruitid,attrname,attrkey,attrvalue) values ('".$_POST[fruitid]."','存储方式','ccfs','".$_POST[ccfs]."');");
	$db->query("insert into t_fruitattr(fruitid,attrname,attrkey,attrvalue) values ('".$_POST[fruitid]."','产品禁忌','spjj','".$_POST[spjj]."');");
	$db->query("insert into t_fruitattr(fruitid,attrname,attrkey,attrvalue) values ('".$_POST[fruitid]."','推荐菜谱','tjcp','".$_POST[tjcp]."');");
	
	$imgpaths = $_POST[imgpaths];
	
	$imgpathArr = explode('|', $imgpaths);
	
	$db->query("delete from t_resources where objid = ".$_POST[fruitid]);
	
	foreach ($imgpathArr as $k=>$v) {
		
		if ($v != '' && $v != null) {
			
			$tmpArr = explode(',',$v);
			
			$sql = "insert into t_resources(objid,objtype,rstatus,type,intime,objurl,name) values ('".$_POST[fruitid]."',1,0,1,now(),'".$tmpArr[0]."','".$tmpArr[1]."')";
		
			$db->query($sql);
		}
	}
	
	$htmlstr = '<!DOCTYPE HTML>
		<html>
		<head>
		<meta charset="utf-8">
		<meta name="viewport" content="width=device-width, initial-scale=1.0,  minimum-scale=1.0" />
		<title>title</title>
		<style>
		body { font-size:12px; margin:0; padding:0; }
		section{ padding:2px 10px;}
		.title{ font-size:larger; margin-top:5px; font-weight:bold; font-size:14px; }
		.detail{ line-height:24px; text-indent: 2em;}
		</style>
		</head>
		
		<body>
		<div class="main">
			<section>
		    	<div class="title">配送范围：'.$_POST[psfw].'</div>
		    </section>
			<section>
		    	<div class="title">产&nbsp;&nbsp;&nbsp;&nbsp;地：'.$_POST[cd].'</div>
		    </section>
		    <section>
		    	<div class="title">品&nbsp;&nbsp;&nbsp;&nbsp;牌：'.$_POST[pp].'</div>
		    </section>
			<section>
		    	<div class="title">商品介绍</div>
				<div class="detail">'.$_POST[spjs].'</div>
		    </section>
			<section>
		    	<div class="title">存储方式</div>
				<div class="detail">'.$_POST[ccfs].'</div>
		    </section>
		    <section>
		    	<div class="title">商品禁忌</div>
				<div class="detail">'.$_POST[spjj].'</div>
		    </section>
		    <section>
		    	<div class="title">推荐菜谱</div>
				<div class="detail">'.$_POST[tjcp].'</div>
		    </section>
		</div>
		</body>
		</html>';
	
	$filename = realpath(BASE_DIR . HTML5_PATH) . '/' . $_POST[fruitid] . '.html';
	$file = fopen($filename, 'w');
	if ($file){//able to create the file
		fwrite($file, $htmlstr);
		fclose($file);
	}
	
	$db->close();
}

?>