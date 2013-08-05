<?php
session_start();
if (!isset($_SESSION["userid"])) {
	echo 'nosession';
} else {
	require('../../class/ConnectionMySQL.php');
	$db = new ConnectionMySQL();
	
	$db->query("update t_order set status = ".$_GET[status]." where orderid = ".$_GET[orderid]);
	
	$db->close();
}

?>