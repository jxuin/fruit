<?php
session_start();
if (!isset($_SESSION["userid"])) {
	echo 'nosession';
} else {
	require('../../class/ConnectionMySQL.php');
	$db = new ConnectionMySQL();
	
	$db->query("update t_fruit set status = ".$_GET[status]." where fruitid = ".$_GET[fruitid]);
	
	$db->close();
}

?>