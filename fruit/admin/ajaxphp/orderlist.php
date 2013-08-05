<?php
session_start();
if (!isset($_SESSION["userid"])) {
	header("location:login.php");
} else {
	require('../../class/ConnectionMySQL.php');
	$db = new ConnectionMySQL();
	
	$pagesize = 15;
	
	$currepage = $_GET[currepage];
	if ($currepage == null || $currepage < 1) {
		$currepage = 1;
	}
	
	$selstatus = $_GET[status];
	
	$subsqlstr = "";
	
	if ($selstatus > -1) {
		$subsqlstr = " and status = ".$selstatus;
	}
	
	$resulttotal = $db->query("select count(orderid) as num from t_order where rstatus = 0".$subsqlstr);
	
	$totalpage = 1;
	
	if($totalpagerow = mysql_fetch_array($resulttotal)) {
		$totalpage = floor(($totalpagerow[num] - 1) / $pagesize) + 1;
	}
	
	if ($currepage > $totalpage && $totalpage > 0) {
		$currepage = $totalpage;
	}
	
	$indexcol = ($currepage - 1) * $pagesize;
	
	$result = $db->query("select orderid,shr,shdz,shsj,sjhm,status,ordernum,money from t_order
						where rstatus = 0 ".$subsqlstr." order by orderid asc limit ".$indexcol.", ".$pagesize);

?>
	<div class="span12 breaddiv">
	<?php
		echo '<div class="span4">
				<select id="status-sel" style="margin:2px 0 0 5px;">
					<option value="-1">全部</option>
					<option value="0">未配送</option>
					<option value="1">正在配送</option>
					<option value="2">已配送</option>
				</select>
			  </div>'."\n";
	?>
	</div>
	<table class="table table-bordered">
		<thead>
		<tr>
			<th width="10%" style="text-align:center">订单号</th>
			<th width="5%" style="text-align:center">总额</th>
			<th width="10%" style="text-align:center">收货人</th>
			<th width="25%" style="text-align:center">地址</th>
			<th width="10%" style="text-align:center">电话号码</th>
			<th width="20%" style="text-align:center">送货时间</th>
			<th width="10%" style="text-align:center">状态</th>
			<th width="10%" style="text-align:center">操作</th>
		</tr>
		</thead>
		<tbody>
		<?php 
		while ($row = mysql_fetch_array($result)) {
			echo '<tr>';
			echo '<td style="text-align:center"><a name="ordernum_a" id="ordernum_'.$row[orderid].'" href="javascript:;">'.$row[ordernum].'</a></td>';
			echo '<td style="text-align:center">'.$row[money].'</td>';
			echo '<td style="text-align:center">'.$row[shr].'</td>';
			echo '<td>'.$row[shdz].'</td>';
			echo '<td style="text-align:center">'.$row[sjhm].'</td>';
			echo '<td>'.$row[shsj].'</td>';
			if ($row[status] == 1) {
				echo '<td style="text-align:center">'.'正在配送'.'</td>';
			} else if ($row[status] == 2) {
				echo '<td style="text-align:center">'.'已配送'.'</td>';
			} else {
				echo '<td style="text-align:center">'.'未配送'.'</td>';
			}
			echo '<td style="text-align:center"><a name="orderopt_a" id="orderopt_'.$row[orderid].'" class="btn btn-mini btn-primary">操作</a></td>';
			echo '</tr>';
		}
		?>
		</tbody>
	</table>
	<div class="pagination">
       	<a href="#" class="first" data-action="first">&laquo;</a>
    	<a href="#" class="previous" data-action="previous">&lsaquo;</a>
    	<input type="text" readonly="readonly" />
    	<a href="#" class="next" data-action="next">&rsaquo;</a>
    	<a href="#" class="last" data-action="last">&raquo;</a>
    </div>
	
	<script type="text/javascript">
	$(document).ready(function(){

		$("#status-sel").val(<?php echo $selstatus?>);
		
		var currepage = <?php echo $currepage?>;
		var totalpage = <?php echo $totalpage?>;
		var status = $("#status-sel").val();
		$(".pagination").jqPagination({
			link_string	: '/?page={page_number}',
			current_page: currepage,
			max_page	: totalpage,
			paged		: function(page) {
				orderlist(page,status);
			}
		});

		$("a[name='ordernum_a']").click(function() {
			var orderid = $(this).attr("id").replace("ordernum_","");
			$.get('ajaxphp/orderdetail.php',{orderid:orderid},function(callback) {
				$.dialog({
					id: 'detaildialog',
					padding: '5px 8px',
					esc: true,
					fixed: true,
				    content: callback
				});
			});
		});

		$("a[name='orderopt_a']").each(function() {
			var orderid = $(this).attr("id").replace("orderopt_","");
			$(this).qtip({
				id:'order-'+orderid,
	            content: {
					button: '关闭',
					title: '操作',
	                text: '<div style="padding: 5px 9px;"><a class="btn btn-mini" onclick="orderstatus('+orderid+',0);">未配送</a>&nbsp;&nbsp;<a class="btn btn-mini" onclick="orderstatus('+orderid+',1);">正在配送</a>&nbsp;&nbsp;<a class="btn btn-mini" onclick="orderstatus('+orderid+',2);">已配送</a></div>'
	            },
	            style: {
	           		width: 195,
	               	classes: 'qtip-light qtip-shadow qtip-rounded'
	            },
	            hide: false,
	            position: {
	               	my: 'center right',
	               	at: 'center left',
	               	target: $(this),
	               	viewport: $(window)
	            }
	        });
		});

		$("#status-sel").change(function() {
			var status = $("#status-sel").val();
			orderlist(1,status);
		});
		
	});

	function orderstatus(orderid, status) {
		$("#qtip-order-" + orderid).hide();
		$.get('ajaxphp/orderstatus.php',{orderid:orderid, status:status},function(callback) {
			if (callback == 'nosession') {
				window.location.href = 'login.php';
			} else {
				var page = <?php echo $currepage?>;
				var status = $("#status-sel").val();
				orderlist(page, status);
			}
		});
	}

	</script>

<?php $db->close();
} ?>
