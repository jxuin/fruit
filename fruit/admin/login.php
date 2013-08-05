<?php
session_start();
//判断session是否存在，否则跳转登陆页
if (isset($_SESSION["userid"])) {
	header("location:admin.php");
}
?>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8">
    <title>社区水果在线商店</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <link href="../resources/css/style-admin.css" rel="stylesheet">
    <link href="../resources/css/dialog/idialog.css" rel="stylesheet"/>
    <link href="../resources/bootstrap/css/bootstrap.min.css" rel="stylesheet" media="screen">
    <link href="../resources/bootstrap/css/bootstrap-responsive.min.css" rel="stylesheet">

    <!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
	<!--[if lt IE 9]>
  	<script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
  	<script src="http://css3-mediaqueries-js.googlecode.com/svn/trunk/css3-mediaqueries.js"></script>
	<![endif]-->
	<link rel="shortcut icon" href="../resources/logo/logo_32.png" type="image/x-icon" />
	<link rel="icon" href="../resources/logo/logo_32.png" />
	<link rel="bookmark" href="../resources/logo/logo_32.png" />
	</head>
	<body>
	<div class="container">
      <form class="form-signin">
        <h2 class="form-signin-heading">登录</h2>
        <input type="text" class="input-block-level" placeholder="用户名" id="username" name="username" />
        <input type="password" class="input-block-level" placeholder="密码" id="password" name="password" />
        <label class="checkbox">
          <input type="checkbox" id="rmbuser" /> 记住我
        </label>
        <button class="btn btn-primary" type=button id="login_btn">登录</button>
      </form>
    </div>
    <script src="../resources/js/jquery.min.js"></script>
    <script src="../resources/js/util.js"></script>
    <script src="../resources/js/plugins/jquery.cookie.js"></script>
    <script src="../resources/js/plugins/jquery.artDialog.js"></script>
    <script src="../resources/bootstrap/js/bootstrap.min.js"></script>
    <script type="text/javascript">
	    $(document).ready(function(){
	    	if ($.cookie("fruitcookie") == "true") {
				$("#rmbuser").attr("checked",true);
		    	$("#username").val($.cookie("userName"));
		    	$("#password").val($.cookie("passWord"));
			}

	    	$(document).keydown(function(e){ 
	            var curKey = e.which; 
	            if(curKey == 13){ 
	                $("#login_btn").click(); 
	                return false; 
	            } 
	        }); 
		
			$("#login_btn").click(function() {
				var username = $("#username").val();
				var password = $("#password").val();
				$.post("ajaxphp/yanz.php", {username:username,password:password}, function (callback){
					var jsonData = jQuery.parseJSON(callback);
					var status = jsonData.status;
					if (status == 0) {
						var checkObj = document.getElementById("rmbuser");
						if (checkObj.checked == true) {
					    	var userName = $("#username").val();
					        var passWord = $("#password").val();
					        $.cookie("fruitcookie", "true", { expires: 7 }); // 存储一个带7天期限的 cookie
					        $.cookie("userName", userName, { expires: 7 }); // 存储一个带7天期限的 cookie
					        $.cookie("passWord", passWord, { expires: 7 }); // 存储一个带7天期限的 cookie
					    }
					    else {
					        $.cookie("fruitcookie", "false", { expires: -1 });
					        $.cookie("userName", '', { expires: -1 });
					        $.cookie("passWord", '', { expires: -1 });
					    }
						window.location.href="admin.php";
					} else {
						$.dialog({ title: "警告", content: "<img src=\"../resources/images/warning.png\" class=\"img_shuff_1\" /> 用户名或密码错误", width:200 });
					}
				});
			});
	    });
    </script>
    </body>
    </html>