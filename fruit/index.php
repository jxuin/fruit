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
        	<?php 
	        	$result_syxslc = $db->query("select syxslc from t_fruit where pfruitid = 0 and rstatus = 0 and status = 0 group by syxslc order by syxslc asc");
	        	
	        	while ($row_syxslc = mysql_fetch_array($result_syxslc)) {
	        		$rows_syxslc[] = $row_syxslc;
	        	}
        		foreach($rows_syxslc as $k => $v) {
        			if (!empty($v)) {
        				
        				echo '<div class="mc-detail">'."\n";
        				echo '<div class="mc-d-menu" id="menu_'.$v[syxslc].'">'."\n";
        				echo '<span class="mc-d-menu-r"><a name="more_a" id="more_'.$v[syxslc].'" href="javascript:;">更多&gt;&gt;</a></span>'."\n";
        				echo '<ul>'."\n";
        				
        				$fruitindex = 0;
        				
        				$fruitactive = 0;
        				
        				$result_fruit1 = $db->query("select fruitid,name from t_fruit where pfruitid = 0 and syxslc = ".$v[syxslc]." and rstatus = 0 and status = 0 order by seq desc, fruitid asc");
        				while ($row_fruit1 = mysql_fetch_array($result_fruit1)) {
							if ($fruitindex == 0) {
								$activefruitid = $row_fruit1[fruitid];
								echo '<li val="'.$row_fruit1[fruitid].'" onclick="fruitlist('.$v[syxslc].','.$row_fruit1[fruitid].',this);" class="active">'.$row_fruit1[name].'</li>'."\n";
							} else {
								echo '<li val="'.$row_fruit1[fruitid].'" onclick="fruitlist('.$v[syxslc].','.$row_fruit1[fruitid].',this);">'.$row_fruit1[name].'</li>'."\n";
							}
							$fruitindex ++;
        				}
        				echo '</ul>'."\n";
        				echo '</div>'."\n";
        				
        				echo '<div class="mc-d-list" id="list_'.$v[syxslc].'">'."\n";
        				echo '<ul>'."\n";
        				$result_fruit2 = $db->query("select fruitid,name,imgpath,spdw,price,kc from t_fruit where pfruitid = ".$activefruitid." and rstatus = 0 and status = 0 order by seq desc, fruitid asc limit 0,5");
        				while ($row_fruit2 = mysql_fetch_array($result_fruit2)) {
        					$spdw = $row_fruit2[spdw];
        					$spdwvalue = "500g";
        					if ($spdw == 2) {
        						$spdwvalue = "箱";
        					} else if ($spdw == 3) {
        						$spdwvalue = "盒";
        					}
							echo '<li>
									<a href="javascript:;" onclick="detaildialog('.$row_fruit2[fruitid].')">
										<img src="'.$row_fruit2[imgpath].'" />
									</a>
									<font>'.$row_fruit2[name].'</font>
									<strong>￥'.$row_fruit2[price].'/'.$spdwvalue.'</strong>
									<span onclick="addtocart('.$row_fruit2[fruitid].', 1, 0);"><font>加入购物车</font></span>
								  </li>'."\n";
        				}
        				echo '</ul>'."\n";
                		echo '</div>'."\n";
        				echo '</div>'."\n";
        			}
        		}
        	?>
        </div>
    </div>
    <!-- footer 页面底部 -->
    <?php include 'footer.php';?>
</body>
<script type="text/javascript">

	$(document).ready(function(){

		$("a[name='more_a']").each(function() {
			var id = $(this).attr("id");
			var syxslc = id.replace("more_","");
			$("#menu_" + syxslc + " li").each(function() {
				if ($(this)[0].className == 'active') {
					$("#more_" + syxslc).attr("href", "list.php?id=" + $(this).attr("val"));
				}
			});
		});
	});
	
	function fruitlist(syxslc, fruitid, obj) {
		if (obj.className == 'active') {
			return;
		} else {
			$("#menu_" + syxslc + " li").each(function() {
				$(this)[0].className = '';
			});
			obj.className = 'active';
			$.get('ajaxphp/fruitlist.php',{fruitid:fruitid},function(callback) {
				$("#list_" + syxslc).html(callback);
			});
			$("#more_" + syxslc).attr("href", "list.php?id=" + fruitid);
		}
	}
</script>
<?php $db->close();?>    
</html>

