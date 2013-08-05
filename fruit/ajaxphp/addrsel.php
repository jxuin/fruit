<?php
	require('../class/ConnectionMySQL.php');
	$db = new ConnectionMySQL();
	
	$paddrid = $_GET[paddrid];
	
	if ($paddrid == null) {
		$paddrid = 0;
	}
	$result = $db->query("select addrid, name from t_shaddr where paddrid = ".$paddrid);
	while ($row = mysql_fetch_array($result)) {
		echo '<option value="'.$row[addrid].'">'.$row[name].'</option>'."\n";
    }
	$db->close();
?>