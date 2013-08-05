<?php
require('../class/ConnectionMySQL.php');
require('../util/Util.php');
require('../config.php');

header('Content-Type: text/html; charset=utf-8');

$db = new ConnectionMySQL();

$updatecache = $_GET[updatecache];

$updatecache = true;

$cache = new Cache(CACHE_TIMEOUT, realpath(CACHE_PATH). '/');

$cachekey = 'shaddr';

$jsondata = $cache->get($cachekey);

if (!empty($updatecache) || empty($jsondata)) {

	$result = $db->query("select addrid,name,paddrid from t_shaddr");

	while($row = mysql_fetch_array($result, MYSQL_ASSOC)) {
		$rows[] = $row;
	}

	if (!empty($rows) && count($rows) > 0) {
		$jsondata = json_encode($rows);
		$cache->put($cachekey, $jsondata);
	}
}
if (empty($jsondata)) {
	$jsondata = '[]';
}
$util = new Util();

$jsondata = $util->ob_gzip($jsondata);

echo $jsondata;

$db->close();
?>