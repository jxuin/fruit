<?php
require('../class/ConnectionMySQL.php');
require('../config.php');
$db = new ConnectionMySQL();
?>
<!DOCTYPE html> 
<html> 
<head> 
	<title>水果在线</title> 
	<?php include 'htmlheader.php';?>
</head> 
<body> 

<div data-role="page">

	<div class="fruit-header">
		<span class="back"><a data-rel="back" data-role="button" data-mini="true" data-inline="true" data-icon="arrow-l" data-theme="b">返回</a></span>
		<span class="search"><a class="btn del-a" style="color:#fff; margin-top:4px; width: 30px;" href="javascript:;">提交</a></span>
	</div><!-- /header -->

	<div data-role="content" class="fruit-content" style="padding: 5px 10px;">
		<label for="shr">收货人:</label>
		<input type="text" name="shr" id="shr" value="">
		<label for="dz">地址:</label>
		<input type="text" name="dz" id="dz" value="">
		<label for="dhhm">电话号码:</label>
		<input type="text" name="dhhm" id="dhhm" value="">
		<label for="shsj">收货时间:</label>
		<input type="text" name="shsj" id="shsj" value="">
	</div>

</div>

</body>
<script type="text/javascript">
	$(document).ready(function(){
		pageInitOpt();
		$("#totalnum").html(<?php echo $totalnum?>);
		$("#totalmoney").html('￥').append(<?php echo $totalmoney?>);
		$("a[name='del']").each(function() {
			$(this).click(function() {
				var id = $(this).attr("id").replace("del_","");
				if (confirm("确认删除?")) {
					deltocart(id);
					$(this).parent().remove();
					var totalArray = getTotalNumAndMoney();
					$("#totalnum").html(totalArray[0]);
					$("#totalmoney").html('￥').append(totalArray[1]);
				}
			});
		});
	});

	$(window).on( "orientationchange", function(event) {
		pageInitOpt();
	});
	$(window).orientationchange();

	function pageInitOpt() {
		$("#cart-list .span-word font").each(function() {
			var width = $(window).width();
			$(this).css('width',(width - 150) + 'px');
		});
	}

</script>
<?php $db->close();?>   
</html>