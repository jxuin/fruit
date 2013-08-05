<?php
	require('../class/ConnectionMySQL.php');
	$db = new ConnectionMySQL();
	
	echo '<ul>'."\n";
    $result_fruit = $db->query("select fruitid,name,imgpath,spdw,price,kc from t_fruit where pfruitid = ".$_GET[fruitid]." and rstatus = 0 and status = 0 order by seq desc, fruitid asc limit 0,5");
    while ($row_fruit = mysql_fetch_array($result_fruit)) {
       	$spdw = $row_fruit[spdw];
        $spdwvalue = "500g";
        if ($spdw == 2) {
        	$spdwvalue = "箱";
        } else if ($spdw == 3) {
        	$spdwvalue = "盒";
        }
		echo '<li>
				<a href="javascript:;" onclick="detaildialog('.$row_fruit[fruitid].')">
					<img src="'.$row_fruit[imgpath].'" />
				</a>
				<font>'.$row_fruit[name].'</font>
				<strong>￥'.$row_fruit[price].'/'.$spdwvalue.'</strong>
				<span onclick="addtocart('.$row_fruit2[fruitid].', 1, 0);"><font>加入购物车</font></span>
			</li>'."\n";
    }
    echo '</ul>'."\n";
    
    $db->close();
?>