var browser = {
	versions : function() {
		var u = navigator.userAgent, app = navigator.appVersion;
		return {// 移动终端浏览器版本信息
			trident : u.indexOf('Trident') > -1, // IE内核
			presto : u.indexOf('Presto') > -1, // opera内核
			webKit : u.indexOf('AppleWebKit') > -1, // 苹果、谷歌内核
			gecko : u.indexOf('Gecko') > -1 && u.indexOf('KHTML') == -1, // 火狐内核
			mobile : !!u.match(/AppleWebKit.*Mobile.*/), // 是否为移动终端
			ios : !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/), // ios终端
			android : u.indexOf('Android') > -1 || u.indexOf('Linux') > -1, // android终端或者uc浏览器
			iPhone : u.indexOf('iPhone') > -1 || u.indexOf('Mac') > -1, // 是否为iPhone或者QQHD浏览器
			iPad : u.indexOf('iPad') > -1, // 是否iPad
			webApp : u.indexOf('Safari') == -1
		// 是否web应该程序，没有头部与底部
		};
	}(),
	language : (navigator.browserLanguage || navigator.language).toLowerCase()
};

function getstr(str,dstr) {
	if (trim(str) == '') {
		str = dstr;
	}
	return str;
}

function trim(s){
	if (s == undefined || s == null) {
		s = '';
	}
    return s.replace(/(^\s*)|(\s*$)/g, "");
}

//是否有效固话
function IsPhone(_str){
    var tmp_str = trim(_str);
    var pattern = /^(0(\d){3}-)?(\d){7}(-(\d){1,6})?|(0(\d){2}-)?(\d){8}(-(\d){1,6})?$/;
    return pattern.test(tmp_str);
}

//是否有效的手机号码;
function IsMobile(_str){
    var tmp_str = trim(_str);
    if(tmp_str.length>11){return false;}
    var pattern = /^1\d{10}$/;
    return pattern.test(tmp_str);
}

//是否有效的整数;
function IsNumber(_str){
    var tmp_str = trim(_str);
    var pattern = /^-?\d+$/;
    return pattern.test(tmp_str);
}

function showDialog(imgpath,content,width){
	content = "<img src=\""+imgpath+"\" class=\"img_shuff_1\" />" + content;
	$.dialog({
		width : width,
	    content: content,
	    ok: function () {
	        this.close();
	    }
	});
}

function removeParent(obj) {
	if(obj){
		var parentObj = obj.parentNode;
		if (parentObj) {
			parentObj.parentNode.removeChild(parentObj);
		}
	}
}

function showDialog(imgpath,content,width){
	content = "<img src=\""+imgpath+"\" class=\"img_shuff_1\" />" + content;
	$.dialog({
		padding: "30px 25px 10px",
		width: width,
	    content: content
	});
}

function domhide(type, name) {
	if (type == 1) {
		$("#" + name).hide();
	} else if (type == 2) {
		$("." + name).hide();
	}
}
