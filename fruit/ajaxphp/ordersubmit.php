<?php
	require('../class/ConnectionMySQL.php');
	$db = new ConnectionMySQL();
	
	$fruits = $_POST[fruits];
	$shr = $_POST[shr];
	$addr = $_POST[addr];
	$sjhm = $_POST[sjhm];
	$shsj = $_POST[shsj];
	
	$keyid = '0';
//	$result_keyid = $db->query("select getseq('t_order') as keyid from dual");
//	if ($row_keyid = mysql_fetch_array($result_keyid)) {
//		$keyid = $row_keyid[keyid];
//	}
	$keyid = time();
	
	$ordernum = time();
//	$result_ordernum = $db->query("select getseq('ordernum') as ordernum from dual");
//	if ($row_ordernum = mysql_fetch_array($result_ordernum)) {
//		$ordernum = $row_ordernum[ordernum];
//	}
	
	$db->query("insert into t_order (orderid,shr,shdz,shsj,sjhm,ordernum) values ('".$keyid."','".$shr."','".$addr."','".$shsj."','".$sjhm."','".$ordernum."')");
	
	$fruits = str_replace('\"','"',$fruits);
	
	$fruitsJson = json_decode($fruits);
	
	$fruitarray = $fruitsJson->root;
	
	$totalmoney = 0;
	
	foreach ($fruitarray as $k => $v) {
		$price = floatval($v->price);
		$num = intval($v->num);
		$money = $price * $num;
		$totalmoney = $totalmoney + $money;
		$db->query("insert into t_orderdetail (fruitid,fruitnum,money,orderid,ordernum) values ('".$v->id."','".$num."','".$money."','".$keyid."','".$ordernum."')");
	}
	$db->query("update t_order set money = '".$totalmoney."' where orderid = ".$keyid);
	
	echo '{"status":"0"}';
	
	$db->close();
?>