<?php
session_start();
if (!isset($_SESSION["userid"])) {
	header("location:login.php");
} else {
	require('../../config.php');
	require('../../class/ConnectionMySQL.php');
	$db = new ConnectionMySQL();
	
	$db->query("update t_admsg set name = '".$_POST[name]."', imgpath = '".$_POST[imgpath]."', linkurl = '".$_POST[linkurl]."' where adid = ".$_POST[adid]);
	
	$db->close();
}

?>