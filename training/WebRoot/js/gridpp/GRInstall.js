document.write("<script type='text/javascript' src='CreateControl.js'></script>");

//插入一个报表对象，用来判断插件是否已经安装，或是否需要安装更新版本
//此函数应该在网页的<head>中调用，具体请看例子 ReportHome.htm 中的用法
function Install_InsertReport()
{
    var typeid;
    if( isIE )
        typeid = 'classid="clsid:25240C9A-6AA5-416c-8CDA-801BBAF03928" ';
    else
        typeid = 'type="application/x-grplugin-report" ';
    typeid += gr_CodeBase;
	document.write('<object id="_ReportOK" ' + typeid);
	document.write(' width="0" height="0" VIEWASTEXT>');
	document.write('</object>');
}

//用来判断插件是否已经安装，或是否需要安装更新版本。如果需要安装，则在网页中插入安装相关的文字内容
//如果插件已经安装且也不要更新，则返回 true，反之为 false。
//此函数应该在网页的<body>开始位置处调用，具体请看例子 ReportHome.htm 中的用法
function Install_Detect()
{
    var _ReportOK = document.getElementById("_ReportOK");
    //if ((_ReportOK == null) || (_ReportOK.Register == undefined))
    if (_ReportOK.Register == undefined)
    {
        document.write('<div style="width: 100%; background-color: #fff8dc; text-align: center; vertical-align: middle; line-height: 20pt; padding-bottom: 12px; padding-top: 12px;">');
            document.write('<strong> 此网站需要安装 锐浪报表插件 才能保证其正常运行<br /></strong>');
            document.write('<a href="' + grInstallPath + '/grbsctl5.exe"><span style="color: #ff0000"><strong>点击此处下载锐浪报表插件安装包<br /></strong></span></a>');
            document.write('锐浪报表插件安装后，<a href="#" onclick="javascript:document.location.reload();">点击此处</a> 重新加载此网站');
        document.write('</div>');
        return false;
    }
    
    //TBD...检查是否应该下载新版本程序
    
    return true;
}
