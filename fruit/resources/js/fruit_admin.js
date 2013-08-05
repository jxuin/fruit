
//xuj add 2013.4.23


function userlist() {
	
}

function rolelist() {
	
}

function funclist() {
	
}

function deptlist() {
	
}

function fruitlist(fruitid, page) {
	if (fruitid == null || fruitid == undefined) {
		fruitid == 0;
	}
	if (page == null || page == undefined) {
		page == 1;
	}
	$.get('ajaxphp/fruitlist.php',{fruitid:fruitid, currepage:page},function(callback) {
		$("#admin_right").html(callback);
	});
}

function orderlist(page, status) {
	if (page == null || page == undefined) {
		page == 1;
	}
	if (status == null || page == undefined) {
		status = -1;
	}
	$.get('ajaxphp/orderlist.php',{currepage:page,status:status},function(callback) {
		$("#admin_right").html(callback);
	});
}

function adlist() {
	$.get('ajaxphp/adlist.php',function(callback) {
		$("#admin_right").html(callback);
	});
}





