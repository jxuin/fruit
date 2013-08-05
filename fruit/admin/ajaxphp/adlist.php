<?php
session_start();
if (!isset($_SESSION["userid"])) {
	header("location:login.php");
} else {
	require('../../class/ConnectionMySQL.php');
	$db = new ConnectionMySQL();
	
	$result = $db->query("select adid,name,seq,status from t_admsg where rstatus = 0 order by seq desc, adid desc");
?>
	<div class="span12 breaddiv">
	<?php
		echo '<div class="span2 pull-right text-right" style="padding:4px 10px;"><a href="javascript:void(0);" class="btn btn-small btn-primary" onclick="adadd();"><i class="icon-plus icon-white" style="margin-top:1px;"></i> 新增</a></div>'."\n";
	?>
	</div>
	<table class="table table-bordered">
		<thead>
		<tr>
			<th width="5%" style="text-align:center">#</th>
			<th width="55%" style="text-align:center">名称</th>
			<th width="10%" style="text-align:center">状态</th>
			<th width="30%" style="text-align:center">操作&nbsp;&nbsp;&nbsp;&nbsp;<a class="btn btn-mini btn-primary" onclick="adseqsave()">保存</a></th>
		</tr>
		</thead>
		<tbody>
		<?php 
		$index = 0;
		while ($row = mysql_fetch_array($result)) {
			$index ++;
			echo '<tr>';
			echo '<td style="text-align:center">'.$index.'</td>';
			echo '<td>'.$row[name].'</td>';
			if ($row[status] == 0) {
				echo '<td style="text-align:center">'.'已发布'.'</td>';
				$statusoptname = '撤销';
			} else {
				echo '<td style="text-align:center">'.'未发布'.'</td>';
				$statusoptname = '发布';
			}
			echo '<td style="text-align:center">
					<a class="btn btn-mini btn-primary" onclick="adedit('.$row[adid].')">编辑</a>&nbsp;&nbsp;
					<a class="btn btn-mini btn-danger" onclick="addel('.$row[adid].')">删除 </a>&nbsp;&nbsp;
					<a class="btn btn-mini" onclick="adpub('.$row[adid].', '.$row[status].')">'.$statusoptname.'</a>&nbsp;&nbsp;
					<input seq="'.$row[seq].'" name="seq_input" id="seq_'.$row[adid].'" type="text" style="height:12px; width:40px; margin-bottom:0;" class="input-mini" value="'.$row[seq].'" />
				  </td>';
			echo '</tr>';
		}
		?>
		</tbody>
	</table>
	
	<script type="text/javascript">
	$(document).ready(function(){

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

	function adadd() {
		$.get('ajaxphp/adadd.php',function(callback) {
			$("#admin_right").html(callback);
		});
	}

	function adedit(adid) {
		$.get('ajaxphp/adedit.php',{adid:adid},function(callback) {
			if (callback == 'nosession') {
				window.location.href = 'login.php';
			} else {
				$("#admin_right").html(callback);
			}
		});
	}

	function addel(adid) {
		$.dialog({
			padding: "30px 25px 10px",
			title: "警告",
		    content: "<img src=\"../resources/images/warning.png\" class=\"img_shuff_1\" /> 确认删除",
		    ok: function () {
		    	$.get('ajaxphp/addel.php',{adid:adid},function(callback) {
					if (callback == 'nosession') {
						window.location.href = 'login.php';
					} else {
						adlist();
					}
				});
		    }
		});
	}

	function adpub(adid, status) {
		var statuschange = 0;
		if (status == 0) {
			statuschange = 1;
		}
		$.get('ajaxphp/adstatus.php',{adid:adid, status:statuschange},function(callback) {
			if (callback == 'nosession') {
				window.location.href = 'login.php';
			} else {
				adlist();
			}
		});
	}

	function adseqsave() {
		var seqs = '';
		$("input[name='seq_input']").each(function() {
			seqs += trim($(this).val()) + ',' + $(this).attr("id").replace("seq_","") + '|';
		});
		$.post('ajaxphp/adseq.php',{seqs:seqs},function(callback) {
			if (callback == 'nosession') {
				window.location.href = 'login.php';
			} else {
				adlist();
			}
		});
	}
	</script>

<?php $db->close();
} ?>
