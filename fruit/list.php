<?php
require('class/ConnectionMySQL.php');
require('config.php');
$db = new ConnectionMySQL();
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
        <div class="mc-details">
            <div class="mclist-detail">
                
            </div>
        </div>
    </div>
    <!-- footer 页面底部 -->
    <?php include 'footer.php';?>
</body>
<script type="text/javascript">
	$(document).ready(function(){
		var fruitid = <?php echo $_GET[id]?>;
		fruitmorelist(fruitid, 1);
	});
</script>
<?php $db->close();?>
</html>

