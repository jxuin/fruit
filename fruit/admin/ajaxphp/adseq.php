<?php
session_start();
if (!isset($_SESSION["userid"])) {
	echo 'nosession';
} else {
	require('../../class/ConnectionMySQL.php');
	$db = new ConnectionMySQL();

	$seqs = $_POST[seqs];

	$seqsArr = explode('|', $seqs);
	
	foreach ($seqsArr as $k=>$v) {
		if (!empty($v)) {
			$tmpArr = explode(',',$v);
			$db->query("update t_admsg set seq = ".$tmpArr[0]." where adid = ".$tmpArr[1]);
		}
	}

	$db->close();
}

?>