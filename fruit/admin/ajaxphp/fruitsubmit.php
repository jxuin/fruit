<?php
session_start();
if (!isset($_SESSION["userid"])) {
	header("location:login.php");
} else {
	require('../../config.php');
	require('../../class/ConnectionMySQL.php');
	$db = new ConnectionMySQL();
	
//	$sql = "select (getseq('t_fruit')) as keyid from dual";
//	
//	$result = $db->query($sql);
//	
//	if ($row = mysql_fetch_array($result)) {
//		
//		$keyid = $row[keyid];
//	}
	$keyid = time();
	
	$imgpath = $_POST[imgpathinput];
	
	if(empty($imgpath)) {
		$imgpath = $_POST[imgpath];
	}
	
	$db->query("insert into t_fruit(fruitid,name,remark,intime,price,spdw,kc,psfw,cd,pp,pfruitid,syxslc,imgpath)
	values ('".$keyid."','".$_POST[name]."','".$_POST[remark]."',now(),'".$_POST[price]."','".$_POST[spdw]."','".$_POST[kc]."','".$_POST[psfw]."','".$_POST[cd]."','".$_POST[pp]."','".$_POST[pfruitid]."','".$_POST[syxslc]."','".$imgpath."');");
	
	$db->query("insert into t_fruitattr(fruitid,attrname,attrkey,attrvalue) values ('".$keyid."','商品介绍','spjs','".$_POST[spjs]."');");
	$db->query("insert into t_fruitattr(fruitid,attrname,attrkey,attrvalue) values ('".$keyid."','存储方式','ccfs','".$_POST[ccfs]."');");
	$db->query("insert into t_fruitattr(fruitid,attrname,attrkey,attrvalue) values ('".$keyid."','产品禁忌','spjj','".$_POST[spjj]."');");
	$db->query("insert into t_fruitattr(fruitid,attrname,attrkey,attrvalue) values ('".$keyid."','推荐菜谱','tjcp','".$_POST[tjcp]."');");
	
	$imgpaths = $_POST[imgpaths];
	
	$imgpathArr = explode('|', $imgpaths);
	
	foreach ($imgpathArr as $k=>$v) {
		
		if ($v != '' && $v != null) {
			
			$tmpArr = explode(',',$v);
			
			$sql = "insert into t_resources(objid,objtype,rstatus,type,intime,objurl,name) values ('".$keyid."',1,0,1,now(),'".$tmpArr[0]."','".$tmpArr[1]."')";
		
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
		.title{ font-size:larger; margin-top:5px; font-weight:bold; padding:0 2px; font-size:14px; }
		.detail{ padding:0 2px; line-height:24px; text-indent: 2em;}
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
		    <hr>
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
	
	$filename = realpath(BASE_DIR . HTML5_PATH) . '/' . $keyid . '.html';
	$file = fopen($filename, 'w');
	if ($file){//able to create the file
		fwrite($file, $htmlstr);
		fclose($file);
	}
	
	$db->close();
}

?>