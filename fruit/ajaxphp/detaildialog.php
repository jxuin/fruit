<?php
require('../class/ConnectionMySQL.php');
require('../config.php');
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
		
<div class="mcdetail">
	<div class="mcdetail-l">
		<div class="mcdetail-l-img">
		<?php 
			if (!empty($imgrows) && count($imgrows) > 0) {
				echo '<img src="'.$imgrows[0][objurl].'" />'."\n";
			}
		?>
		</div>
		<div class="mcdetail-l-imgs">
			<span class="left-span"><img id="mcdetail-left-img" src="resources/images/left-3.png" /></span>
			<span class="right-span"><img id="mcdetail-right-img" src="resources/images/right-2.png" /></span>
			<div class="imgs-div">
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
		</div>
	</div>
	<div class="mcdetail-r">
		<span class="mcdetail-r-title">
			<font title="<?php echo $baserow[name]?>"><?php echo $baserow[name]?>&nbsp;</font>
			<strong title="<?php echo $baserow[remark]?>"><?php echo $baserow[remark]?>&nbsp;</strong>
		</span>
		<span class="mcdetail-r-attr">
			<span>价&nbsp;&nbsp;&nbsp;&nbsp;格：<font class="price"><?php echo $baserow[price].'/'.$spdwvalue?></font></span>
			<span>配送范围：<font><?php echo $baserow[psfw]?></font></span>
			<span>产&nbsp;&nbsp;&nbsp;&nbsp;地：<font><?php echo $baserow[cd]?></font></span>
			<span>品&nbsp;&nbsp;&nbsp;&nbsp;牌：<font><?php echo $baserow[pp]?></font></span>
		</span>
		<span class="mcdetail-r-cart">
			<span>购买数量：<img id="buyminus" class="opt-img" src="resources/images/minus.png" /><input name="buynum" id="buynum" value="1"/><img id="buyplus" class="opt-img" src="resources/images/plus.png" /></span>
			<span><a id="add2cart" href="javascript:;" class="btn">加入购物车</a></span>
		</span>
		<input type="hidden" id="kc" value="<?php echo $baserow[kc]?>" />
		<input type="hidden" id="dialogfruitid" value="<?php echo $baserow[fruitid]?>" />
	</div>
	<div class="mcdetail-b">
	<?php 
		if ($spjsrow != null) {
			echo '<span class="mcdetail-b-title">商品介绍</span>'."\n";
			echo '<span class="mcdetail-b-content">'.$spjsrow[attrvalue].'</span>'."\n";
		}
                
		if ($ccfsrow != null) {
			echo '<span class="mcdetail-b-title">存储方式</span>'."\n";
			echo '<span class="mcdetail-b-content">'.$ccfsrow[attrvalue].'</span>'."\n";
		}
                
		if ($spjjrow != null) {
			echo '<span class="mcdetail-b-title">商品禁忌</span>'."\n";
			echo '<span class="mcdetail-b-content">'.$spjjrow[attrvalue].'</span>'."\n";
		}
                
		if ($tjcprow != null) {
			echo '<span class="mcdetail-b-title">推荐菜谱</span>'."\n";
			echo '<span class="mcdetail-b-content">'.$tjcprow[attrvalue].'</span>'."\n";
		}
		?>
	</div>
</div>
            
<script type="text/javascript">
	$(document).ready(function(){

		$("#buynum").val(getFruitnum('<?php echo $baserow[fruitid]?>'));
		
		$("#buyminus").click(function() {
			if (parseInt($("#buynum").val()) <= 1) {
				showDialog('resources/css/dialog/icons/warning.png','购买数量最少为1',180);
			} else {
				$("#buynum").val(parseInt($("#buynum").val()) - 1);
			}
		});

		$("#buyplus").click(function() {
			if (parseInt($("#buynum").val()) >= parseInt(getstr($("#kc").val(), '10'))) {
				showDialog('resources/css/dialog/icons/warning.png','购买数量已超过库存',180);
			} else {
				$("#buynum").val(parseInt($("#buynum").val()) + 1);
			}
		});

		$("#buynum").blur(function() {
			if (!IsNumber($("#buynum").val())) {
				showDialog('resources/css/dialog/icons/warning.png','请输入整数',180);
				$("#buynum").val(1);
				return false;
			}
			if (parseInt($("#buynum").val()) >= parseInt(getstr($("#kc").val(), '10'))) {
				showDialog('resources/css/dialog/icons/warning.png','购买数量已超过库存',180);
				$("#buynum").val(1);
				return false;
			}
			if (parseInt($("#buynum").val()) <= 0) {
				showDialog('resources/css/dialog/icons/warning.png','购买数量最少为1',180);
				$("#buynum").val(1);
				return false;
			}
			return true;
		});

		$("#add2cart").click(function() {
			if ($("#buynum").blur()) {
				addtocart($("#dialogfruitid").val(), $("#buynum").val(), 1);
				$.dialog.get('detaildialog').close();
			}
		});

		var curimg = 0;
		var imgnum = $(".imgs-div ul li").length;
		var imgdist = 69;
		var showimgnum = 3;

		if (imgnum <= showimgnum) {
			$("#mcdetail-left-img").attr('src','resources/images/left-3.png');
			$("#mcdetail-right-img").attr('src','resources/images/right-3.png');
		}

		$("#mcdetail-left-img").click(function() {
			if (curimg > 0) {
				$(".imgs-div ul").animate({left: -imgdist*(curimg-1)},200);
				curimg --;
				$("#mcdetail-right-img").attr('src','resources/images/right-2.png');
			}
			if (curimg <= 0) {
				$("#mcdetail-left-img").attr('src','resources/images/left-3.png');
			}
		});

		$("#mcdetail-right-img").click(function() {
			if (curimg < imgnum - showimgnum) {
				$(".imgs-div ul").animate({left: -imgdist*(curimg+1)},200);
				curimg ++;
				$("#mcdetail-left-img").attr('src','resources/images/left-2.png');
			} 
			if (curimg >= imgnum - showimgnum) {
				$("#mcdetail-right-img").attr('src','resources/images/right-3.png');
			}
		});

		$(".imgs-div ul li").mouseover(function() {
			$(".imgs-div ul li").each(function() {
				$(this).removeClass('active');
			});
			$(this).addClass('active');
			$(".mcdetail-l-img img").attr("src",$(this).children("img").attr("src"));
		});
	});
</script>
<?php $db->close();?>
