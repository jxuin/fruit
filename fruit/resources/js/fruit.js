
function fruitmorelist(fruitid, page) {
	$.get('ajaxphp/fruitmorelist.php',{fruitid:fruitid, currepage:page},function(callback) {
		$(".mclist-detail").html(callback);
	});
}

function fruitsearchlist(searchkey, page) {
	$.post('ajaxphp/fruitsearchlist.php',{searchkey:searchkey, currepage:page},function(callback) {
//		console.info(callback);
		$(".mclist-detail").html(callback);
	});
}

//默认放至一个到购物车
//type: 0、商品追加   1、替换数量
function addtocart(fruitid, num, type) {
	var jsonDataStr;
	var jsonData;
	var jsonStr = $.cookie("fruit2cart");
	if (type == null || type == undefined) {
		type = 0;
	}
	$.get('ajaxphp/fruitdetailjson.php',{fruitid:fruitid,num:num},function(callback) {
		if(jsonStr != null && jsonStr != '' && jsonStr != undefined ) {
			jsonData = $.parseJSON(jsonStr);
			var fruits = jsonData.root;
			var newfruits = new Array();
			var flag = false;
			for (var i = 0; i < fruits.length; i ++) {
				var fruit = fruits[i];
				if (fruit.id == fruitid) {
					if (type == 0) {
						fruit.num = (parseInt(fruit.num) + parseInt(num)) + '';
					} else {
						fruit.num = parseInt(num) + '';
					}
					flag = true;
				}
				newfruits[i] = fruit;
			}
			if (!flag) {
				newfruits[fruits.length] = $.parseJSON(callback);
			}
			jsonDataStr = $.toJSON(newfruits);
		} else {
			jsonDataStr = '['+callback+']';
		}
		jsonDataStr = '{"root":'+jsonDataStr+'}';
		
		$.cookie("fruit2cart", null);
		$.cookie("fruit2cart", jsonDataStr, { expires: 7 });
		
		setcartnum();
		createGrowl('添加成功');
	});
}

function deltocart(fruitid) {
	var jsonStr = $.cookie("fruit2cart");
	if(jsonStr != null && jsonStr != '' && jsonStr != undefined ) {
		jsonData = $.parseJSON(jsonStr);
		var fruits = jsonData.root;
		var newfruits = new Array();
		var flag = false;
		var index = 0;
		for (var i = 0; i < fruits.length; i ++) {
			var fruit = fruits[i];
			if (fruit.id != fruitid) {
				newfruits[index] = fruit;
				index ++; 
			}
		}
		jsonDataStr = $.toJSON(newfruits);
	}
	jsonDataStr = '{"root":'+jsonDataStr+'}';
	
	$.cookie("fruit2cart", null);
	$.cookie("fruit2cart", jsonDataStr, { expires: 7 });
	
	$(".cartlistspan").html(setcartlist());
	setcartnum();
}

function getCartnum(jsonStr) {
	var num = 0;
	if (trim(jsonStr) == '') {
		jsonStr = $.cookie("fruit2cart");
	}
	if(jsonStr != null && jsonStr != '' && jsonStr != undefined ) {
		var jsonData = $.parseJSON(jsonStr);
		var fruits = jsonData.root;
		var newfruits = new Array();
		var flag = false;
		for (var i = 0; i < fruits.length; i ++) {
			var fruit = fruits[i];
			num += parseInt(fruit.num);
		}
	}
	return num;
}

function getFruitnum(fruitid) {
	var num = 1;
	var jsonStr = $.cookie("fruit2cart");
	if(jsonStr != null && jsonStr != '' && jsonStr != undefined ) {
		var jsonData = $.parseJSON(jsonStr);
		var fruits = jsonData.root;
		for (var i = 0; i < fruits.length; i ++) {
			var fruit = fruits[i];
			if (fruitid == fruit.id) {
				num = parseInt(fruit.num);
			}
		}
	}
	return num;
}

function setcartnum() {
	$(".cartnum").html(getCartnum(''));
}

function setcartlist() {
	var fruitslen = 0;
	var jsonStr = $.cookie("fruit2cart");
	var htmlStr = '';
	var hasfruitflag = false;
	if(jsonStr != null && jsonStr != '' && jsonStr != undefined ) {
		jsonData = $.parseJSON(jsonStr);
		var fruits = jsonData.root;
		fruitslen = fruits.length;
		if (fruitslen >= 4) {
			htmlStr += '<ul class="cartlistul" style="height:253px;">\n';
		} else if (fruitslen > 0) {
			htmlStr += '<ul class="cartlistul">\n';
		}
		var pricetotal = 0;
		for (var i = 0; i < fruitslen; i ++) {
			var fruit = fruits[i];
			pricetotal += parseFloat(fruit.price) * parseInt(fruit.num);
			htmlStr += '<li>\n';
			htmlStr += '<a href="javascript:;"><img src="'+fruit.img+'" /></a>\n';
			htmlStr += '<span class="span-p"><font>￥'+fruit.price+'×'+fruit.num+'</font><a href="javascript:;" onclick="deltocart('+fruit.id+')">删除</a></span>\n';
			htmlStr += '<font class="font-t"><a href="javascript:;" onclick="detaildialog('+fruit.id+')">'+fruit.name+'</a></font>\n';
			htmlStr += '</li>\n';
		}
		if (fruitslen > 0) {
			htmlStr += '<li class="li-total">共 <font>'+getCartnum('')+'</font> 件商品&nbsp;&nbsp;共计<font>￥'+pricetotal+'</font></li>\n';
			htmlStr += '</ul>\n';
		}
	} 
	if (fruitslen == 0) {
		htmlStr += '<ul class="cartlistul">\n';
		htmlStr += '<li class="li-total" style="background: #FFFFFF;">购物车中还没有商品，赶紧选购吧！</li>\n';
		htmlStr += '</ul>\n';
	}
	htmlStr += '</span>\n';
	return htmlStr;
}

function detaildialog(fruitid) {
	$("#qtip-cartopttip").hide();
	$.get('ajaxphp/detaildialog.php', {id:fruitid}, function(callback) {
		$.dialog({
			id: 'detaildialog',
			padding: '5px 8px',
			esc: true,
			fixed: true,
		    content: callback
		});
	});
}


