<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
 %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>系统错误</title>
<style type="text/css">
body {
	background-color: #FFFFFF;
	margin-left: 10px;
	margin-top: 10px;
	margin-right: 10px;
	margin-bottom: 10px;
	font-family:"宋体";
	font-size:15px;
}
</style>
<script type="text/javascript">
</script>
</head>
<body onload="javascript:onOut();">
<div style="">
<center>
<h3>错误信息</h3>
</center>
<%
	if(request.getAttribute("error") == null){
		out.print("错误代码不明,请将操作流程描述并发给实施人员，谢谢合作!");
	}else{
		out.print(request.getAttribute("error"));
	}
 %>
 </div>
</body>
</html>