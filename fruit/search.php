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
    <div class="header">
        <?php include 'header.php';?>
    </div>
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
		var searchkey = '<?php echo $_POST[searchkey]?>';
		$("#searchkey").val(searchkey);
		fruitsearchlist(searchkey, 1);
	});
</script>
<?php $db->close();?> 
</html>

