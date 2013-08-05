<?php
session_start();
if (!isset($_SESSION["userid"])) {
	header("location:login.php");
} else {
	require('../../class/ConnectionMySQL.php');
	$db = new ConnectionMySQL();
	
	$result = $db->query("select a.name,a.fruitid,b.fruitnum,b.money,b.ordernum,a.price,a.spdw from t_fruit a, t_orderdetail b where a.fruitid = b.fruitid and orderid = ".$_GET[orderid]);

?>	
<div class="dialog_table">
	<table class="table table-bordered">
		<thead>
		<tr>
			<th width="55%" style="text-align:center">名称</th>
			<th width="15%" style="text-align:center">价格</th>
			<th width="15%" style="text-align:center">数量</th>
			<th width="15%" style="text-align:center">金额</th>
		</tr>
		</thead>
		<tbody>
		
		<?php 
		while ($row = mysql_fetch_array($result)) {
			echo '<tr>';
			echo '<td style="text-align:center">'.$row[name].'</td>';
			echo '<td style="text-align:center">'.$row[price].'</td>';
			echo '<td style="text-align:center">'.$row[fruitnum].'</td>';
			echo '<td style="text-align:center">'.$row[money].'</td>';
			echo '</tr>';
		}
		?>
		
		</tbody>
	</table>
</div>
<?php 

	$db->close();
}

?>