	<div class="mc-top">
    	<div class="mc-top-c1">
    		<div class="mc-top-imgs">
    			<ul class="mc-top-imgs-ul">
    				<?php 
    					$imgsindex = 0;
    					$result_admsg = $db->query("select imgpath,linkurl from t_admsg where rstatus = 0 and status = 0 limit 0,5");
    					while ($row_admsg = mysql_fetch_array($result_admsg)) {
    						echo '<li><img src="'.$row_admsg[imgpath].'" /></li>'."\n";
    						$imgsindex ++;
    					}
    				?>
    			</ul>
    			<div class="mc-top-imgs-btns">
    			<?php 
    				$spanindex = 0;
					while($spanindex < $imgsindex)
						{
					  		echo '<span></span>';
					  		$spanindex++;
					  	}
    			?>
    			</div>
    		</div>
		</div>
		<div class="mc-top-r">
			<div class="sale-d">
				<span class="sale-d-l">收货人</span>
		        <span class="sale-d-r"><input id="shr" name="shr" /></span>
			</div>
			<div class="sale-d">
				<span class="sale-d-l">地址</span>
				<span id="addrcontent" class="sale-d-r addr"></span>
			</div>
			<div class="sale-d" style="margin-top: 3px;">
				<span class="sale-d-l">&nbsp;</span>
				<span class="sale-d-r" style="text-align: right">
					<span id="addrbtn" class="btn btn-min">选择</span>
				</span>
			</div>
			<div class="sale-d">
				<span class="sale-d-l">电话号码</span>
				<span class="sale-d-r"><input id="sjhm" name="sjhm" /></span>
			</div>
			<div class="sale-d">
				<span class="sale-d-l">送货时间</span>
				<span class="sale-d-r"><input id="shsj" name="shsj" /></span>
			</div>
			<div class="sale-d">
				<span class="sale-d-l">&nbsp;</span>
				<span class="sale-d-r" style="text-align: right">
					<a id="jssm" href="javascript:;" class="jssm">说明</a>
					<a id="jsbtn" href="javascript:;" class="btn">结算</a>
				</span>
			</div>
			<div style="display:none">
				<span class="addrselspan">
					<ul>
						<li><select id="sel_0"></select></li>
						<li><select id="sel_1"></select><select id="sel_2" onchange="addrselchange(2);"></select><select id="sel_3"></select></li>
						<li><a href="javascript:;" class="btn btn-min">确定</a></li>
					</ul>
				</span>
			</div>
		</div>
	</div>
	
	<script type="text/javascript">

		var addrvalues = '';

		var addrarray = new Array();

		var imgwidth;

		var imglen;
		
		$(document).ready(function(){
			
			imgwidth = $(".mc-top-imgs").width();
			imglen = $(".mc-top-imgs ul li").length;
			$(".mc-top-imgs-btns span").css("opacity",0.4).mouseover(function() {
				index = $(".mc-top-imgs-btns span").index(this);
				showPics(index);
			}).eq(0).trigger("mouseover");

			$(".mc-top-imgs ul").css("width",imgwidth * (imglen));

			initJsStr();

			addrselchange(0, 0);
			
			$('#jssm').qtip({
	             content: {
	                 text: '<div class="tipdiv"><?php echo JSSHUOMING?></div>'
	             },
				 style: {
            		width: 298,
            		classes: 'qtip-light qtip-shadow qtip-rounded'
	             },
	             position: {
	                 my: 'center right',
	                 at: 'center left',
	                 target: $('#jssm'),
	                 viewport: $(window),
	              }
			});

			$('#addrbtn').qtip({
				 id: "addrtip",
	             content: {
					text: $(".addrselspan"),
					button: '关闭',
					title: '请选择'
	             },
				 style: {
           			width: 230,
           			classes: 'qtip-light qtip-shadow qtip-rounded'
	             },
	             hide: false,
	             show: {
	                 event: 'click'
	             },
	             position: {
	                 my: 'center right',
	                 at: 'center left',
	                 target: $('#addrbtn'),
	                 viewport: $(window),
	              }
			});

			$(".addrselspan select").each(function() {
				var id = $(this).attr("id");
				var tmpid = parseInt(id.replace('sel_', ''));
				tmpid = tmpid + 1;
				if (tmpid < 3) {
					$(this).change(function() {
						addrselchange($(this).val(), tmpid);
					});
				}
			});

			$(".addrselspan a").each(function() {
				$(this).click(function() {
					var addrstr = '';
					var addrvalues = '';
					$(".addrselspan select").each(function() {
						addrstr += $(this).find("option:selected").text() + ' ';
						addrvalues += $(this).val() + '|';
					});
					$("#addrcontent").html(addrstr);
					$("#qtip-addrtip").hide();
					$.cookie("fruitaddrcontent", addrstr, { expires: 7 });
					$.cookie("fruitaddrvalues", addrvalues, { expires: 7 });
				});
			});

			$("#jsbtn").click(function() {
				var fruitnum = $.cookie("fruit2cartnum");

				var shr = trim($("#shr").val());
				var addr = trim($("#addrcontent").html());
				var sjhm = trim($("#sjhm").val());
				
				if (parseInt(getstr(fruitnum,'0')) == 0) {
					//警告
					showDialog('resources/css/dialog/icons/warning.png','您的购物车内还没有商品',200);
					return;
				}
				if (shr == '') {
					//警告
					showDialog('resources/css/dialog/icons/warning.png','请输入收货人',200);
					return;
				}
				if (addr == '') {
					//警告
					showDialog('resources/css/dialog/icons/warning.png','请选择收货地址',200);
					return;
				}
				if (!IsPhone(sjhm) && !IsMobile(sjhm)) {
					//警告
					showDialog('resources/css/dialog/icons/warning.png','输入正确的电话号码',200);
					return;
				}
				$.cookie("fruitshr",shr,{ expires: 7 });
				$.cookie("fruitsjhm",sjhm,{ expires: 7 });
				$.cookie("fruitaddrcontent",addr,{ expires: 7 });
				$.post("ajaxphp/ordersubmit.php",{fruits:$.cookie("fruit2cart"),shr:shr,addr:addr,sjhm:sjhm,shsj:trim($("#shsj").val())},function(callback) {
					var jsonData = $.parseJSON(callback);
					if (jsonData.status == "0") {
						showDialog('resources/css/dialog/icons/succeed.png','谢谢您的购物',200);
						$.cookie("fruit2cart", null);
						setcartnum();
					}
				});
			});
		});

		function showPics(index) { //普通切换
			var nowLeft = -index*imgwidth; //根据index值计算ul元素的left值
			$(".mc-top-imgs ul").stop(true,false).animate({"left":nowLeft},300); //通过animate()调整ul元素滚动到计算出的position
			$(".mc-top-imgs-btns span").stop(true,false).animate({"opacity":"0.4"},300).eq(index).stop(true,false).animate({"opacity":"1"},300); //为当前的按钮切换到选中的效果
		}
			

		function initJsStr() {
			var addrcontent = $.cookie("fruitaddrcontent");
			var shr = $.cookie("fruitshr");
			var sjhm = $.cookie("fruitsjhm");
			$("#addrcontent").html(trim(addrcontent));
			$("#shr").val(trim(shr));
			$("#sjhm").val(trim(sjhm));
			addrvalues = $.cookie("fruitaddrvalues");
			if (addrvalues != null && addrvalues != undefined) {
				addrarray = addrvalues.split('|');
			}
		}

		function addrselchange(paddrid, selnum) {
			$.get('ajaxphp/addrsel.php', {paddrid: paddrid}, function(callback) {
				$('#sel_' + selnum).html(callback);
				if (addrarray.length > selnum) {
					if (addrarray[selnum] != null && addrarray[selnum] != undefined  && addrarray[selnum] != '') {
						$('#sel_' + selnum).val(addrarray[selnum]);
					}
				}
				paddrid = $('#sel_' + selnum).val();
				if (selnum < 3) {
					selnum ++;
					addrselchange(paddrid, selnum);
				}
			});
		}
	</script>
