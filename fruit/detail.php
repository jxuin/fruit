<?php
require('class/ConnectionMySQL.php');
require('config.php');
$db = new ConnectionMySQL();

$baseattrresult = $db->query("select fruitid,name,remark,intime,price,spdw,kc,psfw,cd,pp,pfruitid,syxslc,imgpath from t_fruit where fruitid = ".$_GET[id]);
$baserow = mysql_fetch_array($baseattrresult);

$spjsresult = $db->query("select attrvalue from t_fruitattr where attrkey = 'spjs' and fruitid = ".$_GET[id]);
$spjsrow = mysql_fetch_array($spjsresult);
$ccfsresult = $db->query("select attrvalue from t_fruitattr where attrkey = 'ccfs' and fruitid = ".$_GET[id]);
$ccfsrow = mysql_fetch_array($ccfsresult);
$spjjresult = $db->query("select attrvalue from t_fruitattr where attrkey = 'spjj' and fruitid = ".$_GET[id]);
$spjjrow = mysql_fetch_array($spjjresult);
$tjcpresult = $db->query("select attrvalue from t_fruitattr where attrkey = 'tjcp' and fruitid = ".$_GET[id]);
$tjcprow = mysql_fetch_array($tjcpresult);

$imgsattrresult = $db->query("select objurl from t_resources where rstatus = 0 and objtype = 1 and type = 1 and objid = ".$_GET[id]);
while ($imgrow = mysql_fetch_array($imgsattrresult)) {
	$imgrows[] = $imgrow;
}

$spdw = $baserow[spdw];
$spdwvalue = "500g";
if ($spdw == 2) {
  	$spdwvalue = "箱";
} else if ($spdw == 3) {
    $spdwvalue = "盒";
}

?>
<!doctype html>
<html>
<head>
<?php include 'htmlhead.php';?>
</head>

<body>
	<!-- header 页面头部 -->
    <?php include 'header.php';?>
    <!--mc: 中间部分-->
    <div class="mc">
        <?php include 'mctop.php';?>
        
    </div>
    <!-- footer 页面底部 -->
    <?php include 'footer.php';?>
</body>
<script type="text/javascript">
	$(document).ready(function(){
	});
</script>
<?php $db->close();?>
</html>

