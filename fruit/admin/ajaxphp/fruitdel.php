<?php
session_start();
if (!isset($_SESSION["userid"])) {
	echo 'nosession';
} else {
	require('../../class/ConnectionMySQL.php');
	$db = new ConnectionMySQL();
	
	$db->query("update t_fruit set rstatus = 1 where fruitid = ".$_GET[fruitid]);
	
	$db->close();
}

?>