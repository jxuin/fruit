<?php

require_once '../config.php';
require_once '../util/Util.php';

//文件保存目录路径
$save_path = BASE_DIR . '/upload';
//文件保存目录URL
$save_url = PHP_URL . '/upload';
//最大文件大小
$max_size = 30000000;

$save_path = realpath($save_path) . '/';
//PHP上传失败
if (!empty($_FILES['Filedata']['error'])) {
	switch($_FILES['Filedata']['error']){
		case '1':
			$error = '超过php.ini允许的大小。';
			break;
		case '2':
			$error = '超过表单允许的大小。';
			break;
		case '3':
			$error = '只有部分被上传。';
			break;
		case '4':
			$error = '请选择。';
			break;
		case '6':
			$error = '找不到临时目录。';
			break;
		case '7':
			$error = '写文件到硬盘出错。';
			break;
		case '8':
			$error = 'File upload stopped by extension。';
			break;
		case '999':
		default:
			$error = '未知错误。';
	}
	alert($error);
}

//有上传文件时
if (empty($_FILES) === false) {
	//原文件名
	$file_name = $_FILES['Filedata']['name'];
	//服务器上临时文件名
	$tmp_name = $_FILES['Filedata']['tmp_name'];
	//文件大小
	$file_size = $_FILES['Filedata']['size'];
	
	//检查文件名
	if (!$file_name) {
		alert("请选择文件。");
	}
	//检查目录
	if (@is_dir($save_path) === false) {
		alert("上传目录不存在。");
	}
	//检查目录写权限
	if (@is_writable($save_path) === false) {
		alert("上传目录没有写权限。");
	}
	//检查是否已上传
	if (@is_uploaded_file($tmp_name) === false) {
		alert("上传失败。");
	}
	//检查文件大小
	if ($file_size > $max_size) {
		alert("上传文件大小超过限制。");
	}
	//获得文件扩展名
	$temp_arr = explode(".", $file_name);
	$file_ext = array_pop($temp_arr);
	$file_ext = trim($file_ext);
	$file_ext = strtolower($file_ext);
	
	//创建文件夹
	if ($dir_name !== '') {
		$save_path .= $dir_name . "/";
		$save_url .= $dir_name . "/";
		if (!file_exists($save_path)) {
			mkdir($save_path);
		}
	}
	$ymd = date("Ymd");
	$save_path .= $ymd . "/";
	$save_url .= $ymd . "/";
	if (!file_exists($save_path)) {
		mkdir($save_path);
	}
	
	$time = explode ( " ", microtime () );  
	$time = $time [1] . ($time [0] * 1000);  
	$time2 = explode ( ".", $time );  
	$time = $time2 [0];
	
	//新文件名
	$new_file_name = $time . '.' . $file_ext;
	//移动文件
	$file_path = $save_path . $new_file_name;
	if (move_uploaded_file($tmp_name, $file_path) === false) {
		alert("上传文件失败。");
	}
	@chmod($file_path, 0644);
	$file_url = $save_url . $new_file_name;

	header('Content-type: text/html; charset=UTF-8');
	$util = new Util();
	echo stripslashes($util->JSON(array('status' => 0, 'filepath' => $file_url, 'filename' => $file_name)));
	exit;
}

function alert($msg) {
	header('Content-type: text/html; charset=UTF-8');
	$util = new Util();
	echo stripslashes($util->JSON(array('status' => 1, 'message' => $msg)));
	exit;
}

?>