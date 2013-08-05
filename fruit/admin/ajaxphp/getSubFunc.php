<?php
session_start();
//判断session是否存在，否则跳转登陆页
if (!isset($_SESSION["userid"])) {
	header("location:login.php");
} else {
	require('../../class/ConnectionMySQL.php');
	$db = new ConnectionMySQL();
	$funcid = $_GET[funcid];
	
	$sql = 'select a.funcid as funcid,funcname,funcurl from t_func a, t_rolefunc b where a.funcid = b.funcid and b.roleid = '.$_SESSION["roleid"].' and pfuncid = '.$_GET[funcid].' order by a.funcid asc';
	$result_func = $db->query($sql);
	echo '<ul class="nav nav-list">'."\n";
	while ($row_func = mysql_fetch_array($result_func)) {
		echo '<li onclick="'.$row_func[funcurl].'"><a>'.$row_func[funcname].'</a></li>'."\n";
	}
	echo '</ul>'."\n";
	$db->close();
}
?>