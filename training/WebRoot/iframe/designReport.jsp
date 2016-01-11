<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>修改报表模板</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	
	<script src="js/gridpp/CreateControl.js" type="text/javascript"></script>

  </head>
  
  <body>
    <body style="margin:0">
    <script type="text/javascript"> 
    	var reportCode = "<%=request.getParameter("reportCode")%>";
    	var basePath = "<%=basePath%>";
        //修改一个报表，在完成报表设计后，将报表保存在web服务器上
        //前面两个参数分别指定模板载入与保存的URL，
        //第三个参数指定报表数据的URL，以便在设计时载入数据及时查看效果
        CreateDesignerEx("100%", "100%", "../report/"+reportCode+".grf", basePath+"ReportServlet?kind=save&reportCode="+reportCode, "", 
            "<param name='OnSaveReport' value='OnSaveReport'>");
    </script>
</body>
  </body>
</html>
