//以下注册号为本机开发测试注册号，报表访问地址为localhost时可以去掉试用标志
//购买注册后，请用您的注册用户名与注册号替换下面变量中值
var gr_UserName = '锐浪报表插件本机开发测试注册';
var gr_SerialNo = '4DFB949E066NYS7W11L8KAT53SA177391Q9LZQ094WUT9C9J3813SX8PTQC4ALPB9UAQN6TMA55Q3BN8E5726Z5A839QAD9P6E76TKNK5';
//区分浏览器(IE or not)
var agent = navigator.userAgent.toLowerCase();
var isIE = (agent.indexOf("msie")>0)? true : false;
//1、变量 gr_CodeBase 指定了报表插件的下载位置与版本号，当客户端初次访问报表
//   时或插件版本升级后访问，将自动下载并安装 Grid++Report 报表插件。应将变量 
//   gr_CodeBase 的值调整为与实际相符。
//2、变量 grInstallPath 等号后面的参数是插件安装文件的下载地址，可以是相对地址，
//   一般从网站的根目录开始寻址，插件安装文件一定要存在于指定目录下。
//3、Version 等号后面的参数是插件安装包的版本号，如果有新版本插件安装包，应上传新版
//   本插件安装文件到服务器对应目录，并更新这里的版本号。
//4、更多详细信息请参考帮助中“报表插件(WEB报表)->在服务器部署插件安装包”部分
var grInstallPath = "grinstall";
var gr_CodeBase;
if( isIE )
    gr_CodeBase = 'codebase="' + grInstallPath + '/grbsctl5.cab#Version==5,8,13,110"';
else
    gr_CodeBase = ''; //TBD... 'codebase="' + grInstallPath + '/grbsctl5.xpi"';

//创建报表对象，报表对象是不可见的对象，详细请查看帮助中的 IGridppReport
//Name - 指定插件对象的ID，可以用js代码 document.getElementById("%Name%") 获取报表对象
//EventParams - 指定报表对象的需要响应的事件，如："<param name='OnInitialize' value=OnInitialize> <param name='OnProcessBegin' value=OnProcessBegin>"形式，可以指定多个事件
function CreateReport(PluginID, EventParams)
{
    var typeid;
    if( isIE )
        typeid = 'classid="clsid:25240C9A-6AA5-416c-8CDA-801BBAF03928" ';
    else
        typeid = 'type="application/x-grplugin-report" ';
    typeid += gr_CodeBase;
	document.write('<object id="' + PluginID + '" ' + typeid);
	document.write(' width="0" height="0" VIEWASTEXT>');
	if (EventParams != undefined)
	    document.write(EventParams);
	document.write('</object>');
	
	document.write('<script type="text/javascript">');
	    document.write(PluginID + '.Register("' + gr_UserName + '", "' + gr_SerialNo + '");');
	document.write('</script>');
}

//用更多的参数创建报表打印显示插件，详细请查看帮助中的 IGRPrintViewer
//PluginID - 插件的ID，可以通过 var ReportViewer = document.getElementById("%PluginID%"); 这样的方式获取插件引用变量
//Width - 插件的显示宽度，"100%"为整个显示区域宽度，"500"表示500个屏幕像素点
//Height - 插件的显示高度，"100%"为整个显示区域高度，"500"表示500个屏幕像素点
//ReportURL - 获取报表模板的URL
//DataURL - 获取报表数据的URL
//AutoRun - 指定插件在创建之后是否自动生成并展现报表,值为false或true
//ExParams - 指定更多的插件属性阐述,形如: "<param name="%ParamName%" value="%Value%">"这样的参数串
function CreatePrintViewerEx2(PluginID, Width, Height, ReportURL, DataURL, AutoRun, ExParams)
{
    var typeid;
    if( isIE )
        typeid = 'classid="clsid:B7EF88E6-A0AD-4235-B418-6F07D8533A9F" ' + gr_CodeBase;
    else
        typeid = 'type="application/x-grplugin-printviewer"';
	document.write('<object id="' + PluginID + '" ' + typeid);
	document.write(' width="' + Width + '" height="' + Height + '">');
	document.write('<param name="ReportURL" value="' + ReportURL + '">');
	document.write('<param name="DataURL" value="' + DataURL + '">');
	document.write('<param name="AutoRun" value=' + AutoRun + '>');
	document.write('<param name="SerialNo" value="' + gr_SerialNo + '">');
	document.write('<param name="UserName" value="' + gr_UserName + '">');
	document.write(ExParams);
	document.write('</object>');
}

//用更多的参数创建报表打印显示插件，详细请查看帮助中的 IGRDisplayViewer
//PluginID - 插件的ID，可以通过 var ReportViewer = document.getElementById("%PluginID%"); 这样的方式获取插件引用变量
//Width - 插件的显示宽度，"100%"为整个显示区域宽度，"500"表示500个屏幕像素点
//Height - 插件的显示高度，"100%"为整个显示区域高度，"500"表示500个屏幕像素点
//ReportURL - 获取报表模板的URL
//DataURL - 获取报表数据的URL
//AutoRun - 指定插件在创建之后是否自动生成并展现报表,值为false或true
//ExParams - 指定更多的插件属性阐述,形如: "<param name="%ParamName%" value="%Value%">"这样的参数串
function CreateDisplayViewerEx2(PluginID, Width, Height, ReportURL, DataURL, AutoRun, ExParams)
{
    var typeid;
    if( isIE )
        typeid = 'classid="clsid:CB45DFE5-6C35-4687-B790-FEC65D512859" ' + gr_CodeBase;
    else
        typeid = 'type="application/x-grplugin-displayviewer"';
	document.write('<object id="' + PluginID + '" ' + typeid);
	document.write(' width="' + Width + '" height="' + Height + '">');
	document.write('<param name="ReportURL" value="' + ReportURL + '">');
	document.write('<param name="DataURL" value="' + DataURL + '">');
	document.write('<param name="AutoRun" value=' + AutoRun + '>');
	document.write('<param name="SerialNo" value="' + gr_SerialNo + '">');
	document.write('<param name="UserName" value="' + gr_UserName + '">');
	document.write(ExParams);
	document.write('</object>');
}

//用更多的参数创建报表设计器插件，详细请查看帮助中的 IGRDesigner
//Width - 插件的显示宽度，"100%"为整个显示区域宽度，"500"表示500个屏幕像素点
//Height - 插件的显示高度，"100%"为整个显示区域高度，"500"表示500个屏幕像素点
//LoadReportURL - 读取报表模板的URL，运行时从此URL读入报表模板数据并加载到设计器插件
//SaveReportURL - 保存报表模板的URL，保存设计后的结果数据，由此URL的服务在WEB服务端将报表模板持久保存
//DataURL - 获取报表运行时数据的URL，在设计器中进入打印视图与查询视图时从此URL获取报表数据
//ExParams - 指定更多的插件属性阐述,形如: "<param name="%ParamName%" value="%Value%">"这样的参数串
function CreateDesignerEx(Width, Height, LoadReportURL, SaveReportURL, DataURL, ExParams)
{
    var typeid;
    if( isIE )
        typeid = 'classid="clsid:3C19F439-B64D-4dfb-A96A-661FE70EA04D" ' + gr_CodeBase;
    else
        typeid = 'type="application/x-grplugin-designer"';


	document.write('<object id="ReportDesigner" ' + typeid);
	document.write(' width="' + Width + '" height="' + Height + '">');
	document.write('<param name="LoadReportURL" value="' + LoadReportURL + '">');
	document.write('<param name="SaveReportURL" value="' + SaveReportURL + '">');
	document.write('<param name="DataURL" value="' + DataURL + '">');
	document.write('<param name="SerialNo" value="' + gr_SerialNo + '">');
	document.write('<param name="UserName" value="' + gr_UserName + '">');
	document.write(ExParams);
	document.write('</object>');
}

function CreatePrintViewerEx(Width, Height, ReportURL, DataURL, AutoRun, ExParams)
{
    CreatePrintViewerEx2("ReportViewer", Width, Height, ReportURL, DataURL, AutoRun, ExParams)
}

function CreateDisplayViewerEx(Width, Height, ReportURL, DataURL, AutoRun, ExParams)
{
    CreateDisplayViewerEx2("ReportViewer", Width, Height, ReportURL, DataURL, AutoRun, ExParams)
}

function CreatePrintViewer(ReportURL, DataURL)
{
    CreatePrintViewerEx("100%", "100%", ReportURL, DataURL, true, "");
}

function CreateDisplayViewer(ReportURL, DataURL)
{
    CreateDisplayViewerEx("100%", "100%", ReportURL, DataURL, true, "");
}

function CreateDesigner(LoadReportURL, SaveReportURL, DataURL)
{
    CreateDesignerEx("100%", "100%", LoadReportURL, SaveReportURL, DataURL, "");
}

//侦测报表插件是否需要安装或需要下载新版本，如果需要则在网页中显示提示文字与下载地址
//如果插件已经安装且不要更新，则返回 true，反之为 false。
function ReportInstall()
{
    //定义一个报表对象，看能否创建成功，成功表示当前运行的客户端电脑已经安装报表插件
    var typeid;
    if( isIE )
        typeid = 'classid="clsid:25240C9A-6AA5-416c-8CDA-801BBAF03928" ';
    else
        typeid = 'type="application/x-grplugin-report" ';
    typeid += gr_CodeBase;
	document.write('<object id="_ReportOK" ' + typeid);
	document.write(' width="0" height="0" VIEWASTEXT>');
	document.write('</object>');
	
    //对FireFox较低版本，必须用(typeof(_ReportOK) == "undefined")这样判断
    if ((typeof(_ReportOK) == "undefined") || (_ReportOK.Register == undefined)) //if (_ReportOK.Register == undefined)
    {
        document.write('<div style="width: 100%; background-color: #fff8dc; text-align: center; vertical-align: middle; line-height: 20pt; padding-bottom: 12px; padding-top: 12px;">');
            document.write('<strong> 此网站需要安装 锐浪报表插件 才能保证其正常运行<br /></strong>');
            document.write('<a href="' + grInstallPath + '/grbsctl5.msi"><span style="color: #ff0000"><strong>点击此处下载锐浪报表插件安装包<br /></strong></span></a>');
            document.write('锐浪报表插件安装后，<a href="#" onclick="javascript:document.location.reload();">点击此处</a> 重新加载此网站');
        document.write('</div>');
        return false;
    }
    
    //TBD...检查是否应该下载新版本程序
    
    return true;
}
