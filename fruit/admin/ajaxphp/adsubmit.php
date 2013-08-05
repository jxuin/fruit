<?php
session_start();
if (!isset($_SESSION["userid"])) {
	header("location:login.php");
} else {
	require('../../config.php');
	require('../../class/ConnectionMySQL.php');
	$db = new ConnectionMySQL();
	
	$db->query("insert into t_admsg(name,linkurl,imgpath) values ('".$_POST[name]."','".$_POST[linkurl]."','".$_POST[imgpath]."');");
	
	$db->close();
}

?>