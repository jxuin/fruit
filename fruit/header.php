<div class="header">	
	<div class="nav">
    	<span class="classify"><a href="index.php">社区水果在线商店</a></span>
        <span class="search">
            <form action="search.php" method="post" id="searchform" name="searchform">
                <span class="search-span"><input autocomplete="off" disableautocomplete id="searchkey" name="searchkey"/><a id="search-btn" href="javascript:;">搜索</a></span>
                <ul class="search-ul"></ul>
            </form>
        </span>
        <span class="cart">
            <span class="cartopt"><img src="resources/images/cart.png"><font>件</font><font class="cartnum">0</font></span>
        </span>
    </div>
</div>  
<div id="qtip-growl-container"></div>  
<script type="text/javascript">

	$(document).ready(function(){

		$(".cartnum").html(getCartnum(''));

         $('.cartopt').qtip({
        	 id: 'cartopttip',
             content: {
                 text: function(api) {
             		return '<span class="cartlistspan">' + setcartlist() + '</span>';	
         		 }
             },
             style: {
            	width: 350,
                classes: 'qtip-light',
                tip: {
                    corner: 'center',
                    mimic: 'center'
                }
             },
             hide: {
                event: 'unfocus'
             },
             position: {
                my: 'top right',
                at: 'bottom right',
                target: $('.cartopt'),
                viewport: $(window),
                adjust: {
                    x: 10
                }
             }
         });

		var timeOutId = null; //定义延迟时间Id
		var delayTime = 500; //默认延迟0.5秒
		var minPrefix = 0; //定义最小几个字符开始搜索
		$("#searchkey").keyup(function(event) {
			var myEvent = event || window.event;
	    	var keyCode = myEvent.keyCode;
		    if (keyCode != 13 && keyCode != 38 && keyCode != 40) {
				if (searchkey.length < minPrefix) return;
				setTimeout(function() {
					var searchkey = $("#searchkey").val();
					$.post("ajaxphp/searchsuggest.php", {searchkey: searchkey}, function(callback) {
						var fruitlist = $.parseJSON(callback).list;
						var len = fruitlist.length;
						if (len > 0) {
							var htmlstr = '';
							for (var i = 0; i < len; i ++) {
								htmlstr += '<li><a href="javascript:;" onclick="detaildialog('+fruitlist[i].id+');" title="'+fruitlist[i].name+'">'+fruitlist[i].name+'</a></li>\n';
							}
							htmlstr += '<li class="search-ul-c"><a href="javascript:;">关闭</a></li>';
							$(".search-ul").html(htmlstr);
							$(".search-ul").show();
						}
					});
				}, delayTime);
		    } else if (keyCode == 13) {
				$("#search-btn").click();
		    }
	    });

		$("#search-btn").click(function() {
			var searchkey = $("#searchkey").val();
			if (searchkey.replace(/(^\s*)|(\s*$)/g, "").length > 0) {
				$("#searchform").submit();
			}
		});

		$("#searchkey").blur(function() {
//			$(".search-ul").hide();

			setTimeout(function() {$(".search-ul").hide()}, 500);
		});
		
		window.createGrowl = function(content) {
		    var target = $('.qtip.jgrowl:visible:last');
			$('<div/>').qtip({
		        content: {
		            text: '<div class="tipdiv">'+content+'</div>'
		        },
		        position: {
		            target: [0,0],
		            container: $('#qtip-growl-container')
		        },
		        show: {
		            event: false,
		            ready: true,
		            effect: function() {
		                $(this).stop(0, 1).animate({ height: 'toggle' }, 400, 'swing');
		            },
		            delay: 0
		        },
		        hide: {
		            event: false,
		            inactive: 1500,
		            effect: function(api) {
		                $(this).stop(0, 1).animate({ height: 'toggle' }, 400, 'swing');
		            }
		        },
		        style: {
		            width: 250,
		            classes: 'jgrowl qtip-light qtip-shadow',
		            tip: false
		        }
		    });
		}
	});

</script>