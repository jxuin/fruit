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
		<span class="logo"><img src="../resources/images/logo.png" /></span>
		<?php include 'header.php';?>
	</div>
	<div data-role="content" class="fruit-content">	
		<div id="fruit-list" data-role="collapsible-set" data-inset="false" class="fruit-list">
		
		<?php 
			$result = $db->query("select fruitid,name,imgpath from t_fruit where pfruitid = 0 and status = 0 and rstatus = 0 order by seq desc, fruitid asc");
		
			while ($row = mysql_fetch_array($result, MYSQL_ASSOC)) {
				
				$fruitid = $row[fruitid];
				
				$name = $row[name];
				
				echo '<div data-role="collapsible">'."\n";
		        echo '<h3><span class="span-title"><img src="'.$row[imgpath].'" />'.$name.'</span></h3>'."\n";
		        echo '<ul data-role="listview">'."\n";
				$result_sub = null;
				$result_sub = $db->query("select fruitid,name,remark,imgpath,spdw,price,kc from t_fruit where pfruitid = ".$fruitid." and status = 0 and rstatus = 0 order by seq desc, fruitid asc");
				$row_sub = null;
				while ($row_sub = mysql_fetch_array($result_sub, MYSQL_ASSOC)) {
					$spdw = $row_sub[spdw];
        			$spdwvalue = "500g";
        			if ($spdw == 2) {
        				$spdwvalue = "箱";
        			} else if ($spdw == 3) {
        				$spdwvalue = "盒";
        			}
					echo '<li>
							<a href="detail.php?id='.$row_sub[fruitid].'" data-ajax="false"><span class="span-img"><img src="'.$row_sub[imgpath].'"></span>
							<span class="span-word">
								<font>'.$row_sub[name].'&nbsp;</font>
								<font>'.$row_sub[remark].'&nbsp;</font>
								<font style="color:red">￥'.$row_sub[price].'/'.$spdwvalue.'</font>
							</span>
							</a>
						  </li>'."\n";
				}
				echo '</ul>'."\n";
		    	echo '</div>'."\n";
			}

				
		?>
		</div>			
	</div>

</div>

</body>

<script type="text/javascript">

	$(document).ready(function(){
		pageInitOpt();
	});

	$( window ).on( "orientationchange", function( event ) {
		pageInitOpt();
//		  $( "#orientation" ).text( "This device is in " + event.orientation + " mode!" );
	});
	$( window ).orientationchange();

	function pageInitOpt() {
		$("#fruit-list .span-word font").each(function() {
			var width = $(window).width();
			$(this).css('width',(width - 135) + 'px');
		});
	}

</script>
<?php $db->close();?>   
</html>