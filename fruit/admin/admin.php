<?php
session_start();
//判断session是否存在，否则跳转登陆页
if (!isset($_SESSION["userid"])) {
	header("location:login.php");
}
require('../class/ConnectionMySQL.php');
require('../config.php');
$db = new ConnectionMySQL();
?>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8">
    <title>社区水果在线商店</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <link href="../resources/bootstrap/css/bootstrap.min.css" rel="stylesheet" media="screen">
    <link href="../resources/css/style-admin.css" rel="stylesheet">
    <link href="../resources/css/dialog/idialog.css" rel="stylesheet"/>
    <link href="../resources/kindeditor/themes/default/default.css" rel="stylesheet" />
    <link href="../resources/css/qtip/jquery.qtip.min.css" rel="stylesheet" />
    <link href="../resources/bootstrap/css/bootstrap-responsive.min.css" rel="stylesheet">
	
    <!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
	<!--[if lt IE 9]>
  	<script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
  	<script src="http://css3-mediaqueries-js.googlecode.com/svn/trunk/css3-mediaqueries.js"></script>
	<![endif]-->
	<link rel="shortcut icon" href="../resources/images/logo_32.png" type="image/x-icon" />
	<link rel="icon" href="../resources/images/logo_32.png" />
	<link rel="bookmark" href="../resources/images/logo_32.png" />
	<body>
         <div class="navbar navbar-inverse navbar-fixed-top">
	      <div class="navbar-inner">
	        <div class="container-fluid">
	          <a class="brand" href="#" style="color:#fff">社区水果在线商店</a>
	          <div class="nav-collapse collapse">
	            <p class="navbar-text pull-right" style="color:#fff">欢迎：<a href="<?php echo PHP_URL?>/admin/admin.php" class="navbar-link" style="color:#fff"><?php echo $_SESSION[realname]?></a>&nbsp;[<a id="logout" href="javascript:;" class="navbar-link" style="color:#fff">退出</a>]</p>
	          </div>
	        </div>
	      </div>
	    </div>
    	<div class="container-fluid">
      		<div class="row-fluid">
        		<div id="admin_left" class="span2 doc-sitebar">
          			<div id="funcdiv" class="sidebar-nav">
          			<?php 
          				$sql = 'select a.funcid as funcid,funcname,funcurl from t_func a, t_rolefunc b where a.funcid = b.funcid and b.roleid = '.$_SESSION["roleid"].' and pfuncid = 0 order by a.funcid asc';
          				$result_func = $db->query($sql);
          				while ($row_func = mysql_fetch_array($result_func)) {
          					echo '<div class="accordion-group">'."\n";
          					echo '<div class="accordion-heading">'."\n";
          					echo '<a name="accordion_func" class="accordion-toggle" data-toggle="collapse" data-parent="#funcdiv" href="#collapse_'.$row_func[funcid].'">'."\n";
							echo $row_func[funcname]."\n";
							echo '</a>'."\n";
							echo '</div>'."\n";
							echo '<div id="collapse_'.$row_func[funcid].'" class="accordion-body collapse in">'."\n";
							echo '<div name="collapseinner" id="collapseinner_'.$row_func[funcid].'" class="accordion-inner">'."\n";
							echo '</div>'."\n";
							echo '</div>'."\n";
							echo '</div>'."\n";
          				}
          			?>
			        </div>
        		</div>
	        	<div id="admin_right" class="span10">
	        	 	
	        	</div> 
      		</div>
      	</div>
      	<div id="modaldialog" class="modal hide fade"></div>
      </body>
		<script src="../resources/js/jquery.min.js"></script>
		<script src="../resources/kindeditor/kindeditor.js"></script>
	    <script src="../resources/kindeditor/lang/zh_CN.js"></script>
	    <script src="../resources/js/plugins/jquery.cookie.js"></script>
	    <script src="../resources/js/plugins/jquery.artDialog.js"></script>
	    <script src="../resources/js/plugins/jquery.uploadify.js"></script>
	    <script src="../resources/js/plugins/jquery.jqpagination.min.js"></script>
	    <script src="../resources/bootstrap/js/bootstrap.min.js"></script>
	    <script src="../resources/js/util.js"></script>
	    <script src="../resources/js/plugins/jquery.qtip.min.js"></script>
	    <script src="../resources/js/fruit_admin.js"></script>
	    
	    <script type="text/javascript">
		    $(document).ready(function(){
				$("#logout").click(function() {
					$.get('ajaxphp/session-destroy.php',function(callback) {
						window.location.href = 'login.php';
					});
				});

				$("#funcdiv div[name='collapseinner']").each(function() {
					var id = $(this).attr('id');
					var funcid = id.split('_')[1];
					var $obj = $(this);
					$.get('ajaxphp/getSubFunc.php',{funcid:funcid},function(callback) {
						$obj.html(callback);
					});
				});
		    });
	    </script>
    </html>
    
<?php 
   $db->close();
?>