<?php
session_start();
if (!isset($_SESSION["userid"])) {
	header("location:login.php");
} else {	
	?>
		<div class="span12 breaddiv">
           <div class="span4" style="padding:7px 8px; font-size:14px;">新增</div>
           <div style="padding:4px 10px; float:right; margint-right:10px;"><a href="javascript:void(0);" class="btn btn-small" id="backbtn">返回</a></div>
           <div class="span2 pull-right text-right" style="padding:4px 10px;"><a href="javascript:void(0);" class="btn btn-small btn-primary" id="submitbtn">提交</a></div>
        </div>
        <form class="form-horizontal" style="padding:0 20px;" id="addform">
	        <div class="span12 index-tab">
	        	<ul class="nav nav-tabs">
					<li class="active"><a href="#baseattr" data-toggle="tab">基本属性</a></li>
					<li><a href="#imgsattr" data-toggle="tab">详细图片</a></li>
					<li><a href="#otherattr" data-toggle="tab">其他属性</a></li>
				</ul>
				<div class="tab-content">
					<div class="tab-pane active" id="baseattr">
						<div class="control-group">
			                 <label class="control-label">名称</label>
			                 <div class="controls">
			                   <input type="text" class="input-xxlarge" id="name" name="name" placeholder="名称" />
			                 </div>
			            </div>
			            <div class="control-group">
			                 <label class="control-label">描述</label>
			                 <div class="controls">
			                   <textarea id="remark" class="input-xxlarge" name="remark" placeholder="描述"></textarea>
			                 </div>
			            </div>
			            <div class="control-group">
			                 <label class="control-label">缩略图</label>
			                 <div class="controls">
			                   <div class="span1"><input id="imgupload_input" name="imgupload_input" type="file" multiple="true" /></div>
			                   <div class="span6" id="imgupload_preview" style="vertical-align:middle"></div>
			                 </div>
			                 <div class="controls">
			                   <input style="margin-top:5px;" type="text" name="imgpathinput" id="imgpathinput" class="input-xxlarge" />
			                 </div>
						</div>
			           	<div class="control-group">
			                 <label class="control-label">价格</label>
			                 <div class="controls">
			                   <input type="text" class="input-xxlarge" id="price" name="price" placeholder="价格" />
			                 </div>
			            </div>
			            <div class="control-group">
			                 <label class="control-label">商品单位</label>
			                 <div class="controls">
			                 	<select id="spdw" name="spdw">
  									<option value="1">500g</option>
  									<option value="2">箱</option>
  									<option value="3">盒</option>
								</select>
			                 </div>
			            </div>
			            <div class="control-group">
			                 <label class="control-label">库存</label>
			                 <div class="controls">
			                   <input type="text" class="input-xxlarge" id="kc" name="kc" placeholder="库存" />
			                 </div>
			            </div>
			            <div class="control-group">
			                 <label class="control-label">配送范围</label>
			                 <div class="controls">
			                   <textarea id="psfw" class="input-xxlarge" name="psfw" placeholder="配送范围"></textarea>
			                 </div>
			            </div>
			            <div class="control-group">
			                 <label class="control-label">产地</label>
			                 <div class="controls">
			                   <input type="text" class="input-xxlarge" id="cd" name="cd" placeholder="产地" />
			                 </div>
			            </div>
			            <div class="control-group">
			                 <label class="control-label">品牌</label>
			                 <div class="controls">
			                   <input type="text" class="input-xxlarge" id="pp" name="pp" placeholder="品牌" />
			                 </div>
			            </div>
			            <div class="control-group">
			                 <label class="control-label">首页显示</label>
			                 <div class="controls">
			                   <input type="text" class="input-xxlarge" id="syxslc" name="syxslc" placeholder="首页显示楼层" value="1" />
			                 </div>
			            </div>
					</div>
					<div class="tab-pane" id="imgsattr">
						<div class="control-group">
			                 <label class="control-label">详细图片</label>
			                 <div class="controls controls-row">
			                   <div class="span1"><input id="imgsupload_input" name="imgsupload_input" type="file" multiple="true" /></div>
			                   <div class="span6 imgprediv" id="imgsupload_preview"></div>
			                 </div>
			            </div>
					</div>
					<div class="tab-pane" id="otherattr">
						<div class="control-group">
			                 <label class="control-label">商品介绍</label>
			                 <div class="controls">
			                   <textarea name="spjs" id="spjs" style="width:700px;height:100px;"></textarea>
			                </div>
			            </div>
			            <div class="control-group">
			                 <label class="control-label">存储方式</label>
			                 <div class="controls">
			                   <textarea name="ccfs" id="ccfs" style="width:700px;height:100px;"></textarea>
			                </div>
			            </div>
			            <div class="control-group">
			                 <label class="control-label">商品禁忌</label>
			                 <div class="controls">
			                   <textarea name="spjj" id="spjj" style="width:700px;height:100px;"></textarea>
			                </div>
			            </div>
			            <div class="control-group">
			                 <label class="control-label">推荐菜谱</label>
			                 <div class="controls">
			                   <textarea name="tjcp" id="tjcp" style="width:700px;height:100px;"></textarea>
			                </div>
			            </div>
					</div>
				</div>
	        </div>
	        
            <input type="hidden" name="pfruitid" id="pfruitid" value="<?php echo $_GET[fruitid]?>" />
            <input type="hidden" name="imgpaths" id="imgpaths" value="" />
            <input type="hidden" name="imgpath" id="imgpath" value="" />
            <div id="imgupload_input-queue" style="display:none" class="uploadify-queue"></div>
            <div id="imgsupload_input-queue" style="display:none" class="uploadify-queue"></div>
        </form>
        
        <script type="text/javascript">
        $(document).ready(function(){
        	var spjseditor,ccfseditor,spjjeditor,tjcpeditor;
        	spjseditor = KindEditor.create('textarea[name="spjs"]', {
                allowFileManager : true,
                uploadJson : '../resources/kindeditor/editor_upload.php',
                fileManagerJson : '../resources/kindeditor/editor_file_manager.php',
                afterBlur: function(){this.sync();}
            });

        	ccfseditor = KindEditor.create('textarea[name="ccfs"]', {
                allowFileManager : true,
                uploadJson : '../resources/kindeditor/editor_upload.php',
                fileManagerJson : '../resources/kindeditor/editor_file_manager.php',
                afterBlur: function(){this.sync();}
            });

        	spjjeditor = KindEditor.create('textarea[name="spjj"]', {
                allowFileManager : true,
                uploadJson : '../resources/kindeditor/editor_upload.php',
                fileManagerJson : '../resources/kindeditor/editor_file_manager.php',
                afterBlur: function(){this.sync();}
            });

        	tjcpeditor = KindEditor.create('textarea[name="tjcp"]', {
                allowFileManager : true,
                uploadJson : '../resources/kindeditor/editor_upload.php',
                fileManagerJson : '../resources/kindeditor/editor_file_manager.php',
                afterBlur: function(){this.sync();}
            });

            $("#backbtn").click(function() {
            	fruitlist($("#pfruitid").val(), 1);
            });
	    	
			$("#submitbtn").click(function() {
				var imgpathStr = '';
				$("#imgsupload_preview a[name='imgpatha']").each(function() {
					imgpathStr += $(this).attr('href') + ',' + $(this).html() + '|';
				});
				$("#imgpaths").val(imgpathStr);

				spjseditor.sync();
				ccfseditor.sync();
				spjjeditor.sync();
				tjcpeditor.sync();
				
				var linkurl = $("#linkurl").val();
				if (linkurl != "" && linkurl != null) {
					if (linkurl.indexOf('http://') != 0 && linkurl.indexOf('https://') != 0) {
						$("#linkurl").val("http://" + $("#linkurl").val());
					}
				}
				$.post("ajaxphp/fruitsubmit.php",$('#addform').serialize(),function(callback) {
					$.dialog({
						title: "消息",
						padding: "30px 25px 10px",
					    content: "<img src=\"../resources/images/valid.png\" class=\"img_shuff_1\" /> 保存成功",
					    ok: function () {
						    fruitlist($("#pfruitid").val(), 1);
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
				onUploadError: function(file, errorCode, errorMsg){
//					showDialog('warning.png','上传失败',150);
				}
			});

			$('#imgsupload_input').uploadify({
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
					var previewHtml = '<span class="imgpreview" ><a name="imgpatha" href="'+filepath+'" target="_blank">'+filename+'</a><a href="javascript:;" class="del_a" onclick="removeParent(this);">删除</a></span>';
					$("#imgsupload_preview").append(previewHtml);
				},
				onUploadError: function(file, errorCode, errorMsg){
//					showDialog('warning.png','上传失败',150);
				}
			});
        });
		</script>
			
<?php }?>
	        
	        
	        