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
	
	$resulttotal = $db->query("select count(fruitid) as num from t_fruit where pfruitid = '".$_GET[fruitid]."' and rstatus = 0");
	
	$totalpage = 1;
	
	if($totalpagerow = mysql_fetch_array($resulttotal)) {
		$totalpage = floor(($totalpagerow[num] - 1) / $pagesize) + 1;
	}
	
	if ($currepage > $totalpage && $totalpage > 0) {
		$currepage = $totalpage;
	}
	
	$indexcol = ($currepage - 1) * $pagesize;
	
	$result = $db->query("select fruitid,name,seq,spdw,status from t_fruit
						where pfruitid = '".$_GET[fruitid]."' and rstatus = 0 order by seq desc, fruitid desc limit ".$indexcol.", ".$pagesize);

	$result1 = $db->query("select name from t_fruit where fruitid = '".$_GET[fruitid]."' limit 0,1");
?>
	<div class="span12 breaddiv">
	<?php
		if ($row1 = mysql_fetch_array($result1)) {
			echo '<div class="span4" style="padding:7px 8px; font-size:14px;">'.$row1[name].'</div>'."\n";
			echo '<div style="padding:4px 10px; float:right; margint-right:10px;"><a href="javascript:void(0);" class="btn btn-small" id="backbtn">返回</a></div>'."\n";
		}
		echo '<div class="span2 pull-right text-right" style="padding:4px 10px;"><a href="javascript:void(0);" class="btn btn-small btn-primary" onclick="fruitadd('.$_GET[fruitid].');"><i class="icon-plus icon-white" style="margin-top:1px;"></i> 新增</a></div>'."\n";
	?>
	</div>
	<table class="table table-bordered">
		<thead>
		<tr>
			<th width="5%" style="text-align:center">#</th>
			<th width="55%" style="text-align:center">名称</th>
			<th width="10%" style="text-align:center">状态</th>
			<th width="30%" style="text-align:center">操作&nbsp;&nbsp;&nbsp;&nbsp;<a class="btn btn-mini btn-primary" onclick="fruitseqsave()">保存</a></th>
		</tr>
		</thead>
		<tbody>
		<?php 
		$index = 0;
		while ($row = mysql_fetch_array($result)) {
			$index ++;
			echo '<tr>';
			echo '<td style="text-align:center">'.$index.'</td>';
			if (empty($_GET[fruitid]) || $_GET[fruitid] == 0) {
				echo '<td><a href="javascript:;" onclick="fruitlist('.$row[fruitid].')">'.$row[name].'</a></td>';
			} else {
				echo '<td>'.$row[name].'</td>';
			}
			if ($row[status] == 0) {
				echo '<td style="text-align:center">'.'已上架'.'</td>';
				$statusoptname = '下架';
			} else {
				echo '<td style="text-align:center">'.'未上架'.'</td>';
				$statusoptname = '上架';
			}
			echo '<td style="text-align:center">
					<a class="btn btn-mini btn-primary" onclick="fruitedit('.$row[fruitid].')">编辑</a>&nbsp;&nbsp;
					<a class="btn btn-mini btn-danger" onclick="fruitdel('.$row[fruitid].')">删除 </a>&nbsp;&nbsp;
					<a class="btn btn-mini" onclick="fruitpub('.$row[fruitid].', '.$row[status].')">'.$statusoptname.'</a>&nbsp;&nbsp;
					<input seq="'.$row[seq].'" name="seq_input" id="seq_'.$row[fruitid].'" type="text" style="height:12px; width:40px; margin-bottom:0;" class="input-mini" value="'.$row[seq].'" />
				  </td>';
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
		var currepage = <?php echo $currepage?>;
		var totalpage = <?php echo $totalpage?>;
		var fruitid = <?php echo $_GET[fruitid]?>;
		$(".pagination").jqPagination({
			link_string	: '/?page={page_number}',
			current_page: currepage,
			max_page	: totalpage,
			paged		: function(page) {
				fruitlist(fruitid,page);
			}
		});

		$("#backbtn").click(function() {
        	fruitlist(0, 1);
        });

		$("input[name='seq_input']").each(function() {
			$(this).blur(function() {
				if (IsNumber($(this).val())) {
					$(this).attr("seq",trim($(this).val()));
				} else {
					$(this).val($(this).attr("seq"));
					showDialog('../resources/css/dialog/icons/warning.png','请输入数字',150);
				}
			});
		});
	});

	function fruitadd(fruitid) {
		$.get('ajaxphp/fruitadd.php',{fruitid:fruitid},function(callback) {
			$("#admin_right").html(callback);
		});
	}

	function fruitedit(fruitid) {
		$.get('ajaxphp/fruitedit.php',{fruitid:fruitid},function(callback) {
			if (callback == 'nosession') {
				window.location.href = 'login.php';
			} else {
				$("#admin_right").html(callback);
			}
		});
	}

	function fruitdel(fruitid) {
		$.dialog({
			title: "警告",
		    content: "<img src=\"../resources/images/warning.png\" class=\"img_shuff_1\" /> 确认删除",
		    ok: function () {
		    	$.get('ajaxphp/fruitdel.php',{fruitid:fruitid},function(callback) {
					if (callback == 'nosession') {
						window.location.href = 'login.php';
					} else {
						var pfruitid = <?php echo $_GET[fruitid]?>;
						var page = <?php echo $currepage?>;
						fruitlist(pfruitid, page);
					}
				});
		    }
		});
	}

	function fruitpub(fruitid, status) {
		var statuschange = 0;
		if (status == 0) {
			statuschange = 1;
		}
		$.get('ajaxphp/fruitstatus.php',{fruitid:fruitid, status:statuschange},function(callback) {
			if (callback == 'nosession') {
				window.location.href = 'login.php';
			} else {
				var pfruitid = <?php echo $_GET[fruitid]?>;
				var page = <?php echo $currepage?>;
				fruitlist(pfruitid, page);
			}
		});
	}

	function fruitseqsave() {
		var seqs = '';
		$("input[name='seq_input']").each(function() {
			seqs += trim($(this).val()) + ',' + $(this).attr("id").replace("seq_","") + '|';
		});
		$.post('ajaxphp/fruitseq.php',{seqs:seqs},function(callback) {
			if (callback == 'nosession') {
				window.location.href = 'login.php';
			} else {
				var pfruitid = <?php echo $_GET[fruitid]?>;
				var page = <?php echo $currepage?>;
				fruitlist(pfruitid, page);
			}
		});
	}
	</script>

<?php $db->close();
} ?>
