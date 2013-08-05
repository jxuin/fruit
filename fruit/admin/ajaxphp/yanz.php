<?php
require('../../class/ConnectionMySQL.php');
$db = new ConnectionMySQL();

$username = $_POST[username];
$password = $_POST[password];
$result = $db->query("select a.userid as userid,username,realname,b.roleid as roleid from t_user a, t_userrole b, t_role c where username = '".$username."' and password = '".$password."' and a.rstatus = 0 and c.rstatus = 0 and a.userid = b.userid and b.roleid = c.roleid");
if ($row = mysql_fetch_array($result)) {
	session_start();
	$_SESSION["userid"] = $row[userid];
	$_SESSION["username"] = $row[username];
	$_SESSION["realname"] = $row[realname];
	$_SESSION["roleid"] = $row[roleid];
	echo '{"status":"0"}';
} else {
	echo '{"status":"1"}';
}

$db->close();

?>