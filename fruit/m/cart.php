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
		<span class="search"><img src="../resources/images/search.png" /></span>
	</div><!-- /header -->

	<div data-role="content" class="fruit-content" style="padding:0;">
		<div id="cart-list" class="cart-list">
			<ul id="listview">
			<?php 
				$jsonstr = $_COOKIE["fruit2cart"];
				$jsonstr = str_replace('\"','"',$jsonstr);
				$fruitsJson = json_decode($jsonstr);
				$fruitarray = $fruitsJson->root;
				$totalnum = 0;
				$totalmoney = 0;
				echo '<li>
						<span>共 <font id="totalnum"></font> 件商品&nbsp;&nbsp;
						共计<font id="totalmoney">￥</font><span>
						<a id="js" class="del-a btn" style="margin-top:-5px; color:#fff; font-weight: normal;" href="js.php">结算</a>
					  </li>';
				foreach ($fruitarray as $k => $v) {
					$price = floatval($v->price);
					$num = intval($v->num);
					$totalnum += $num;
					$money = $price * $num;
					$totalmoney = $totalmoney + $money;
					echo '<li>
							<a href="detail.php?id='.$v->id.'" data-ajax="false">
							<span class="span-img"><img src="'.$v->img.'"></span>
							<span class="span-word">
								<font>'.$v->name.'&nbsp;</font>
								<font>&nbsp;</font>
								<font style="color:red;">￥'.$v->price.' x '.$num.'</font>
							</span>
							</a>
							<a id="del_'.$v->id.'" name="del" class="del-a btn" href="javascript:;" style="display:inline-block;">删除</a>
							
						  </li>'."\n";
				}
			?>
			</ul>
		</div>
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