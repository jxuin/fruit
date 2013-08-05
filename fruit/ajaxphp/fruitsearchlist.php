<?php
	require('../class/ConnectionMySQL.php');
	$db = new ConnectionMySQL();
	
	$searchkey = $_POST[searchkey];
	
	$pagesize = 15;
	
	$currepage = $_POST[currepage];
	if ($currepage == null || $currepage < 1) {
		$currepage = 1;
	}
	
	$resulttotal = $db->query("select count(fruitid) as num from t_fruit where name like '%".$searchkey."%' and rstatus = 0 and status = 0");
	
	$totalpage = 1;
	
	if($totalpagerow = mysql_fetch_array($resulttotal)) {
		$totalpage = floor(($totalpagerow[num] - 1) / $pagesize) + 1;
	}
	
	if ($currepage > $totalpage && $totalpage > 0) {
		$currepage = $totalpage;
	}
	
	$indexcol = ($currepage - 1) * $pagesize;
	
	$result_fruit = $db->query("select fruitid,name,imgpath,spdw,price,kc from t_fruit
						where name like '%".$searchkey."%' and rstatus = 0 order by seq desc, fruitid desc limit ".$indexcol.", ".$pagesize);
	
	echo '<div class="mclist-d-list">'."\n";
	echo '<ul>'."\n";
    while ($row_fruit = mysql_fetch_array($result_fruit)) {
       	$spdw = $row_fruit[spdw];
        $spdwvalue = "500g";
        if ($spdw == 2) {
        	$spdwvalue = "箱";
        } else if ($spdw == 3) {
        	$spdwvalue = "盒";
        }
		echo '<li><a href="javascript:;" onclick="detaildialog('.$row_fruit[fruitid].')"><img src="'.$row_fruit[imgpath].'" /></a><font>'.$row_fruit[name].'</font><strong>￥'.$row_fruit[price].'/'.$spdwvalue.'</strong><span onclick="addtocart('.$row_fruit[fruitid].', 1, 0);"><font>加入购物车</font></span></li>'."\n";
    }
    echo '</ul>'."\n";
    echo '</div>'."\n";
    
?>
	<div class="pagination">
       	<a href="#" class="first" data-action="first">&laquo;</a>
    	<a href="#" class="previous" data-action="previous">&lsaquo;</a>
    	<input type="text" readonly="readonly" />
    	<a href="#" class="next" data-action="next">&rsaquo;</a>
    	<a href="#" class="last" data-action="last">&raquo;</a>
    </div>
    
    <script type="text/javascript">

	$(document).ready(function(){
		var currepage = <?php echo $currepage?>;
		var totalpage = <?php echo $totalpage?>;
		var searchkey = '<?php echo $_POST[searchkey]?>';
		$(".pagination").jqPagination({
			link_string	: '/?page={page_number}',
			current_page: currepage,
			max_page	: totalpage,
			paged		: function(page) {
				fruitsearchlist(fruitid,page);
			}
		});
	});

	</script>

<?php 
    $db->close();
?>