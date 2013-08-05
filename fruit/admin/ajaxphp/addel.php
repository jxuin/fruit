<?php
session_start();
if (!isset($_SESSION["userid"])) {
	echo 'nosession';
} else {
	require('../../class/ConnectionMySQL.php');
	$db = new ConnectionMySQL();
	
	$db->query("update t_admsg set rstatus = 1 where adid = ".$_GET[adid]);
	
	$db->close();
}

?>