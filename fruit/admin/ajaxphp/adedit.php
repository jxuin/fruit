<?php
session_start();
if (!isset($_SESSION["userid"])) {
	header("location:login.php");
} else {	
	require('../../class/ConnectionMySQL.php');
	$db = new ConnectionMySQL();
	
	$baseattrresult = $db->query("select adid,name,linkurl,imgpath from t_admsg where adid = ".$_GET[adid]);
	$baserow = mysql_fetch_array($baseattrresult);
	
	?>
		<div class="span12 breaddiv">
           <div class="span4" style="padding:7px 8px; font-size:14px;">编辑</div>
           <div style="padding:4px 10px; float:right; margint-right:10px;"><a href="javascript:void(0);" class="btn btn-small" id="backbtn">返回</a></div>
           <div class="span2 pull-right text-right" style="padding:4px 10px;"><a href="javascript:void(0);" class="btn btn-small btn-primary" id="submitbtn">提交</a></div>
        </div>
        <form class="form-horizontal" style="padding:0 20px;" id="addform">
	        <div class="control-group">
                 <label class="control-label">名称</label>
                 <div class="controls">
                   <input type="text" class="input-xxlarge" id="name" name="name" value="<?php echo $baserow[name]?>" placeholder="名称" />
                 </div>
            </div>
            <div class="control-group">
                 <label class="control-label">链接</label>
                 <div class="controls">
                   <input type="url" class="input-xxlarge" id="linkurl" name="linkurl" value="<?php echo $baserow[linkurl]?>" placeholder="链接" />
                 </div>
            </div>
            <div class="control-group">
                 <label class="control-label">缩略图</label>
                 <div class="controls controls-row">
                   <div class="span1"><input id="imgupload_input" name="imgupload_input" type="file" multiple="true" /></div>
                   <div class="span4" style="padding:6px 0 0" id="imgupload_preview">
                   	<?php 
	                   	if (!empty($baserow[imgpath])) {
	                   		echo '<span class="imgpreview"><a name="imgpatha" href="'.$baserow[imgpath].'" target="_blank">预览</a><a href="javascript:;" class="del_a" onclick="removeParent(this);">删除</a></span>'."\n";
	                   	}
	                ?>
                   </div>
                 </div>
            </div>
	        
            <input type="hidden" name="adid" id="adid" value="<?php echo $baserow[adid]?>" />
            <input type="hidden" name="imgpath" id="imgpath" value="<?php echo $baserow[imgpath]?>" />
            <div id="imgupload_input-queue" style="display:none" class="uploadify-queue"></div>
        </form>
        
        <script type="text/javascript">
        $(document).ready(function(){


        	$("#backbtn").click(function() {
            	adlist();
            });
	    	
			$("#submitbtn").click(function() {
				
				var linkurl = $("#linkurl").val();
				if (linkurl != "" && linkurl != null) {
					if (linkurl.indexOf('http://') != 0 && linkurl.indexOf('https://') != 0) {
						$("#linkurl").val("http://" + $("#linkurl").val());
					}
				}
				$.post("ajaxphp/adupdate.php",$('#addform').serialize(),function(callback) {
					$.dialog({
						title: "消息",
						padding: "30px 25px 10px",
					    content: "<img src=\"../resources/images/valid.png\" class=\"img_shuff_1\" /> 保存成功",
					    ok: function () {
					    	adlist();
					    }
					});
				});
			});

			$('#imgupload_input').uploadify({
				'swf'      : '../resources/js/plugins/uploadify.swf',
				'uploader' : '../ajaxphp/uploadify.php',
				'method'   : 'post',
				'fileTypeDesc': '请选择图片格式',
				'fileTypeExts': '*.jpg;*.jpeg;*.png;*.gif;*.JPG;*.JPEG;*.PNG;*.GIF;',
				buttonText: '<button type="button" class="btn btn-primary">上传</button>',
				width:60,
				onUploadSuccess: function(file, data, response){
					var jsonData = jQuery.parseJSON(data);
					var filepath = jsonData.filepath;
					var filename = jsonData.filename;
					filename = filename.substr(0, filename.lastIndexOf("."));
					$("#imgpath").val(filepath);
					var previewHtml = '<span class="imgpreview" ><a name="imgpatha" href="'+filepath+'" target="_blank">预览</a><a href="javascript:;" class="del_a" onclick="removeParent(this);">删除</a></span>';
					$("#imgupload_preview").html(previewHtml);
				},
				onUploadError: function(file, errorCode, errorMsg){}
			});

        });
		</script>
			
<?php 
	$db->close();
}?>
	        
	        
	        