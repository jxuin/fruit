
//type: 0、商品追加   1、替换数量
function addtocart(fruitid, num, type) {
	var jsonDataStr;
	var jsonData;
	var jsonStr = $.cookie("fruit2cart");
	if (type == null || type == undefined) {
		type = 0;
	}
	$.get('../ajaxphp/fruitdetailjson.php',{fruitid:fruitid,num:num},function(callback) {
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
		
		var fruitnum = getCartnum(jsonDataStr);
		if (fruitnum <= 0) {
			$(".cartnum").hide();
		} else {
			$(".cartnum").html(fruitnum);
		}
		
		$('#tipwindow').popup("open");
		setTimeout(function() { $('#tipwindow').popup("close"); }, 3000);
	});
}

function showJqmTip(content) {
	$('#tipwindow p').html(content);
	$('#tipwindow').popup("open");
	setTimeout(function() { $('#tipwindow').popup("close"); }, 3000);
}

function getCartnum(jsonStr) {
	var num = 0;
	if (trim(jsonStr) == '') {
		jsonStr = $.cookie("fruit2cart");
	}
	if(jsonStr != null && jsonStr != '' && jsonStr != undefined ) {
		var jsonData = $.parseJSON(jsonStr);
		var fruits = jsonData.root;
		for (var i = 0; i < fruits.length; i ++) {
			var fruit = fruits[i];
			num += parseInt(fruit.num);
		}
	}
	return num;
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
}

function getTotalNumAndMoney() {
	var jsonStr = $.cookie("fruit2cart");
	var totalNum = 0;
	var totalMoney = 0;
	var jsonStr = $.cookie("fruit2cart");
	if(jsonStr != null && jsonStr != '' && jsonStr != undefined ) {
		jsonData = $.parseJSON(jsonStr);
		var fruits = jsonData.root;
		for (var i = 0; i < fruits.length; i ++) {
			var fruit = fruits[i];
			totalNum += parseInt(fruit.num);
			totalMoney += parseFloat(fruit.price) * parseInt(fruit.num);
		}
	}
	var totalArray = new Array();
	totalArray[0] = totalNum;
	totalArray[1] = totalMoney;
	return totalArray;
	
}

