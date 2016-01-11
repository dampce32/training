<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String data=request.getParameter("data");
data=data.replace("^","&");
%>
<html>
	<head>
		<title>打印报表</title>
		<meta http-equiv="content-type" content="text/html; charset=utf-8">
		<script src="js/gridpp/GRInstall.js" type="text/javascript"></script>
		<script src="js/gridpp/CreateControl.js" type="text/javascript"></script>
	    <style type="text/css">
	        html,body {
	            margin:0;
	            height:100%;
	        }
	    </style>
	    <script language="javascript" type="text/javascript">
		    Install_InsertReport();
		</script>
	</head>
	<body style="margin:0">
		<script type="text/javascript">
			var Installed = Install_Detect();
			if ( Installed )
				CreateReport("Report");
		</script>
		<script type="text/javascript">
			
		    var report = "<%=request.getParameter("report")%>";
		    var data = "<%=data%>"+"&reportCode="+report;
		    CreatePrintViewerEx("100%", "100%","report/"+ report+".grf", data, true, "");
		</script>
	</body>
</html>
