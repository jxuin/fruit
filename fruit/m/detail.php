<?php
require('../class/ConnectionMySQL.php');
require('../config.php');
$db = new ConnectionMySQL();

$fruitid = $_GET[id];

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
		<?php include 'header.php';?>
	</div><!-- /header -->

	<div data-role="content" class="fruit-content">
		<?php 
			$result = $db->query("select fruitid,name,imgpath from t_fruit where pfruitid = 0 and status = 0 and rstatus = 0 order by seq desc, fruitid asc");
			
		?>
		<section class="section-imgs">
			<div class="div-imgs">
				<ul>
					<?php 
						if (!empty($imgrows) && count($imgrows) > 0) {
							foreach ($imgrows as $k=>$v) {
								echo '<li><img src="'.$v[objurl].'" /></li>'."\n";
							}
						}
					?>
				</ul>
			</div>
		</section>
		<section>
			<div style="font-size:16px; font-weight:bold;"><?php echo $baserow[name]?></div>
			<div style="font-size:14px; line-height: 18px; color:red; font-weight:bold; margin-top:10px"><?php echo $baserow[remark]?></div>
			<div style="font-size:14px; color:red; font-weight:bold; margin-top:10px">价格：￥<?php echo $baserow[price].'/'.$spdwvalue?></div>
			<div style="font-size:14px; font-weight:bold;;">购买数量：<img id="buyminus" class="opt-img" src="../resources/images/minus.png" /><span style="display:inline-block; width:50px; margin-right:5px;"><input data-mini="true" data-inline="true" style="text-align:center;" name="buynum" id="buynum" value="1"/></span><img id="buyplus" class="opt-img" src="../resources/images/plus.png" />
			<div><a id="add2cart" data-role="button" data-mini="true" data-inline="true" data-theme="b">加入购物车</a></div>
		</section>
		<section style="border-bottom: 0;">
			<div class="title">配送范围：<?php echo $baserow[psfw]?></div>
			<div class="title">产地：<?php echo $baserow[cd]?></div>
			<div class="title">品牌：<?php echo $baserow[pp]?></div>
			<div class="title">商品介绍</div>
			<div class="detail"><?php echo $spjsrow[attrvalue]?></div>
			<div class="title">存储方式</div>
			<div class="detail"><?php echo $ccfsrow[attrvalue]?></div>
			<div class="title">商品禁忌</div>
			<div class="detail"><?php echo $spjjrow[attrvalue]?></div>
			<div class="title">推荐菜谱</div>
			<div class="detail"><?php echo $tjcprow[attrvalue]?></div>
		</section>
		<input type="hidden" type="text" id="kc" value="<?php echo $baserow[kc]?>" />
		<div data-role="popup" id="tipwindow" class="ui-content" data-theme="d">
    		<p>添加成功</p>
		</div>
	</div>
</div>
</body>

<script type="text/javascript">

	var curimg = 0;
	var imgnum = 0;
	var imgdist = 0;
	var width = 0;
	var ulleft = 0;

	$(document).ready(function(){

		$(".div-imgs ul").bind("swipeone", function(event, obj) {
			var deltaX = obj.delta[0].startX;
			if (deltaX < 0) {
				if (curimg < imgnum - 1) {
					$(".div-imgs ul").animate({left: -imgdist*(curimg+1) + ulleft},500);
					curimg ++;
				}
			} else if (deltaX > 0) {
				if (curimg > 0) {
					$(".div-imgs ul").animate({left: -imgdist*(curimg-1) + ulleft},500);
					curimg --;
				}
				
			}
		});

		pageInitOpt();

		$("#add2cart").click(function() {
			var fruitid = <?php echo $fruitid?>;
			var num = $("#buynum").val();
			addtocart(fruitid, num, 1);
		});
	});

	$("#buyminus").click(function() {
		if (parseInt($("#buynum").val()) <= 1) {
			showJqmTip('购买数量最少为1');
		} else {
			$("#buynum").val(parseInt($("#buynum").val()) - 1);
		}
	});

	$("#buyplus").click(function() {
		if (parseInt($("#buynum").val()) >= parseInt(getstr($("#kc").val(), '10'))) {
			showJqmTip('购买数量已超过库存');
		} else {
			$("#buynum").val(parseInt($("#buynum").val()) + 1);
		}
	});

	$("#buynum").blur(function() {
		if (!IsNumber($("#buynum").val())) {
			showJqmTip('请输入整数');
			$("#buynum").val(1);
			return false;
		}
		if (parseInt($("#buynum").val()) >= parseInt(getstr($("#kc").val(), '10'))) {
			showJqmTip('购买数量已超过库存');
			$("#buynum").val(1);
			return false;
		}
		if (parseInt($("#buynum").val()) <= 0) {
			showJqmTip('购买数量最少为1');
			$("#buynum").val(1);
			return false;
		}
		return true;
	});

	$( window ).on( "orientationchange", function( event ) {
		pageInitOpt();
//		  $( "#orientation" ).text( "This device is in " + event.orientation + " mode!" );
	});
	$( window ).orientationchange();

	function pageInitOpt() {
		curimg = 0;
		imgnum = $(".div-imgs ul li").length;
		imgdist = 206;

		width = $(window).width();
		ulleft = (width - 240) / 2;
		$(".div-imgs ul").css("left", ulleft + "px");
	}

</script>
<?php $db->close();?>   
</html>